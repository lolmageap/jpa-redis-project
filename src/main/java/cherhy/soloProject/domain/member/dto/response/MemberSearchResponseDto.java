package cherhy.soloProject.domain.member.dto.response;

import cherhy.soloProject.domain.member.entity.Member;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public record MemberSearchResponseDto(Long memberId, String name) implements Serializable {

    public static List<MemberSearchResponseDto> from(List<Member> findMemberList) {
        return findMemberList.stream()
                .map(m -> new MemberSearchResponseDto(m.getId(), m.getName()))
                .collect(Collectors.toList());
    }

}
