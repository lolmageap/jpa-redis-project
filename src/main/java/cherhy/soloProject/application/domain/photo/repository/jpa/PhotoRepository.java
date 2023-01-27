package cherhy.soloProject.application.domain.photo.repository.jpa;

import cherhy.soloProject.application.domain.photo.entity.Photo;
import cherhy.soloProject.application.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByPostId(Long id);
    void deleteByPostId(Long id);
}
