package com.clouddrive.example.service;

import com.clouddrive.example.dto.PostItem;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/** 帖子业务层：目前返回固定示例数据，不请求外部网站或数据库。 */
@Service
public class PostService {

    public List<PostItem> listPosts() {
        // id 取自链接中的帖子编号；按用户提供的顺序返回，标题不补全、不改写。
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
}
