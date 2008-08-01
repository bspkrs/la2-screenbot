package ru.snslabs.la2;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static int targetWindowTop;
    private static int targetWindowLeft;
    private static int targetWindowHeight;
    private static int targetWindowWidth;

    private static int personWindowTop;
    private static int personWindowLeft;
    private static int personWindowHeight;
    private static int personWindowWidth;

    private static int fishingStatusWindowTop;
    private static int fishingStatusWindowLeft;
    private static int fishingStatusWindowHeight;
    private static int fishingStatusWindowWidth;

    private static int fishHealthWindowTop;
    private static int fishHealthWindowLeft;
    private static int fishHealthWindowHeight;
    private static int fishHealthWindowWidth;
    
    
    static{
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("config.properties"));
            
            targetWindowTop = Integer.valueOf(props.getProperty("targetWindowTop"));
            targetWindowLeft = Integer.valueOf(props.getProperty("targetWindowLeft"));
            targetWindowHeight = Integer.valueOf(props.getProperty("targetWindowHeight"));
            targetWindowWidth = Integer.valueOf(props.getProperty("targetWindowWidth"));

            personWindowTop = Integer.valueOf(props.getProperty("personWindowTop"));
            personWindowLeft = Integer.valueOf(props.getProperty("personWindowLeft"));
            personWindowHeight = Integer.valueOf(props.getProperty("personWindowHeight"));
            personWindowWidth = Integer.valueOf(props.getProperty("personWindowWidth"));

            fishingStatusWindowTop = Integer.valueOf(props.getProperty("fishingStatusWindowTop"));
            fishingStatusWindowLeft = Integer.valueOf(props.getProperty("fishingStatusWindowLeft"));
            fishingStatusWindowHeight = Integer.valueOf(props.getProperty("fishingStatusWindowHeight"));
            fishingStatusWindowWidth = Integer.valueOf(props.getProperty("fishingStatusWindowWidth"));

            fishHealthWindowTop = Integer.valueOf(props.getProperty("fishHealthWindowTop"));
            fishHealthWindowLeft = Integer.valueOf(props.getProperty("fishHealthWindowLeft"));
            fishHealthWindowHeight = Integer.valueOf(props.getProperty("fishHealthWindowHeight"));
            fishHealthWindowWidth = Integer.valueOf(props.getProperty("fishHealthWindowWidth"));
            
            
        }
        catch (IOException e) {
            System.out.println("Cannot locate properties file!\n" +
                    "Application shut down");
            System.exit(1);
        }
    }

    public static int getTargetWindowTop() {
        return targetWindowTop;
    }

    public static int getTargetWindowLeft() {
        return targetWindowLeft;
    }

    public static int getTargetWindowHeight() {
        return targetWindowHeight;
    }

    public static int getTargetWindowWidth() {
        return targetWindowWidth;
    }

    public static int getPersonWindowTop() {
        return personWindowTop;
    }

    public static int getPersonWindowLeft() {
        return personWindowLeft;
    }

    public static int getPersonWindowHeight() {
        return personWindowHeight;
    }

    public static int getPersonWindowWidth() {
        return personWindowWidth;
    }

    public static int getFishingStatusWindowTop() {
        return fishingStatusWindowTop;
    }

    public static int getFishingStatusWindowLeft() {
        return fishingStatusWindowLeft;
    }

    public static int getFishingStatusWindowHeight() {
        return fishingStatusWindowHeight;
    }

    public static int getFishingStatusWindowWidth() {
        return fishingStatusWindowWidth;
    }

    public static int getFishHealthWindowTop() {
        return fishHealthWindowTop;
    }

    public static int getFishHealthWindowLeft() {
        return fishHealthWindowLeft;
    }

    public static int getFishHealthWindowHeight() {
        return fishHealthWindowHeight;
    }

    public static int getFishHealthWindowWidth() {
        return fishHealthWindowWidth;
    }
}
