package com.example.subscriptionmanager.security;

import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {

    public Integer getCurrentUserId() {
        return 1;
    }
}
