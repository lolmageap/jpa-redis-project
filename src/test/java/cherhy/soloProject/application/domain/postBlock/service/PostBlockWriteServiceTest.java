package cherhy.soloProject.domain.postBlock.service;

import cherhy.soloProject.application.usecase.MemberPostBlockUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostBlockWriteServiceTest {

    @Autowired
    MemberPostBlockUseCase memberPostBlockUseCase;

    @Test
    public void test(){
//        ResponseEntity responseEntity = memberPostBlockUseCase.blockPost(1L, 1L);
//        Assertions.assertThat(responseEntity).isEqualTo(ResponseEntity.ok(200));
    }
}