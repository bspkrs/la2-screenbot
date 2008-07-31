package ru.snslabs.la2;

abstract public class Log {
    public void dbg(Object o){
        log(1,o);
    }
    public void info(Object o){
        log(2,o);
    }
    public void warn(Object o){
        log(3,o);
    }
    public void error(Object o){
        log(4,o);
    }
    public void fatal(Object o){
        log(5,o);
    }
    
    private void log(int level, Object o){
        for (LogHandler logHandler : LogHandler.getLogHandlers()) {
            logHandler.log(level, o);
        }
    }
}
