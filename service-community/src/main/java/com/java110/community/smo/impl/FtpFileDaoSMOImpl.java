package com.java110.community.smo.impl;

import com.java110.community.dao.FtpFileDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.entity.community.FtpFile;
import com.java110.intf.community.FtpFileDaoSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * (FtpFile)表接口feign层
 *
 * @author lmf
 * @since 2022-07-27 09:26:11
 */
@RestController
public class FtpFileDaoSMOImpl extends BaseServiceSMO implements FtpFileDaoSMO {

    @Autowired
    private FtpFileDao ftpFileDao;

    @Override
    public FtpFile queryById(@RequestBody String ftpId){
        return ftpFileDao .queryById(ftpId);
    }

    @Override
    public List<FtpFile> queryAllByLimit(@RequestBody FtpFile ftpFile){
        return ftpFileDao .queryAllByLimit(ftpFile);
    }

    @Override
    public long count(@RequestBody FtpFile ftpFile){
        return ftpFileDao .count(ftpFile);
    }

    @Override
    public int insert(@RequestBody FtpFile ftpFile){
        return ftpFileDao.insert(ftpFile);
    }

    @Override
    public int insertBatch(@RequestBody List<FtpFile> entities){
        return ftpFileDao .insertBatch(entities);
    }

    @Override
    public int insertOrUpdateBatch(@RequestBody  List<FtpFile> entities){
        return ftpFileDao .insertOrUpdateBatch(entities);
    }
    
    @Override
    public int update(@RequestBody FtpFile ftpFile){
        return ftpFileDao .update(ftpFile);
    }

    @Override
    public int deleteById(@RequestBody String ftpId){
        return ftpFileDao .deleteById(ftpId);
    }

}

