package com.tweetapp.cloudgateway.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Configuration
public class OptionsCorsFilter {
    @Bean
    public WebFilter corsFilter() {
        return (exchange, chain) -> {
            if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
                ServerHttpResponse response = exchange.getResponse();
                HttpHeaders headers = response.getHeaders();
                headers.setAccessControlAllowOrigin("*");
                headers.setAccessControlAllowHeaders(Arrays.asList("*"));
                headers.setAccessControlAllowMethods(Arrays.asList(HttpMethod.POST, HttpMethod.GET));
                headers.setAccessControlAllowCredentials(true);
                return chain.filter(exchange);
            }
            return chain.filter(exchange);
        };
    }
}
