package com.abbytech.shoppingapp;


import com.abbytech.shoppingapp.util.PropertiesLoader;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Properties;

public class PropertiesLoaderTest {

    @Test
    public void testLoadNetProperties() throws Exception {
        Properties properties = PropertiesLoader.loadProperties("net");
        Assert.assertNotNull(properties);
    }
}
