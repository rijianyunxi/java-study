package com.clouddrive.quark.dto;

/** 换取分享页访问 token。 */
public class SharePageTokenRequest {
    private String pwdId;
    private String passcode = "";

    public String getPwdId() { return pwdId; }
    public void setPwdId(String pwdId) { this.pwdId = pwdId; }
    public String getPasscode() { return passcode; }
    public void setPasscode(String passcode) { this.passcode = passcode; }
}
