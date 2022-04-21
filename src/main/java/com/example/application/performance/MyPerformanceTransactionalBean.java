package com.example.application.performance;

import java.math.BigDecimal;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyPerformanceTransactionalBean {

    @Transactional
    public BigDecimal getterTransactional() {
        return BigDecimal.ZERO;
    }

}
