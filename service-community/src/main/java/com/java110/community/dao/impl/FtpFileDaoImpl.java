package com.java110.community.dao.impl;

import com.java110.community.dao.FtpFileDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.entity.community.FtpFile;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (FtpFile)表数据库访问层
 *
 * @author lmf
 * @since 2022-07-27 09:26:09
 */
@Service("ftpFileDaoImpl")
public class FtpFileDaoImpl extends BaseServiceDao implements FtpFileDao {

    @Override
    public FtpFile queryById(String ftpId){
        return sqlSessionTemplate.selectOne("ftpFileDaoImpl.queryById", ftpId);
    }

    @Override
    public List<FtpFile> queryAllByLimit(FtpFile ftpFile){
        return sqlSessionTemplate.selectList("ftpFileDaoImpl.queryAllByLimit", ftpFile);
    }

    @Override
    public long count(FtpFile ftpFile){
        return sqlSessionTemplate.selectOne("ftpFileDaoImpl.count",ftpFile);
    }

    @Override
    public int insert(FtpFile ftpFile){
        return sqlSessionTemplate.insert("ftpFileDaoImpl.insert", ftpFile);
    }

    @Override
    public int insertBatch(List<FtpFile> entities){
        return sqlSessionTemplate.insert("ftpFileDaoImpl.insertBatch", entities);
    }

    @Override
    public int insertOrUpdateBatch(List<FtpFile> entities){
        return sqlSessionTemplate.insert("ftpFileDaoImpl.insertOrUpdateBatch", entities);
    }
    
    @Override
    public int update(FtpFile ftpFile){
        return sqlSessionTemplate.update("ftpFileDaoImpl.update", ftpFile);
    }

    @Override
    public int deleteById(String ftpId){
        return sqlSessionTemplate.delete("ftpFileDaoImpl.deleteById", ftpId);
    }

}

