package com.example.application.knowledge;

import org.hibernate.engine.internal.StatisticalLoggingSessionEventListener;

public class CustomStatisticalLoggingSessionEventListener extends StatisticalLoggingSessionEventListener {

    @Override
    public void jdbcConnectionAcquisitionEnd() {
        super.jdbcConnectionAcquisitionEnd();
    }
}
