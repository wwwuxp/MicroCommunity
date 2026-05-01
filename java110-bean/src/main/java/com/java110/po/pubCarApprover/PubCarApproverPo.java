package com.java110.po.pubCarApprover;

import java.io.Serializable;

/**
 * @author hh
 * @date 2021/12/11
 */
public class PubCarApproverPo implements Serializable {

    private static final long serialVersionUID = 3489802042973990L;

    /**
     * 局领导
     */
    public static final Integer PUBCAR_BUREAU_LEADER = 1;

    /**
     * 用车管理员
     */
    public static final Integer PUBCAR_ADMINISTRATOR = 2;

    /**
     * 部门领导
     */
    public static final Integer DEPARTMENT_LEADER = 3;

    /**
     * 修车审批人
     */
    public static final Integer VEHICLE_REPAIR = 4;

    /**
     * 用车审批人
     */
    public static final Integer TYPE_PUBCAR = 1000;

    /**
     * 外出审批人
     */
    public static final Integer TYPE_OUTGOING = 2000;

    /**
     * 外出局领导
     */
    public static final Integer OUTGOING_BUREAU_LEADER = 2;

    /**
     * 局负责人
     */
    public static final Integer OUTGOING_BUREAU_HEADER = 1;


}
