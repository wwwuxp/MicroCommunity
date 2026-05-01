package com.java110.ext.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.java110.ext.excel.OrgStaffRelExcel;
import com.java110.ext.excel.listener.OrgStaffExportListener;
import com.java110.ext.mapper.DictMapper;
import com.java110.ext.mapper.OrgMapper;
import com.java110.ext.mapper.OrgStaffRelMapper;
import com.java110.ext.mapper.UserMapper;
import com.java110.ext.pojo.DictPo;
import com.java110.ext.pojo.OrgPo;
import com.java110.ext.pojo.OrgStaffRelPo;
import com.java110.ext.pojo.UserPo;
import com.java110.ext.service.ISyncDataService;
import com.java110.ext.utils.KeyUtils;
import com.java110.ext.vo.OrgStaffVo;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private RestTemplate restTemplate;

    @Autowired
    private DictMapper dictMapper;

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
