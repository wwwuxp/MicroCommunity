package com.java110.common.dao;

import com.java110.dto.tablePicture.TablePictureDto;

import java.util.List;

/**
 * @author hh
 * @date 2022/1/12
 */
public interface ITablePictureServiceDao {

    /**
     * 保存二进制图片数据到数据库
     * @param toDbTablePicture 参数
     * @return int
     */
    int saveTablePicture(TablePictureDto toDbTablePicture);

    /**
     * 获取图片信息
     * @param tablePictureDto 参数
     * @return list
     */
    List<TablePictureDto> getPictures(TablePictureDto tablePictureDto);

    /**
     * 根据id获取图片
     * @param id 图片id
     * @return
     */
    TablePictureDto getPicture(Integer id);

    /**
     * 是否在库中已经存在相同条件的数据了
     * @param existPicture 参数
     * @return List
     */
    List<Integer> existTablePicture(TablePictureDto existPicture);

    /**
     * 更新图片信息
     * @param toDbTablePicture 参数
     * @return int
     */
    int updateTablePicture(TablePictureDto toDbTablePicture);

    /**
     * 根据主键获取图片
     * @param priKey 主键
     * @return TablePictureDto
     */
    TablePictureDto getPictureByPriKey(String priKey);

}
