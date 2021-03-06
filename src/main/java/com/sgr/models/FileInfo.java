package com.sgr.models;

import java.util.Date;

public class FileInfo {
    private String fileName;
    private int downloads;
    private String ip;
    private String fileUrl;
    private BrowserDetails browser;
    private Date lastDownload;
    private Date createdAt;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getDownloads() {
        return downloads;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public BrowserDetails getBrowser() {
        return browser;
    }

    public void setBrowser(BrowserDetails browser) {
        this.browser = browser;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getLastDownload() {
        return lastDownload;
    }

    public void setLastDownload(Date lastDownload) {
        this.lastDownload = lastDownload;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
