package com.example.taco_cloud_authorization_server.app.configuration.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import com.example.taco_cloud_authorization_server.app.common.Urls;
import com.example.taco_cloud_authorization_server.app.model.Client;
import com.example.taco_cloud_authorization_server.app.repository.ClientRepository;
import com.example.taco_cloud_authorization_server.app.repository.UserRepository;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
@EnableWebSecurity
public class AuthorizationServerConfiguration {
    @Bean
    @Order(1)
    SecurityFilterChain authServerFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer configurer = OAuth2AuthorizationServerConfigurer.authorizationServer();
        /* configurer.authorizationEndpoint(auth -> auth
            .consentPage(null)
        ); */

        return http
            .securityMatcher(configurer.getEndpointsMatcher())
            /* .with(configurer, authServer -> authServer
                .oidc(Customizer.withDefaults())    // Enbale OpenID Connect 1.0
            ) */
            .with(configurer, Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
                //.requestMatchers("/" + Urls.LOGIN.get(), "/" + Urls.REGISTER.get(), "/css/**", "/jss/**", "/images/**").permitAll()    
                .anyRequest().authenticated()
            )
            .formLogin(Customizer.withDefaults())
            .exceptionHandling(e -> e // Redirect if failed login
                .defaultAuthenticationEntryPointFor(
                    new LoginUrlAuthenticationEntryPoint("/" + Urls.LOGIN.get()), 
                    new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                )
            )
            .build();
    }

    @Bean
    RegisteredClientRepository registeredClientRepository(ClientRepository clientRepo) {
        return new RegisteredClientRepository() {
            @Override
            @Nullable
            public RegisteredClient findByClientId(String clientId) {
                Optional<Client> client = clientRepo.findByClientId(clientId);
                if (!client.isPresent()) return null;

                String redirectUri = Urls.CLIENT_HOST.get() + "/" + Urls.LOGIN.get() + "/" + Urls.OAUTH2.get() + "/" + Urls.CODE.get() + "/" + client.get().getClientId();

                return RegisteredClient
                    .withId(client.get().getUuid())
                    .clientId(client.get().getClientId())
                    .clientName(client.get().getName())
                    .clientSecret(client.get().getSecret())
                    .scopes(scopes -> scopes.addAll(client.get().getScopes()))
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    .redirectUri(redirectUri)
                    .postLogoutRedirectUri(Urls.CLIENT_HOST.get())
                    .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(15))
                        .refreshTokenTimeToLive(Duration.ofMinutes(45))
                        .reuseRefreshTokens(true)
                        .build()
                    )
                    .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
                    .build();
            }

            @Override
            @Nullable
            public RegisteredClient findById(String id) {
                return null;
            }

            @Override
            public void save(RegisteredClient registeredClient) {}
        };
    }

    @Bean
    JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID(UUID.randomUUID().toString())
            .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            keyPair = generator.generateKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return keyPair;
    }

    @Bean
    JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    /* @Bean
    UserDetailsService userDetailsService(UserRepository userRepo) {
        return username -> userRepo.findByUsername(username).get();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    } */
}
