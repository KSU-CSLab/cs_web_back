package kr.ac.ks.cs_web_back.domain.post.repository;

import kr.ac.ks.cs_web_back.domain.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}