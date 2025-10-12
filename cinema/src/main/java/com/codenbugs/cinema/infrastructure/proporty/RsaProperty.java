package com.codenbugs.cinema.infrastructure.proporty;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPublicKey;
@ConfigurationProperties("security.rsa")
public record RsaProperty(
        RSAPublicKey publicKey) {
}
