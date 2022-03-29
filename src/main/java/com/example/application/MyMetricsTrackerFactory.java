package com.example.application;

import com.zaxxer.hikari.metrics.IMetricsTracker;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import com.zaxxer.hikari.metrics.PoolStats;

public class MyMetricsTrackerFactory implements MetricsTrackerFactory {

    private PoolStats poolStats;

    @Override
    public IMetricsTracker create(String poolName, PoolStats poolStats) {
        this.poolStats = poolStats;
        return new MyMetricsTracker();
    }

    public PoolStats getPoolStats() {
        return poolStats;
    }
    
}
