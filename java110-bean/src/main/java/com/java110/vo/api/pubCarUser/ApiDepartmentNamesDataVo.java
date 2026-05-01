package com.java110.vo.api.pubCarUser;

import java.io.Serializable;

public class ApiDepartmentNamesDataVo implements Serializable {

    private static final long serialVersionUID = 3964953310754446311L;

    private String departmentId;
    private String departmentName;

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
