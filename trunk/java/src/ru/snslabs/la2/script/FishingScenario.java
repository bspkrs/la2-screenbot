package ru.snslabs.la2.script;

import ru.snslabs.la2.AbstractLoggable;

public class FishingScenario extends AbstractLoggable implements Scenario {
    public FishingScenario() {
        dbg("Fishing scenario initialized");
    }

    public void execute() {
        dbg("Fishing started...");

        dbg("Fishing ended.");
    }

    public String toString() {
        return "Fishing";
    }
}