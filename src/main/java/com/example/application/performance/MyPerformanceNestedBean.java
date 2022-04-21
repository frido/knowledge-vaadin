package com.example.application.performance;

import java.math.BigDecimal;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class MyPerformanceNestedBean {
    
    @Transactional
    public BigDecimal getterTransactional() {
        return BigDecimal.ZERO;
    }
}
