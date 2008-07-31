package ru.snslabs.la2.script;

import ru.snslabs.la2.AbstractLoggable;

public class ManorScenario extends AbstractLoggable implements Scenario {
    public ManorScenario() {
        dbg("Manor scenario initialized");
    }

    public void execute() {
        dbg("Manor started...");
        
        
        dbg("Manor ended.");
    }

    public String toString() {
        return "Manor";
    }
}
