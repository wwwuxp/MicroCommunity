package com.hezong.pubcar.api;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.bmo.pubCarTripMileage.IDeletePubCarTripMileageBMO;
import com.hezong.pubcar.bmo.pubCarTripMileage.IGetPubCarTripMileageBMO;
import com.hezong.pubcar.bmo.pubCarTripMileage.ISavePubCarTripMileageBMO;
import com.hezong.pubcar.bmo.pubCarTripMileage.IUpdatePubCarTripMileageBMO;
import com.java110.dto.pubCarTripMileage.PubCarTripMileageDto;
import com.java110.po.pubCarTripMileage.PubCarTripMileagePo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/pubCarTripMileage")
public class PubCarTripMileageApi {

    @Autowired
    private ISavePubCarTripMileageBMO savePubCarTripMileageBMOImpl;
    @Autowired
    private IUpdatePubCarTripMileageBMO updatePubCarTripMileageBMOImpl;
    @Autowired
    private IDeletePubCarTripMileageBMO deletePubCarTripMileageBMOImpl;

    @Autowired
    private IGetPubCarTripMileageBMO getPubCarTripMileageBMOImpl;

    /**
     * 保存
     *
     * @param reqJson
     * @return
     * @serviceCode /pubCarTripMileage/savePubCarTripMileage
     * @path /app/pubCarTripMileage/savePubCarTripMileage
     */
    @RequestMapping(value = "/savePubCarTripMileage", method = RequestMethod.POST)
    public ResponseEntity<String> savePubCarTripMileage(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "mileage", "请求报文中未包含mileage");


        PubCarTripMileagePo pubCarTripMileagePo = BeanConvertUtil.covertBean(reqJson, PubCarTripMileagePo.class);
        return savePubCarTripMileageBMOImpl.save(pubCarTripMileagePo);
    }

    /**
     * 修改
     *
     * @param reqJson
     * @return
     * @serviceCode /pubCarTripMileage/updatePubCarTripMileage
     * @path /app/pubCarTripMileage/updatePubCarTripMileage
     */
    @RequestMapping(value = "/updatePubCarTripMileage", method = RequestMethod.POST)
    public ResponseEntity<String> updatePubCarTripMileage(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "mileage", "请求报文中未包含mileage");
        Assert.hasKeyAndValue(reqJson, "mileageId", "mileageId不能为空");


        PubCarTripMileagePo pubCarTripMileagePo = BeanConvertUtil.covertBean(reqJson, PubCarTripMileagePo.class);
        return updatePubCarTripMileageBMOImpl.update(pubCarTripMileagePo);
    }

    /**
     * 删除
     *
     * @param reqJson
     * @return
     * @serviceCode /pubCarTripMileage/deletePubCarTripMileage
     * @path /app/pubCarTripMileage/deletePubCarTripMileage
     */
    @RequestMapping(value = "/deletePubCarTripMileage", method = RequestMethod.POST)
    public ResponseEntity<String> deletePubCarTripMileage(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "mileageId", "mileageId不能为空");


        PubCarTripMileagePo pubCarTripMileagePo = BeanConvertUtil.covertBean(reqJson, PubCarTripMileagePo.class);
        return deletePubCarTripMileageBMOImpl.delete(pubCarTripMileagePo);
    }

    /**
     * 查询
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /pubCarTripMileage/queryPubCarTripMileage
     * @path /app/pubCarTripMileage/queryPubCarTripMileage
     */
    @RequestMapping(value = "/queryPubCarTripMileage", method = RequestMethod.GET)
    public ResponseEntity<String> queryPubCarTripMileage(@RequestParam(value = "communityId") String communityId,
                                                         @RequestParam(value = "page") int page,
                                                         @RequestParam(value = "row") int row) {
        PubCarTripMileageDto pubCarTripMileageDto = new PubCarTripMileageDto();
        pubCarTripMileageDto.setPage(page);
        pubCarTripMileageDto.setRow(row);
        pubCarTripMileageDto.setCommunityId(communityId);
        return getPubCarTripMileageBMOImpl.get(pubCarTripMileageDto);
    }
}
