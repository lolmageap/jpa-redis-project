package cherhy.soloProject.domain.postBlock.service;

import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.service.MemberReadService;
import cherhy.soloProject.domain.member.service.MemberWriteService;
import cherhy.soloProject.domain.member.service.MemberWriteServiceTest;
import cherhy.soloProject.domain.post.dto.request.PostRequestDto;
import cherhy.soloProject.domain.post.entity.Post;
import cherhy.soloProject.domain.post.service.PostReadService;
import cherhy.soloProject.domain.post.service.PostWriteService;
import cherhy.soloProject.domain.postBlock.entity.PostBlock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PostBlockWriteServiceTest {

    @Autowired
    PostBlockReadService postBlockReadService;
    @Autowired
    PostBlockWriteService postBlockWriteService;
    @Autowired
    MemberReadService memberReadService;
    @Autowired
    MemberWriteService memberWriteService;
    @Autowired
    PostReadService postReadService;
    @Autowired
    PostWriteService postWriteService;

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
    }

    @Test
    @DisplayName("차단하기")
    public void testPostBlock(){
        Member me = memberReadService.getMember(3L);
        Post post = postReadService.getPost(1L);

        postBlockWriteService.block(me, post);
        List<PostBlock> postBlocks = postBlockReadService.getPostBlocks(me);
        assertThat(postBlocks.size()).isEqualTo(1);
    }
}