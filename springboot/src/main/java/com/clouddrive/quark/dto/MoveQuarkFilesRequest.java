package com.clouddrive.quark.dto;

import java.util.ArrayList;
import java.util.List;

/** 移动文件或文件夹。 */
public class MoveQuarkFilesRequest {
    private String toPdirFid;
    private List<String> filelist = new ArrayList<>();
    private List<String> excludeFids = new ArrayList<>();

    public String getToPdirFid() { return toPdirFid; }
    public void setToPdirFid(String toPdirFid) { this.toPdirFid = toPdirFid; }
    public List<String> getFilelist() { return filelist; }
    public void setFilelist(List<String> filelist) { this.filelist = filelist; }
    public List<String> getExcludeFids() { return excludeFids; }
    public void setExcludeFids(List<String> excludeFids) { this.excludeFids = excludeFids; }
}
