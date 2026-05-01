package com.hezong.pubcar.api;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.bmo.pubCarUser.IDeletePubCarUserBMO;
import com.hezong.pubcar.bmo.pubCarUser.IGetPubCarUserBMO;
import com.hezong.pubcar.bmo.pubCarUser.ISavePubCarUserBMO;
import com.hezong.pubcar.bmo.pubCarUser.IUpdatePubCarUserBMO;
import com.java110.dto.pubCarUser.PubCarUserDto;
import com.java110.po.pubCarUser.PubCarUserPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/pubCarUser")
public class PubCarUserApi {

    @Autowired
    private ISavePubCarUserBMO savePubCarUserBMOImpl;
    @Autowired
    private IUpdatePubCarUserBMO updatePubCarUserBMOImpl;
    @Autowired
    private IDeletePubCarUserBMO deletePubCarUserBMOImpl;

    @Autowired
    private IGetPubCarUserBMO getPubCarUserBMOImpl;

    /**
     * 保存
     *
     * @param reqJson
     * @return
     * @serviceCode /pubCarUser/savePubCarUser
     * @path /app/pubCarUser/savePubCarUser
     */
    @RequestMapping(value = "/savePubCarUser", method = RequestMethod.POST)
    public ResponseEntity<String> savePubCarUser(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "userId", "请求报文中未包含userId");


        PubCarUserPo pubCarUserPo = BeanConvertUtil.covertBean(reqJson, PubCarUserPo.class);
        return savePubCarUserBMOImpl.save(pubCarUserPo);
    }

    /**
     * 修改
     *
     * @param reqJson
     * @return
     * @serviceCode /pubCarUser/updatePubCarUser
     * @path /app/pubCarUser/updatePubCarUser
     */
    @RequestMapping(value = "/updatePubCarUser", method = RequestMethod.POST)
    public ResponseEntity<String> updatePubCarUser(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "userId", "请求报文中未包含userId");
        Assert.hasKeyAndValue(reqJson, "carUserId", "carUserId不能为空");


        PubCarUserPo pubCarUserPo = BeanConvertUtil.covertBean(reqJson, PubCarUserPo.class);
        return updatePubCarUserBMOImpl.update(pubCarUserPo);
    }

    /**
     * 删除
     *
     * @param reqJson
     * @return
     * @serviceCode /pubCarUser/deletePubCarUser
     * @path /app/pubCarUser/deletePubCarUser
     */
    @RequestMapping(value = "/deletePubCarUser", method = RequestMethod.POST)
    public ResponseEntity<String> deletePubCarUser(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "carUserId", "carUserId不能为空");


        PubCarUserPo pubCarUserPo = BeanConvertUtil.covertBean(reqJson, PubCarUserPo.class);
        return deletePubCarUserBMOImpl.delete(pubCarUserPo);
    }

    /**
     * 查询
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /pubCarUser/queryPubCarUser
     * @path /app/pubCarUser/queryPubCarUser
     */
    @RequestMapping(value = "/queryPubCarUser", method = RequestMethod.GET)
    public ResponseEntity<String> queryPubCarUser(@RequestParam(value = "communityId") String communityId,
                                                  @RequestParam(value = "page") int page,
                                                  @RequestParam(value = "row") int row) {
        PubCarUserDto pubCarUserDto = new PubCarUserDto();
        pubCarUserDto.setPage(page);
        pubCarUserDto.setRow(row);
        pubCarUserDto.setCommunityId(communityId);
        return getPubCarUserBMOImpl.get(pubCarUserDto);
    }
}
