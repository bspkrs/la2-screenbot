package ru.snslabs.la2.script;

public class FishingScenario extends AbstractLA2Scenario {
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