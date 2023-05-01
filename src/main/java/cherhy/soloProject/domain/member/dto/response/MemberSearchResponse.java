package cherhy.soloProject.domain.member.dto.response;

import cherhy.soloProject.domain.member.entity.Member;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public record MemberSearchResponse(Long memberId, String name) implements Serializable {

    public static List<MemberSearchResponse> from(List<Member> findMemberList) {
        return findMemberList.stream()
                .map(m -> new MemberSearchResponse(m.getId(), m.getName()))
                .collect(Collectors.toList());
    }

}
