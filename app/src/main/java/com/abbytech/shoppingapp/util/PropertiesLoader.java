package com.abbytech.shoppingapp.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
    public static Properties loadProperties(String name) throws IOException{
        Properties properties = new Properties();
        InputStream inputStream;
        String fileName = name + ".properties";
        inputStream = PropertiesLoader.class.getClassLoader().getResourceAsStream(fileName);
        properties.load(inputStream);
        return properties;
    }
}
