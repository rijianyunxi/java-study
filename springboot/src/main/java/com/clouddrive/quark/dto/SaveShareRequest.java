package com.clouddrive.quark.dto;

import java.util.ArrayList;
import java.util.List;

/** 将分享页文件转存到自己的网盘。 */
public class SaveShareRequest {
    private List<String> fidList = new ArrayList<>();
    private String toPdirFid;
    private String pwdId;
    private String stoken;
    private String pdirFid = "0";
    private boolean pdirSaveAll;
    private List<String> excludeFids = new ArrayList<>();
    private String scene = "link";
    private String toPdirName;

    public List<String> getFidList() { return fidList; }
    public void setFidList(List<String> fidList) { this.fidList = fidList; }
    public String getToPdirFid() { return toPdirFid; }
    public void setToPdirFid(String toPdirFid) { this.toPdirFid = toPdirFid; }
    public String getPwdId() { return pwdId; }
    public void setPwdId(String pwdId) { this.pwdId = pwdId; }
    public String getStoken() { return stoken; }
    public void setStoken(String stoken) { this.stoken = stoken; }
    public String getPdirFid() { return pdirFid; }
    public void setPdirFid(String pdirFid) { this.pdirFid = pdirFid; }
    public boolean isPdirSaveAll() { return pdirSaveAll; }
    public void setPdirSaveAll(boolean pdirSaveAll) { this.pdirSaveAll = pdirSaveAll; }
    public List<String> getExcludeFids() { return excludeFids; }
    public void setExcludeFids(List<String> excludeFids) { this.excludeFids = excludeFids; }
    public String getScene() { return scene; }
    public void setScene(String scene) { this.scene = scene; }
    public String getToPdirName() { return toPdirName; }
    public void setToPdirName(String toPdirName) { this.toPdirName = toPdirName; }
}
