package com.clouddrive.quark.dto;

/** 创建夸克网盘文件夹。 */
public class CreateQuarkFolderRequest {
    private String pdirFid = "0";
    private String fileName;

    public String getPdirFid() { return pdirFid; }
    public void setPdirFid(String pdirFid) { this.pdirFid = pdirFid; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
}
