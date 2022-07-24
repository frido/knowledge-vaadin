package com.example.application;

import java.util.List;
import java.util.ServiceLoader;

import org.apache.commons.collections4.IteratorUtils;
import org.junit.jupiter.api.Test;

import com.example.application.spi.PlugableService;
import com.example.application.spi.SomePlugableService;
import com.google.common.truth.Truth;

/**
 * Use ServiceProvider to load Features
 */
public class ServiceProviderTest {
    
    @Test
    public void load() {
        ServiceLoader<PlugableService> loader = ServiceLoader.load(PlugableService.class);
        List<PlugableService> list = IteratorUtils.toList(loader.iterator());
        Truth.assertThat(list).isNotEmpty();
        Truth.assertThat(list.get(0)).isInstanceOf(SomePlugableService.class);
    }
}
