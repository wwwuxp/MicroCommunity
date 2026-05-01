package com.hezong.pubcar.api;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.bmo.outGoingPersonnel.IDeleteOutGoingPersonnelBMO;
import com.hezong.pubcar.bmo.outGoingPersonnel.IGetOutGoingPersonnelBMO;
import com.hezong.pubcar.bmo.outGoingPersonnel.ISaveOutGoingPersonnelBMO;
import com.hezong.pubcar.bmo.outGoingPersonnel.IUpdateOutGoingPersonnelBMO;
import com.java110.dto.outGoingPersonnel.OutGoingPersonnelDto;
import com.java110.po.outGoingPersonnel.OutGoingPersonnelPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/outGoingPersonnel")
public class OutGoingPersonnelApi {

    @Autowired
    private ISaveOutGoingPersonnelBMO saveOutGoingPersonnelBMOImpl;
    @Autowired
    private IUpdateOutGoingPersonnelBMO updateOutGoingPersonnelBMOImpl;
    @Autowired
    private IDeleteOutGoingPersonnelBMO deleteOutGoingPersonnelBMOImpl;

    @Autowired
    private IGetOutGoingPersonnelBMO getOutGoingPersonnelBMOImpl;

    /**
     * 保存
     *
     * @param reqJson
     * @return
     * @serviceCode /outGoingPersonnel/saveOutGoingPersonnel
     * @path /app/outGoingPersonnel/saveOutGoingPersonnel
     */
    @RequestMapping(value = "/saveOutGoingPersonnel", method = RequestMethod.POST)
    public ResponseEntity<String> saveOutGoingPersonnel(@RequestBody JSONObject reqJson) {

        OutGoingPersonnelPo outGoingPersonnelPo = BeanConvertUtil.covertBean(reqJson, OutGoingPersonnelPo.class);
        return saveOutGoingPersonnelBMOImpl.save(outGoingPersonnelPo);
    }

    /**
     * 修改
     *
     * @param reqJson
     * @return
     * @serviceCode /outGoingPersonnel/updateOutGoingPersonnel
     * @path /app/outGoingPersonnel/updateOutGoingPersonnel
     */
    @RequestMapping(value = "/updateOutGoingPersonnel", method = RequestMethod.POST)
    public ResponseEntity<String> updateOutGoingPersonnel(@RequestBody JSONObject reqJson) {

        OutGoingPersonnelPo outGoingPersonnelPo = BeanConvertUtil.covertBean(reqJson, OutGoingPersonnelPo.class);
        return updateOutGoingPersonnelBMOImpl.update(outGoingPersonnelPo);
    }

    /**
     * 删除
     *
     * @param reqJson
     * @return
     * @serviceCode /outGoingPersonnel/deleteOutGoingPersonnel
     * @path /app/outGoingPersonnel/deleteOutGoingPersonnel
     */
    @RequestMapping(value = "/deleteOutGoingPersonnel", method = RequestMethod.POST)
    public ResponseEntity<String> deleteOutGoingPersonnel(@RequestBody JSONObject reqJson) {

        OutGoingPersonnelPo outGoingPersonnelPo = BeanConvertUtil.covertBean(reqJson, OutGoingPersonnelPo.class);
        return deleteOutGoingPersonnelBMOImpl.delete(outGoingPersonnelPo);
    }

    /**
     * 查询
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /outGoingPersonnel/queryOutGoingPersonnel
     * @path /app/outGoingPersonnel/queryOutGoingPersonnel
     */
    @RequestMapping(value = "/queryOutGoingPersonnel", method = RequestMethod.GET)
    public ResponseEntity<String> queryOutGoingPersonnel(@RequestParam(value = "communityId") String communityId,
                                                         @RequestParam(value = "page") int page,
                                                         @RequestParam(value = "row") int row) {
        OutGoingPersonnelDto outGoingPersonnelDto = new OutGoingPersonnelDto();
        outGoingPersonnelDto.setPage(page);
        outGoingPersonnelDto.setRow(row);
        return getOutGoingPersonnelBMOImpl.get(outGoingPersonnelDto);
    }
}
