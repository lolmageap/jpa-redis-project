package cherhy.soloProject.application.repository.jpa;

import cherhy.soloProject.application.domain.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByPostId(Long id);
}
