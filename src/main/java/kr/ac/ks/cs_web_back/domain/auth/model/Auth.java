package kr.ac.ks.cs_web_back.domain.auth.model;

import jakarta.persistence.*;
import lombok.Generated;

@Entity
public class Auth {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private long id;
    @Column(
            unique = true,
            nullable = false
    )
    private String username;
    @Column(
            nullable = false
    )
    private String password;

    public Auth(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static Auth of(String username, String password) {
        return new Auth(username, password);
    }

    @Generated
    public long getId() {
        return this.id;
    }

    @Generated
    public String getUsername() {
        return this.username;
    }

    @Generated
    public String getPassword() {
        return this.password;
    }

    @Generated
    public Auth() {
    }
}
