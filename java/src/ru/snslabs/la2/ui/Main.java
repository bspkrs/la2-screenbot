package ru.snslabs.la2.ui;

import ru.snslabs.la2.LogHandler;
import ru.snslabs.la2.Scripter;
import ru.snslabs.la2.script.FishingScenario;
import ru.snslabs.la2.script.ManorScenario;
import ru.snslabs.la2.script.Scenario;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends LogHandler {
    // view controls
    private JComboBox cbWindowHandle;
    private JButton startButton;
    private JButton stopButton;
    private JComboBox cbScenario;
    private JScrollPane spLog;
    private JTextArea taLog;
    private JPanel mainContentPane;
    // models
    private DefaultComboBoxModel scenarioComboBoxModel;
    // control - engine
    private Scripter scripter;

    public Main() {
        // initialize data
        // 1. Scenarios
        scenarioComboBoxModel = new DefaultComboBoxModel();
        scenarioComboBoxModel.addElement(new ManorScenario());
        scenarioComboBoxModel.addElement(new FishingScenario());
        
        
        
        cbScenario.setModel(scenarioComboBoxModel);
        
        
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopButton.setEnabled(true);
                scripter.setScenario((Scenario) cbScenario.getSelectedItem());
                scripter.startScenario();
            }
        });
    }

    public JPanel getMainContentPane() {
        return mainContentPane;
    }

    public Scripter getScripter() {
        return scripter;
    }

    public void setScripter(Scripter scripter) {
        this.scripter = scripter;
    }

    public void log(int level, Object o){
        taLog.append(String.valueOf(o)+"\n");
    }
}
