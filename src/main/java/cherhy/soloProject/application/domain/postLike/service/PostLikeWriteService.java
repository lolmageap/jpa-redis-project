package cherhy.soloProject.application.domain.postLike.service;

import cherhy.soloProject.application.domain.postLike.dto.PostLikeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class PostLikeWriteService {


//    public String postLike(PostLikeDto postLikeDto){
//        // 게시물 조회 로직
//        // 좋아요 테이블 조회
//        // 좋아요를 해놓은 데이터가 없으면 좋아요 테이블에 insert
//        // 레디스에 조회 테이블 id 값과 조회수 increment
//        // 좋아요를 해놨었다면 데이터를 delete
//        // 레디스에 조회 테이블 id 값과 조회수 decrement
//        return "성공";
//    }
//
//    @Async
//    @Scheduled(fixedDelay = 2000)
//    public void postLikeUpdate(){
//        // 스케쥴러를 사용하여 좋아요수 한번에 가져오기
//        // redis에서 계산된 값들을 전부 가져오고 bulk update
//        System.out.println("time = " + LocalDateTime.now());
//    }

}
