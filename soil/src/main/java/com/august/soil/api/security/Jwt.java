package com.august.soil.api.security;

import com.august.soil.api.model.user.Email;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**
 * JWT 인증에 필요한 정보를 담은 클래스
 */
@Getter
public class Jwt {
  
  private final String issuer;
  
  private final String clientSecret;
  
  private final int expirySeconds;

  private final Algorithm algorithm;

  private final JWTVerifier jwtVerifier;

  public Jwt(String issuer, String clientSecret, int expirySeconds) {
    this.issuer = issuer;
    this.clientSecret = clientSecret;
    this.expirySeconds = expirySeconds;
    this.algorithm = Algorithm.HMAC512(clientSecret);
    this.jwtVerifier = JWT.require(algorithm)
      .withIssuer(issuer)
      .build();
  }
  
  public String newToken(Claims claims) {
    Date now = new Date();
    JWTCreator.Builder builder = JWT.create();
    builder.withIssuer(issuer);
    builder.withIssuedAt(now);
    if (expirySeconds > 0) {
      builder.withExpiresAt(new Date(now.getTime() + expirySeconds * 1_000L));
    }
    builder.withClaim("userKey", claims.userKey);
    builder.withClaim("name", claims.name);
    builder.withClaim("email", claims.email.getEmail());
    builder.withArrayClaim("roles", claims.roles);
    return "Bearer " + builder.sign(algorithm);
  }
  
  public String refreshToken(String token) throws JWTVerificationException {
    Claims claims = verify(token);
    claims.eraseIat();
    claims.eraseExp();
    return newToken(claims);
  }
  
  public Claims verify(String token) throws JWTVerificationException {
    return new Claims(jwtVerifier.verify(token));
  }
  
  @NoArgsConstructor
  static public class Claims {
    Long userKey;
    String name;
    Email email;
    String[] roles;
    Date iat;
    Date exp;
  
    Claims(DecodedJWT decodedJWT) {
      Claim userKey = decodedJWT.getClaim("userKey");
      if (!userKey.isNull())
        this.userKey = userKey.asLong();
      Claim name = decodedJWT.getClaim("name");
      if (!name.isNull())
        this.name = name.asString();
      Claim email = decodedJWT.getClaim("email");
      if (!email.isNull())
        this.email = new Email(email.asString());
      Claim roles = decodedJWT.getClaim("roles");
      if (!roles.isNull())
        this.roles = roles.asArray(String.class);
      this.iat = decodedJWT.getIssuedAt();
      this.exp = decodedJWT.getExpiresAt();
    }
  
    public static Claims of(long userKey, String name, Email email, String[] roles) {
      Claims claims = new Claims();
      claims.userKey = userKey;
      claims.name = name;
      claims.email = email;
      claims.roles = roles;
      return claims;
    }
    
    long iat() {
      return iat != null ? iat.getTime() : -1;
    }
    
    long exp() {
      return exp != null ? exp.getTime() : -1;
    }
    
    void eraseIat() {
      iat = null;
    }
    
    void eraseExp() {
      exp = null;
    }
  
    @Override
    public String toString() {
      return reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
  }
}
