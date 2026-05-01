package com.hezong.pubcar.api;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.bmo.pubCarTripCarPassenger.IDeletePubCarTripCarPassengerBMO;
import com.hezong.pubcar.bmo.pubCarTripCarPassenger.IGetPubCarTripCarPassengerBMO;
import com.hezong.pubcar.bmo.pubCarTripCarPassenger.ISavePubCarTripCarPassengerBMO;
import com.hezong.pubcar.bmo.pubCarTripCarPassenger.IUpdatePubCarTripCarPassengerBMO;
import com.java110.dto.pubCarTripCarPassenger.PubCarTripCarPassengerDto;
import com.java110.po.pubCarTripCarPassenger.PubCarTripCarPassengerPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/pubCarTripCarPassenger")
public class PubCarTripCarPassengerApi {

    @Autowired
    private ISavePubCarTripCarPassengerBMO savePubCarTripCarPassengerBMOImpl;
    @Autowired
    private IUpdatePubCarTripCarPassengerBMO updatePubCarTripCarPassengerBMOImpl;
    @Autowired
    private IDeletePubCarTripCarPassengerBMO deletePubCarTripCarPassengerBMOImpl;

    @Autowired
    private IGetPubCarTripCarPassengerBMO getPubCarTripCarPassengerBMOImpl;

    /**
     * 保存
     *
     * @param reqJson
     * @return
     * @serviceCode /pubCarTripCarPassenger/savePubCarTripCarPassenger
     * @path /app/pubCarTripCarPassenger/savePubCarTripCarPassenger
     */
    @RequestMapping(value = "/savePubCarTripCarPassenger", method = RequestMethod.POST)
    public ResponseEntity<String> savePubCarTripCarPassenger(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "tripType", "请求报文中未包含tripType");
        Assert.hasKeyAndValue(reqJson, "passengerNum", "请求报文中未包含passengerNum");


        PubCarTripCarPassengerPo pubCarTripCarPassengerPo = BeanConvertUtil.covertBean(reqJson, PubCarTripCarPassengerPo.class);
        return savePubCarTripCarPassengerBMOImpl.save(pubCarTripCarPassengerPo);
    }

    /**
     * 修改
     *
     * @param reqJson
     * @return
     * @serviceCode /pubCarTripCarPassenger/updatePubCarTripCarPassenger
     * @path /app/pubCarTripCarPassenger/updatePubCarTripCarPassenger
     */
    @RequestMapping(value = "/updatePubCarTripCarPassenger", method = RequestMethod.POST)
    public ResponseEntity<String> updatePubCarTripCarPassenger(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "tripType", "请求报文中未包含tripType");
        Assert.hasKeyAndValue(reqJson, "passengerNum", "请求报文中未包含passengerNum");
        Assert.hasKeyAndValue(reqJson, "passengerId", "passengerId不能为空");


        PubCarTripCarPassengerPo pubCarTripCarPassengerPo = BeanConvertUtil.covertBean(reqJson, PubCarTripCarPassengerPo.class);
        return updatePubCarTripCarPassengerBMOImpl.update(pubCarTripCarPassengerPo);
    }

    /**
     * 删除
     *
     * @param reqJson
     * @return
     * @serviceCode /pubCarTripCarPassenger/deletePubCarTripCarPassenger
     * @path /app/pubCarTripCarPassenger/deletePubCarTripCarPassenger
     */
    @RequestMapping(value = "/deletePubCarTripCarPassenger", method = RequestMethod.POST)
    public ResponseEntity<String> deletePubCarTripCarPassenger(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "passengerId", "passengerId不能为空");


        PubCarTripCarPassengerPo pubCarTripCarPassengerPo = BeanConvertUtil.covertBean(reqJson, PubCarTripCarPassengerPo.class);
        return deletePubCarTripCarPassengerBMOImpl.delete(pubCarTripCarPassengerPo);
    }

    /**
     * 查询
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /pubCarTripCarPassenger/queryPubCarTripCarPassenger
     * @path /app/pubCarTripCarPassenger/queryPubCarTripCarPassenger
     */
    @RequestMapping(value = "/queryPubCarTripCarPassenger", method = RequestMethod.GET)
    public ResponseEntity<String> queryPubCarTripCarPassenger(@RequestParam(value = "communityId") String communityId,
                                                              @RequestParam(value = "page") int page,
                                                              @RequestParam(value = "row") int row) {
        PubCarTripCarPassengerDto pubCarTripCarPassengerDto = new PubCarTripCarPassengerDto();
        pubCarTripCarPassengerDto.setPage(page);
        pubCarTripCarPassengerDto.setRow(row);
//        pubCarTripCarPassengerDto.setCommunityId(communityId);
        return getPubCarTripCarPassengerBMOImpl.get(pubCarTripCarPassengerDto);
    }
}
