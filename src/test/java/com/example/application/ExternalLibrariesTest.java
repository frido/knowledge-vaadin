package com.example.application;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.Test;

// TODO: check libraries v projekte
public class ExternalLibrariesTest {
    
    /**
     * org.apache.commons
     */
    @Test
    public void apache() {
        // StringUtils
        // BooleanUtils
        // CollectionUtils
        assertTrue(true);
    }

    /**
     * com.google.common
     * 
     * Apache provides more Utils classes with static methods to operate with jdk data structures.
     * Goole provides data new structures with additional features.
     */
    @Test
    public void google() {
        
        assertTrue(true);
    }

    /**
     * ehcache
     */
    @Test
    public void ehcache() {
        // TODO: test cache
        fail();
    }

    /**
     * org.joda.time.DateTime
     */
    @Test
    public void joda() {
        // TODO: compare joda (DateTime) vs jdk (LocalDateTime/ZonedDateTime)
        fail();
    }

    /**
     * org.mapstruct.Mapper
     */
    @Test
    public void mapstruct() {
        // TODO: use mapstruct as mapper
        fail();
    }
    
}
