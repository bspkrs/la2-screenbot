package ru.snslabs.la2.script;

import ru.snslabs.la2.model.Status;

public class ManorScenario extends AbstractLA2Scenario {
    public ManorScenario() {
        dbg("Manor scenario initialized");
    }

    public void execute() throws Exception {
        info("Manor started...");

        for (int i = 1; i < 100; i++) {
            try {
                Status st = getStatus();
                if (!"AmberBasilisk".equals(st.getTargetName()) && !"AmbemBasilisk".equals(st.getTargetName())) {
                    type("/target Amber Basilisk\r");
                    Thread.sleep(1000);
                    type("/target Amber Basilisk\r");
                    Thread.sleep(1000);
                    type("/useshortcut 1 8\r");
                    Status status = getStatus();
                    while (status.isTargetAlive()) {
                        Thread.sleep(2000);
                        status = getStatus();

                        if (status.getHp() < status.getMaxHp() * 0.3) {
                            type("/useshortcut 2 3\r");
                            Thread.sleep(1000);
                            type("/useshortcut 1 12\r");
                        }
                        if (status.getHp() < status.getMaxHp() * 0.6) {
                            type("/useshortcut 2 7\r");
                        }
                        if (status.getHp() < status.getMaxHp() * 0.8) {
                            type("/attack\r");
                        }
                    }
                    type("/useshortcut 1 9\r");
                    type("/pickup\r/pickup\r/pickup\r/pickup\r");
                    status = getStatus();
                    if (status.getHp() < status.getMaxHp() * 0.6) {
                        type("/useshortcut 2 7\r");
                        Thread.sleep(15000);
                    }
                    Thread.sleep(5000);
                }
            }
            catch (Exception e) {
                fatal("Cannot process step... :-(" + e.getMessage());
            }
            Thread.sleep(2000);
        }


        info("Manor ended.");
    }

    public String toString() {
        return "Manor";
    }
}
