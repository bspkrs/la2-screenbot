package ru.snslabs.la2.integration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class reads windowsinfo.ini file of Lineage II to determine default windows position.
 * This settings will be used to automatically adjust positions of screenshot areas taken.
 */
public class WindowsInfo {
    private Map<String, Map<String, Integer>> windowsMap = new HashMap<String, Map<String, Integer>>();
    private static WindowsInfo instance;
    private static final String POS_X = "posX";
    private static final String POS_Y = "posY";
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    /**
     * List of windows available
     */
    public static final String DuelManager = "DuelManager";
    public static final String PartyWnd = "PartyWnd";
    public static final String PetStatusWnd = "PetStatusWnd";
    public static final String StatusWnd = "StatusWnd";
    public static final String SummonedStatusWnd = "SummonedStatusWnd";
    public static final String TargetStatusWnd = "TargetStatusWnd";
    public static final String AbnormalStatusWnd = "AbnormalStatusWnd";
    public static final String BoardWnd = "BoardWnd";
    public static final String CalculatorWnd = "CalculatorWnd";
    public static final String FishViewportWnd = "FishViewportWnd";
    public static final String GMInventoryWnd = "GMInventoryWnd";
    public static final String HennaInfoWnd = "HennaInfoWnd";
    public static final String HennaListWnd = "HennaListWnd";
    public static final String HeroTowerWnd = "HeroTowerWnd";
    public static final String InventoryWnd = "InventoryWnd";
    public static final String MacroEditWnd = "MacroEditWnd";
    public static final String MacroInfoWnd = "MacroInfoWnd";
    public static final String MacroListWnd = "MacroListWnd";
    public static final String ManorInfoWnd = "ManorInfoWnd";
    public static final String MinimapWnd = "MinimapWnd";
    public static final String MoviePlayerWnd = "MoviePlayerWnd";
    public static final String MultiSellWnd = "MultiSellWnd";
    public static final String OlympiadBuff1Wnd = "OlympiadBuff1Wnd";
    public static final String OlympiadBuff2Wnd = "OlympiadBuff2Wnd";
    public static final String OlympiadPlayer1Wnd = "OlympiadPlayer1Wnd";
    public static final String OlympiadTargetWnd = "OlympiadTargetWnd";
    public static final String OptionWnd = "OptionWnd";
    public static final String PartyWndCompact = "PartyWndCompact";
    public static final String PetWnd = "PetWnd";
    public static final String PVPDetailedWnd = "PVPDetailedWnd";
    public static final String QuestAlarmWnd = "QuestAlarmWnd";
    public static final String QuestListWnd = "QuestListWnd";
    public static final String RefineryWnd = "RefineryWnd";
    public static final String ShortcutWndVertical_1 = "ShortcutWndVertical_1";
    public static final String ShortcutWndVertical_2 = "ShortcutWndVertical_2";
    public static final String ShortcutWndHorizontal = "ShortcutWndHorizontal";
    public static final String ShortcutWndJoypad = "ShortcutWndJoypad";
    public static final String ShortcutWndJoypadExpand = "ShortcutWndJoypadExpand";
    public static final String SiegeInfoWnd = "SiegeInfoWnd";
    public static final String SkillEnchantWnd = "SkillEnchantWnd";
    public static final String SSQMainBoard = "SSQMainBoard";
    public static final String SummonedWnd = "SummonedWnd";
    public static final String SkillTrainClanTreeWnd = "SkillTrainClanTreeWnd";
    public static final String SkillTrainInfoWnd = "SkillTrainInfoWnd";
    public static final String SkillTrainListWnd = "SkillTrainListWnd";
    public static final String TutorialViewerWnd = "TutorialViewerWnd";
    public static final String UnionDetailWnd = "UnionDetailWnd";
    public static final String UnionWnd = "UnionWnd";
    public static final String UnrefineryWnd = "UnrefineryWnd";


    public WindowsInfo(String filePath) throws IOException {
        BufferedReader r = new BufferedReader(new FileReader(filePath));
        String line = null;
        while ((line = r.readLine()) != null) {
            if (line.startsWith("[")) {
                Map<String, Integer> window = new HashMap<String, Integer>();
                windowsMap.put(line.replaceAll("[\\[\\]]", "").toUpperCase(), window);
                while ((line = r.readLine()) != null) {
                    if (line == null || "".equals(line.trim())) {
                        break;
                    }
                    final String[] property = line.split("=");
                    window.put(property[0], Integer.valueOf(property[1]));
                }
            }
        }
    }


    public static  int getPosX(String window) {
        return instance.windowsMap.get(window.toUpperCase()).get(POS_X);
    }

    public static  int getPosY(String window) {
        return instance.windowsMap.get(window.toUpperCase()).get(POS_Y);
    }

    public static  int getWidth(String window) {
        return instance.windowsMap.get(window.toUpperCase()).get(WIDTH);
    }

    public static  int getHeight(String window) {
        return instance.windowsMap.get(window.toUpperCase()).get(HEIGHT);
    }

    public static void init(String pathToWindowsInfo) throws IOException {
        instance = new WindowsInfo(pathToWindowsInfo);
    }
}
