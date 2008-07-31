package ru.snslabs.la2.script;

import java.util.Collections;
import java.util.Set;

final public class ScenarioFactory {
    
    private static Set<Scenario> allScenario;
    {
        allScenario.add(new ManorScenario());
        allScenario.add(new FishingScenario());
    }
    private ScenarioFactory() {
    }

    public static Set<Scenario> getAllScenario(){
        return Collections.unmodifiableSet(allScenario);
    }
}
