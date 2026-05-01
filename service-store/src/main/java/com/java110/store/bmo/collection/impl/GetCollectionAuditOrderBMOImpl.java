package com.java110.store.bmo.collection.impl;

import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.entity.audit.AuditUser;
import com.java110.intf.common.IGoodCollectionUserInnerServiceSMO;
import com.java110.intf.store.IPurchaseApplyInnerServiceSMO;
import com.java110.store.bmo.collection.IGetCollectionAuditOrderBMO;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import com.java110.vo.api.purchaseApply.PurchaseApplyDetailVo;
import com.java110.vo.api.resourceOrder.ApiOrderSimpleDataVo;
import com.java110.vo.api.resourceOrder.ApiResourceOrderDataVo;
import com.java110.vo.api.resourceOrder.ApiResourceSimpleDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


@Service("getCollectionAuditOrderBMOImpl")
public class GetCollectionAuditOrderBMOImpl implements IGetCollectionAuditOrderBMO {

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;


    @Autowired
    private IGoodCollectionUserInnerServiceSMO goodCollectionUserInnerServiceSMOImpl;


    @Override
    public ResponseEntity<String> auditOrder(AuditUser auditUser) {
        //物品领用待办 （默认只查询和当前登录用户相关并且是审批或者结束待办事项）
        long count = goodCollectionUserInnerServiceSMOImpl.getUserTaskCount(auditUser);

        if (Objects.isNull(auditUser.getType())) {
            List<ApiResourceOrderDataVo> auditOrders = null;

            if (count > 0) {
                List<PurchaseApplyDto> purchaseApplyDtos = goodCollectionUserInnerServiceSMOImpl.getUserTasks(auditUser);
                auditOrders = BeanConvertUtil.covertBeanList(purchaseApplyDtos, ApiResourceOrderDataVo.class);
                for (ApiResourceOrderDataVo apiResourceOrderDataVo : auditOrders) {
                    switch (apiResourceOrderDataVo.getState()) {
                        case "1000":
                            apiResourceOrderDataVo.setStateName("待审核");
                            break;
                        case "1001":
                            apiResourceOrderDataVo.setStateName("审核中");
                            break;
                        case "1002":
                            apiResourceOrderDataVo.setStateName("已审核");
                            break;
                    }
                    apiResourceOrderDataVo.setResOrderTypeName("出库申请");
                }
            } else {
                auditOrders = new ArrayList<>();
            }

            return ResultVo.createResponseEntity((int) Math.ceil((double) count / (double) auditUser.getRow()), (int) count, auditOrders);
        } else {
            List<ApiOrderSimpleDataVo> auditOrders;

            if (count > 0) {
                List<PurchaseApplyDto> purchaseApplyDtos = goodCollectionUserInnerServiceSMOImpl.getUserTasks(auditUser);
                // 整理数据
                auditOrders = new ArrayList<>();
                for (PurchaseApplyDto purchaseApplyDto : purchaseApplyDtos) {
                    ApiOrderSimpleDataVo orderSimpleDataVo = new ApiOrderSimpleDataVo();
                    orderSimpleDataVo.setApplyOrderId(purchaseApplyDto.getApplyOrderId());
                    orderSimpleDataVo.setTaskId(purchaseApplyDto.getTaskId());

                    List<PurchaseApplyDetailVo> purchaseApplyDetailVos = purchaseApplyDto.getPurchaseApplyDetailVo();
                    ArrayList<ApiResourceSimpleDataVo> apiResourceSimpleDataVos = new ArrayList<>();
                    for (PurchaseApplyDetailVo purchaseApplyDetailVo : purchaseApplyDetailVos) {
                        ApiResourceSimpleDataVo apiResourceSimpleDataVo = new ApiResourceSimpleDataVo();
                        apiResourceSimpleDataVo.setId(purchaseApplyDetailVo.getId());
                        apiResourceSimpleDataVo.setResName(purchaseApplyDetailVo.getResName());
                        apiResourceSimpleDataVo.setQuantity(purchaseApplyDetailVo.getQuantity());
                        apiResourceSimpleDataVo.setStock(purchaseApplyDetailVo.getStock());
                        apiResourceSimpleDataVo.setResId(purchaseApplyDetailVo.getResId());
                        apiResourceSimpleDataVos.add(apiResourceSimpleDataVo);
                    }

                    orderSimpleDataVo.setResources(apiResourceSimpleDataVos);

                    orderSimpleDataVo.setUserName(purchaseApplyDto.getUserName());
                    orderSimpleDataVo.setDepartPosition(purchaseApplyDto.getDepartment() + purchaseApplyDto.getPosition());
                    orderSimpleDataVo.setTel(purchaseApplyDto.getTel());
                    orderSimpleDataVo.setDescription(purchaseApplyDto.getDescription());
                    orderSimpleDataVo.setState(purchaseApplyDto.getState());
                    orderSimpleDataVo.setResOrderType(purchaseApplyDto.getResOrderType());

                    auditOrders.add(orderSimpleDataVo);
                }

            } else {
                auditOrders = Collections.emptyList();
            }

            return ResultVo.createResponseEntity((int) Math.ceil((double) count / (double) auditUser.getRow()), (int) count, auditOrders);
        }

    }
}
