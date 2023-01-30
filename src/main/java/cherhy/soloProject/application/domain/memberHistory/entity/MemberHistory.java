package cherhy.soloProject.application.domain.memberHistory.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MemberHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
