package com.java.cloudgateway.filter;

import com.java.cloudgateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter(){
        super(Config.class);
    }
    @Override
    public GatewayFilter apply(AuthenticationFilter.Config config) {
        return ((exchange,chain) -> {

            ServerHttpRequest serverHttpRequest = null;
            if(routeValidator.isSecured.test(exchange.getRequest())) {
                if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                    throw new RuntimeException("Header is missing");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if(authHeader != null && authHeader.startsWith("Bearer")){
                    authHeader = authHeader.substring(7);
                }

                try{
                    jwtUtil.validateToken(authHeader);

                    serverHttpRequest = exchange.getRequest().mutate().header("loggedInUser", jwtUtil.extractUserName(authHeader)).build();
                }catch (Exception e){
                    throw new RuntimeException("Invalid token");
                }
            }

            return chain.filter(exchange.mutate().request(serverHttpRequest).build());
        });
    }

    public static class Config{

    }
}
