package cherhy.soloProject.application.domain.postBlock.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostBlockWriteServiceTest {

    @Autowired
    PostBlockWriteService postBlockWriteService;

    @Test
    public void test(){
        String s = postBlockWriteService.blockPost(1L, 1L);
        Assertions.assertThat(s).isEqualTo("차단 성공");
    }
}