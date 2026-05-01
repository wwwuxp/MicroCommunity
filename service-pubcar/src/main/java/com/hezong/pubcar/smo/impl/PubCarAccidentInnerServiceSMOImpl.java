package com.hezong.pubcar.smo.impl;

import com.hezong.pubcar.dao.IPubCarAccidentServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.pubCarAccident.PubCarAccidentDto;
import com.java110.intf.pubcar.IPubCarAccidentInnerServiceSMO;
import com.java110.vo.api.pubCarAccident.ApiPubCarAccidentDataVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hh
 * @date 2021/10/11
 */
@RestController
public class PubCarAccidentInnerServiceSMOImpl extends BaseServiceSMO implements IPubCarAccidentInnerServiceSMO {

    @Autowired
    private IPubCarAccidentServiceDao pubCarAccidentServiceDaoImpl;

    @Override
    public int listPubCarAccidentsCount(@RequestBody PubCarAccidentDto pubCarAccidentDto) {
        return pubCarAccidentServiceDaoImpl.listPubCarAccidentsCount(pubCarAccidentDto);
    }

    @Override
    public List<ApiPubCarAccidentDataVo> listPubCarAccidents(@RequestBody PubCarAccidentDto pubCarAccidentDto) {
        //校验是否传了 分页信息
        int page = pubCarAccidentDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            pubCarAccidentDto.setPage((page - 1) * pubCarAccidentDto.getRow());
        }
        List<ApiPubCarAccidentDataVo> pubCarAccidentDataVos = pubCarAccidentServiceDaoImpl.listPubCarAccidents(pubCarAccidentDto);
        for (ApiPubCarAccidentDataVo pubCarAccidentDataVo : pubCarAccidentDataVos) {
            String accidentSceneImg = pubCarAccidentDataVo.getAccidentSceneImg();
            String[] accidentSceneImgArray = new String[0];
            if (StringUtils.isNotBlank(accidentSceneImg)){
                accidentSceneImgArray = accidentSceneImg.split(",");
            }
            pubCarAccidentDataVo.setAccidentSceneImgs(accidentSceneImgArray);

            String responsibilityConfirmImg = pubCarAccidentDataVo.getResponsibilityConfirmImg();
            String[] responsibilityConfirmImgArray = new String[0];
            if (StringUtils.isNotBlank(responsibilityConfirmImg)){
                responsibilityConfirmImgArray = responsibilityConfirmImg.split(",");
            }
            pubCarAccidentDataVo.setResponsibilityConfirmImgs(responsibilityConfirmImgArray);
            pubCarAccidentDataVo.setAccidentSceneImg(null);
            pubCarAccidentDataVo.setResponsibilityConfirmImg(null);
        }
        return pubCarAccidentDataVos;
    }
}
