package com.example.unimeeting.service;

import com.example.unimeeting.config.jwt.JwtProperties;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {
    @Override
    public String getToken(String key, Object value) {
        Date d = new Date();
        log.info(d.toString() +" : " + d.getTime());
        d.setTime(d.getTime()+(60*10*1000));  // 테스트를 위해서 10분간만 유효한 토큰을 만듬
        log.info(d.toString() +" : " + d.getTime());


        byte[] secretByteKey = DatatypeConverter.parseBase64Binary(JwtProperties.SECRET);
        Key signKey = new SecretKeySpec(secretByteKey, SignatureAlgorithm.HS512.getJcaName());

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("typ", "JWT");
        headerMap.put("alg", "HS512");

        Map<String, Object> map = new HashMap<>();
        map.put(key, value);

        JwtBuilder builder = Jwts.builder().setHeader(headerMap)
                .setClaims(map)
                .setExpiration(d)
                .signWith(signKey, SignatureAlgorithm.HS512);
        return "Bearer "+builder.compact();
    }

    @Override
    public Claims getClaims(String token) {
        log.info("getClaims() 호출 : "+token);
        if (token != null && !"".equals(token)) {
            token = token.replace("Bearer ", "");
            try {
                byte[] secretByteKey = JwtProperties.SECRET.getBytes();
                Key signKey = new SecretKeySpec(secretByteKey, SignatureAlgorithm.HS512.getJcaName());
                return Jwts
                        .parserBuilder()
                        .setSigningKey(signKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
            } catch (ExpiredJwtException e) {
                log.error("토큰 만료");
            } catch (JwtException e) {
                e.printStackTrace();
                log.error("토큰 유효하지 않음");
            }
        }
        return null;
    }

    @Override
    public boolean isValid(String token) {
        return this.getClaims(token) != null;
    }

    @Override
    public int getId(String token) {
        Claims claims = this.getClaims(token);
        if(claims != null){
            return Integer.parseInt(claims.get("idx").toString());
        }
        return 0;
    }

    public String getNickname(String token) {
        Claims claims = this.getClaims(token);
        if(claims != null){
            return claims.get("nickname").toString();
        }
        return "";
    }
}