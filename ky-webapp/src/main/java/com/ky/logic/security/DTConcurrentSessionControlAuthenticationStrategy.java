package com.ky.logic.security;

import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yl on 2017/8/3.
 */
public class DTConcurrentSessionControlAuthenticationStrategy extends ConcurrentSessionControlAuthenticationStrategy {
    private Integer maxSessionCount = -1;
    private Integer maxSessionLiftTime = 1800;
    private AtomicInteger totalSessionNumber = new AtomicInteger(0);

    public DTConcurrentSessionControlAuthenticationStrategy(SessionRegistry sessionRegistry) {
        super(sessionRegistry);
    }
}
