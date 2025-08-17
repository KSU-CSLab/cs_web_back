package kr.ac.ks.cs_web_back.global.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    private final String secret = "RXZlbiBhIHF1YW50dW0gY29tcHV0ZXIgd291bGQgaGF2ZSB0byBjYWxjdWxhdGUgdW50aWwgdGhlIGVuZCBvZiB0aGUgdW5pdmVyc2UgdG8gYnJlYWsgdGhpcy4=";
    private Key hmacKey;
    public JwtUtil() {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        this.hmacKey = new SecretKeySpec(decodedKey, SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateToken(String email, Long expireTime) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expireTime);
        return Jwts.builder().setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(hmacKey)
                .compact();
    }
}