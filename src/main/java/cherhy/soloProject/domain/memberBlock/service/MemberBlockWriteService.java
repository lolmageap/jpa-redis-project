package cherhy.soloProject.domain.memberBlock.service;

import cherhy.soloProject.domain.memberBlock.entity.MemberBlock;
import cherhy.soloProject.domain.memberBlock.repository.jpa.MemberBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberBlockWriteService {

    private final MemberBlockRepository memberBlockRepository;

    // TODO : 차단
    public void block(MemberBlock memberBlock) {
        memberBlockRepository.save(memberBlock);
    }

    // TODO : 차단 해제
    public void unblock(MemberBlock memberBlock) {
        memberBlockRepository.delete(memberBlock);
    }

}
