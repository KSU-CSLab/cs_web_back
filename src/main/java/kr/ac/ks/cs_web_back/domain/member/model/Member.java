package kr.ac.ks.cs_web_back.domain.member.model;

import jakarta.persistence.*;
import kr.ac.ks.cs_web_back.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @Column
    private LocalDate birthdate;

    @Column
    private String number;

    @Builder
    private Member(String email,  String password, String username, LocalDate birthdate, String number) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.birthdate = birthdate;
        this.number = number;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }
}
