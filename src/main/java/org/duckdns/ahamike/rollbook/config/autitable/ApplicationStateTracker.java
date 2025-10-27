package org.duckdns.ahamike.rollbook.config.autitable;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStateTracker implements ApplicationListener<ApplicationReadyEvent> {

    private static volatile boolean booted = false;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        booted = true;
    }

    public static boolean isBooted() {
        return booted;
    }
}
