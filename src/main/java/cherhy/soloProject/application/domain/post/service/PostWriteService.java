package cherhy.soloProject.application.domain.post.service;


import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.application.domain.photo.entity.Photo;
import cherhy.soloProject.application.domain.photo.repository.jpa.PhotoRepository;
import cherhy.soloProject.application.domain.post.dto.request.PostRequestDto;
import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.post.entity.TimeLine;
import cherhy.soloProject.application.domain.post.repository.jpa.PostRepository;
import cherhy.soloProject.application.domain.post.repository.jpa.TimeLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@Service
@Transactional
@RequiredArgsConstructor
public class PostWriteService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PhotoRepository photoRepository;
    private final TimeLineRepository timeLineRepository;
    private final StringRedisTemplate redisTemplate;

    public String createPost(PostRequestDto postRequestDto){

        Member findMember = findMember(postRequestDto);
        Post addPost = buildPost(postRequestDto, findMember);
        Post savePost = postRepository.save(addPost);
        insertPhoto(postRequestDto, addPost);
        String result = insertTimeLineValue(findMember, addPost);
        addPostLikeToRedis(savePost);
        return result;
    }

    private String insertTimeLineValue(Member findMember, Post post) {
        List<Member> findAllByFollower = memberRepository.findAllByFollowers(findMember.getId());
        return createTimeLine(post, findAllByFollower);
    }

    public String modifyPost(PostRequestDto postRequestDto, Long postId){ //게시물 저장
        Post findPost = getFindPost(postId);
        modify(postRequestDto, findPost);
        return "변경 성공";
    }
    private String createTimeLine(Post post, List<Member> findAllByFollower) {
        if (findAllByFollower.size() < 1){
            return "성공";
        }else {
            uploadTimeLine(post, findAllByFollower);
            return "업로드 성공";
        }
    }

    private Member findMember(PostRequestDto postRequestDto) {
        return memberRepository.findById(postRequestDto.memberId())
                .orElseThrow(() -> new NullPointerException("회원정보가 없습니다"));
    }

    private void addPostLikeToRedis(Post savePost) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String formatPost = String.format("postLike" + savePost.getId());
        ops.set(formatPost, "0");
        ops.get(formatPost);
    }


    private void uploadTimeLine(Post post,  List<Member> findAllByFollower) {
        List<TimeLine> timeLineMember = new ArrayList<>();

        for (Member member : findAllByFollower) {
            TimeLine timeLine = buildTimeLine(post, member);
            timeLineMember.add(timeLine);
        }

        List<TimeLine> timeLines = timeLineRepository.saveAll(timeLineMember);
    }

    private TimeLine buildTimeLine(Post post, Member member) {
        return TimeLine.builder()
                .post(post)
                .member(member)
                .build();
    }

    private Post modify(PostRequestDto postRequestDto, Post findPost) {
        if (!postRequestDto.content().isEmpty()){
            findPost.changeContent(postRequestDto.content());
        }
        if (!postRequestDto.title().isEmpty()){
            findPost.changeTitle(postRequestDto.title());
        }
        if (!postRequestDto.photos().isEmpty()){
            photoRepository.deleteByPostId(findPost.getId());
            insertPhoto(postRequestDto, findPost);
        }
        return findPost;
    }

    private void insertPhoto(PostRequestDto postRequestDto, Post addPost) {
        Post findPost = getFindPost(addPost.getId());
        if(!postRequestDto.photos().isEmpty()){ //사진 업로드가 없을 경우
            buildPhoto(postRequestDto, findPost);
        }
    }

    private Post getFindPost(Long p) {
        return postRepository.findById(p)
                .orElseThrow(() -> new NoSuchElementException("게시물이 존재하지 않습니다."));
    }

    private Post buildPost(PostRequestDto postRequestDto, Member findMember) {
        return Post.builder()
                .member(findMember)
                .title(postRequestDto.title())
                .content(postRequestDto.content())
                .build();
    }

    private void buildPhoto(PostRequestDto postRequestDto, Post findPost) {
        for (String photo : postRequestDto.photos()) {
            Photo build = Photo.builder()
                    .post(findPost)
                    .photo(photo)
                    .build();
            photoRepository.save(build);
        }
    }
}
