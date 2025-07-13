package com.example.taco_cloud.app.service.taco;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;

import com.example.taco_cloud.app.common.Urls;
import com.example.taco_cloud.app.model.taco.TacoOrder;
import com.example.taco_cloud.app.service.taco.impl.RestPageImpl;

@Service
@RequestScope
public class OrderService {
    private final RestTemplate restTemplate;

    public OrderService(OAuth2AuthorizedClientService clientService) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String tokenValue;

        if (authentication instanceof OAuth2AuthenticationToken token) {
            OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(token.getAuthorizedClientRegistrationId(), token.getName());
            tokenValue = client.getAccessToken().getTokenValue();
        } else {
            tokenValue = null;
        }

        this.restTemplate = new RestTemplate();
        if (tokenValue != null) {
            this.restTemplate.getInterceptors().add((request, bytes, execution) -> {
                request.getHeaders().add("Authorization", "Bearer " + tokenValue);
                return execution.execute(request, bytes);
            });
        }
    }

    public Page<TacoOrder> getOrders(int page, int size) {
        String url = Urls.RES_SERV_HOST.get() + "/" + Urls.ORDERS.get() + "?page=" + page + "&size=" + size;
        ResponseEntity<RestPageImpl<TacoOrder>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<RestPageImpl<TacoOrder>>() {}
        );
        return response.getBody();
    }

    public TacoOrder postOrder(TacoOrder order) {
        return restTemplate.postForObject(Urls.RES_SERV_HOST.get() + "/" + Urls.ORDERS.get(), order, TacoOrder.class);
    }
}