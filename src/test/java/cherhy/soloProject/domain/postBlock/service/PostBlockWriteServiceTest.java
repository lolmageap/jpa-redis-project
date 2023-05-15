package cherhy.soloProject.domain.postBlock.service;

import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.domain.post.entity.Post;
import cherhy.soloProject.domain.postBlock.entity.PostBlock;
import cherhy.soloProject.domain.postBlock.repository.jpa.PostBlockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class PostBlockWriteServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    PostBlockReadService postBlockReadService;
    @Autowired
    PostBlockWriteService postBlockWriteService;

    @Autowired
    PostBlockRepository postBlockRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("유저가 게시물 차단하기")
    public void testPostBlock(){
        // given
        List<String> photos = List.of("one", "two", "three", "four", "five");
        Member member = new Member("abcdef", "정철희", "ekxk1234@gmail.com", "1234");
        Post post = new Post(member, "첫번째 게시물", " 첫번째 내용", photos, 0);

        memberRepository.save(member);

        PostBlock postBlock = PostBlock.of(member, post);

        // when
        postBlockWriteService.block(postBlock);

        em.clear();
        em.flush();

        // then
        PostBlock find = postBlockRepository.findById(1L).get();
        assertThat(find.getMember().getName()).isEqualTo(member.getName());
    }

}