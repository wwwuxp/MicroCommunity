package com.java110.vo.api.pubCarViolation;

import java.io.Serializable;

/**
 * @author hh
 * @date 2021/10/22
 */
public class ApiSimplePubCarViolationDataVo implements Serializable {

    private static final long serialVersionUID = 8238892135672454134L;

    private String violationId;
    private String violationDate;
    private String violationAddress;
    private String violationContent;
    private String deduction;
    private String fine;

    public String getViolationId() {
        return violationId;
    }

    public void setViolationId(String violationId) {
        this.violationId = violationId;
    }

    public String getViolationDate() {
        return violationDate;
    }

    public void setViolationDate(String violationDate) {
        this.violationDate = violationDate;
    }

    public String getViolationAddress() {
        return violationAddress;
    }

    public void setViolationAddress(String violationAddress) {
        this.violationAddress = violationAddress;
    }

    public String getViolationContent() {
        return violationContent;
    }

    public void setViolationContent(String violationContent) {
        this.violationContent = violationContent;
    }

    public String getDeduction() {
        return deduction;
    }

    public void setDeduction(String deduction) {
        this.deduction = deduction;
    }

    public String getFine() {
        return fine;
    }

    public void setFine(String fine) {
        this.fine = fine;
    }
}
