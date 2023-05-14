package cherhy.soloProject.domain.post.service;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.service.MemberReadService;
import cherhy.soloProject.domain.member.service.MemberWriteService;
import cherhy.soloProject.domain.member.service.MemberWriteServiceTest;
import cherhy.soloProject.domain.post.dto.PostPhotoDto;
import cherhy.soloProject.domain.post.dto.request.PostRequestDto;
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

import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class PostReadServiceTest {

    @Autowired
    PostWriteService postWriteService;
    @Autowired
    PostReadService postReadService;
    @Autowired
    MemberWriteService memberWriteService;
    @Autowired
    MemberReadService memberReadService;

    @BeforeEach
    @DisplayName("회원, 게시물 추가")
    void addMember(){
        MemberWriteServiceTest memberWriteServiceTest = new MemberWriteServiceTest(memberReadService, memberWriteService);
        memberWriteServiceTest.addMember();
        Member member = memberReadService.getMember(1L);

        List<String> photos1 = List.of("one", "two", "three", "four", "five");
        PostRequestDto request1 = new PostRequestDto("첫번째 게시물입니다", "첫번째 게시물의 내용입니다.", photos1);
        Post post1 = Post.of(request1, member);
        postWriteService.save(post1);

        List<String> photos2 = new ArrayList<>();
        PostRequestDto request2 = new PostRequestDto("두번째 게시물입니다", "두번째 게시물의 내용입니다.", photos2);
        Post post2 = Post.of(request2, member);
        postWriteService.save(post2);

        List<String> photos3 = List.of("1", "2");
        PostRequestDto request3 = new PostRequestDto("세번째 게시물입니다", "세번째 게시물의 내용입니다.", photos3);
        Post post3 = Post.of(request3, member);
        postWriteService.save(post3);

        List<String> photos4 = List.of("3", "4", "5");
        PostRequestDto request4 = new PostRequestDto("네번째 게시물입니다", "네번째 게시물의 내용입니다.", photos4);
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
        Member me = memberReadService.getMember(1L);
        Member you = memberReadService.getMember(2L);

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
        Member me = memberReadService.getMember(1L);
        Member you = memberReadService.getMember(2L);
        ScrollRequest scrollRequest = new ScrollRequest(null);

        //when
        List<Post> posts = postReadService.getPostByMemberIdCursor(me, scrollRequest);
        List<PostPhotoDto> postPhotoDto = PostPhotoDto.from(posts);
        long nextKey = postReadService.getNextKey(postPhotoDto);

        // then
        Assertions.assertThat(postPhotoDto.get(2).getId()).isEqualTo(2);
        Assertions.assertThat(nextKey).isEqualTo(2);
    }

    @Test
    @DisplayName("로그인 후 상대방 게시물 조회, 무한 스크롤")
    void testGetScrollToLogin(){
        // given
        Member me = memberReadService.getMember(1L);
        Member you = memberReadService.getMember(2L);
        ScrollRequest scrollRequest = new ScrollRequest(null);

        //when
        List<Post> posts = postReadService.getPostByMemberIdCursor(me, you, scrollRequest);
        List<PostPhotoDto> postPhotoDto = PostPhotoDto.from(posts);
        long nextKey = postReadService.getNextKey(postPhotoDto);

        // then
        Assertions.assertThat(postPhotoDto.size()).isEqualTo(3);
        Assertions.assertThat(nextKey).isEqualTo(2);

    }


}