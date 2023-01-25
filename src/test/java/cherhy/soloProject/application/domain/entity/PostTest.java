package cherhy.soloProject.application.domain.entity;

import cherhy.soloProject.application.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.application.domain.member.service.MemberWriteService;
import cherhy.soloProject.application.domain.photo.repository.jpa.PhotoRepository;
import cherhy.soloProject.application.domain.post.dto.PostDto;
import cherhy.soloProject.application.domain.post.repository.jpa.PostRepository;
import cherhy.soloProject.application.domain.post.service.PostReadService;
import cherhy.soloProject.application.domain.post.service.PostWriteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;


@SpringBootTest
@Transactional
@Rollback(value = false)
class PostTest {
    @PersistenceContext
    EntityManager em;
    @Autowired
    PostWriteService postWriteService;
    @Autowired
    PostReadService postReadService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    MemberWriteService memberWriteService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PhotoRepository photoRepository;

//    @BeforeEach
//    public void bef(){
////        MemberDto member = new MemberDto("aaaa","홍길동","abcd@naver.com","abcd", LocalDate.now());
//        MemberDto member = new MemberDto("aaaa","홍길동","abcd@naver.com","abcd");
//        memberWriteService.signUp(member);
//    }

    @Test
    public void postTest(){
//      사진 입력받음

        ArrayList<String> photoList = new ArrayList<>();
        for (int i = 0; i < 52; i++) {
        photoList.add("test number one" + i);
        photoList.add("test number two" + i);
        photoList.add("test number three" + i);

        PostDto postDto = new PostDto(2L,i+i + "개",i+i  + "장", photoList);
        postWriteService.save(postDto);
        }

//        em.flush();
//        em.clear();
//
//        List<PostPhotoDto> findByPosts = postReadService.findPostByMemberId(postDto.memberId());
//        List<Photo> photos = photoRepository.findByPostId(findByPosts.get(0).getId());
//
//        assertThat(photos.size()).isEqualTo(3);

    }

}