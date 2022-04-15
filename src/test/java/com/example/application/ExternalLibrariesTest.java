package com.example.application;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.Test;

/**
 * External libraies in the project.
 */
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
        // TODO: com.google.common.cache.Cache;
        fail();
    }

    /**
     * org.joda.time.DateTime
     * 
     * Note that Joda-Time is considered to be a largely “finished” project. 
     * No major enhancements are planned. 
     * If using Java SE 8, please migrate to java.time (JSR-310).
     * 
     * https://code-examples.net/en/q/1c5f3cd
     */
    @Test
    public void joda() {
        assertTrue(true);
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
