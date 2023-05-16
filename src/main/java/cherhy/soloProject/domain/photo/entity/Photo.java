package cherhy.soloProject.domain.photo.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Photo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Long id;
    private String photo;

    public static Photo of(String photo){
        return new Photo(photo);
    }

    public Photo(String photo) {
        this.photo = photo;
    }

}
