package com.clouddrive.quark.service;

import com.clouddrive.quark.client.QuarkApiClient;
import com.clouddrive.quark.config.QuarkProperties;
import com.clouddrive.quark.dto.CreateQuarkShareRequest;
import com.clouddrive.quark.dto.SaveShareRequest;
import com.clouddrive.quark.dto.ShareIdRequest;
import com.clouddrive.quark.dto.SharePageTokenRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/** 夸克分享、分享页浏览和转存代理服务。 */
@Service
public class QuarkShareService {

    private final QuarkApiClient client;
    private final QuarkProperties properties;

    public QuarkShareService(QuarkApiClient client, QuarkProperties properties) {
        this.client = client;
        this.properties = properties;
    }

    public ResponseEntity<String> create(CreateQuarkShareRequest request, String cookie) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("fid_list", request.getFidList());
        body.put("title", request.getTitle());
        body.put("url_type", request.getUrlType());
        body.put("expired_type", request.getExpiredType());
        if (request.getExpiredAt() != null) {
            body.put("expired_at", request.getExpiredAt());
        }
        if (request.getPasscode() != null && !request.getPasscode().trim().isEmpty()) {
            body.put("passcode", request.getPasscode());
        }
        return client.post(properties.getApiBaseUrl(), "share", null, body, cookie);
    }

    public ResponseEntity<String> getShareUrl(ShareIdRequest request, String cookie) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("share_id", request.getShareId());
        return client.post(properties.getApiBaseUrl(), "share/password", null, body, cookie);
    }

    public ResponseEntity<String> listMine(int page, int size, String cookie) {
        Map<String, String> query = new LinkedHashMap<>();
        query.put("_page", String.valueOf(page));
        query.put("_size", String.valueOf(size));
        query.put("_order_field", "created_at");
        query.put("_order_type", "desc");
        query.put("_fetch_total", "1");
        query.put("_fetch_notify_follow", "1");
        return client.get(properties.getApiBaseUrl(), "share/mypage/detail", query, cookie);
    }

    public ResponseEntity<String> delete(ShareIdRequest request, String cookie) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("share_id", request.getShareId());
        return client.post(properties.getApiBaseUrl(), "share/delete", null, body, cookie);
    }

    public ResponseEntity<String> getShareToken(SharePageTokenRequest request, String cookie) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("pwd_id", request.getPwdId());
        body.put("passcode", request.getPasscode() == null ? "" : request.getPasscode());
        body.put("support_visit_limit_private_share", true);
        return client.post(properties.getShareBaseUrl(), "share/sharepage/token", null, body, cookie);
    }

    public ResponseEntity<String> listShareFiles(String pwdId, String stoken, String pdirFid,
                                                 int page, int size, String cookie) {
        Map<String, String> query = new LinkedHashMap<>();
        query.put("pwd_id", pwdId);
        query.put("stoken", stoken);
        query.put("pdir_fid", pdirFid == null ? "0" : pdirFid);
        query.put("force", "0");
        query.put("_page", String.valueOf(page));
        query.put("_size", String.valueOf(size));
        query.put("_fetch_banner", "1");
        query.put("_fetch_share", "1");
        query.put("_fetch_total", "1");
        query.put("_sort", "file_type:asc,file_name:asc");
        return client.get(properties.getShareBaseUrl(), "share/sharepage/detail", query, cookie);
    }

    public ResponseEntity<String> save(SaveShareRequest request, String cookie) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("fid_list", request.getFidList());
        body.put("fid_token_list", new Object[0]);
        body.put("to_pdir_fid", request.getToPdirFid());
        body.put("pwd_id", request.getPwdId());
        body.put("stoken", request.getStoken());
        body.put("pdir_fid", request.getPdirFid());
        body.put("pdir_save_all", request.isPdirSaveAll());
        body.put("exclude_fids", request.getExcludeFids());
        body.put("scene", request.getScene() == null ? "link" : request.getScene());
        if (request.getToPdirName() != null && !request.getToPdirName().trim().isEmpty()) {
            body.put("to_pdir_name", request.getToPdirName());
        }
        return client.post(properties.getShareBaseUrl(), "share/sharepage/save", null, body, cookie);
    }
}
