package com.example.application.knowledge;

import java.util.Arrays;
import java.util.List;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.jpa.boot.spi.IntegratorProvider;

public class CustomIntegratorProvider implements IntegratorProvider  {

    @Override
    public List<Integrator> getIntegrators() {
        return Arrays.asList(new CustomEventListenerIntegrator());
    }
}
