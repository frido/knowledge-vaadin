package com.example.application.features;

import org.togglz.core.Feature;
import org.togglz.core.context.FeatureContext;

public enum MyFeatures implements Feature{
    FEATURE_ONE,
    FEATURE_TWO;

    public boolean isActive() {
        return FeatureContext.getFeatureManager().isActive(this);
    }
}
