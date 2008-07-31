package ru.snslabs.la2.script;

public interface Scenario {
    
    void setWindowHandle(int hWnd);
    
    void execute() throws Exception;
    
}
