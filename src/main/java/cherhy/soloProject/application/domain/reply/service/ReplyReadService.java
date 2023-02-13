package cherhy.soloProject.application.domain.reply.service;

import cherhy.soloProject.Util.scrollDto.PageScroll;
import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.post.repository.jpa.PostRepository;
import cherhy.soloProject.application.domain.reply.dto.response.ResponseReplyDto;
import cherhy.soloProject.application.domain.reply.entity.Reply;
import cherhy.soloProject.application.domain.reply.repository.jpa.ReplyRepository;
import cherhy.soloProject.application.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyReadService {
    private final ReplyRepository replyRepository;
    private final PostRepository postRepository;
    private final StringRedisTemplate stringRedisTemplate;

    public List<ResponseReplyDto> getReply(Long postId) {
        Post findPost = getPost(postId);
        List<Reply> replies = findPost.getReplies();
        return changeResponseReplyDto(postId, replies);
    }

    // 레디스를 사용한 무한 스크롤
    public PageScroll<ResponseReplyDto> getReplyScrollInRedis(Long postId, ScrollRequest scrollRequest) {
        ZSetOperations zSetOps = stringRedisTemplate.opsForZSet();

        Post findPost = getPost(postId);

        String postRedis = "postReplyOrderByLastModifyDate:" + findPost.getId(); // redis key 값

        List<Long> sortedKeys = getSortedKeys(scrollRequest, zSetOps, postRedis); // scrollRequest 정렬 되어있는 키 가져오기

        Long getNextKey = NextKeyCheck(scrollRequest, sortedKeys); // 다음 키값을 가져옴

        List<Reply> repliesScroll = replyRepository.findByPostIdScroll(postId, sortedKeys); // 정렬된 값을 기준으로 값 가져오기

        List<ResponseReplyDto> responseReplyDtos = changeResponseReplyDto(postId, repliesScroll); //dto로 변환

//        long getNextKey = nextKey(scrollRequest, repliesScroll);
        return new PageScroll<>(scrollRequest.next(getNextKey),responseReplyDtos);
    }

    private Long NextKeyCheck(ScrollRequest scrollRequest, List<Long> sortedKeys) {
        if (sortedKeys.size()-1 == scrollRequest.size()){
            Long nextKey = sortedKeys.get(scrollRequest.size());
            sortedKeys.remove(scrollRequest.size());
            return nextKey;
        }
        return scrollRequest.NONE_KEY;
    }
    private List<Long> getSortedKeys(ScrollRequest scrollRequest, ZSetOperations zSetOps, String postRedis) {
        Long checkKey = keyCheck(scrollRequest, zSetOps, postRedis);
        Set<String> keys = zSetOps.reverseRange(postRedis, checkKey, scrollRequest.size());
        List<Long> parseKeys = keys.stream().map(v -> Long.parseLong(v)).collect(Collectors.toList());
        return parseKeys;
    }

    private Long keyCheck(ScrollRequest scrollRequest, ZSetOperations zSetOps, String postRedis) {
        long key = scrollRequest.hasKey() ? zSetOps.reverseRank(postRedis, Long.toString(scrollRequest.key())) : 0L;
        return key;
    }

    private List<ResponseReplyDto> changeResponseReplyDto(Long postId, List<Reply> replies) {
        return replies.stream()
                .map(r -> new ResponseReplyDto(
                        r.getMember().getId(), r.getMember().getName(), postId, r.getContent())
                ).collect(Collectors.toList());
    }

    private Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
    }
}
