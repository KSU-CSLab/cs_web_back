package kr.ac.ks.cs_web_back.domain.member.model;

import jakarta.persistence.*;
import kr.ac.ks.cs_web_back.domain.common.BaseEntity;
import kr.ac.ks.cs_web_back.domain.member.dto.request.MemberCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(nullable = false)
    private String number;

    private Member(String email,  String password, String username, LocalDate birthdate, String number) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.birthdate = birthdate;
        this.number = number;
    }

    public static Member of(MemberCreateRequest request) {
        return new Member(
                request.email(),
                request.password(),
                request.username(),
                request.birthdate(),
                request.number()
        );
    }
}
