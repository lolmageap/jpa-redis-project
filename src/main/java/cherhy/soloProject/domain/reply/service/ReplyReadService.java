package cherhy.soloProject.domain.reply.service;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.reply.dto.response.ResponseReplyDto;
import cherhy.soloProject.domain.reply.entity.Reply;
import cherhy.soloProject.domain.reply.repository.jpa.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReplyReadService {
    public final ReplyRepository replyRepository;

    // TODO : 레디스에서 댓글 id 조회
    public List<Reply> getPostIdScroll(Long postId, List<Long> sortedKeys) {
        return replyRepository.findByPostIdScroll(postId, sortedKeys);
    }

    // TODO : 레디스에서 다음 키값 조회
    public Long NextKeyCheck(ScrollRequest scrollRequest, List<Long> sortedKeys) {
        if (sortedKeys.size()-1 == ScrollRequest.size){
            Long nextKey = sortedKeys.get(ScrollRequest.size);
            sortedKeys.remove(ScrollRequest.size);
            return nextKey;
        }
        return scrollRequest.NONE_KEY;
    }

    // TODO : scrollRequest에서 정렬 되어있는 키 조회
    public List<Long> getSortedKeys(ScrollRequest scrollRequest, ZSetOperations zSetOps, String postRedis) {
        Long checkKey = keyCheck(scrollRequest, zSetOps, postRedis);
        Set<String> keys = zSetOps.reverseRange(postRedis, checkKey, ScrollRequest.size);
        List<Long> parseKeys = keys.stream().map(v -> Long.parseLong(v)).collect(Collectors.toList());
        return parseKeys;
    }

    // TODO : 키가 있으면 키값 조회, 키가 없으면 0 반환
    public Long keyCheck(ScrollRequest scrollRequest, ZSetOperations zSetOps, String postRedis) {
        long key = scrollRequest.hasKey() ? zSetOps.reverseRank(postRedis, Long.toString(scrollRequest.key())) : 0L;
        return key;
    }

}
