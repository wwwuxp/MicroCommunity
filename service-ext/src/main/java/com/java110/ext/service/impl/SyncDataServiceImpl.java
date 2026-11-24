package com.java110.ext.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.java110.ext.excel.OrgStaffRelExcel;
import com.java110.ext.excel.listener.OrgStaffExportListener;
import com.java110.ext.mapper.*;
import com.java110.ext.pojo.*;
import com.java110.ext.pojo.attendance.SysDepartPo;
import com.java110.ext.pojo.attendance.SysUserDepartPo;
import com.java110.ext.pojo.attendance.SysUserPo;
import com.java110.ext.service.AttendanceSysService;
import com.java110.ext.service.ISyncDataService;
import com.java110.ext.utils.KeyUtils;
import com.java110.ext.utils.UuidUtil;
import com.java110.ext.vo.OrgStaffVo;
import com.java110.ext.vo.SysUserDepartVo;
import com.java110.utils.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 数据同步服务实现
 * 统一管理用户体系数据的导出、导入和同步
 */
@Slf4j
@Service("syncDataServiceImpl")
public class SyncDataServiceImpl implements ISyncDataService {

    private static final Logger logger = LoggerFactory.getLogger(SyncDataServiceImpl.class);

    @Autowired
    private OrgMapper orgMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrgStaffRelMapper orgStaffRelMapper;

    @Autowired
    private StoreUserMapper storeUserMapper;

    @Autowired
    private PrivilegeUserMapper privilegeUserMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DictMapper dictMapper;

    @Autowired
    private AttendanceSysService attendanceSysService;

    // ==================== 同步功能 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> syncAllData() {
        Map<String, Object> result = new HashMap<>();
        String taskId = UUID.randomUUID().toString().replace("-", "");

        final int[] orgSyncSuccessCount = {0};
        final int[] orgSyncFailCount = {0};
        int staffCount = 0;
        int relCount = 0;
        int failCount = 0;
        List<String> errors = new ArrayList<>();

        try {
            // 调取外部用户体系数据
            ResponseEntity<JSONObject> sourceDataRes = restTemplate.getForEntity("http://150.100.16.58/api/v1/outside/upms/group/tree/ns", JSONObject.class);
            if (Objects.isNull(sourceDataRes.getBody()) || sourceDataRes.getBody().getInteger("code") != 0 || Objects.isNull(sourceDataRes.getBody().getString("data"))){
                throw new RuntimeException("调用数字化平台用户体系接口异常");
            }

            JSONArray groupList = sourceDataRes.getBody().getJSONObject("data").getJSONArray("groupList");
            JSONArray userList = sourceDataRes.getBody().getJSONObject("data").getJSONArray("userList");

            //获取第三级数据，并只更新第三级数据信息
            LambdaQueryWrapper<OrgPo> orgQuery = new LambdaQueryWrapper<>();
            orgQuery.eq(OrgPo::getParentOrgId, "1398");
            orgQuery.eq(OrgPo::getStatusCd, 0);
            orgQuery.orderByAsc(OrgPo::getOrgId);

            List<OrgPo> orgPos = orgMapper.selectList(orgQuery);
            //只取稽查局数据
            List<JSONObject> groupJsonList = IntStream.range(0, groupList.size()).mapToObj(groupList::getJSONObject).filter(x-> StringUtils.equals(x.getString("parentId"), "1266215241180516398")).collect(Collectors.toList());
            groupJsonList.forEach(groupJson->{
                //匹配是否存在：
                // 方式1：jsSwjgDm不为空时，使用jsSwjgDm
                // 方式2：jsSwjgDm为空时，使用名字，并移除前缀"稽查局"
                try {
                    if (orgPos.stream().anyMatch(o->(StringUtils.isNotBlank(o.getSourceOrgId()) && StringUtils.equals(o.getSourceOrgId(), groupJson.getString("id")))
                            || (StringUtils.isBlank(o.getSourceOrgId()) && StringUtils.equals(o.getOrgName(), groupJson.getString("name").replace("稽查局", ""))))){
                        System.out.println(groupJson.getString("name").replace("稽查局", ""));
                        OrgPo orgPo = orgPos.stream().filter(o->(StringUtils.isNotBlank(o.getSourceOrgId()) && StringUtils.equals(o.getSourceOrgId(), groupJson.getString("id")))
                                || (StringUtils.isBlank(o.getSourceOrgId()) && StringUtils.equals(o.getOrgName(), groupJson.getString("name").replace("稽查局", "")))).findFirst().get();
                        orgPo.setSourceOrgId(groupJson.getString("id"));
                        orgPo.setSyncTime(new Date());
                        orgPo.setDescription(groupJson.getString("jsSwjgmc"));
                        orgMapper.updateById(orgPo);
                    }else {
                        OrgPo orgPo = new OrgPo();
                        orgPo.setSyncTime(new Date());
                        orgPo.setSourceOrgId(groupJson.getString("id"));
                        orgPo.setCreateTime(new Date());
                        orgPo.setStoreId(orgPos.get(0).getStoreId());
                        orgPo.setAllowOperation("T");
                        orgPo.setDescription(groupJson.getString("jsSwjgmc"));
                        orgPo.setOrgLevel("3");
                        orgPo.setOrgName(groupJson.getString("name").replace("稽查局", ""));
                        orgPo.setParentOrgId("1398");
                        orgPo.setStatusCd("0");
                        orgPo.setFlag(2);
                        orgPo.setOrgId(KeyUtils.genKey("84"));
                        orgMapper.insert(orgPo);
                        orgPos.add(orgPo);
                    }
                    orgSyncSuccessCount[0]++;
                }catch (Exception e){
                    errors.add("同步组织失败: " + groupJson.get("id") + ":" + groupJson.get("jsSwjgmc") + ", 错误: " + e.getMessage());
                    logger.error("同步组织失败: {}", groupJson.get("id") + ":" + groupJson.get("jsSwjgmc"), e);
                    orgSyncFailCount[0]++;
                }
            });

            //获取有sourceOrgId的科室
            List<String> sourceOrgIdList = orgPos.stream().filter(x->StringUtils.isNotBlank(x.getSourceOrgId())).map(x->x.getSourceOrgId()).collect(Collectors.toList());
            //获取数字化平台用户属于有sourceOrgId的科室的用户
            List<JSONObject> userJsonList = IntStream.range(0, userList.size()).mapToObj(userList::getJSONObject).filter(x-> sourceOrgIdList.contains(x.getString("groupId"))).collect(Collectors.toList());

            List<String> exsitsUserId = new ArrayList<>();

            //循环科室，同步用户
            orgPos.stream().filter(x->StringUtils.isNotBlank(x.getSourceOrgId())).forEach(orgPo->{
                //本地用户
                List<OrgStaffVo> existOrgStaffVos = orgStaffRelMapper.selectOrgUser(orgPo.getSourceOrgId(),null);
                existOrgStaffVos = existOrgStaffVos.stream().filter(x->StringUtils.isNotBlank(x.getUserFlag())).collect(Collectors.toList());
                userJsonList.stream().filter(user->StringUtils.equals(orgPo.getSourceOrgId(), user.getString("groupId")) && StringUtils.isNotBlank(user.getString("nickName"))).forEach(user->{
                    DictPo dictPo = getOrGenDictJob(user.getString("job"),null);
                    //先用id匹配
                    LambdaQueryWrapper<UserPo> userQuery = new LambdaQueryWrapper<>();
                    userQuery.eq(UserPo::getSourceUserId, user.getString("id"));
                    userQuery.eq(UserPo::getStatusCd, 0);
                    UserPo userPo = userMapper.selectOne(userQuery);
                    //再用nickName匹配
                    if (Objects.isNull(userPo)){
                        userQuery = new LambdaQueryWrapper<>();
                        userQuery.eq(UserPo::getName, user.getString("nickName"));
                        userQuery.eq(UserPo::getStatusCd, 0);
                        userPo = userMapper.selectOne(userQuery);
                    }
                    //用户存在
                    if (Objects.nonNull(userPo)){
                        userPo.setSourceUserId(user.getString("id"));
                        userPo.setTel(user.getString("phone"));
                        userPo.setSyncDate(new Date());
                        userMapper.updateById(userPo);

                        List<OrgStaffVo> orgStaffRelVos = orgStaffRelMapper.selectOrgUser(null, userPo.getUserId());
                        //用户不在该部门
                        if (CollectionUtils.isEmpty(orgStaffRelVos)) {
                            //插入用户部门映射关系
                            OrgStaffRelPo orgStaffRelPo = new OrgStaffRelPo();
                            orgStaffRelPo.setRelId(KeyUtils.genKey("84"));
                            orgStaffRelPo.setCreateTime(new Date());
                            orgStaffRelPo.setOrgId(orgPo.getOrgId());
                            orgStaffRelPo.setRelCd(dictPo.getStatusCd());
                            orgStaffRelPo.setStaffId(userPo.getUserId());
                            orgStaffRelPo.setStoreId("402021121452660010");
                            orgStaffRelPo.setFlag("2");
                            orgStaffRelPo.setBId("-8");
                            orgStaffRelPo.setSyncTime(new Date());
                            orgStaffRelMapper.insert(orgStaffRelPo);
                        }else {
                            final boolean[] orgStaffExistFlag = {false};
                            orgStaffRelVos.forEach(orgStaff->{
                                //通过部门+岗位匹配用户，若用户不存在则直接作废掉
                                if (!(StringUtils.equals(orgStaff.getOrgId(), orgPo.getOrgId()) && StringUtils.equals(orgStaff.getRelCd(), dictPo.getStatusCd()))){
                                    OrgStaffRelPo updateDo = new OrgStaffRelPo();
                                    updateDo.setStatusCd("1");
                                    LambdaQueryWrapper<OrgStaffRelPo> orgStaffRelQuery = new LambdaQueryWrapper<>();
                                    orgStaffRelQuery.eq(OrgStaffRelPo::getRelId, orgStaff.getRelId());
                                    orgStaffRelMapper.update(updateDo, orgStaffRelQuery);
                                }else {
                                    orgStaffExistFlag[0] = true;
                                }
                            });
                            if (!orgStaffExistFlag[0]){
                                //插入用户部门映射关系
                                OrgStaffRelPo orgStaffRelPo = new OrgStaffRelPo();
                                orgStaffRelPo.setRelId(KeyUtils.genKey("84"));
                                orgStaffRelPo.setCreateTime(new Date());
                                orgStaffRelPo.setOrgId(orgPo.getOrgId());
                                orgStaffRelPo.setRelCd(dictPo.getStatusCd());
                                orgStaffRelPo.setStaffId(userPo.getUserId());
                                orgStaffRelPo.setStoreId("402021121452660010");
                                orgStaffRelPo.setFlag("2");
                                orgStaffRelPo.setBId("-8");
                                orgStaffRelPo.setSyncTime(new Date());
                                orgStaffRelMapper.insert(orgStaffRelPo);
                            }
                        }
                        exsitsUserId.add(userPo.getUserId());
                    }
                    //用户不存在
                    else {
                        String invitorId;
                        do {
                            invitorId = RandomStringUtils.randomNumeric(6);
                        }while (Objects.nonNull(userMapper.hasSameInvitorId(invitorId)));
                        //插入用户
                        String userId = StringUtils.isBlank(user.getString("username"))?user.getString("id"):user.getString("username");
                        userQuery = new LambdaQueryWrapper<>();
                        userQuery.eq(UserPo::getUserId, user.getString("username"));
                        userQuery.eq(UserPo::getStatusCd, 0);
                        UserPo existUserDo = userMapper.selectOne(userQuery);
                        if (Objects.nonNull(existUserDo)){
                            userId = user.getString("id");
                        }
                        userPo = new UserPo();
                        userPo.setSyncDate(new Date());
                        userPo.setSourceUserId(user.getString("id"));
                        userPo.setName(user.getString("nickName"));
                        userPo.setLevelCd("01");
                        userPo.setTel(user.getString("phone"));
                        userPo.setStatusCd("0");
                        userPo.setUserId(userId);
                        userPo.setInvitorId(invitorId);
                        userPo.setBId("-8");
                        userPo.setFlag("2");
                        userPo.setPassword("9ef8b90414d6ff1202baa99049adb5cd");
                        userPo.setCreateTime(new Date());
                        userMapper.insert(userPo);
                        exsitsUserId.add(userPo.getUserId());
                        //插入科室关联关系
                        OrgStaffRelPo orgStaffRelPo = new OrgStaffRelPo();
                        orgStaffRelPo.setRelId(KeyUtils.genKey("84"));
                        orgStaffRelPo.setCreateTime(new Date());
                        orgStaffRelPo.setOrgId(orgPo.getOrgId());
                        orgStaffRelPo.setRelCd(dictPo.getStatusCd());
                        orgStaffRelPo.setStaffId(userPo.getUserId());
                        orgStaffRelPo.setStoreId("402021121452660010");
                        orgStaffRelPo.setFlag("2");
                        orgStaffRelPo.setBId("-8");
                        orgStaffRelPo.setSyncTime(new Date());
                        orgStaffRelMapper.insert(orgStaffRelPo);
                    }
                    //商户用户插入
                    LambdaQueryWrapper<StoreUserPo> storeUserQuery = new LambdaQueryWrapper<>();
                    storeUserQuery.eq(StoreUserPo::getUserId, userPo.getUserId());
                    storeUserQuery.eq(StoreUserPo::getStoreId, "402021121452660010");
                    storeUserQuery.eq(StoreUserPo::getStatusCd, "0");
                    if (storeUserMapper.selectCount(storeUserQuery) < 1){
                        StoreUserPo storeUserPo = new StoreUserPo();
                        storeUserPo.setStoreUserId(KeyUtils.genKey("45"));
                        storeUserPo.setUserId(userPo.getUserId());
                        storeUserPo.setBId("-8");
                        storeUserPo.setStoreId("402021121452660010");
                        storeUserPo.setFlag("2");
                        storeUserPo.setCreateTime(new Date());
                        storeUserPo.setRelCd("600311000002");
                        storeUserPo.setStatusCd("0");
                        storeUserMapper.insert(storeUserPo);
                    }
                    //普通用户权限
                    LambdaQueryWrapper<PrivilegeUserPo> privilegeUserQuery = new LambdaQueryWrapper<>();
                    privilegeUserQuery.eq(PrivilegeUserPo::getUserId, userPo.getUserId());
                    privilegeUserQuery.eq(PrivilegeUserPo::getStoreId, "402021121452660010");
                    privilegeUserQuery.eq(PrivilegeUserPo::getStatusCd, "0");
                    privilegeUserQuery.eq(PrivilegeUserPo::getPId, "6002022010746310005");
                    if (privilegeUserMapper.selectCount(privilegeUserQuery) < 1){
                        PrivilegeUserPo privilegeUserPo = new PrivilegeUserPo();
                        privilegeUserPo.setUserId(userPo.getUserId());
                        privilegeUserPo.setPId("6002022010746310005");
                        privilegeUserPo.setStoreId("402021121452660010");
                        privilegeUserPo.setPrivilege_flag("1");
                        privilegeUserPo.setCreateTime(new Date());
                        privilegeUserPo.setStatusCd("0");
                        privilegeUserMapper.insert(privilegeUserPo);
                    }
                });
            });

            //不存在用户体系且非手动新增的用户
            List<String> noExistUserIds =userMapper.selectUserIdByNotInUserAndFlagNotNull(exsitsUserId);
            if (!CollectionUtils.isEmpty(noExistUserIds)){
                //用户置为无效
                UserPo updateUserPo = new UserPo();
                updateUserPo.setStatusCd("1");
                updateUserPo.setSyncDate(new Date());
                LambdaQueryWrapper<UserPo> userUpdateQuery = new LambdaQueryWrapper<>();
                userUpdateQuery.in(UserPo::getUserId, noExistUserIds);
                userMapper.update(updateUserPo, userUpdateQuery);
                //用户组织关系置为无效
                OrgStaffRelPo orgStaffRelUpdatePo = new OrgStaffRelPo();
                orgStaffRelUpdatePo.setSyncTime(new Date());
                orgStaffRelUpdatePo.setStatusCd("1");
                LambdaQueryWrapper<OrgStaffRelPo> orgStaffRelUpdateQuery = new LambdaQueryWrapper<>();
                orgStaffRelUpdateQuery.in(OrgStaffRelPo::getStaffId, noExistUserIds);
                orgStaffRelUpdateQuery.eq(OrgStaffRelPo::getStatusCd, "0");
                orgStaffRelMapper.update(orgStaffRelUpdatePo, orgStaffRelUpdateQuery);
            }

            result.put("success", true);
            result.put("taskId", taskId);
            result.put("orgSyncSuccessCount", orgSyncSuccessCount[0]);
            result.put("orgSyncFailCount", orgSyncFailCount[0]);
            result.put("staffCount", staffCount);
            result.put("relCount", relCount);
            result.put("failCount", failCount);
            result.put("errors", errors);

        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
            logger.error("同步数据失败", e);
            throw new RuntimeException("同步数据失败: " + e.getMessage(), e);
        }

        return result;
    }

    private DictPo getOrGenDictJob(String job, String statusCd) {
        LambdaQueryWrapper<DictPo> dictQuery = new LambdaQueryWrapper<>();
//        dictQuery.eq(DictPo::getTableName, "u_org_staff_rel");
        dictQuery.eq(DictPo::getTableColumns, "rel_cd");
        if (StringUtils.isBlank(job)){
            dictQuery.eq(DictPo::getName, "无");
        }else {
            dictQuery.eq(DictPo::getName, job);
        }
        DictPo dictPo = dictMapper.selectOne(dictQuery);
        if (Objects.isNull(dictPo)){
            dictPo = new DictPo();
            dictPo.setCreateTime(new Date());
            dictPo.setDescription(job);
            dictPo.setName(job);
            dictPo.setTableName("u_org_staff_rel");
            dictPo.setTableColumns("rel_cd");
            dictPo.setStatusCd(StringUtils.isBlank(statusCd)?KeyUtils.genKey("10"):statusCd);
            dictMapper.insert(dictPo);
        }
        return dictPo;
    }

    // ==================== 导出功能 ====================

    @Override
    public List<OrgStaffRelExcel> exportData(Map<String, Object> params) {
        List<OrgStaffRelExcel> excels =orgStaffRelMapper.selectExportSourceData();
        return excels;
    }

    // ==================== 导入功能 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importData(MultipartFile file) throws IOException {
        //读取文件内容
        OrgStaffExportListener listener = new OrgStaffExportListener();
        EasyExcel.read(file.getInputStream(), OrgStaffRelExcel.class, listener).sheet().headRowNumber(1).doRead();
        List<OrgStaffRelExcel> list = listener.getDataList();

        List<String> exsitsUserId = new ArrayList<>();
        Map<String, String> orgMap = new HashMap<>();

        for (OrgStaffRelExcel excelItem: list){
            OrgPo orgPo = new OrgPo();
            if (StringUtils.isNotBlank(excelItem.getOrgId())) {
                //已更新的不需要重新更新
                if (!orgMap.containsKey(excelItem.getSourceOrgId())) {
                    //判断org是否存在,1、先用id匹配
                    orgPo = orgMapper.selectById(excelItem.getOrgId());
                    if (Objects.isNull(orgPo)) {
                        //2、再用sourceOrgId
                        LambdaQueryWrapper<OrgPo> orgQuery = new LambdaQueryWrapper<>();
                        orgQuery.eq(OrgPo::getSourceOrgId, excelItem.getSourceOrgId());
                        orgQuery.eq(OrgPo::getStatusCd, "0");
                        orgPo = orgMapper.selectOne(orgQuery);
                        if (Objects.isNull(orgPo)) {
                            //3、最后使用名字匹配
                            orgQuery = new LambdaQueryWrapper<>();
                            orgQuery.eq(OrgPo::getOrgName, excelItem.getOrgName());
                            orgQuery.eq(OrgPo::getStatusCd, "0");
                            orgPo = orgMapper.selectOne(orgQuery);
                        }
                    }
                    //org存在则更新
                    if (Objects.nonNull(orgPo)) {
                        orgPo.setSyncTime(new Date());
                        orgPo.setDescription(excelItem.getDescription());
                        orgPo.setAllowOperation(excelItem.getAllowOperation());
                        orgPo.setSourceOrgId(excelItem.getSourceOrgId());
                        orgPo.setOrgName(orgPo.getOrgName());
                        orgMapper.updateById(orgPo);
                    }
                    //不存在则新增
                    else {
                        orgPo = new OrgPo();
                        orgPo.setSyncTime(new Date());
                        orgPo.setSourceOrgId(excelItem.getSourceOrgId());
                        orgPo.setCreateTime(new Date());
                        orgPo.setStoreId(excelItem.getStoreId());
                        orgPo.setAllowOperation("T");
                        orgPo.setDescription(excelItem.getDescription());
                        orgPo.setOrgLevel("3");
                        orgPo.setOrgName(excelItem.getOrgName());
                        orgPo.setParentOrgId("1398");
                        orgPo.setStatusCd("0");
                        orgPo.setFlag(2);
                        orgPo.setOrgId(excelItem.getOrgId());
                        orgMapper.insert(orgPo);
                    }
                } else {
                    LambdaQueryWrapper<OrgPo> orgQuery = new LambdaQueryWrapper<>();
                    orgQuery.eq(OrgPo::getSourceOrgId, excelItem.getSourceOrgId());
                    orgQuery.eq(OrgPo::getStatusCd, "0");
                    orgPo = orgMapper.selectOne(orgQuery);
                }
            }

            //用户的数字化平台来源id为空则跳过不同步
            if (StringUtils.isBlank(excelItem.getSourceOrgId())){
                continue;
            }
            //处理用户和关系
            DictPo dictPo = getOrGenDictJob(excelItem.getRelCdName(), excelItem.getRelCd());
            UserPo userPo = userMapper.selectById(excelItem.getUserId());
            if (Objects.isNull(userPo)){
                LambdaQueryWrapper<UserPo> userQuery = new LambdaQueryWrapper<>();
                if (StringUtils.isNotBlank(excelItem.getSourceOrgId())) {
                    userQuery.eq(UserPo::getSourceUserId, excelItem.getSourceOrgId());
                    userQuery.eq(UserPo::getStatusCd, 0);
                    userPo = userMapper.selectOne(userQuery);
                }
                //再用nickName匹配
                if (Objects.isNull(userPo)){
                    userQuery = new LambdaQueryWrapper<>();
                    userQuery.eq(UserPo::getName, excelItem.getUserName());
                    userQuery.eq(UserPo::getStatusCd, 0);
                    userPo = userMapper.selectOne(userQuery);
                }
            }
            //用户存在更新用户
            if (Objects.nonNull(userPo)) {
                userPo.setSourceUserId(excelItem.getSourceUserId());
                userPo.setTel(excelItem.getTel());
                userPo.setSyncDate(new Date());
                userPo.setName(excelItem.getUserName());
                userPo.setAddress(excelItem.getAddress());
                userPo.setCardNumber(excelItem.getCardNumber());
                userPo.setEmail(excelItem.getEmail());
                userPo.setSex(excelItem.getSex());
                userMapper.updateById(userPo);
                exsitsUserId.add(userPo.getUserId());

                //获取用户部门关系
                List<OrgStaffVo> orgStaffRelVos = orgStaffRelMapper.selectOrgUser(null, userPo.getUserId());
                //用户不在该部门
                if (CollectionUtils.isEmpty(orgStaffRelVos)) {
                    //插入用户部门映射关系
                    OrgStaffRelPo orgStaffRelPo = new OrgStaffRelPo();
                    orgStaffRelPo.setRelId(excelItem.getRelId());
                    orgStaffRelPo.setCreateTime(new Date());
                    orgStaffRelPo.setOrgId(orgPo.getOrgId());
                    orgStaffRelPo.setRelCd(dictPo.getStatusCd());
                    orgStaffRelPo.setStaffId(userPo.getUserId());
                    orgStaffRelPo.setStoreId("402021121452660010");
                    orgStaffRelPo.setFlag("2");
                    orgStaffRelPo.setBId("-8");
                    orgStaffRelPo.setSyncTime(new Date());
                    orgStaffRelMapper.insert(orgStaffRelPo);
                }else {
                    boolean orgStaffExistFlag = false;
                    for (OrgStaffVo orgStaff: orgStaffRelVos){
                        //通过部门+岗位匹配用户，若用户不存在则直接作废掉
                        if (!(StringUtils.equals(orgStaff.getOrgId(), orgPo.getOrgId()) && StringUtils.equals(orgStaff.getRelCd(), dictPo.getStatusCd()))){
                            OrgStaffRelPo updateDo = new OrgStaffRelPo();
                            updateDo.setStatusCd("1");
                            LambdaQueryWrapper<OrgStaffRelPo> orgStaffRelQuery = new LambdaQueryWrapper<>();
                            orgStaffRelQuery.eq(OrgStaffRelPo::getRelId, orgStaff.getRelId());
                            orgStaffRelMapper.update(updateDo, orgStaffRelQuery);
                        }else {
                            orgStaffExistFlag = true;
                        }
                    }
                    if (!orgStaffExistFlag){
                        //插入用户部门映射关系
                        OrgStaffRelPo orgStaffRelPo = new OrgStaffRelPo();
                        orgStaffRelPo.setRelId(excelItem.getRelId());
                        orgStaffRelPo.setCreateTime(new Date());
                        orgStaffRelPo.setOrgId(orgPo.getOrgId());
                        orgStaffRelPo.setRelCd(dictPo.getStatusCd());
                        orgStaffRelPo.setStaffId(userPo.getUserId());
                        orgStaffRelPo.setStoreId("402021121452660010");
                        orgStaffRelPo.setFlag("2");
                        orgStaffRelPo.setBId("-8");
                        orgStaffRelPo.setSyncTime(new Date());
                        orgStaffRelMapper.insert(orgStaffRelPo);
                    }
                }
            }else {
                //用户不存在插入用户
                userPo = new UserPo();
                userPo.setSex(excelItem.getSex());
                userPo.setEmail(excelItem.getEmail());
                userPo.setCardNumber(excelItem.getCardNumber());
                userPo.setAddress(excelItem.getAddress());
                userPo.setName(excelItem.getUserName());
                userPo.setLevelCd("01");
                userPo.setFlag("2");
                userPo.setSyncDate(new Date());
                userPo.setStatusCd("0");
                userPo.setCreateTime(new Date());
                userPo.setPassword("9ef8b90414d6ff1202baa99049adb5cd");
                userPo.setUserId(excelItem.getUserId());
                userPo.setInvitorId(excelItem.getInvitorId());
                userPo.setTel(excelItem.getTel());
                userPo.setSourceUserId(excelItem.getSourceUserId());
                userPo.setBId("-8");
                userMapper.insert(userPo);
                exsitsUserId.add(userPo.getUserId());

                //插入用户部门映射关系
                OrgStaffRelPo orgStaffRelPo = new OrgStaffRelPo();
                orgStaffRelPo.setRelId(excelItem.getRelId());
                orgStaffRelPo.setCreateTime(new Date());
                orgStaffRelPo.setOrgId(orgPo.getOrgId());
                orgStaffRelPo.setRelCd(dictPo.getStatusCd());
                orgStaffRelPo.setStaffId(userPo.getUserId());
                orgStaffRelPo.setStoreId("402021121452660010");
                orgStaffRelPo.setFlag("2");
                orgStaffRelPo.setBId("-8");
                orgStaffRelPo.setSyncTime(new Date());
                orgStaffRelMapper.insert(orgStaffRelPo);
            }

            //商户用户插入
            LambdaQueryWrapper<StoreUserPo> storeUserQuery = new LambdaQueryWrapper<>();
            storeUserQuery.eq(StoreUserPo::getUserId, userPo.getUserId());
            storeUserQuery.eq(StoreUserPo::getStoreId, "402021121452660010");
            storeUserQuery.eq(StoreUserPo::getStatusCd, "0");
            if (storeUserMapper.selectCount(storeUserQuery) < 1){
                StoreUserPo storeUserPo = new StoreUserPo();
                storeUserPo.setStoreUserId(KeyUtils.genKey("45"));
                storeUserPo.setUserId(userPo.getUserId());
                storeUserPo.setBId("-8");
                storeUserPo.setStoreId("402021121452660010");
                storeUserPo.setFlag("2");
                storeUserPo.setCreateTime(new Date());
                storeUserPo.setRelCd("600311000002");
                storeUserPo.setStatusCd("0");
                storeUserMapper.insert(storeUserPo);
            }
            //普通用户权限
            LambdaQueryWrapper<PrivilegeUserPo> privilegeUserQuery = new LambdaQueryWrapper<>();
            privilegeUserQuery.eq(PrivilegeUserPo::getUserId, userPo.getUserId());
            privilegeUserQuery.eq(PrivilegeUserPo::getStoreId, "402021121452660010");
            privilegeUserQuery.eq(PrivilegeUserPo::getStatusCd, "0");
            privilegeUserQuery.eq(PrivilegeUserPo::getPId, "6002022010746310005");
            if (privilegeUserMapper.selectCount(privilegeUserQuery) < 1){
                PrivilegeUserPo privilegeUserPo = new PrivilegeUserPo();
                privilegeUserPo.setUserId(userPo.getUserId());
                privilegeUserPo.setPId("6002022010746310005");
                privilegeUserPo.setStoreId("402021121452660010");
                privilegeUserPo.setPrivilege_flag("1");
                privilegeUserPo.setCreateTime(new Date());
                privilegeUserPo.setStatusCd("0");
                privilegeUserMapper.insert(privilegeUserPo);
            }
        }

        //不存在用户体系且非手动新增的用户
        List<String> noExistUserIds =userMapper.selectUserIdByNotInUserAndFlagNotNull(exsitsUserId);
        if (!CollectionUtils.isEmpty(noExistUserIds)){
            //用户置为无效
            UserPo updateUserPo = new UserPo();
            updateUserPo.setStatusCd("1");
            updateUserPo.setSyncDate(new Date());
            LambdaQueryWrapper<UserPo> userUpdateQuery = new LambdaQueryWrapper<>();
            userUpdateQuery.in(UserPo::getUserId, noExistUserIds);
            userMapper.update(updateUserPo, userUpdateQuery);
            //用户组织关系置为无效
            OrgStaffRelPo orgStaffRelUpdatePo = new OrgStaffRelPo();
            orgStaffRelUpdatePo.setSyncTime(new Date());
            orgStaffRelUpdatePo.setStatusCd("1");
            LambdaQueryWrapper<OrgStaffRelPo> orgStaffRelUpdateQuery = new LambdaQueryWrapper<>();
            orgStaffRelUpdateQuery.in(OrgStaffRelPo::getStaffId, noExistUserIds);
            orgStaffRelUpdateQuery.eq(OrgStaffRelPo::getStatusCd, "0");
            orgStaffRelMapper.update(orgStaffRelUpdatePo, orgStaffRelUpdateQuery);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void writeToAttendanceSys() {
        List<OrgStaffRelExcel> excels =orgStaffRelMapper.selectExportSourceData();
        for (OrgStaffRelExcel item: excels){
            //先以SourceUserId匹配
            SysUserPo sysUserPo = attendanceSysService.getSysUserBySourceUserId(item.getSourceUserId());
            if (Objects.isNull(sysUserPo)){
                //SourceUserId匹配不上使用realname匹配
                List<SysUserPo> sysUserPos = attendanceSysService.getSysUserByRealname(item.getUserName());
                //如果名字只有一个用户则直接命中
                if (sysUserPos.size() == 1){
                    sysUserPo = sysUserPos.get(0);
                }else {
                    //如果多个则使用手机号再匹配一次
                    for (SysUserPo userItem: sysUserPos) {
                        if (StringUtils.equals(userItem.getPhone(), item.getTel())){
                            sysUserPo = userItem;
                            break;
                        }
                    }
                }
            }
            //如果匹配不上用户则跳出
            if (Objects.isNull(sysUserPo)){
                continue;
            }
            //更新用户SourceUserId，后续可直接使用SourceUserId
            sysUserPo.setPost(item.getRelCdName());
            sysUserPo.setSourceUserId(item.getSourceUserId());
            sysUserPo.setSyncTime(new Date());
            attendanceSysService.updateSysUserById(sysUserPo);

            //根据用户id查询科室
            List<SysUserDepartVo> sysUserDepartVos = attendanceSysService.getUserDepartByUserId(sysUserPo.getId());
            if (CollectionUtils.isEmpty(sysUserDepartVos)){
                SysDepartPo sysDepartPo = attendanceSysService.getDepartIdByName(item.getOrgName());
                if (Objects.isNull(sysDepartPo)){
                    throw new BusinessException("500",sysDepartPo.getDepartName()+"，科室找不到");
                }else {
                    //插入用户科室
                    SysUserDepartPo sysUserDepartPo = new SysUserDepartPo();
                    sysUserDepartPo.setId(UuidUtil.get32UUID());
                    sysUserDepartPo.setUserId(sysUserPo.getId());
                    sysUserDepartPo.setDepId(sysDepartPo.getId());
                    attendanceSysService.insertUserDepart(sysUserDepartPo);
                }
            }else {
                boolean existsDepart = false;
                for (SysUserDepartVo userDepartVo: sysUserDepartVos){
                    //用户部门名称不同的删除
                    if (!StringUtils.equals(userDepartVo.getDepartName(), item.getOrgName())){
                        attendanceSysService.deleteSysDepartById(userDepartVo.getUserDepartId());
                    }else {
                        existsDepart = true;
                    }
                }
                //如果用户科室不存在则插入
                if (!existsDepart){
                    SysDepartPo sysDepartPo = attendanceSysService.getDepartIdByName(item.getOrgName());
                    if (Objects.isNull(sysDepartPo)){
                        throw new BusinessException("500",sysDepartPo.getDepartName()+"，科室找不到");
                    }else {
                        //插入用户科室
                        SysUserDepartPo sysUserDepartPo = new SysUserDepartPo();
                        sysUserDepartPo.setId(UuidUtil.get32UUID());
                        sysUserDepartPo.setUserId(sysUserPo.getId());
                        sysUserDepartPo.setDepId(sysDepartPo.getId());
                        attendanceSysService.insertUserDepart(sysUserDepartPo);
                    }
                }
            }
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncFromVisitorDb() {
        logger.info("【syncFromVisitorDb】开始从visitor_db_guoshui同步数据到tt");

        // 0. 先同步部门 sys_depart → u_org
        syncDeparts();

        // 1. 从visitor_db_guoshui(ds2)获取所有活跃用户
        List<SysUserPo> sysUsers = attendanceSysService.getAllActiveSysUsers();
        logger.info("【syncFromVisitorDb】从visitor_db_guoshui获取到{}条活跃用户数据", sysUsers.size());

        int updateUserCount = 0;
        int insertUserCount = 0;
        int insertRelCount = 0;
        int skipCount = 0;

        for (SysUserPo sysUser : sysUsers) {
            try {
                // 2. 在tt(ds1)中查找匹配的用户
                UserPo userPo = null;

                // 先用sourceUserId匹配（u_user.source_user_id = sys_user.id）
                if (StringUtils.isNotBlank(sysUser.getId())) {
                    LambdaQueryWrapper<UserPo> userQuery = new LambdaQueryWrapper<>();
                    userQuery.eq(UserPo::getPersonId, sysUser.getId());
                    userQuery.eq(UserPo::getStatusCd, 0);
                    userPo = userMapper.selectOne(userQuery);
                }

                // 再用realname匹配
                if (Objects.isNull(userPo) && StringUtils.isNotBlank(sysUser.getRealname())) {
                    LambdaQueryWrapper<UserPo> userQuery = new LambdaQueryWrapper<>();
                    userQuery.eq(UserPo::getName, sysUser.getRealname());
                    userQuery.eq(UserPo::getStatusCd, 0);
                    List<UserPo> matchUsers = userMapper.selectList(userQuery);
                    if (!CollectionUtils.isEmpty(matchUsers)) {
                        if (matchUsers.size() == 1) {
                            userPo = matchUsers.get(0);
                        } else {
                            // 多个同名用户时，用手机号再匹配
                            for (UserPo u : matchUsers) {
                                if (StringUtils.isNotBlank(sysUser.getPhone()) && StringUtils.equals(u.getTel(), sysUser.getPhone())) {
                                    userPo = u;
                                    break;
                                }
                            }
                        }
                    }
                }

                if (Objects.nonNull(userPo)) {
                    // 3. 用户已存在，只更新非空字段，不强制覆盖
                    boolean needUpdate = false;

                    if (StringUtils.isNotBlank(sysUser.getRealname())) {
                        userPo.setName(sysUser.getRealname());
                        needUpdate = true;
                    }
                    if (StringUtils.isNotBlank(sysUser.getPhone())) {
                        userPo.setTel(sysUser.getPhone());
                        needUpdate = true;
                    }
                    // 补充sourceUserId关联
                    if (StringUtils.isBlank(userPo.getPersonId()) && StringUtils.isNotBlank(sysUser.getId())) {
                        userPo.setPersonId(sysUser.getId());
                        needUpdate = true;
                    }

                    if (needUpdate) {
                        userPo.setSyncDate(new Date());
                        userMapper.updateById(userPo);
                        updateUserCount++;
                    }

                    // 4. 同步用户部门关系 sys_user_depart → u_org_staff_rel
                    syncUserDepartRel(sysUser, userPo);
                } else {
                    // 5. 用户不存在，创建新用户
                    logger.info("【syncFromVisitorDb】在tt中未找到匹配用户: {}({}), 创建新用户", sysUser.getRealname(), sysUser.getId());

                    String invitorId;
                    do {
                        invitorId = RandomStringUtils.randomNumeric(6);
                    } while (Objects.nonNull(userMapper.hasSameInvitorId(invitorId)));

                    String userId = StringUtils.isBlank(sysUser.getUsername()) ? sysUser.getId() : sysUser.getUsername();
                    // 检查userId是否已存在
                    UserPo existUser = userMapper.selectById(userId);
                    if (Objects.nonNull(existUser)) {
                        userId = sysUser.getId();
                    }

                    userPo = new UserPo();
                    userPo.setUserId(userId);
                    userPo.setName(sysUser.getRealname());
                    userPo.setTel(sysUser.getPhone());
                    userPo.setSourceUserId(sysUser.getId());
                    userPo.setLevelCd("01");
                    userPo.setStatusCd("0");
                    userPo.setInvitorId(invitorId);
                    userPo.setBId("-8");
                    userPo.setFlag("2");
                    userPo.setPassword("9ef8b90414d6ff1202baa99049adb5cd");
                    userPo.setCreateTime(new Date());
                    userPo.setSyncDate(new Date());
                    userMapper.insert(userPo);
                    insertUserCount++;

                    // 同步部门关系
                    syncUserDepartRel(sysUser, userPo);

                    // 商户用户插入
                    ensureStoreUser(userPo.getUserId());

                    // 普通用户权限
                    ensurePrivilegeUser(userPo.getUserId());
                }
            } catch (Exception e) {
                logger.error("【syncFromVisitorDb】同步用户失败: {}({})", sysUser.getRealname(), sysUser.getId(), e);
                skipCount++;
            }
        }

        logger.info("【syncFromVisitorDb】同步完成，更新用户: {}，新增用户: {}，新增关系: {}，跳过: {}",
                updateUserCount, insertUserCount, insertRelCount, skipCount);
    }

    /**
     * 同步单个用户的部门关系（sys_user_depart → u_org_staff_rel）
     */
    private void syncUserDepartRel(SysUserPo sysUser, UserPo userPo) {
        List<SysUserDepartVo> sysUserDeparts = attendanceSysService.getUserDepartByUserId(sysUser.getId());
        if (CollectionUtils.isEmpty(sysUserDeparts)) {
            return;
        }

        // 获取用户当前在tt中的所有部门关系
        List<OrgStaffVo> currentOrgStaffVos = orgStaffRelMapper.selectOrgUser(null, userPo.getUserId());
        List<String> matchedOrgIds = new ArrayList<>();

        for (SysUserDepartVo sysUserDepart : sysUserDeparts) {
            if (StringUtils.isBlank(sysUserDepart.getDepartName())) {
                continue;
            }
            // 通过部门名称在tt中匹配u_org
            OrgPo orgPo = findOrgByName(sysUserDepart.getDepartName());
            if (Objects.isNull(orgPo)) {
                logger.warn("【syncFromVisitorDb】在tt中未找到匹配的部门: {}", sysUserDepart.getDepartName());
                continue;
            }

            matchedOrgIds.add(orgPo.getOrgId());

            // 检查是否已存在该关系
            boolean relExists = false;
            if (!CollectionUtils.isEmpty(currentOrgStaffVos)) {
                for (OrgStaffVo orgStaff : currentOrgStaffVos) {
                    if (StringUtils.equals(orgStaff.getOrgId(), orgPo.getOrgId())) {
                        relExists = true;
                        break;
                    }
                }
            }

            if (!relExists) {
                DictPo dictPo = getOrGenDictJob(null, null);
                OrgStaffRelPo orgStaffRelPo = new OrgStaffRelPo();
                orgStaffRelPo.setRelId(KeyUtils.genKey("84"));
                orgStaffRelPo.setCreateTime(new Date());
                orgStaffRelPo.setOrgId(orgPo.getOrgId());
                orgStaffRelPo.setRelCd(dictPo.getStatusCd());
                orgStaffRelPo.setStaffId(userPo.getUserId());
                orgStaffRelPo.setStoreId("402021121452660010");
                orgStaffRelPo.setFlag("2");
                orgStaffRelPo.setBId("-8");
                orgStaffRelPo.setSyncTime(new Date());
                orgStaffRelMapper.insert(orgStaffRelPo);
            }
        }

        // 用户在tt中有部门关系，但visitor_db_guoshui中没有对应部门，则作废多余关系
        if (!CollectionUtils.isEmpty(currentOrgStaffVos) && !CollectionUtils.isEmpty(matchedOrgIds)) {
            for (OrgStaffVo orgStaff : currentOrgStaffVos) {
                if (!matchedOrgIds.contains(orgStaff.getOrgId()) && StringUtils.isNotBlank(orgStaff.getUserFlag())) {
                    OrgStaffRelPo updateDo = new OrgStaffRelPo();
                    updateDo.setStatusCd("1");
                    updateDo.setSyncTime(new Date());
                    LambdaQueryWrapper<OrgStaffRelPo> relQuery = new LambdaQueryWrapper<>();
                    relQuery.eq(OrgStaffRelPo::getRelId, orgStaff.getRelId());
                    orgStaffRelMapper.update(updateDo, relQuery);
                }
            }
        }
    }

    /**
     * 同步部门 sys_depart → u_org
     * 从visitor_db_guoshui获取所有部门，在tt中按名称匹配，不存在则新建
     */
    private void syncDeparts() {
        List<SysDepartPo> sysDeparts = attendanceSysService.getAllActiveDeparts();
        logger.info("【syncDeparts】从visitor_db_guoshui获取到{}条部门数据", sysDeparts.size());

        // 获取tt中当前所有有效部门（parentOrgId=1398下）
        LambdaQueryWrapper<OrgPo> orgQuery = new LambdaQueryWrapper<>();
        orgQuery.eq(OrgPo::getParentOrgId, "1398");
        orgQuery.eq(OrgPo::getStatusCd, "0");
        List<OrgPo> existingOrgs = orgMapper.selectList(orgQuery);

        int updateCount = 0;
        int insertCount = 0;

        for (SysDepartPo sysDepart : sysDeparts) {
            if (StringUtils.isBlank(sysDepart.getDepartName())) {
                continue;
            }
            // 按名称在tt中匹配部门
            OrgPo matchedOrg = null;
            for (OrgPo org : existingOrgs) {
                if (StringUtils.equals(org.getOrgName(), sysDepart.getDepartName())) {
                    matchedOrg = org;
                    break;
                }
            }

            if (Objects.nonNull(matchedOrg)) {
                // 部门已存在，更新同步时间和来源关联
                matchedOrg.setSyncTime(new Date());
                matchedOrg.setDeptId(sysDepart.getId());
                orgMapper.updateById(matchedOrg);
                updateCount++;
            } else {
                // 部门不存在，新建
                OrgPo newOrg = new OrgPo();
                newOrg.setOrgId(KeyUtils.genKey("84"));
                newOrg.setOrgName(sysDepart.getDepartName());
                newOrg.setDeptId(sysDepart.getId());
                newOrg.setParentOrgId("1398");
                newOrg.setOrgLevel("3");
                newOrg.setStatusCd("0");
                newOrg.setAllowOperation("T");
                newOrg.setStoreId(existingOrgs.isEmpty() ? null : existingOrgs.get(0).getStoreId());
                newOrg.setFlag(2);
                newOrg.setCreateTime(new Date());
                newOrg.setSyncTime(new Date());
                newOrg.setBId("-8");
                newOrg.setBelongCommunityId("");
                orgMapper.insert(newOrg);
                existingOrgs.add(newOrg);
                insertCount++;
                logger.info("【syncDeparts】新建部门: {}(sourceOrgId={})", sysDepart.getDepartName(), sysDepart.getId());
            }
        }

        logger.info("【syncDeparts】部门同步完成，更新: {}，新增: {}", updateCount, insertCount);
    }

    /**
     * 根据部门名称在tt中查找u_org
     */
    private OrgPo findOrgByName(String orgName) {
        LambdaQueryWrapper<OrgPo> query = new LambdaQueryWrapper<>();
        query.eq(OrgPo::getOrgName, orgName);
        query.eq(OrgPo::getStatusCd, "0");
        return orgMapper.selectOne(query);
    }

    /**
     * 确保商户用户记录存在
     */
    private void ensureStoreUser(String userId) {
        LambdaQueryWrapper<StoreUserPo> storeUserQuery = new LambdaQueryWrapper<>();
        storeUserQuery.eq(StoreUserPo::getUserId, userId);
        storeUserQuery.eq(StoreUserPo::getStoreId, "402021121452660010");
        storeUserQuery.eq(StoreUserPo::getStatusCd, "0");
        if (storeUserMapper.selectCount(storeUserQuery) < 1) {
            StoreUserPo storeUserPo = new StoreUserPo();
            storeUserPo.setStoreUserId(KeyUtils.genKey("45"));
            storeUserPo.setUserId(userId);
            storeUserPo.setBId("-8");
            storeUserPo.setStoreId("402021121452660010");
            storeUserPo.setFlag("2");
            storeUserPo.setCreateTime(new Date());
            storeUserPo.setRelCd("600311000002");
            storeUserPo.setStatusCd("0");
            storeUserMapper.insert(storeUserPo);
        }
    }

    /**
     * 确保普通用户权限记录存在
     */
    private void ensurePrivilegeUser(String userId) {
        LambdaQueryWrapper<PrivilegeUserPo> privilegeUserQuery = new LambdaQueryWrapper<>();
        privilegeUserQuery.eq(PrivilegeUserPo::getUserId, userId);
        privilegeUserQuery.eq(PrivilegeUserPo::getStoreId, "402021121452660010");
        privilegeUserQuery.eq(PrivilegeUserPo::getStatusCd, "0");
        privilegeUserQuery.eq(PrivilegeUserPo::getPId, "6002022010746310005");
        if (privilegeUserMapper.selectCount(privilegeUserQuery) < 1) {
            PrivilegeUserPo privilegeUserPo = new PrivilegeUserPo();
            privilegeUserPo.setUserId(userId);
            privilegeUserPo.setPId("6002022010746310005");
            privilegeUserPo.setStoreId("402021121452660010");
            privilegeUserPo.setPrivilege_flag("1");
            privilegeUserPo.setCreateTime(new Date());
            privilegeUserPo.setStatusCd("0");
            privilegeUserMapper.insert(privilegeUserPo);
        }
    }


    private String getStringValue(Map<String, Object> map, String... keys) {
        for (String key : keys) {
            Object value = map.get(key);
            if (value != null && !String.valueOf(value).isEmpty()) {
                return String.valueOf(value);
            }
        }
        return "";
    }


}
