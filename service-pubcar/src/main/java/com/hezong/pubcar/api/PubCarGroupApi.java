package com.hezong.pubcar.api;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.bmo.pubCarGroup.IDeletePubCarGroupBMO;
import com.hezong.pubcar.bmo.pubCarGroup.IGetPubCarGroupBMO;
import com.hezong.pubcar.bmo.pubCarGroup.ISavePubCarGroupBMO;
import com.hezong.pubcar.bmo.pubCarGroup.IUpdatePubCarGroupBMO;
import com.java110.dto.pubCarGroup.PubCarGroupDto;
import com.java110.po.pubCarGroup.PubCarGroupPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/pubCarGroup")
public class PubCarGroupApi {

    @Autowired
    private ISavePubCarGroupBMO savePubCarGroupBMOImpl;
    @Autowired
    private IUpdatePubCarGroupBMO updatePubCarGroupBMOImpl;
    @Autowired
    private IDeletePubCarGroupBMO deletePubCarGroupBMOImpl;

    @Autowired
    private IGetPubCarGroupBMO getPubCarGroupBMOImpl;

    /**
     * 保存
     *
     * @param reqJson
     * @return
     * @serviceCode /pubCarGroup/savePubCarGroup
     * @path /app/pubCarGroup/savePubCarGroup
     */
    @RequestMapping(value = "/savePubCarGroup", method = RequestMethod.POST)
    public ResponseEntity<String> savePubCarGroup(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "groupType", "请求报文中未包含groupType");
        Assert.hasKeyAndValue(reqJson, "groupName", "请求报文中未包含groupName");


        PubCarGroupPo pubCarGroupPo = BeanConvertUtil.covertBean(reqJson, PubCarGroupPo.class);
        return savePubCarGroupBMOImpl.save(pubCarGroupPo);
    }

    /**
     * 修改
     *
     * @param reqJson
     * @return
     * @serviceCode /pubCarGroup/updatePubCarGroup
     * @path /app/pubCarGroup/updatePubCarGroup
     */
    @RequestMapping(value = "/updatePubCarGroup", method = RequestMethod.POST)
    public ResponseEntity<String> updatePubCarGroup(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "groupType", "请求报文中未包含groupType");
        Assert.hasKeyAndValue(reqJson, "groupName", "请求报文中未包含groupName");
        Assert.hasKeyAndValue(reqJson, "groupId", "groupId不能为空");


        PubCarGroupPo pubCarGroupPo = BeanConvertUtil.covertBean(reqJson, PubCarGroupPo.class);
        return updatePubCarGroupBMOImpl.update(pubCarGroupPo);
    }

    /**
     * 删除
     *
     * @param reqJson
     * @return
     * @serviceCode /pubCarGroup/deletePubCarGroup
     * @path /app/pubCarGroup/deletePubCarGroup
     */
    @RequestMapping(value = "/deletePubCarGroup", method = RequestMethod.POST)
    public ResponseEntity<String> deletePubCarGroup(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "groupId", "groupId不能为空");


        PubCarGroupPo pubCarGroupPo = BeanConvertUtil.covertBean(reqJson, PubCarGroupPo.class);
        return deletePubCarGroupBMOImpl.delete(pubCarGroupPo);
    }

    /**
     * 查询
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /pubCarGroup/queryPubCarGroup
     * @path /app/pubCarGroup/queryPubCarGroup
     */
    @RequestMapping(value = "/queryPubCarGroup", method = RequestMethod.GET)
    public ResponseEntity<String> queryPubCarGroup(@RequestParam(value = "communityId") String communityId,
                                                   @RequestParam(value = "page") int page,
                                                   @RequestParam(value = "row") int row) {
        PubCarGroupDto pubCarGroupDto = new PubCarGroupDto();
        pubCarGroupDto.setPage(page);
        pubCarGroupDto.setRow(row);
        pubCarGroupDto.setCommunityId(communityId);
        return getPubCarGroupBMOImpl.get(pubCarGroupDto);
    }
}
