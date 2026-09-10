package com.clouddrive.quark.dto;

/** 重命名文件或文件夹。 */
public class RenameQuarkFileRequest {
    private String fid;
    private String fileName;

    public String getFid() { return fid; }
    public void setFid(String fid) { this.fid = fid; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
}
