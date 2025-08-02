package kr.ac.ks.cs_web_back.domain.member.repository;

import kr.ac.ks.cs_web_back.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}