package com.example.application.spi;

public class SomePlugableService implements PlugableService {

    @Override
    public String getName() {
        return "Some dummy service";
    }
    
}
