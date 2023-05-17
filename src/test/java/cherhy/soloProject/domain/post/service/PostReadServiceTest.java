package cherhy.soloProject.domain.post.service;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.service.MemberReadService;
import cherhy.soloProject.domain.member.service.MemberWriteService;
import cherhy.soloProject.domain.member.service.MemberWriteServiceTest;
import cherhy.soloProject.domain.post.dto.response.PostPhotoResponse;
import cherhy.soloProject.domain.post.dto.request.PostRequest;
import cherhy.soloProject.domain.post.entity.Post;
import cherhy.soloProject.exception.PostNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class PostReadServiceTest {

    @Autowired
    PostWriteService postWriteService;
    @Autowired
    PostReadService postReadService;

    @BeforeEach
    @DisplayName("회원, 게시물 추가")
    void addMember(){
        Member member = getMember("abcdef","정철희","ekxk1234@gmail.com", "1234");

        List<String> photos1 = List.of("one", "two", "three", "four", "five");
        PostRequest request1 = new PostRequest("첫번째 게시물입니다", "첫번째 게시물의 내용입니다.", photos1);
        Post post1 = Post.of(request1, member);
        postWriteService.save(post1);

        List<String> photos2 = new ArrayList<>();
        PostRequest request2 = new PostRequest("두번째 게시물입니다", "두번째 게시물의 내용입니다.", photos2);
        Post post2 = Post.of(request2, member);
        postWriteService.save(post2);

        List<String> photos3 = List.of("1", "2");
        PostRequest request3 = new PostRequest("세번째 게시물입니다", "세번째 게시물의 내용입니다.", photos3);
        Post post3 = Post.of(request3, member);
        postWriteService.save(post3);

        List<String> photos4 = List.of("3", "4", "5");
        PostRequest request4 = new PostRequest("네번째 게시물입니다", "네번째 게시물의 내용입니다.", photos4);
        Post post4 = Post.of(request4, member);
        postWriteService.save(post4);
    }

    @Test
    @DisplayName("로그인 전 게시물 조회")
    void testGetPost(){
        // given
        Post post = postReadService.getPost(1L);

        // then
        Assertions.assertThat(post.getTitle()).isEqualTo("첫번째 게시물입니다");
    }

    @Test
    @DisplayName("로그인 후 게시물 조회")
    void testGetPostToLogin(){
        // given
        Member me = getMember("abcdef","정철희","ekxk1234@gmail.com", "1234");
        Member you = getMember("test1234","유재석","ekxk1234@naver.com", "4321");

        //when
        Post myPost = postReadService.getMyPost(1L, me);

        // then
        Assertions.assertThat(myPost.getTitle()).isEqualTo("첫번째 게시물입니다");
        assertThrows(PostNotFoundException.class,
                () -> postReadService.getMyPost(1L, you));

    }

    @Test
    @DisplayName("로그인 전 상대방 게시물 조회, 무한 스크롤")
    void testGetScroll(){
        // given
        Member you = getMember("test1234","유재석","ekxk1234@naver.com", "4321");
        ScrollRequest scrollRequest = new ScrollRequest(null);

        //when
        List<Post> posts = postReadService.getPostByMemberIdCursor(you, scrollRequest);
        List<PostPhotoResponse> postPhotoDto = posts.stream().map(PostPhotoResponse::of).collect(Collectors.toList());
        long nextKey = postReadService.getNextKey(postPhotoDto);

        // then
        Assertions.assertThat(postPhotoDto.get(2).getId()).isEqualTo(2);
        Assertions.assertThat(nextKey).isEqualTo(2);
    }

    @Test
    @DisplayName("로그인 후 상대방 게시물 조회, 무한 스크롤")
    void testGetScrollToLogin(){
        // given
        Member me = getMember("abcdef","정철희","ekxk1234@gmail.com", "1234");

        Member you = getMember("test1234","유재석","ekxk1234@naver.com", "4321");
        ScrollRequest scrollRequest = new ScrollRequest(null);

        //when
        List<Post> posts = postReadService.getPostByMemberIdCursor(me, you, scrollRequest);
        List<PostPhotoResponse> postPhotoDto = posts.stream().map(PostPhotoResponse::of).collect(Collectors.toList());
        long nextKey = postReadService.getNextKey(postPhotoDto);

        // then
        Assertions.assertThat(postPhotoDto.size()).isEqualTo(3);
        Assertions.assertThat(nextKey).isEqualTo(2);

    }

    private Member getMember(String userId, String name, String email, String password) {
        return Member.builder()
                .userId(userId)
                .name(name)
                .email(email)
                .password(password)
                .build();
    }

}