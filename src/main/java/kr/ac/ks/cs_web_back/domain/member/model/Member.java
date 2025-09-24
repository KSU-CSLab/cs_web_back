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
<<<<<<< HEAD
import java.util.Collection;
import java.util.Collections;
=======
import java.time.LocalDateTime;
>>>>>>> 65597fa (컨트롤러 & dto & 서비스 구현 feat/#9)

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

    @Column(name = "is_withdrawn", nullable = false)
    private boolean isWithdrawn;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    @Builder
    private Member(String email,  String password, String username, LocalDate birthdate, String number) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.birthdate = birthdate;
        this.number = number;
    }

<<<<<<< HEAD
    public String getNickname() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        return email;
=======
    public void withdrawNow(){
        this.isWithdrawn = true;
        this.deletedAt = LocalDateTime.now();
>>>>>>> 65597fa (컨트롤러 & dto & 서비스 구현 feat/#9)
    }
}
