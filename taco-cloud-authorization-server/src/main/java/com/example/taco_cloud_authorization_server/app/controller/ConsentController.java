/* package com.example.taco_cloud_authorization_server.app.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.taco_cloud_authorization_server.app.common.Urls;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("oauth2/consent")
@RequiredArgsConstructor
public class ConsentController {
    private final RegisteredClientRepository registeredClientRepo;
    
    @GetMapping
    public String consent(HttpServletRequest request, Model model) {        
        String clientId = request.getParameter("client_id");
        String state = request.getParameter("state");
        String redirectUri = request.getParameter("redirect_uri");
        
        String scope = request.getParameter("scope");
        Set<String> scopes = new HashSet<>();
        if (scope != null && !scope.isEmpty()) {
            scopes = new HashSet<>(Arrays.asList(scope.split(" ")));
        } else {
            RegisteredClient registeredClient = this.registeredClientRepo.findByClientId(clientId);
            if (registeredClient != null) {
                scopes = registeredClient.getScopes();
            }
        }

        model.addAttribute("clientId", clientId);
        model.addAttribute("state", state);
        model.addAttribute("redirectUri", redirectUri);
        model.addAttribute("scopes", scopes);

        return Urls.CONSENT.get();
    }
}
 */