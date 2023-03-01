package cherhy.soloProject.domain.photo.repository.jpa;

import cherhy.soloProject.domain.photo.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    void deleteByPostId(Long id);
}
