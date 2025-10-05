package com.shir.compsecurity.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtUtil {

    private static final Base64.Encoder URL_ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder URL_DECODER = Base64.getUrlDecoder();

    private final String secret;
    private final long expirationMs;
    private final String issuer;
    private final String audience;

    private final ObjectMapper mapper = new ObjectMapper();

    public JwtUtil(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms}") long expirationMs,
            @Value("${app.jwt.issuer:}") String issuer,
            @Value("${app.jwt.audience:}") String audience
    ) {
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalArgumentException("JWT secret must be at least 32 bytes");
        }
        this.secret = secret;
        this.expirationMs = expirationMs;
        this.issuer = issuer;
        this.audience = audience;
    }

    public String generateToken(String username) {
        try {
            Map<String, Object> header = Map.of("alg", "HS256", "typ", "JWT");
            long now = Instant.now().getEpochSecond();
            long exp = now + Math.max(1, expirationMs / 1000);

            Map<String, Object> payload = new HashMap<>();
            payload.put("sub", username);
            payload.put("iat", now);
            payload.put("exp", exp);
            if (!issuer.isBlank()) payload.put("iss", issuer);
            if (!audience.isBlank()) payload.put("aud", audience);

            String headerB64 = base64UrlEncode(mapper.writeValueAsBytes(header));
            String payloadB64 = base64UrlEncode(mapper.writeValueAsBytes(payload));

            String signingInput = headerB64 + "." + payloadB64;
            String signatureB64 = base64UrlEncode(hmacSha256(signingInput));
            return signingInput + "." + signatureB64;
        } catch (Exception e) {
            throw new RuntimeException("JWT generation error", e);
        }
    }

    public boolean isValid(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return false;

            String headerB64 = parts[0];
            String payloadB64 = parts[1];
            String sigB64 = parts[2];

            byte[] expectedSig = hmacSha256(headerB64 + "." + payloadB64);
            byte[] providedSig = URL_DECODER.decode(sigB64);
            if (!MessageDigest.isEqual(expectedSig, providedSig)) return false;

            Map<String, Object> payload = mapper.readValue(URL_DECODER.decode(payloadB64), Map.class);
            Object expObj = payload.get("exp");
            if (expObj == null) return false;
            long exp = ((Number) expObj).longValue();
            if (Instant.now().getEpochSecond() >= exp) return false;

            if (!issuer.isBlank()) {
                String iss = (String) payload.get("iss");
                if (iss == null || !issuer.equals(iss)) return false;
            }
            if (!audience.isBlank()) {
                String aud = (String) payload.get("aud");
                if (aud == null || !audience.equals(aud)) return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) return null;
            Map<String, Object> payload = mapper.readValue(URL_DECODER.decode(parts[1]), Map.class);
            return (String) payload.get("sub");
        } catch (Exception e) {
            return null;
        }
    }

    private static String base64UrlEncode(byte[] bytes) { return URL_ENCODER.encodeToString(bytes); }

    private byte[] hmacSha256(String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }
}
