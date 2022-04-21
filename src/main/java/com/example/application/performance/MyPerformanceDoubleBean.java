package com.example.application.performance;

import java.math.BigDecimal;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyPerformanceDoubleBean {

    @Transactional
    public BigDecimal getterTransactional2() {
        return BigDecimal.ZERO;
    }

    @Transactional
    public BigDecimal getterTransactionalDouble() {
        return getterTransactional2();
    }
}
