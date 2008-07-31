package ru.snslabs.la2;

import com.jniwrapper.Function;
import com.jniwrapper.Pointer;
import com.jniwrapper.UInt;
import ru.snslabs.la2.script.Scenario;
import ru.snslabs.la2.ui.Main;

import javax.swing.*;
import java.io.BufferedReader;

public class ScenarioExecutor extends Log {
    private static final UInt WM_KEY_DOWN = new UInt(0x100);
    private static final UInt WM_KEY_UP = new UInt(0x101);
    private static final UInt WM_CHAR = new UInt(0x102);
    private Function postMessage;
    private Pointer.Void hWnd;
    private BufferedReader bufferedReader;
    private JFrame mainFrame;
    private Main form;
    private Scenario scenario;

    public static void main(String[] args) throws Exception {
        System.out.println("Usage:\n" +
                "ScenarioExecutor {hWnd} {scriptFilePath}");

        JFrame mainFrame = new JFrame("LA2-ScreenBot");
        Main form  = new Main();
        final ScenarioExecutor scenarioExecutor = new ScenarioExecutor(mainFrame, form);
        form.setScripter(scenarioExecutor);
        
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(300,400);
        mainFrame.setContentPane(form.getMainContentPane());
        mainFrame.setVisible(true);
        
    }

    public ScenarioExecutor(JFrame mainFrame, Main form) {
        this.mainFrame = mainFrame;
        this.form = form;
    }

    public void startScenario(final CallBack endScenarioHandler){
        final Scenario scenario = this.getScenario();
        final Thread scriptThread = new Thread(
                new Runnable() {
                    public void run() {
                        try {
                            scenario.execute();
                            endScenarioHandler.execute();
                        }
                        catch (Exception e) {
                            error("Scenario run was interrupted!\n");
                            dbg(e.getMessage());
                        }
                    }
                }
        );
        form.setScriptThread(scriptThread);
        scriptThread.start();
    }

    public Scenario getScenario() {
        return scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }
}
