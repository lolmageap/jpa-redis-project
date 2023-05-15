package cherhy.soloProject.domain.postBlock.service;

import cherhy.soloProject.domain.postBlock.entity.PostBlock;
import cherhy.soloProject.domain.postBlock.repository.jpa.PostBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostBlockWriteService {
    private final PostBlockRepository postBlockRepository;

    // TODO : 차단
    public void block(PostBlock postBlock) {
        postBlockRepository.save(postBlock);
    }

    // TODO : 차단 해제
    public void unblock(PostBlock postBlock) {
        postBlockRepository.delete(postBlock);
    }
}
