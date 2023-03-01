package cherhy.soloProject.domain.postLike.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostLikeWriteServiceTest {


    // 스케쥴러를 사용하여 좋아요수 한번에 가져오기
    @Test
    @Async
    @Scheduled(fixedDelay = 1000)
    public void postLikeUpdate() throws InterruptedException {
        System.out.println("time = " + LocalDateTime.now());
    }

}