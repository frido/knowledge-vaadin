package com.example.application.features;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import org.togglz.core.manager.FeatureManager;
import org.togglz.core.manager.FeatureManagerBuilder;
import org.togglz.core.repository.StateRepository;
import org.togglz.core.repository.file.FileBasedStateRepository;
import org.togglz.core.spi.FeatureManagerProvider;
import org.togglz.core.user.NoOpUserProvider;

public class SingletonFeatureManagerProvider implements FeatureManagerProvider {

    private static FeatureManager featureManager;

    @Override
    public int priority() {
        return 30;
    }

    @Override
    public synchronized FeatureManager getFeatureManager() {

        if (featureManager == null) {
            featureManager = new FeatureManagerBuilder()
                    .featureEnum(MyFeatures.class)
                    .stateRepository(getStateRepository())
                    .userProvider(new NoOpUserProvider())
                    .build();
        }

        return featureManager;

    }

    private StateRepository getStateRepository() {
        try {
            URL resource = getClass().getClassLoader().getResource("features.properties");
            File file = Paths.get(resource.toURI()).toFile();
            return new FileBasedStateRepository(file);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

}
