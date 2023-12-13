package edward.iv.restapi.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value(value = "${app.jwtSecret}")
    private String jwtSecret;

    @Value(value = "${app.jwtExpirationInMs}")
    private long jwtExpirationInMs;

    public String generateToken(Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        /*
         * 1. Token 구조
         * Header { "typ": "JWT", "alg": "HS512" }
         * Payload {
         *     Registered Claims: iss(발급자), sub(제목), aud(대상자)...
         *     Public Claim
         *     Private Claim: username: admin
         * }
         * Signature: Encode(Header.Payload.SecretKey)
         *
         * 2. Claim: Payload 정보의 한 조각
         */
        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public Long getUserIdFromJWT(String token) {

        String result = extractClaim(token, Claims::getSubject);
        return Long.valueOf(result);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty");
        }
        return false;
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {

        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolvers.apply(claims);
    }

    private Key getSigningKey() {

        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
