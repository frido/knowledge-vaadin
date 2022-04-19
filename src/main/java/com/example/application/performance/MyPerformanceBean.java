package com.example.application.performance;

import java.math.BigDecimal;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class MyPerformanceBean {

    @Transactional
    public BigDecimal getterTransactional() {
        return BigDecimal.ZERO;
    }

    public BigDecimal getter() {
        return BigDecimal.ZERO;
    }
}
