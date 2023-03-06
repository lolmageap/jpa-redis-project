package cherhy.soloProject.application.key;

import lombok.Getter;

@Getter
public enum RedisKey {

    REPLY_MODIFY_DESC("postReplyOrderByLastModifyDate:"),
    SEARCH_LOG("SearchLog:"),
    SEARCH_RANK("SearchRank"),
    POST_LIKE("postLike:");

    RedisKey(String value) {}
}
