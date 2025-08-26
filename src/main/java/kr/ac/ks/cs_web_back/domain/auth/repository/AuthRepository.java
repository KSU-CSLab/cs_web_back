package kr.ac.ks.cs_web_back.domain.auth.repository;

import kr.ac.ks.cs_web_back.domain.auth.model.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {
}
