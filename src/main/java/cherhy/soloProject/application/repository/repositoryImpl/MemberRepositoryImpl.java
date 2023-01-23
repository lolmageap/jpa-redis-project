package cherhy.soloProject.application.repository.repositoryImpl;

import cherhy.soloProject.application.domain.dto.MemberDto;
import cherhy.soloProject.application.domain.entity.Member;
import cherhy.soloProject.application.repository.querydsl.MemberRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Transactional
@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Override
    public Member createNewMember(MemberDto dto) {

        return null;
    }

    @Override
    public Member findByMemberName(Long id) {
        return null;
    }
}
