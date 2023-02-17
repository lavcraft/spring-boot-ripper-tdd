package com.governance.embassy.service;

import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BackgroundDataLoader {
    @PostConstruct//(ContextRefreshedEvent.class)
    public void loadData(ApplicationContext applicationContext) {
        if (!getData()) {
            throw new RuntimeException("Cannot receiver data from any environment");
        }
    }

    private boolean getData() {
        return false;
    }
}
