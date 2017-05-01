package com.abbytech.shoppingapp;


import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.abbytech.shoppingapp.map.LocationProvider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LocationProviderTest {

    private LocationProvider provider;

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        provider = new LocationProvider(context);
    }

    @Test
    public void detectsRegion() throws Exception {
        Thread.sleep(5000);
        int count = provider.getCurrentRegions().size();
        Assert.assertTrue(count>0);
    }
}
