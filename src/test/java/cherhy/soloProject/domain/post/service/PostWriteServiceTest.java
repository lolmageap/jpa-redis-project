package cherhy.soloProject.domain.post.service;

import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.service.MemberReadService;
import cherhy.soloProject.domain.member.service.MemberWriteService;
import cherhy.soloProject.domain.member.service.MemberWriteServiceTest;
import cherhy.soloProject.domain.post.dto.request.PostRequest;
import cherhy.soloProject.domain.post.entity.Post;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional
@SpringBootTest
class PostWriteServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    PostWriteService postWriteService;
    @Autowired
    PostReadService postReadService;
    @Autowired
    MemberWriteService memberWriteService;
    @Autowired
    MemberReadService memberReadService;
    
    @Test
    @DisplayName("폼데이터를 전달받아 게시글을 등록한다.")
    void testSavePost(){
        // given
        Member member = Member.builder()
                .userId("abcdef")
                .name("정철희")
                .email("ekxk1234@gmail.com")
                .password("1234")
                .build();

        List<String> photos = List.of("one", "two", "three", "four", "five");
        PostRequest request = new PostRequest("첫번째 게시물입니다", "첫번째 게시물의 내용입니다.", photos);

        Post post = Post.of(request, member);
        postWriteService.save(post);

        em.flush();
        em.clear();

        // when
        Post findPost = postReadService.getPost(1L);

        // then
        Assertions.assertThat(findPost.getId()).isEqualTo(1L);
        Assertions.assertThat(findPost.getPhotos().size()).isEqualTo(5);
    }

    @Test
    @DisplayName("폼데이터를 전달받아 게시글을 수정한다.")
    void testUpdatePost(){
        // given
        Member member = Member.builder()
                .userId("abcdef")
                .name("정철희")
                .email("ekxk1234@gmail.com")
                .password("1234")
                .build();

        List<String> photos = List.of("1", "2", "3");
        PostRequest request = new PostRequest("바꾼 게시글 입니다.", "내용도 같이 수정했습니다.", photos);

        Post post = Post.of(request, member);
        postWriteService.save(post);

        em.flush();
        em.clear();

        // when
        Post updatePost = postWriteService.update(request, post);

        // then
        Assertions.assertThat(updatePost.getId()).isEqualTo(1L);
        Assertions.assertThat(updatePost.getPhotos().size()).isEqualTo(3);
    }

}