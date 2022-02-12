package com.hello.webfluxstart.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Cart {
    @Id
    private String id;
    private List<CartItem> cartItems;

    public Cart(String id) {
        this.id = id;
        this.cartItems = new ArrayList<>();
    }

    public static String cartName(Authentication authentication) {
        return Optional.ofNullable(authentication).map(Principal::getName).orElse("Visitor") + "'s Cart";
    }

    public static String cartName(OAuth2User oAuth2User) {
        return Optional.ofNullable(oAuth2User).map(AuthenticatedPrincipal::getName).orElse("Visitor") + "'s Cart";
    }
}
