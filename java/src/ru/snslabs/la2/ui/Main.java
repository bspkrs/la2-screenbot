package ru.snslabs.la2.ui;

import ru.snslabs.la2.CallBack;
import ru.snslabs.la2.LogHandler;
import ru.snslabs.la2.ScenarioExecutor;
import ru.snslabs.la2.integration.WindowsInfo;
import static ru.snslabs.la2.integration.WindowsInfo.TargetStatusWnd;
import static ru.snslabs.la2.integration.WindowsInfo.FishViewportWnd;
import ru.snslabs.la2.script.CraftPremiumFishOilScenario;
import ru.snslabs.la2.script.FishingScenario;
import ru.snslabs.la2.script.ManorScenario;
import ru.snslabs.la2.script.Scenario;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.MessageFormat;

public class Main extends LogHandler {
    // view controls
    private JComboBox cbWindowHandle;
    transient private JButton startButton;
    transient private JButton stopButton;
    private JComboBox cbScenario;
    private JScrollPane spLog;
    private JTextArea taLog;
    private JPanel mainContentPane;
    private JTextField tfPathToWindowsInfo;
    private JButton bBrowse;
    // models
    private DefaultComboBoxModel scenarioComboBoxModel;
    // control - engine
    private ScenarioExecutor scenarioExecutor;
    private Thread scriptThread;
    private WindowsInfo winInfo;

    public Main() {
        // initialize data
        // 1. Scenarios
        scenarioComboBoxModel = new DefaultComboBoxModel();
        scenarioComboBoxModel.addElement(new FishingScenario());
        scenarioComboBoxModel.addElement(new CraftPremiumFishOilScenario());
        scenarioComboBoxModel.addElement(new ManorScenario());


        cbScenario.setModel(scenarioComboBoxModel);


        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopButton.setEnabled(true);
                startButton.setEnabled(false);
                final Scenario scenario = (Scenario) cbScenario.getSelectedItem();
                scenario.setWindowHandle(Integer.valueOf(String.valueOf(cbWindowHandle.getSelectedItem())));
                scenarioExecutor.setScenario(scenario);
                scenarioExecutor.startScenario(new CallBack() {
                    public void execute() {
                        stopButton.setEnabled(false);
                        startButton.setEnabled(true);
                    }
                });
            }
        });

        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (scriptThread != null && scriptThread.isAlive()) {
                    scriptThread.interrupt();
                }
                stopButton.setEnabled(false);
                startButton.setEnabled(true);
            }
        });

        try {
            WindowsInfo.init(this.tfPathToWindowsInfo.getText());
            dbg("Windows properties were loaded...\n");
            dbg(MessageFormat.format("Target position: ({0},{1})", 
                    WindowsInfo.getPosX(TargetStatusWnd), 
                    WindowsInfo.getPosY(TargetStatusWnd))
            );
            dbg(MessageFormat.format("Fishing position: ({0},{1})", 
                    WindowsInfo.getPosX(FishViewportWnd), 
                    WindowsInfo.getPosY(FishViewportWnd))
            );
        }                                                 
        catch (IOException e) {
            warn("Cannot load windows properties from file\n"+this.tfPathToWindowsInfo.getText());
        }
    }

    public JPanel getMainContentPane() {
        return mainContentPane;
    }

    public ScenarioExecutor getScripter() {
        return scenarioExecutor;
    }

    public void setScripter(ScenarioExecutor scenarioExecutor) {
        this.scenarioExecutor = scenarioExecutor;
    }

    public void log(int level, Object o) {
        taLog.append(String.valueOf(o) + "\n");
    }

    public void setScriptThread(Thread scriptThread) {
        this.scriptThread = scriptThread;
    }
}
