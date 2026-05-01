package com.java110.common.dao.impl;

import com.java110.common.dao.ITablePictureServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.tablePicture.TablePictureDto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hh
 * @date 2022/1/12
 */
@Service("tablePictureServiceDaoImpl")
public class TablePictureServiceDaoImpl extends BaseServiceDao implements ITablePictureServiceDao {

    @Override
    public int saveTablePicture(TablePictureDto toDbTablePicture) {
        return sqlSessionTemplate.insert("tablePictureServiceDaoImpl.saveTablePicture", toDbTablePicture);
    }

    @Override
    public List<TablePictureDto> getPictures(TablePictureDto tablePictureDto) {
        return sqlSessionTemplate.selectList("tablePictureServiceDaoImpl.getPictures", tablePictureDto);
    }

    @Override
    public TablePictureDto getPicture(Integer id) {
        Map<String, Integer> param = new HashMap<>();
        param.put("id", id);
        return sqlSessionTemplate.selectOne("tablePictureServiceDaoImpl.getPicture", param);
    }

    @Override
    public List<Integer> existTablePicture(TablePictureDto existPicture) {
        return sqlSessionTemplate.selectList("tablePictureServiceDaoImpl.existTablePicture", existPicture);
    }

    @Override
    public int updateTablePicture(TablePictureDto toDbTablePicture) {
        return sqlSessionTemplate.update("tablePictureServiceDaoImpl.updateTablePicture", toDbTablePicture);
    }

    @Override
    public TablePictureDto getPictureByPriKey(String priKey) {
        Map<String, String> param = new HashMap<>();
        param.put("priKey", priKey);
        return sqlSessionTemplate.selectOne("tablePictureServiceDaoImpl.getPictureByPriKey", param);
    }
}
