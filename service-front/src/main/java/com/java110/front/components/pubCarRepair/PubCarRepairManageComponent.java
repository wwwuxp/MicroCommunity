package com.java110.front.components.pubCarRepair;


import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarRepair.IListPubCarRepairsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 车辆维修信息表组件管理类
 * add by hh
 */
@Component("pubCarRepairManage")
public class PubCarRepairManageComponent {

    @Autowired
    private IListPubCarRepairsSMO listPubCarRepairsSMOImpl;

    /**
     * 查询车辆维修信息表列表
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd){
        return listPubCarRepairsSMOImpl.listPubCarRepairs(pd);
    }

}
