package cherhy.soloProject.application.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class MemberHistory {
    @Id @GeneratedValue
    private Long id;
}
