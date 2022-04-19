package com.example.application.performance;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class MyPerformanceSessionBean {
    public BigDecimal getter() {
        return BigDecimal.ZERO;
    }
}
