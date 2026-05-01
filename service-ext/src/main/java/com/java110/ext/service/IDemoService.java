package com.java110.ext.service;

import java.util.List;

import com.java110.ext.pojo.DemoPo;

/**
 * 示例服务接口
 */
public interface IDemoService {

    /**
     * 查询列表
     *
     * @param demoPo 查询条件
     * @return 列表数据
     */
    List<DemoPo> listDemo(DemoPo demoPo);

    /**
     * 保存数据
     *
     * @param demoPo 数据对象
     * @return 影响行数
     */
    int saveDemo(DemoPo demoPo);

    /**
     * 更新数据
     *
     * @param demoPo 数据对象
     * @return 影响行数
     */
    int updateDemo(DemoPo demoPo);

    /**
     * 删除数据
     *
     * @param demoPo 数据对象
     * @return 影响行数
     */
    int deleteDemo(DemoPo demoPo);

    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 单条数据
     */
    DemoPo getDemoById(String id);
}
