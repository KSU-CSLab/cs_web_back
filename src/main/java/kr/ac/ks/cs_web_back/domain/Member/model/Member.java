package kr.ac.ks.cs_web_back.domain.Member.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
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
    private String birthdate;

    @Column(nullable = false)
    private String number;

    private Member(String email,  String password, String username, String birthdate, String number) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.birthdate = birthdate;
        this.number = number;
    }

    public static Member of(String email, String password, String username, String birthdate, String number) {
        return new Member(email, password, username, birthdate, number);
    }
}
