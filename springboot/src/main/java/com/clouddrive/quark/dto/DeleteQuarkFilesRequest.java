package com.clouddrive.quark.dto;

import java.util.ArrayList;
import java.util.List;

/** 删除文件或文件夹。 */
public class DeleteQuarkFilesRequest {
    private List<String> filelist = new ArrayList<>();
    private List<String> excludeFids = new ArrayList<>();

    public List<String> getFilelist() { return filelist; }
    public void setFilelist(List<String> filelist) { this.filelist = filelist; }
    public List<String> getExcludeFids() { return excludeFids; }
    public void setExcludeFids(List<String> excludeFids) { this.excludeFids = excludeFids; }
}
