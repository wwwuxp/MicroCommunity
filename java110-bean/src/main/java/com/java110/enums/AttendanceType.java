package com.java110.enums;

import org.springframework.util.StringUtils;

public enum  AttendanceType {

    ZC(1, "正常"),
    CD(2,"迟到"),
    ZT(3, "早退"),
    KG(4,"旷工"),
    TXJ(5, "调休假"),
    YGWC(6,"因公外出"),
    BJ(7,"病假"),
    CJ(8,"产假"),
    JLJ(9,"护理假"),
    NXJ(10,"年休假"),
    PCJ(11, "陪产假"),
    TQJ(12,"探亲假"),
    YEJ(13, "育儿假"),
    HJ(14,"婚假"),
    BRJ(15, "哺乳假"),
    SJ(16,"丧假"),
    SJ2(17, "事假"),
    JYJ(18,"节育假")
    ;
    private Integer code;
    private String name;

    AttendanceType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getNameByCode(Integer code){
        if (code == null || code == 0){
            throw new RuntimeException("获取假期类型异常");
        }
        for (AttendanceType attendanceType: values()){
            if (attendanceType.getCode() == code){
                return attendanceType.getName();
            }
        }
        throw new RuntimeException("获取假期类型异常："+code);
    }

    public static Integer getCodeByName(String name){
        if (StringUtils.isEmpty(name)){
            throw new RuntimeException("获取假期类型异常");
        }
        for (AttendanceType attendanceType: values()){
            if (attendanceType.getName().equals(name)){
                return attendanceType.getCode();
            }
        }
        throw new RuntimeException("获取假期类型异常："+name);
    }
}
