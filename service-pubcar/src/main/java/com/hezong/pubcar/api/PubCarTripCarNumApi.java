package com.hezong.pubcar.api;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.bmo.pubCarTripCarNum.IDeletePubCarTripCarNumBMO;
import com.hezong.pubcar.bmo.pubCarTripCarNum.IGetPubCarTripCarNumBMO;
import com.hezong.pubcar.bmo.pubCarTripCarNum.ISavePubCarTripCarNumBMO;
import com.hezong.pubcar.bmo.pubCarTripCarNum.IUpdatePubCarTripCarNumBMO;
import com.java110.dto.pubCarTripCarNum.PubCarTripCarNumDto;
import com.java110.po.pubCarTripCarNum.PubCarTripCarNumPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/pubCarTripCarNum")
public class PubCarTripCarNumApi {

    @Autowired
    private ISavePubCarTripCarNumBMO savePubCarTripCarNumBMOImpl;
    @Autowired
    private IUpdatePubCarTripCarNumBMO updatePubCarTripCarNumBMOImpl;
    @Autowired
    private IDeletePubCarTripCarNumBMO deletePubCarTripCarNumBMOImpl;

    @Autowired
    private IGetPubCarTripCarNumBMO getPubCarTripCarNumBMOImpl;

    /**
     * 保存
     * @serviceCode /pubCarTripCarNum/savePubCarTripCarNum
     * @path /app/pubCarTripCarNum/savePubCarTripCarNum
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/savePubCarTripCarNum", method = RequestMethod.POST)
    public ResponseEntity<String> savePubCarTripCarNum(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "tripType", "请求报文中未包含tripType");
Assert.hasKeyAndValue(reqJson, "passengerNum", "请求报文中未包含passengerNum");


        PubCarTripCarNumPo pubCarTripCarNumPo = BeanConvertUtil.covertBean(reqJson, PubCarTripCarNumPo.class);
        return savePubCarTripCarNumBMOImpl.save(pubCarTripCarNumPo);
    }

    /**
     * 修改
     * @serviceCode /pubCarTripCarNum/updatePubCarTripCarNum
     * @path /app/pubCarTripCarNum/updatePubCarTripCarNum
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/updatePubCarTripCarNum", method = RequestMethod.POST)
    public ResponseEntity<String> updatePubCarTripCarNum(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "tripType", "请求报文中未包含tripType");
Assert.hasKeyAndValue(reqJson, "passengerNum", "请求报文中未包含passengerNum");
Assert.hasKeyAndValue(reqJson, "numId", "numId不能为空");


        PubCarTripCarNumPo pubCarTripCarNumPo = BeanConvertUtil.covertBean(reqJson, PubCarTripCarNumPo.class);
        return updatePubCarTripCarNumBMOImpl.update(pubCarTripCarNumPo);
    }

    /**
     * 删除
     * @serviceCode /pubCarTripCarNum/deletePubCarTripCarNum
     * @path /app/pubCarTripCarNum/deletePubCarTripCarNum
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/deletePubCarTripCarNum", method = RequestMethod.POST)
    public ResponseEntity<String> deletePubCarTripCarNum(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "numId", "numId不能为空");


        PubCarTripCarNumPo pubCarTripCarNumPo = BeanConvertUtil.covertBean(reqJson, PubCarTripCarNumPo.class);
        return deletePubCarTripCarNumBMOImpl.delete(pubCarTripCarNumPo);
    }

    /**
     * 查询
     * @serviceCode /pubCarTripCarNum/queryPubCarTripCarNum
     * @path /app/pubCarTripCarNum/queryPubCarTripCarNum
     * @param communityId 小区ID
     * @return
     */
    @RequestMapping(value = "/queryPubCarTripCarNum", method = RequestMethod.GET)
    public ResponseEntity<String> queryPubCarTripCarNum(@RequestParam(value = "communityId") String communityId,
                                                      @RequestParam(value = "page") int page,
                                                      @RequestParam(value = "row") int row) {
        PubCarTripCarNumDto pubCarTripCarNumDto = new PubCarTripCarNumDto();
        pubCarTripCarNumDto.setPage(page);
        pubCarTripCarNumDto.setRow(row);
//        pubCarTripCarNumDto.setCommunityId(communityId);
        return getPubCarTripCarNumBMOImpl.get(pubCarTripCarNumDto);
    }
}
