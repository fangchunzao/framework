package cn.com.tm.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by T420 on 2016/12/21.
 */
@Configuration
public  class  FtpUtil {
    @Value("${ftp.ip}")
    public  String ftpIp;

    @Value("${ftp.port}")
    public  int ftpPort;

    @Value("${ftp.username}")
    public  String ftpUsername;

    @Value("${ftp.password}")
    public  String ftpPassword;

    @Value("${ftp.filePath}")
    public  String filePath;

    @Value("${ftp.tempFilePath}")
    public  String tempFilePath;

    @Value("${ftp.previewPath}")
    public  String previewPath;

    @Value("${ftp.previewPathPdf}")
    public  String previewPathPdf;

    @Value("${ftp.dir}")
    public  String ftpDir;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTempFilePath() {
        return tempFilePath;
    }

    public void setTempFilePath(String tempFilePath) {
        this.tempFilePath = tempFilePath;
    }

    public String getPreviewPath() {
        return previewPath;
    }

    public void setPreviewPath(String previewPath) {
        this.previewPath = previewPath;
    }

    public String getPreviewPathPdf() {
        return previewPathPdf;
    }

    public void setPreviewPathPdf(String previewPathPdf) {
        this.previewPathPdf = previewPathPdf;
    }

    public String getFtpIp() {
        return ftpIp;
    }

    public void setFtpIp(String ftpIp) {
        this.ftpIp = ftpIp;
    }

    public int getFtpPort() {
        return ftpPort;
    }

    public void setFtpPort(int ftpPort) {
        this.ftpPort = ftpPort;
    }

    public String getFtpUsername() {
        return ftpUsername;
    }

    public void setFtpUsername(String ftpUsername) {
        this.ftpUsername = ftpUsername;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public String getFtpDir() {
        return ftpDir;
    }

    public void setFtpDir(String ftpDir) {
        this.ftpDir = ftpDir;
    }
}
