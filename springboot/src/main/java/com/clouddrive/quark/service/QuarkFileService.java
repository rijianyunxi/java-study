package com.clouddrive.quark.service;

import com.clouddrive.quark.client.QuarkApiClient;
import com.clouddrive.quark.config.QuarkProperties;
import com.clouddrive.quark.dto.CreateQuarkFolderRequest;
import com.clouddrive.quark.dto.DeleteQuarkFilesRequest;
import com.clouddrive.quark.dto.DownloadQuarkFilesRequest;
import com.clouddrive.quark.dto.MoveQuarkFilesRequest;
import com.clouddrive.quark.dto.RenameQuarkFileRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/** 夸克网盘文件与异步任务代理服务。 */
@Service
public class QuarkFileService {

    private final QuarkApiClient client;
    private final QuarkProperties properties;

    public QuarkFileService(QuarkApiClient client, QuarkProperties properties) {
        this.client = client;
        this.properties = properties;
    }

    public ResponseEntity<String> list(String pdirFid, int page, int size, String sort, String cookie) {
        Map<String, String> query = new LinkedHashMap<>();
        query.put("pdir_fid", defaultValue(pdirFid, "0"));
        query.put("_page", String.valueOf(page));
        query.put("_size", String.valueOf(size));
        query.put("_fetch_total", "1");
        query.put("_sort", defaultValue(sort, "file_name:asc"));
        return client.get(properties.getApiBaseUrl(), "file/sort", query, cookie);
    }

    public ResponseEntity<String> detail(String fid, String cookie) {
        Map<String, String> query = new LinkedHashMap<>();
        query.put("fids", fid);
        return client.get(properties.getApiBaseUrl(), "file", query, cookie);
    }

    public ResponseEntity<String> createFolder(CreateQuarkFolderRequest request, String cookie) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("pdir_fid", defaultValue(request.getPdirFid(), "0"));
        body.put("file_name", request.getFileName());
        body.put("dir_init_lock", false);
        body.put("dir_path", "");
        return client.post(properties.getApiBaseUrl(), "file", null, body, cookie);
    }

    public ResponseEntity<String> delete(DeleteQuarkFilesRequest request, String cookie) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("action_type", 2);
        body.put("filelist", request.getFilelist());
        body.put("exclude_fids", request.getExcludeFids());
        return client.post(properties.getApiBaseUrl(), "file/delete", null, body, cookie);
    }

    public ResponseEntity<String> rename(RenameQuarkFileRequest request, String cookie) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("fid", request.getFid());
        body.put("file_name", request.getFileName());
        return client.post(properties.getApiBaseUrl(), "file/rename", null, body, cookie);
    }

    public ResponseEntity<String> search(String keyword, int page, int size, String sort, String cookie) {
        Map<String, String> query = new LinkedHashMap<>();
        query.put("q", keyword);
        query.put("_page", String.valueOf(page));
        query.put("_size", String.valueOf(size));
        query.put("_fetch_total", "1");
        query.put("_sort", defaultValue(sort, "file_type:desc,updated_at:desc"));
        query.put("_is_hl", "1");
        return client.get(properties.getApiBaseUrl(), "file/search", query, cookie);
    }

    public ResponseEntity<String> tree(String pdirFid, int maxDepth, String cookie) {
        Map<String, String> query = new LinkedHashMap<>();
        query.put("pdir_fid", defaultValue(pdirFid, "0"));
        query.put("max_depth", String.valueOf(maxDepth));
        return client.get(properties.getApiBaseUrl(), "file/tree", query, cookie);
    }

    public ResponseEntity<String> move(MoveQuarkFilesRequest request, String cookie) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("action_type", 1);
        body.put("to_pdir_fid", request.getToPdirFid());
        body.put("filelist", request.getFilelist());
        body.put("exclude_fids", request.getExcludeFids());
        return client.post(properties.getApiBaseUrl(), "file/move", null, body, cookie);
    }

    public ResponseEntity<String> task(String taskId, int retryIndex, String cookie) {
        Map<String, String> query = new LinkedHashMap<>();
        query.put("task_id", taskId);
        query.put("retry_index", String.valueOf(retryIndex));
        return client.get(properties.getApiBaseUrl(), "task", query, cookie);
    }

    public ResponseEntity<String> download(DownloadQuarkFilesRequest request, String cookie) {
        Map<String, String> query = new LinkedHashMap<>();
        query.put("sys", "win32");
        query.put("ve", "2.5.56");
        query.put("ut", "");
        query.put("guid", "");
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("fids", request.getFids());
        return client.post(properties.getApiBaseUrl(), "file/download", query, body, cookie);
    }

    private String defaultValue(String value, String fallback) {
        return value == null || value.trim().isEmpty() ? fallback : value;
    }
}
