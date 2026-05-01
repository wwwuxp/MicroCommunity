package com.hezong.pubcar.api;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.bmo.pubCarRepair.IDeletePubCarRepairBMO;
import com.hezong.pubcar.bmo.pubCarRepair.IGetPubCarRepairBMO;
import com.hezong.pubcar.bmo.pubCarRepair.ISavePubCarRepairBMO;
import com.hezong.pubcar.bmo.pubCarRepair.IUpdatePubCarRepairBMO;
import com.java110.dto.pubCarRepair.PubCarRepairDto;
import com.java110.po.pubCarRepair.PubCarRepairPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/pubCarRepair")
public class PubCarRepairApi {

    @Autowired
    private ISavePubCarRepairBMO savePubCarRepairBMOImpl;
    @Autowired
    private IUpdatePubCarRepairBMO updatePubCarRepairBMOImpl;
    @Autowired
    private IDeletePubCarRepairBMO deletePubCarRepairBMOImpl;

    @Autowired
    private IGetPubCarRepairBMO getPubCarRepairBMOImpl;

    /**
     * 保存
     *
     * @param reqJson
     * @return
     * @serviceCode /pubCarRepair/savePubCarRepair
     * @path /app/pubCarRepair/savePubCarRepair
     */
    @RequestMapping(value = "/savePubCarRepair", method = RequestMethod.POST)
    public ResponseEntity<String> savePubCarRepair(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "date", "请求报文中未包含date");
        Assert.hasKeyAndValue(reqJson, "content", "请求报文中未包含content");
        Assert.hasKeyAndValue(reqJson, "price", "请求报文中未包含price");


        PubCarRepairPo pubCarRepairPo = BeanConvertUtil.covertBean(reqJson, PubCarRepairPo.class);
        return savePubCarRepairBMOImpl.save(pubCarRepairPo);
    }

    /**
     * 修改
     *
     * @param reqJson
     * @return
     * @serviceCode /pubCarRepair/updatePubCarRepair
     * @path /app/pubCarRepair/updatePubCarRepair
     */
    @RequestMapping(value = "/updatePubCarRepair", method = RequestMethod.POST)
    public ResponseEntity<String> updatePubCarRepair(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "date", "请求报文中未包含date");
        Assert.hasKeyAndValue(reqJson, "content", "请求报文中未包含content");
        Assert.hasKeyAndValue(reqJson, "price", "请求报文中未包含price");
        Assert.hasKeyAndValue(reqJson, "repairId", "repairId不能为空");


        PubCarRepairPo pubCarRepairPo = BeanConvertUtil.covertBean(reqJson, PubCarRepairPo.class);
        return updatePubCarRepairBMOImpl.update(pubCarRepairPo);
    }

    /**
     * 删除
     *
     * @param reqJson
     * @return
     * @serviceCode /pubCarRepair/deletePubCarRepair
     * @path /app/pubCarRepair/deletePubCarRepair
     */
    @RequestMapping(value = "/deletePubCarRepair", method = RequestMethod.POST)
    public ResponseEntity<String> deletePubCarRepair(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "repairId", "repairId不能为空");


        PubCarRepairPo pubCarRepairPo = BeanConvertUtil.covertBean(reqJson, PubCarRepairPo.class);
        return deletePubCarRepairBMOImpl.delete(pubCarRepairPo);
    }

    /**
     * 查询
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /pubCarRepair/queryPubCarRepair
     * @path /app/pubCarRepair/queryPubCarRepair
     */
    @RequestMapping(value = "/queryPubCarRepair", method = RequestMethod.GET)
    public ResponseEntity<String> queryPubCarRepair(@RequestParam(value = "communityId") String communityId,
                                                    @RequestParam(value = "page") int page,
                                                    @RequestParam(value = "row") int row) {
        PubCarRepairDto pubCarRepairDto = new PubCarRepairDto();
        pubCarRepairDto.setPage(page);
        pubCarRepairDto.setRow(row);
        pubCarRepairDto.setCommunityId(communityId);
        return getPubCarRepairBMOImpl.get(pubCarRepairDto);
    }
}
