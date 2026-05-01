package com.hezong.pubcar.api;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.bmo.pubCarViolation.IDeletePubCarViolationBMO;
import com.hezong.pubcar.bmo.pubCarViolation.IGetPubCarViolationBMO;
import com.hezong.pubcar.bmo.pubCarViolation.ISavePubCarViolationBMO;
import com.hezong.pubcar.bmo.pubCarViolation.IUpdatePubCarViolationBMO;
import com.java110.dto.pubCarViolation.PubCarViolationDto;
import com.java110.po.pubCarViolation.PubCarViolationPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/pubCarViolation")
public class PubCarViolationApi {

    @Autowired
    private ISavePubCarViolationBMO savePubCarViolationBMOImpl;
    @Autowired
    private IUpdatePubCarViolationBMO updatePubCarViolationBMOImpl;
    @Autowired
    private IDeletePubCarViolationBMO deletePubCarViolationBMOImpl;

    @Autowired
    private IGetPubCarViolationBMO getPubCarViolationBMOImpl;

    /**
     * 保存
     *
     * @param reqJson
     * @return
     * @serviceCode /pubCarViolation/savePubCarViolation
     * @path /app/pubCarViolation/savePubCarViolation
     */
    @RequestMapping(value = "/savePubCarViolation", method = RequestMethod.POST)
    public ResponseEntity<String> savePubCarViolation(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "date", "请求报文中未包含date");
        Assert.hasKeyAndValue(reqJson, "address", "请求报文中未包含address");
        Assert.hasKeyAndValue(reqJson, "content", "请求报文中未包含content");
        Assert.hasKeyAndValue(reqJson, "driver", "请求报文中未包含driver");


        PubCarViolationPo pubCarViolationPo = BeanConvertUtil.covertBean(reqJson, PubCarViolationPo.class);
        return savePubCarViolationBMOImpl.save(pubCarViolationPo);
    }

    /**
     * 修改
     *
     * @param reqJson
     * @return
     * @serviceCode /pubCarViolation/updatePubCarViolation
     * @path /app/pubCarViolation/updatePubCarViolation
     */
    @RequestMapping(value = "/updatePubCarViolation", method = RequestMethod.POST)
    public ResponseEntity<String> updatePubCarViolation(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "date", "请求报文中未包含date");
        Assert.hasKeyAndValue(reqJson, "address", "请求报文中未包含address");
        Assert.hasKeyAndValue(reqJson, "content", "请求报文中未包含content");
        Assert.hasKeyAndValue(reqJson, "driver", "请求报文中未包含driver");
        Assert.hasKeyAndValue(reqJson, "violationId", "violationId不能为空");


        PubCarViolationPo pubCarViolationPo = BeanConvertUtil.covertBean(reqJson, PubCarViolationPo.class);
        return updatePubCarViolationBMOImpl.update(pubCarViolationPo);
    }

    /**
     * 删除
     *
     * @param reqJson
     * @return
     * @serviceCode /pubCarViolation/deletePubCarViolation
     * @path /app/pubCarViolation/deletePubCarViolation
     */
    @RequestMapping(value = "/deletePubCarViolation", method = RequestMethod.POST)
    public ResponseEntity<String> deletePubCarViolation(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "violationId", "violationId不能为空");


        PubCarViolationPo pubCarViolationPo = BeanConvertUtil.covertBean(reqJson, PubCarViolationPo.class);
        return deletePubCarViolationBMOImpl.delete(pubCarViolationPo);
    }

    /**
     * 查询
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /pubCarViolation/queryPubCarViolation
     * @path /app/pubCarViolation/queryPubCarViolation
     */
    @RequestMapping(value = "/queryPubCarViolation", method = RequestMethod.GET)
    public ResponseEntity<String> queryPubCarViolation(@RequestParam(value = "communityId") String communityId,
                                                       @RequestParam(value = "page") int page,
                                                       @RequestParam(value = "row") int row) {
        PubCarViolationDto pubCarViolationDto = new PubCarViolationDto();
        pubCarViolationDto.setPage(page);
        pubCarViolationDto.setRow(row);
        pubCarViolationDto.setCommunityId(communityId);
        return getPubCarViolationBMOImpl.get(pubCarViolationDto);
    }
}
