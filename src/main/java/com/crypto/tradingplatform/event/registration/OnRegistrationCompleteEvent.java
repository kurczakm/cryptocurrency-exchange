package com.crypto.tradingplatform.event.registration;

import com.crypto.tradingplatform.domain.User;
import org.springframework.context.ApplicationEvent;

public final class OnRegistrationCompleteEvent extends ApplicationEvent {
    private final String appUrl;
    private final User user;

    public OnRegistrationCompleteEvent(String appUrl, User user) {
        super(user);
        this.appUrl = appUrl;
        this.user = user;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public User getUser() {
        return user;
    }
}
