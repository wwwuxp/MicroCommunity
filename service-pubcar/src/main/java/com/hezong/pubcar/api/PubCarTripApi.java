package com.hezong.pubcar.api;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.bmo.pubCarTrip.IDeletePubCarTripBMO;
import com.hezong.pubcar.bmo.pubCarTrip.IGetPubCarTripBMO;
import com.hezong.pubcar.bmo.pubCarTrip.ISavePubCarTripBMO;
import com.hezong.pubcar.bmo.pubCarTrip.IUpdatePubCarTripBMO;
import com.java110.dto.pubCarTrip.PubCarTripDto;
import com.java110.po.pubCarTrip.PubCarTripPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/pubCarTrip")
public class PubCarTripApi {

    @Autowired
    private ISavePubCarTripBMO savePubCarTripBMOImpl;
    @Autowired
    private IUpdatePubCarTripBMO updatePubCarTripBMOImpl;
    @Autowired
    private IDeletePubCarTripBMO deletePubCarTripBMOImpl;

    @Autowired
    private IGetPubCarTripBMO getPubCarTripBMOImpl;

    /**
     * 保存
     *
     * @param reqJson
     * @return
     * @serviceCode /pubCarTrip/savePubCarTrip
     * @path /app/pubCarTrip/savePubCarTrip
     */
    @RequestMapping(value = "/savePubCarTrip", method = RequestMethod.POST)
    public ResponseEntity<String> savePubCarTrip(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "start", "请求报文中未包含start");
        Assert.hasKeyAndValue(reqJson, "destination", "请求报文中未包含destination");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "applyNum", "请求报文中未包含applyNum");


        PubCarTripPo pubCarTripPo = BeanConvertUtil.covertBean(reqJson, PubCarTripPo.class);
        return savePubCarTripBMOImpl.save(pubCarTripPo);
    }

    /**
     * 修改
     *
     * @param reqJson
     * @return
     * @serviceCode /pubCarTrip/updatePubCarTrip
     * @path /app/pubCarTrip/updatePubCarTrip
     */
    @RequestMapping(value = "/updatePubCarTrip", method = RequestMethod.POST)
    public ResponseEntity<String> updatePubCarTrip(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "start", "请求报文中未包含start");
        Assert.hasKeyAndValue(reqJson, "destination", "请求报文中未包含destination");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "applyNum", "请求报文中未包含applyNum");
        Assert.hasKeyAndValue(reqJson, "tripId", "tripId不能为空");


        PubCarTripPo pubCarTripPo = BeanConvertUtil.covertBean(reqJson, PubCarTripPo.class);
        return updatePubCarTripBMOImpl.update(pubCarTripPo);
    }

    /**
     * 删除
     *
     * @param reqJson
     * @return
     * @serviceCode /pubCarTrip/deletePubCarTrip
     * @path /app/pubCarTrip/deletePubCarTrip
     */
    @RequestMapping(value = "/deletePubCarTrip", method = RequestMethod.POST)
    public ResponseEntity<String> deletePubCarTrip(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "tripId", "tripId不能为空");


        PubCarTripPo pubCarTripPo = BeanConvertUtil.covertBean(reqJson, PubCarTripPo.class);
        return deletePubCarTripBMOImpl.delete(pubCarTripPo);
    }

    /**
     * 查询
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /pubCarTrip/queryPubCarTrip
     * @path /app/pubCarTrip/queryPubCarTrip
     */
    @RequestMapping(value = "/queryPubCarTrip", method = RequestMethod.GET)
    public ResponseEntity<String> queryPubCarTrip(@RequestParam(value = "communityId") String communityId,
                                                  @RequestParam(value = "page") int page,
                                                  @RequestParam(value = "row") int row) {
        PubCarTripDto pubCarTripDto = new PubCarTripDto();
        pubCarTripDto.setPage(page);
        pubCarTripDto.setRow(row);
        pubCarTripDto.setCommunityId(communityId);
        return getPubCarTripBMOImpl.get(pubCarTripDto);
    }
}
