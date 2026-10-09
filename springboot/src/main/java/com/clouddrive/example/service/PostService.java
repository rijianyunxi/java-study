package com.clouddrive.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.clouddrive.example.dto.PostDetail;
import com.clouddrive.example.dto.PostItem;
import com.clouddrive.example.entity.Post;
import com.clouddrive.mapper.PostMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/** 帖子业务层：列表暂用示例数据，详情从 MySQL 查询。 */
@Service
public class PostService {

    private final PostMapper postMapper;

    public PostService(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    public List<PostItem> listPosts() {
        // 保留项目原有列表示例，不让原有列表接口依赖数据库数据。
        return Arrays.asList(
                new PostItem(1578941L, "没有灵根真的不能修仙"),
                new PostItem(1578939L, "来4个拼多多助力50"),
                new PostItem(1578839L, "天翼云电脑保活+自动"),
                new PostItem(1578923L, "求个可用的抖音解析"),
                new PostItem(1578917L, "直接闭关锁国好了。"),
                new PostItem(1538278L, "个人存妖晶。勿进"),
                new PostItem(1578937L, "每天一问联通卡破二限"),
                new PostItem(1578938L, "110出几个菲区GP"),
                new PostItem(1485694L, "绝命毒师S01-S0"),
                new PostItem(1578861L, "水滴筹求助，打扰大家"),
                new PostItem(1578868L, "番茄🍅小说怎么不更新"),
                new PostItem(1578902L, "想收个10T天翼云盘"),
                new PostItem(1578936L, "真的被他们把上电云电"),
                new PostItem(1578719L, "炒粉是真香"),
                new PostItem(1578940L, "【追剧神器】Webh"),
                new PostItem(1578930L, "天翼云电脑面板2.0"),
                new PostItem(1578928L, "有没有推荐的第三方电"),
                new PostItem(1578871L, "联通29长期卡来了")
        );
    }

    /** 根据主键从 posts 表查询帖子详情。 */
    public PostDetail postDetail(Long id) {
        Post post = postMapper.selectById(id);
        if (post == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "帖子不存在：" + id);
        }
        return new PostDetail(post.getId(), post.getTitle(), post.getPostTime());
    }

    /** 通过 BaseMapper 的条件查询示例：按标题精确查询并转成列表 DTO。 */
    public List<PostItem> findByTitle(String title) {
        QueryWrapper<Post> query = new QueryWrapper<>();
        query.eq("title", title);
        return postMapper.selectList(query).stream()
                .map(post -> new PostItem(post.getId(), post.getTitle()))
                .collect(Collectors.toList());
    }
}
