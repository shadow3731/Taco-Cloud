package com.example.taco_cloud_authorization_server.app.loader;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.stereotype.Component;

import com.example.taco_cloud_authorization_server.app.model.Client;
import com.example.taco_cloud_authorization_server.app.repository.ClientRepository;

import lombok.RequiredArgsConstructor;

@Component
@Order(120)
@RequiredArgsConstructor
public class ClientsLoader implements CommandLineRunner {
    private final ClientRepository clientRepo;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        List<Client> clients = this.getInitClients();
        for (Client client : clients) {
            if (!this.clientRepo.existsByClientId(client.getClientId())) {
                this.clientRepo.save(client);
            }
        }
    }

    private List<Client> getInitClients() {
        Client client = new Client();
        client.setClientId("client1");
        client.setName("client1-name");
        client.setSecret(encoder.encode("secret"));
        
        Set<String> scopes = new HashSet<>(Set.of(
            OidcScopes.OPENID,
            "writeIngredients",
            "deleteIngredients"
        ));
        client.setScopes(scopes);

        Client client2 = new Client();
        client2.setClientId("client2");
        client2.setName("client2-name");
        client2.setSecret(encoder.encode("secret"));
        
        Set<String> scopes2 = new HashSet<>(Set.of(
            "readOrders",
            "writeOrders",
            "deleteOrders",
            "readTacos",
            "writeTacos"
        ));
        scopes2.addAll(scopes);
        client2.setScopes(scopes2);

        return List.of(client, client2);
    }
}
