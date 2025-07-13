package com.example.taco_cloud.app.service.taco;

import java.util.Arrays;

import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.taco_cloud.app.common.App;
import com.example.taco_cloud.app.common.Urls;
import com.example.taco_cloud.app.model.taco.Ingredient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestIngredientService implements IngredientService {
    private final OAuth2AuthorizedClientManager authorizedClientManager;
    private final String RESOURCE_SERVER_URL = Urls.RES_SERV_HOST + "/" + Urls.API + "/" + Urls.INGREDIENTS;

    public Iterable<Ingredient> findAll() {
        RestTemplate restTemplate = this.buildRestTemplateWithAccessToken();
        Ingredient[] ingredients = restTemplate.getForObject(this.RESOURCE_SERVER_URL, Ingredient[].class);
        return Arrays.asList(ingredients);
    }

    public Ingredient addIngredient(Ingredient ingredient) {
        RestTemplate restTemplate = this.buildRestTemplateWithAccessToken();
        return restTemplate.postForObject(this.RESOURCE_SERVER_URL, ingredient, Ingredient.class);
    }

    private RestTemplate buildRestTemplateWithAccessToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest
            .withClientRegistrationId(App.AUTH_CLIENT.get())
            .principal(authentication)
            .build();
        OAuth2AuthorizedClient client = this.authorizedClientManager.authorize(request);

        RestTemplate restTemplate = new RestTemplate();
        if (client != null) {
            restTemplate.getInterceptors().add(getBearerTokenInterceptor(client.getAccessToken().getTokenValue()));
        }
        return restTemplate;
    }

    private ClientHttpRequestInterceptor getBearerTokenInterceptor(String accessToken) {
        return (request, bytes, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + accessToken);
            return execution.execute(request, bytes);
        };
    }
}
