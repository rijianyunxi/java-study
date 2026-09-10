package com.clouddrive.quark.dto;

import java.util.ArrayList;
import java.util.List;

/** 获取文件下载直链。 */
public class DownloadQuarkFilesRequest {
    private List<String> fids = new ArrayList<>();

    public List<String> getFids() { return fids; }
    public void setFids(List<String> fids) { this.fids = fids; }
}
