package com.java110.ext.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java110.ext.mapper.DemoMapper;
import com.java110.ext.pojo.DemoPo;
import com.java110.ext.service.IDemoService;

/**
 * 示例服务实现
 */
@Service("demoServiceImpl")
public class DemoServiceImpl implements IDemoService {

    @Autowired
    private DemoMapper demoMapper;

    @Override
    public List<DemoPo> listDemo(DemoPo demoPo) {
        LambdaQueryWrapper<DemoPo> wrapper = new LambdaQueryWrapper<>();
        // 添加查询条件
        if (demoPo.getName() != null && !demoPo.getName().isEmpty()) {
            wrapper.like(DemoPo::getName, demoPo.getName());
        }
        if (demoPo.getStatusCd() != null && !demoPo.getStatusCd().isEmpty()) {
            wrapper.eq(DemoPo::getStatusCd, demoPo.getStatusCd());
        }
        wrapper.orderByDesc(DemoPo::getCreateTime);
        return demoMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveDemo(DemoPo demoPo) {
        return demoMapper.insert(demoPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDemo(DemoPo demoPo) {
        return demoMapper.updateById(demoPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteDemo(DemoPo demoPo) {
        // 逻辑删除
        demoPo.setStatusCd("1");
        return demoMapper.updateById(demoPo);
    }

    @Override
    public DemoPo getDemoById(String id) {
        return demoMapper.selectById(id);
    }

    /**
     * 分页查询
     *
     * @param demoPo 查询条件
     * @param page   页码
     * @param row    每页数量
     * @return 分页结果
     */
    public IPage<DemoPo> pageDemo(DemoPo demoPo, int page, int row) {
        LambdaQueryWrapper<DemoPo> wrapper = new LambdaQueryWrapper<>();
        // 添加查询条件
        if (demoPo.getName() != null && !demoPo.getName().isEmpty()) {
            wrapper.like(DemoPo::getName, demoPo.getName());
        }
        wrapper.orderByDesc(DemoPo::getCreateTime);
        Page<DemoPo> pageParam = new Page<>(page, row);
        return demoMapper.selectPage(pageParam, wrapper);
    }
}
