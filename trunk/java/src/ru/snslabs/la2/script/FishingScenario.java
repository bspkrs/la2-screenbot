package ru.snslabs.la2.script;

import ru.snslabs.la2.model.Status;

public class FishingScenario extends AbstractLA2Scenario {
    public FishingScenario() {
        dbg("Fishing scenario initialized");
    }

    public void execute() throws Exception {
        dbg("Fishing scenario started...");
        while (!Thread.currentThread().isInterrupted()) {
            Status st = getStatus();

            // fishing loop
            dbg("Start fishing...");
            startFishing();
            sleep(3000);

            // waiting for fish to take bait
            // wait no more then 30 sec
            int i = 30;
            st = getStatus();
            // condition to stop waiting are: fishing stopped or 30 seconds pass or fish appears
            while (st.isFishing() && i-- > 0 && st.getFishHp() < 100) {
                sleep(1000);
                st = getStatus();
            }
            // happens one of two - fish took bait, then i > 0, or fish didn't take bait and i = 0
            if (i == 0) {
                continue;
            }
            // so fish took the bait...
            int oldFishHP;

            // now we need to decide - Reel (F3), or Pump (F2)
            while (st.isFishing()) { // loop while fish is not caught and not gone - fishing is active
                oldFishHP = st.getFishHp();
                sleep(1800); // wait 2 secs, this time should be enough to make skills ready for use
                st = getStatus();
                if (st.getFishHp() == -1) {
                    // canoot determine fish HP because of glowing HP bar
                    // retry after 0.5 sec will be good
                    sleep(400);
                    st = getStatus();
                }
                if (st.getFishHp() > oldFishHP) {
                    // fish HP increased - need to Reel
//                    dbg("Use reeling");
                    useFishShot();
                    useReeling();
                }
                else {
                    // fish HP wasn't increased - need to Pump
//                    dbg("Use pumping");
                    useFishShot();
                    usePumpung();
                }
                sleep(300); // wait a little to allow server respond to commands
                st = getStatus(); // obtaining new status
                
                
                if(st.isFishing()){
//                    dbg("Fish health : " + st.getFishHp());
                }
                else{
                    dbg("Fishing ended");
                }

            }
            // check if we caught a sea monster
            targetNext();
            st = getStatus();
            if(st.getTargetHp().intValue() > 20 && st.getHp() < st.getMaxHp()){
                // we caught a monster
//                stopFishing();
                takeWeapons();
                attack();
                st = getStatus();
                while(st.getTargetHp() != null && st.getTargetHp().intValue() > 0){
                    sleep(2000);
                    attack();
                }
                // better do not pick up, because this will change our position and fail future fishing
                /*
                pickUp();
                sleep(300);
                pickUp();
                sleep(300);
                pickUp();
                sleep(300);
                */

                //take fishing road
                takeFishingRodAndBait();
                // continue fishing...
            }
            
            sleep(1000);
            
        }
        info("Fishing scenario ended.");
    }

    private void takeFishingRodAndBait() {
        pressFKey(5); 
        pressFKey(9); 
    }

    private void pickUp() {
        pressFKey(10); // pickup
    }

    private void attack() {
        pressFKey(8); // kill!!!!
    }

    private void takeWeapons() {
        pressFKey(6); // take SLS
        pressFKey(7); // take shield
    }

    private void targetNext() {
        pressFKey(11);
    }

    private void usePumpung() {
        pressFKey(2); // use Pumping
    }

    private void useReeling() {
        pressFKey(3); // use Reeling
    }

    private void useFishShot() {
        pressFKey(4); // use fish shot
    }

    private void startFishing() {
        pressFKey(1);
    }

    private void stopFishing() {
        pressFKey(1);
    }

    public String toString() {
        return "Fishing";
    }
}