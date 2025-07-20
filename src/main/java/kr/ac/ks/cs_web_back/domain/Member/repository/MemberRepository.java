package kr.ac.ks.cs_web_back.domain.Member.repository;

import kr.ac.ks.cs_web_back.domain.Member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
