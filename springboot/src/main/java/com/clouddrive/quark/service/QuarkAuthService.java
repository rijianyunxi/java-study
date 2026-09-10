package com.clouddrive.quark.service;

import com.clouddrive.quark.client.QuarkApiClient;
import com.clouddrive.quark.config.QuarkProperties;
import com.clouddrive.quark.dto.QrCodeData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/** 夸克扫码登录代理。登录态最终仍由用户决定是否保存到浏览器或环境变量。 */
@Service
public class QuarkAuthService {

    private final QuarkApiClient client;
    private final QuarkProperties properties;
    private final ObjectMapper objectMapper;

    public QuarkAuthService(QuarkApiClient client, QuarkProperties properties,
                            ObjectMapper objectMapper) {
        this.client = client;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    public QrCodeData createQrCode() {
        Map<String, String> query = new LinkedHashMap<>();
        query.put("client_id", "532");
        query.put("v", "1.2");
        query.put("request_id", UUID.randomUUID().toString());
        ResponseEntity<String> response = client.get(properties.getAuthBaseUrl(),
                "cas/ajax/getTokenForQrcodeLogin", query, null);
        String upstream = response.getBody() == null ? "" : response.getBody();
        String token = findToken(upstream);
        String qrUrl = UriComponentsBuilder
                .fromHttpUrl("https://su.quark.cn/4_eMHBJ")
                .queryParam("token", token)
                .queryParam("client_id", "532")
                .queryParam("ssb", "weblogin")
                .queryParam("uc_param_str", "")
                .queryParam("uc_biz_str", "S:custom|OPT:SAREA@0|OPT:IMMERSIVE@1|OPT:BACK_BTN_STYLE@0")
                .build().encode().toUriString();
        return new QrCodeData(token, qrUrl, upstream);
    }

    public ResponseEntity<String> pollQrCode(String token) {
        Map<String, String> query = new LinkedHashMap<>();
        query.put("client_id", "532");
        query.put("v", "1.2");
        query.put("token", token);
        query.put("request_id", UUID.randomUUID().toString());
        return client.get(properties.getAuthBaseUrl(),
                "cas/ajax/getServiceTicketByQrcodeToken", query, null);
    }

    public ResponseEntity<String> exchangeTicket(String ticket) {
        Map<String, String> query = new LinkedHashMap<>();
        query.put("st", ticket);
        query.put("lw", "scan");
        return client.get("https://pan.quark.cn", "account/info", query, null);
    }

    private String findToken(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode token = root.findValue("token");
            return token == null ? "" : token.asText("");
        } catch (Exception ignored) {
            return "";
        }
    }
}
