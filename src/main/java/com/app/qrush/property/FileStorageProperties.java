package com.app.qrush.property;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "storage")
public class FileStorageProperties {

    private String baseDir;
    private String qrCodeDir;

    private String userDir;

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public String getQrCodeDir() {
        return qrCodeDir;
    }

    public void setQrCodeDir(String qrCodeDir) {
        this.qrCodeDir = qrCodeDir;
    }

    public String getUserDir() {
        return userDir;
    }

    public void setUserDir(String userDir) {
        this.userDir = userDir;
    }
}

