package com.hezong.pubcar.api;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.bmo.outGoingUnitContact.IDeleteOutGoingUnitContactBMO;
import com.hezong.pubcar.bmo.outGoingUnitContact.IGetOutGoingUnitContactBMO;
import com.hezong.pubcar.bmo.outGoingUnitContact.ISaveOutGoingUnitContactBMO;
import com.hezong.pubcar.bmo.outGoingUnitContact.IUpdateOutGoingUnitContactBMO;
import com.java110.dto.outGoingUnitContact.OutGoingUnitContactDto;
import com.java110.po.outGoingUnitContact.OutGoingUnitContactPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/outGoingUnitContact")
public class OutGoingUnitContactApi {

    @Autowired
    private ISaveOutGoingUnitContactBMO saveOutGoingUnitContactBMOImpl;
    @Autowired
    private IUpdateOutGoingUnitContactBMO updateOutGoingUnitContactBMOImpl;
    @Autowired
    private IDeleteOutGoingUnitContactBMO deleteOutGoingUnitContactBMOImpl;

    @Autowired
    private IGetOutGoingUnitContactBMO getOutGoingUnitContactBMOImpl;

    /**
     * 保存
     *
     * @param reqJson
     * @return
     * @serviceCode /outGoingUnitContact/saveOutGoingUnitContact
     * @path /app/outGoingUnitContact/saveOutGoingUnitContact
     */
    @RequestMapping(value = "/saveOutGoingUnitContact", method = RequestMethod.POST)
    public ResponseEntity<String> saveOutGoingUnitContact(@RequestBody JSONObject reqJson) {
        OutGoingUnitContactPo outGoingUnitContactPo = BeanConvertUtil.covertBean(reqJson, OutGoingUnitContactPo.class);
        return saveOutGoingUnitContactBMOImpl.save(outGoingUnitContactPo);
    }

    /**
     * 修改
     *
     * @param reqJson
     * @return
     * @serviceCode /outGoingUnitContact/updateOutGoingUnitContact
     * @path /app/outGoingUnitContact/updateOutGoingUnitContact
     */
    @RequestMapping(value = "/updateOutGoingUnitContact", method = RequestMethod.POST)
    public ResponseEntity<String> updateOutGoingUnitContact(@RequestBody JSONObject reqJson) {

        OutGoingUnitContactPo outGoingUnitContactPo = BeanConvertUtil.covertBean(reqJson, OutGoingUnitContactPo.class);
        return updateOutGoingUnitContactBMOImpl.update(outGoingUnitContactPo);
    }

    /**
     * 删除
     *
     * @param reqJson
     * @return
     * @serviceCode /outGoingUnitContact/deleteOutGoingUnitContact
     * @path /app/outGoingUnitContact/deleteOutGoingUnitContact
     */
    @RequestMapping(value = "/deleteOutGoingUnitContact", method = RequestMethod.POST)
    public ResponseEntity<String> deleteOutGoingUnitContact(@RequestBody JSONObject reqJson) {

        OutGoingUnitContactPo outGoingUnitContactPo = BeanConvertUtil.covertBean(reqJson, OutGoingUnitContactPo.class);
        return deleteOutGoingUnitContactBMOImpl.delete(outGoingUnitContactPo);
    }

    /**
     * 查询
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /outGoingUnitContact/queryOutGoingUnitContact
     * @path /app/outGoingUnitContact/queryOutGoingUnitContact
     */
    @RequestMapping(value = "/queryOutGoingUnitContact", method = RequestMethod.GET)
    public ResponseEntity<String> queryOutGoingUnitContact(@RequestParam(value = "communityId") String communityId,
                                                           @RequestParam(value = "page") int page,
                                                           @RequestParam(value = "row") int row) {
        OutGoingUnitContactDto outGoingUnitContactDto = new OutGoingUnitContactDto();
        outGoingUnitContactDto.setPage(page);
        outGoingUnitContactDto.setRow(row);
        return getOutGoingUnitContactBMOImpl.get(outGoingUnitContactDto);
    }
}
