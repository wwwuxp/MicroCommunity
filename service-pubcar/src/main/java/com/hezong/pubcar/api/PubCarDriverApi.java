package com.hezong.pubcar.api;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.bmo.pubCarDriver.IDeletePubCarDriverBMO;
import com.hezong.pubcar.bmo.pubCarDriver.IGetPubCarDriverBMO;
import com.hezong.pubcar.bmo.pubCarDriver.ISavePubCarDriverBMO;
import com.hezong.pubcar.bmo.pubCarDriver.IUpdatePubCarDriverBMO;
import com.java110.dto.pubCarDriver.PubCarDriverDto;
import com.java110.po.pubCarDriver.PubCarDriverPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/pubCarDriver")
public class PubCarDriverApi {

    @Autowired
    private ISavePubCarDriverBMO savePubCarDriverBMOImpl;
    @Autowired
    private IUpdatePubCarDriverBMO updatePubCarDriverBMOImpl;
    @Autowired
    private IDeletePubCarDriverBMO deletePubCarDriverBMOImpl;

    @Autowired
    private IGetPubCarDriverBMO getPubCarDriverBMOImpl;

    /**
     * 保存
     *
     * @param reqJson
     * @return
     * @serviceCode /pubCarDriver/savePubCarDriver
     * @path /app/pubCarDriver/savePubCarDriver
     */
    @RequestMapping(value = "/savePubCarDriver", method = RequestMethod.POST)
    public ResponseEntity<String> savePubCarDriver(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "name", "请求报文中未包含name");
        Assert.hasKeyAndValue(reqJson, "password", "请求报文中未包含password");
        Assert.hasKeyAndValue(reqJson, "department", "请求报文中未包含department");
        Assert.hasKeyAndValue(reqJson, "position", "请求报文中未包含position");
        Assert.hasKeyAndValue(reqJson, "tel", "请求报文中未包含tel");
        Assert.hasKeyAndValue(reqJson, "job_num", "请求报文中未包含job_num");


        PubCarDriverPo pubCarDriverPo = BeanConvertUtil.covertBean(reqJson, PubCarDriverPo.class);
        return savePubCarDriverBMOImpl.save(pubCarDriverPo);
    }

    /**
     * 修改
     *
     * @param reqJson
     * @return
     * @serviceCode /pubCarDriver/updatePubCarDriver
     * @path /app/pubCarDriver/updatePubCarDriver
     */
    @RequestMapping(value = "/updatePubCarDriver", method = RequestMethod.POST)
    public ResponseEntity<String> updatePubCarDriver(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "name", "请求报文中未包含name");
        Assert.hasKeyAndValue(reqJson, "password", "请求报文中未包含password");
        Assert.hasKeyAndValue(reqJson, "department", "请求报文中未包含department");
        Assert.hasKeyAndValue(reqJson, "position", "请求报文中未包含position");
        Assert.hasKeyAndValue(reqJson, "tel", "请求报文中未包含tel");
        Assert.hasKeyAndValue(reqJson, "job_num", "请求报文中未包含job_num");
        Assert.hasKeyAndValue(reqJson, "driverId", "driverId不能为空");

        PubCarDriverPo pubCarDriverPo = BeanConvertUtil.covertBean(reqJson, PubCarDriverPo.class);
        return updatePubCarDriverBMOImpl.update(pubCarDriverPo);
    }

    /**
     * 删除
     *
     * @param reqJson
     * @return
     * @serviceCode /pubCarDriver/deletePubCarDriver
     * @path /app/pubCarDriver/deletePubCarDriver
     */
    @RequestMapping(value = "/deletePubCarDriver", method = RequestMethod.POST)
    public ResponseEntity<String> deletePubCarDriver(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "driverId", "driverId不能为空");


        PubCarDriverPo pubCarDriverPo = BeanConvertUtil.covertBean(reqJson, PubCarDriverPo.class);
        return deletePubCarDriverBMOImpl.delete(pubCarDriverPo);
    }

    /**
     * 查询
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /pubCarDriver/queryPubCarDriver
     * @path /app/pubCarDriver/queryPubCarDriver
     */
    @RequestMapping(value = "/queryPubCarDriver", method = RequestMethod.GET)
    public ResponseEntity<String> queryPubCarDriver(@RequestParam(value = "communityId") String communityId,
                                                    @RequestParam(value = "page") int page,
                                                    @RequestParam(value = "row") int row) {
        PubCarDriverDto pubCarDriverDto = new PubCarDriverDto();
        pubCarDriverDto.setPage(page);
        pubCarDriverDto.setRow(row);
        pubCarDriverDto.setCommunityId(communityId);
        return getPubCarDriverBMOImpl.get(pubCarDriverDto);
    }
}
