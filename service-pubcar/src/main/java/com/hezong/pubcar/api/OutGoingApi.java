package com.hezong.pubcar.api;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.bmo.outGoing.IDeleteOutGoingBMO;
import com.hezong.pubcar.bmo.outGoing.IGetOutGoingBMO;
import com.hezong.pubcar.bmo.outGoing.ISaveOutGoingBMO;
import com.hezong.pubcar.bmo.outGoing.IUpdateOutGoingBMO;
import com.java110.dto.outGoing.OutGoingDto;
import com.java110.po.outGoing.OutGoingPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/outGoing")
public class OutGoingApi {

    @Autowired
    private ISaveOutGoingBMO saveOutGoingBMOImpl;
    @Autowired
    private IUpdateOutGoingBMO updateOutGoingBMOImpl;
    @Autowired
    private IDeleteOutGoingBMO deleteOutGoingBMOImpl;

    @Autowired
    private IGetOutGoingBMO getOutGoingBMOImpl;

    /**
     * 保存
     *
     * @param reqJson
     * @return
     * @serviceCode /outGoing/saveOutGoing
     * @path /app/outGoing/saveOutGoing
     */
    @RequestMapping(value = "/saveOutGoing", method = RequestMethod.POST)
    public ResponseEntity<String> saveOutGoing(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "destination", "请求报文中未包含destination");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "handlingMatter", "请求报文中未包含handlingMatter");
        Assert.hasKeyAndValue(reqJson, "tripId", "请求报文中未包含tripId");


        OutGoingPo outGoingPo = BeanConvertUtil.covertBean(reqJson, OutGoingPo.class);
        return saveOutGoingBMOImpl.save(outGoingPo);
    }

    /**
     * 修改
     *
     * @param reqJson
     * @return
     * @serviceCode /outGoing/updateOutGoing
     * @path /app/outGoing/updateOutGoing
     */
    @RequestMapping(value = "/updateOutGoing", method = RequestMethod.POST)
    public ResponseEntity<String> updateOutGoing(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "destination", "请求报文中未包含destination");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "handlingMatter", "请求报文中未包含handlingMatter");
        Assert.hasKeyAndValue(reqJson, "tripId", "请求报文中未包含tripId");
        Assert.hasKeyAndValue(reqJson, "outId", "outId不能为空");


        OutGoingPo outGoingPo = BeanConvertUtil.covertBean(reqJson, OutGoingPo.class);
        return updateOutGoingBMOImpl.update(outGoingPo);
    }

    /**
     * 删除
     *
     * @param reqJson
     * @return
     * @serviceCode /outGoing/deleteOutGoing
     * @path /app/outGoing/deleteOutGoing
     */
    @RequestMapping(value = "/deleteOutGoing", method = RequestMethod.POST)
    public ResponseEntity<String> deleteOutGoing(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "outId", "outId不能为空");


        OutGoingPo outGoingPo = BeanConvertUtil.covertBean(reqJson, OutGoingPo.class);
        return deleteOutGoingBMOImpl.delete(outGoingPo);
    }

    /**
     * 查询
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /outGoing/queryOutGoing
     * @path /app/outGoing/queryOutGoing
     */
    @RequestMapping(value = "/queryOutGoing", method = RequestMethod.GET)
    public ResponseEntity<String> queryOutGoing(@RequestParam(value = "communityId") String communityId,
                                                @RequestParam(value = "page") int page,
                                                @RequestParam(value = "row") int row) {
        OutGoingDto outGoingDto = new OutGoingDto();
        outGoingDto.setPage(page);
        outGoingDto.setRow(row);
        outGoingDto.setCommunityId(communityId);
        return getOutGoingBMOImpl.get(outGoingDto);
    }
}
