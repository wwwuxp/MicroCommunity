package com.hezong.pubcar.smo.impl;


import com.alibaba.fastjson.JSONArray;
import com.hezong.pubcar.dao.PubcarRepairFormFlowDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.pubcar.PubcarRepairFormFlow;
import com.java110.entity.user.TTemp;
import com.java110.intf.pubcar.PubcarRepairFormFlowSMO;
import com.java110.intf.user.TTempSMO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * (PubcarRepairFormFlow)表接口feign层
 *
 * @author lmf
 * @since 2022-09-21 09:07:32
 */
@RestController
public class PubcarRepairFormFlowSMOImpl extends BaseServiceSMO implements PubcarRepairFormFlowSMO {

    @Autowired
    private PubcarRepairFormFlowDao pubcarRepairFormFlowDao;

    @Autowired
    TTempSMO tTempSMO;

    @Override
    public PubcarRepairFormFlow queryById(@RequestBody String flowId){
        return pubcarRepairFormFlowDao .queryById(flowId);
    }

    @Override
    public List<PubcarRepairFormFlow> queryList(@RequestBody PubcarRepairFormFlow pubcarRepairFormFlow){
        return pubcarRepairFormFlowDao .queryList(pubcarRepairFormFlow);
    }

    @Override
    public long count(@RequestBody PubcarRepairFormFlow pubcarRepairFormFlow){
        return pubcarRepairFormFlowDao .count(pubcarRepairFormFlow);
    }

    @Override
    public int insert(@RequestBody PubcarRepairFormFlow pubcarRepairFormFlow){
        return pubcarRepairFormFlowDao .insert(pubcarRepairFormFlow);
    }

    @Override
    public int insertBatch(@RequestBody List<PubcarRepairFormFlow> entities){
        return pubcarRepairFormFlowDao .insertBatch(entities);
    }

    @Override
    public int insertOrUpdateBatch(@RequestBody List<PubcarRepairFormFlow> entities){
        return pubcarRepairFormFlowDao .insertOrUpdateBatch(entities);
    }

    @Override
    public int update(@RequestBody PubcarRepairFormFlow pubcarRepairFormFlow){
        return pubcarRepairFormFlowDao .update(pubcarRepairFormFlow);
    }

    @Override
    public int deleteById(@RequestBody String flowId){
        return pubcarRepairFormFlowDao .deleteById(flowId);
    }

    @Override
    public List<PubcarRepairFormFlow> queryListByIds(@RequestBody List<String> ids) {
        return pubcarRepairFormFlowDao .queryListByIds(ids);
    }

    /**
     * 插入流程表
     * @param param
     */
    @Override
    public int insertFlow(@RequestBody PubcarRepairFormFlow param){
        // 当前时间
        Date now = new Date();

        // 获取到修车审批的id
        String repairId = param.getRepairId();
        // // 查看是否存在审批状态
        String status = param.getStatus();
        // 判断是否存在用户id 存在表示这里是审批
        // 数据列表
        List<PubcarRepairFormFlow> dataArr;
        List<PubcarRepairFormFlow> paramList = new ArrayList<>();
        if(StringUtils.isBlank(status)){
            TTemp tTemp = tTempSMO.queryOne(new TTemp("RepairFrom"));
            if(tTemp != null){
                // 这里获取模板
                //[{
                //     "userId":"xxx",
                //     "userName":"xxx",
                //     "flowName":"副科长审批",
                //     "index":"1"
                // }]
                String tempValue = tTemp.getTempValue();
                dataArr = JSONArray.parseArray(tempValue, PubcarRepairFormFlow.class);
                // 添加申请人步骤
                PubcarRepairFormFlow oneEntity = new PubcarRepairFormFlow();
                oneEntity.setFlowId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.REPAIR_MANAGEMENT_FLOW_DATA_ID));
                oneEntity.setFlowStep(0);
                oneEntity.setStatus(PAST);
                oneEntity.setRepairId(repairId);
                oneEntity.setUserName(param.getUserName());
                oneEntity.setUserId(param.getUserId());
                oneEntity.setIndex(0);
                oneEntity.setFlowName("发起申请");
                oneEntity.setRemark("发起申请");
                oneEntity.setCreateTime(now);
                oneEntity.setUpdateTime(now);
                pubcarRepairFormFlowDao.insert(oneEntity);
                // 同步骤的用户拼装数据插入
                Map<Integer, PubcarRepairFormFlow> flowMap = dataArr.stream().collect(Collectors.toMap(k -> k.getIndex(), v -> v, (val1, val2) -> {
                    val1.setUserId(val1.getUserId() + "," + val2.getUserId());
                    val1.setUserName(val1.getUserName() + "," + val2.getUserName());
                    return val1;
                }));
                flowMap.values().forEach(r->{
                    if(r.getIndex() == 1 && r.getUserId().equals(param.getUserId())){
                        return;
                    }
                    String newId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.REPAIR_MANAGEMENT_FLOW_DATA_ID);
                    r.setStatus(NOT_APPROVED);
                    r.setFlowId(newId);
                    r.setRepairId(repairId);
                    r.setCreateTime(now);
                    pubcarRepairFormFlowDao.insert(r);
                });
                return OK;
            }else {
                // 如果没有查询到模板返回错误信息
                return ERR;
            }
        } else {
            // 这里是审批的逻辑
            //首先查询出所有流程的步骤
            dataArr = pubcarRepairFormFlowDao.queryList(new PubcarRepairFormFlow(repairId));
            if(dataArr != null && dataArr.size() > 0){
                //然后循环判断是否审批过，如果没有，判断userid是否与审批的相同，如果不同，那么返回错误，如果相同，修改状态且判断是否为最后一条
                for (int i = 0, len = dataArr.size(); i < len; i++) {
                    PubcarRepairFormFlow entity = dataArr.get(i);
                    String entityStatus = entity.getStatus();
                    //判断是否为撤回
                    entity.setUpdateTime(now);
                    if(GO_BACK_ONE_STEP.equals(param.getStatus())){
                        // 判断是否为发起人 不是不允许撤回
                        if(!dataArr.get(0).getUserId().equals(param.getUserId())){
                            return ERR;
                        }
                        // 如果为撤回，新增一条撤回的状态
                        entity.setFlowId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.REPAIR_MANAGEMENT_FLOW_DATA_ID));
                        entity.setFlowStep(null);
                        entity.setStatus(GO_BACK_ONE_STEP);
                        entity.setFlowName("撤回");
                        entity.setUpdateTime(now);
                        entity.setRemark(param.getRemark() == null ? "撤回" : param.getRemark());
                        pubcarRepairFormFlowDao.insert(entity);
                        return END;
                    }
                    if(entityStatus.equals(NOT_APPROVED)){

                        //这里表示未审批
                        String[] uids = entity.getUserId().split(",");
                        if(!Arrays.asList(uids).contains(param.getUserId())){
                            //审批用户不同的话，那么直接返回错误
                            return ERR;
                        }
                        entity.setRemark(param.getRemark());
                        // 取上一条的步长
                        Integer flowStep = dataArr.get(i-1).getFlowStep();
                        // 这里本来是做退回上一步的，但是现在直接撤回此消息
                        // if(GO_BACK_ONE_STEP.equals(param.getStatus())){
                        //     // 这里表示退回
                        //     if(entity.getIndex().intValue() == 0){
                        //         // 这里表示数据为第一条，返回异常
                        //         return ERR;
                        //     }
                        //     // 或当前实体
                        //     entity.setFlowId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.REPAIR_MANAGEMENT_FLOW_DATA_ID));
                        //     entity.setFlowStep(++flowStep);
                        //     entity.setStatus(GO_BACK_ONE_STEP);
                        //     entity.setUpdateTime(now);
                        //     entity.setRemark(param.getRemark() == null ? "退回" : param.getRemark());
                        //     pubcarRepairFormFlowDao.insert(entity);
                        //     // 取上一条数据
                        //     PubcarRepairFormFlow newEntity = getThePreviousOne(dataArr, i-1, dataArr.get(i-1));
                        //     // 初始化数据
                        //     newEntity.setFlowId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.REPAIR_MANAGEMENT_FLOW_DATA_ID));
                        //     newEntity.setFlowStep(null);
                        //     newEntity.setStatus(NOT_APPROVED);
                        //     newEntity.setUpdateTime(now);
                        //     pubcarRepairFormFlowDao.insert(newEntity);
                        //     return NEXT;
                        // }
                        if(PAST.equals(param.getStatus())){
                            //这里表示通过
                            entity.setStatus(PAST);
                            entity.setFlowStep(++flowStep);
                        }else if(FAIL.equals(param.getStatus())) {
                            //不通过直接返回结束
                            entity.setStatus(FAIL);
                            entity.setFlowStep(++flowStep);
                            pubcarRepairFormFlowDao.update(entity);
                            return END;
                        }else {
                            // 错误的状态编码
                            return ERR;
                        }
                        pubcarRepairFormFlowDao.update(entity);
                        //判断是否还有下一步
                        return i+1 == len ? END : NEXT;
                    }
                }
                return ERR;
            }else {
                // 如果没有查询到审批流程返回错误信息
                return ERR;
            }
        }
    }

    private PubcarRepairFormFlow getThePreviousOne(List<PubcarRepairFormFlow> dataArr, int index, PubcarRepairFormFlow entity){
        for (int i = index; i >= 0; i--) {
            PubcarRepairFormFlow pubcarRepairFormFlow = dataArr.get(i);
            if(pubcarRepairFormFlow.getIndex().intValue() == entity.getIndex().intValue() - 1) {
                return pubcarRepairFormFlow;
            }
        }
        return dataArr.get(index);
    }

}

