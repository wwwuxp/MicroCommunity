package com.java110.front.components.pubCarUser;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarUser.IListPubCarUsersSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 查询部门名称组件
 * 说明：部门表为项目原有表，为不入侵项目原有代码，将查询部门名称接口写入到用车人表(pubcar_car_user)所在类文件中。
 * add by hh
 */
@Component("listDepartmentNames")
public class ListDepartmentNameComponent {

    @Autowired
    private IListPubCarUsersSMO listPubCarUsersSMOImpl;

    /**
     * 查询应用列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listPubCarUsersSMOImpl.listDepartmentNames(pd);
    }

}
