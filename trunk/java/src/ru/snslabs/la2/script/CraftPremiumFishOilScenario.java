package ru.snslabs.la2.script;

import ru.snslabs.la2.model.Status;

public class CraftPremiumFishOilScenario  extends AbstractLA2Scenario{
    public static final int CRAFT_MANA_COST = 92;
    
    public CraftPremiumFishOilScenario() {
        dbg("Craft Premium fish oil scenario initialized");
    }

    public void execute() throws Exception {
        Status st = null;
        for(int i =0;i<100;i++){
            st = getStatus();
            if (st.getMp() > CRAFT_MANA_COST){
                System.out.println("good");
                craft();
                sleep(10000);
                craft();
            }
            else{
                sleep(60000); // wait 1 minute to replenish mana
                System.out.println("bad");
            }
            
        }
    }

    private void craft() {
        
        info("Crafting...");
        
        leftMouseButtonClick(784, 741);
        
        
        
    }

    public String toString() {
        return "Craft Premium Oil";
    }
}
