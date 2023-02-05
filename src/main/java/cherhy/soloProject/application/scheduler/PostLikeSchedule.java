package cherhy.soloProject.application.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class PostLikeSchedule {

    // 스케쥴러를 사용하여 좋아요수 한번에 가져오기
    @Async
    @Scheduled(fixedDelay = 2000)
    public void postLikeUpdate(){
        System.out.println("time = " + LocalDateTime.now());
    }
}
