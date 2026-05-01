package com.hezong.pubcar.api;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.bmo.pubCarAlarm.IDeletePubCarAlarmBMO;
import com.hezong.pubcar.bmo.pubCarAlarm.IGetPubCarAlarmBMO;
import com.hezong.pubcar.bmo.pubCarAlarm.ISavePubCarAlarmBMO;
import com.hezong.pubcar.bmo.pubCarAlarm.IUpdatePubCarAlarmBMO;
import com.java110.dto.pubCarAlarm.PubCarAlarmDto;
import com.java110.po.pubCarAlarm.PubCarAlarmPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/pubCarAlarm")
public class PubCarAlarmApi {

    @Autowired
    private ISavePubCarAlarmBMO savePubCarAlarmBMOImpl;
    @Autowired
    private IUpdatePubCarAlarmBMO updatePubCarAlarmBMOImpl;
    @Autowired
    private IDeletePubCarAlarmBMO deletePubCarAlarmBMOImpl;

    @Autowired
    private IGetPubCarAlarmBMO getPubCarAlarmBMOImpl;

    /**
     * 保存
     * @serviceCode /pubCarAlarm/savePubCarAlarm
     * @path /app/pubCarAlarm/savePubCarAlarm
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/savePubCarAlarm", method = RequestMethod.POST)
    public ResponseEntity<String> savePubCarAlarm(@RequestBody JSONObject reqJson) {

        

        PubCarAlarmPo pubCarAlarmPo = BeanConvertUtil.covertBean(reqJson, PubCarAlarmPo.class);
        return savePubCarAlarmBMOImpl.save(pubCarAlarmPo);
    }

    /**
     * 修改
     * @serviceCode /pubCarAlarm/updatePubCarAlarm
     * @path /app/pubCarAlarm/updatePubCarAlarm
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/updatePubCarAlarm", method = RequestMethod.POST)
    public ResponseEntity<String> updatePubCarAlarm(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "alarmId", "alarmId不能为空");


        PubCarAlarmPo pubCarAlarmPo = BeanConvertUtil.covertBean(reqJson, PubCarAlarmPo.class);
        return updatePubCarAlarmBMOImpl.update(pubCarAlarmPo);
    }

    /**
     * 删除
     * @serviceCode /pubCarAlarm/deletePubCarAlarm
     * @path /app/pubCarAlarm/deletePubCarAlarm
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/deletePubCarAlarm", method = RequestMethod.POST)
    public ResponseEntity<String> deletePubCarAlarm(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "alarmId", "alarmId不能为空");


        PubCarAlarmPo pubCarAlarmPo = BeanConvertUtil.covertBean(reqJson, PubCarAlarmPo.class);
        return deletePubCarAlarmBMOImpl.delete(pubCarAlarmPo);
    }

    /**
     * 查询
     * @serviceCode /pubCarAlarm/queryPubCarAlarm
     * @path /app/pubCarAlarm/queryPubCarAlarm
     * @param communityId 小区ID
     * @return
     */
    @RequestMapping(value = "/queryPubCarAlarm", method = RequestMethod.GET)
    public ResponseEntity<String> queryPubCarAlarm(@RequestParam(value = "communityId") String communityId,
                                                      @RequestParam(value = "page") int page,
                                                      @RequestParam(value = "row") int row) {
        PubCarAlarmDto pubCarAlarmDto = new PubCarAlarmDto();
        pubCarAlarmDto.setPage(page);
        pubCarAlarmDto.setRow(row);
        pubCarAlarmDto.setCommunityId(communityId);
        return getPubCarAlarmBMOImpl.get(pubCarAlarmDto);
    }
}
