package com.java110.front.components.pubCar;


import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCar.IListPubCarsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 车辆管理表组件管理类
 *
 * add by wuxw
 *
 * 2019-06-29
 */
@Component("pubCarManage")
public class PubCarManageComponent {

    @Autowired
    private IListPubCarsSMO listPubCarsSMOImpl;

    /**
     * 查询车辆管理表列表
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd){
        return listPubCarsSMOImpl.listPubCars(pd);
    }

}
