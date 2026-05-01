package com.java110.front.smo.ftp;

import com.java110.utils.util.FtpFileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lmf
 * @date 2022/8/2
 */
@Configuration
public class FtpConfig {

    @Value("${ftp.ftpIp}")
    private String ftpIp;
    @Value("${ftp.ftpUser}")
    private String ftpUser;
    @Value("${ftp.ftpPwd}")
    private String ftpPwd;
    @Value("${ftp.ftpPort}")
    private Integer ftpPort;

    @Bean("ftpFileUtil")
    public FtpFileUtil getFtpFileUtil(){
        FtpFileUtil ftpFileUtil = new FtpFileUtil();
        ftpFileUtil.setFtpIp(ftpIp);
        ftpFileUtil.setFtpPwd(ftpPwd);
        ftpFileUtil.setFtpPort(ftpPort);
        ftpFileUtil.setFtpUser(ftpUser);
        return ftpFileUtil;
    }

    public void setFtpIp(String ftpIp) {
        this.ftpIp = ftpIp;
    }

    public void setFtpUser(String ftpUser) {
        this.ftpUser = ftpUser;
    }

    public void setFtpPwd(String ftpPwd) {
        this.ftpPwd = ftpPwd;
    }

    public void setFtpPort(Integer ftpPort) {
        this.ftpPort = ftpPort;
    }
}
