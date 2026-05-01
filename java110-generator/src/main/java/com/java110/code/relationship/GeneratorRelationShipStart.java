package com.java110.code.relationship;

import com.alibaba.fastjson.JSONObject;
import com.java110.code.back.BaseGenerator;
import com.java110.code.web.GeneratorStart;

/**
 * @ClassName GeneratorRelationShipStart
 * @Author wuxw
 * @Date 2019/7/27 11:48
 * @Version 1.0
 * add by wuxw 2019/7/27
 **/
public class GeneratorRelationShipStart extends BaseGenerator {

    public static void main(String[] args) {

        StringBuffer sb = readFile(GeneratorStart.class.getResource("/relationship/template_complaint.json").getFile());

        JSONObject data = JSONObject.parseObject(sb.toString());

        GeneratorBindingComponent generatorBindingComponent = new GeneratorBindingComponent();
        generatorBindingComponent.generator(data);

        GeneratorFlow generatorFlow = new GeneratorFlow();
        generatorFlow.generator(data);
    }
}
