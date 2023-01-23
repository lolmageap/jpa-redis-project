package cherhy.soloProject.application.domain.entity;

import cherhy.soloProject.application.domain.dto.MemberDto;
import cherhy.soloProject.application.domain.dto.PostDto;
import cherhy.soloProject.application.domain.dto.PostPhotoDto;
import cherhy.soloProject.application.repository.jpa.MemberRepository;
import cherhy.soloProject.application.repository.jpa.PhotoRepository;
import cherhy.soloProject.application.repository.jpa.PostRepository;
import cherhy.soloProject.application.service.memberService.MemberWriteService;
import cherhy.soloProject.application.service.postService.PostReadService;
import cherhy.soloProject.application.service.postService.PostWriteService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

    @BeforeEach
    public void bef(){
//        MemberDto member = new MemberDto("aaaa","홍길동","abcd@naver.com","abcd", LocalDate.now());
        MemberDto member = new MemberDto("aaaa","홍길동","abcd@naver.com","abcd");
        memberWriteService.signUp(member);
    }

    @Test
    public void postTest(){
//      사진 입력받음
        ArrayList<String> photoList = new ArrayList<>();
        photoList.add("test1");
        photoList.add("test2");
        photoList.add("test3");

        PostDto postDto = new PostDto(1L,"photo","3장", photoList);
        postWriteService.save(postDto);

        em.flush();
        em.clear();

        List<PostPhotoDto> findByPosts = postReadService.findPostByMemberId(postDto.memberId());
        List<Photo> photos = photoRepository.findByPostId(findByPosts.get(0).getId());

        assertThat(photos.size()).isEqualTo(3);

    }









}