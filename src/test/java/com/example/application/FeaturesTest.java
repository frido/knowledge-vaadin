package com.example.application;

import org.junit.jupiter.api.Test;

import com.example.application.features.MyFeatures;
import com.google.common.truth.Truth;

/**
 * https://www.togglz.org/
 * 
 * Configuration implemented as SPI in {@code SingletonFeatureManagerProvider}
 * 
 * TODO: Spring integration - actuator
 */
public class FeaturesTest {
    
    @Test
    public void feature() {
        Truth.assertThat(MyFeatures.FEATURE_ONE.isActive()).isTrue();
        Truth.assertThat(MyFeatures.FEATURE_TWO.isActive()).isFalse();
    }
}
