package ru.snslabs.la2;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class LogHandler {
    private static Set<LogHandler> logHandlers = new HashSet<LogHandler>();
    protected LogHandler() {
        logHandlers.add(this);
    }

    public static Set<LogHandler> getLogHandlers() {
        return Collections.unmodifiableSet(logHandlers);
    }

    public void dbg(Object o) {
        log(1,o);
    }

    public void info(Object o) {
        log(2,o);
    }

    public void warn(Object o) {
        log(3,o);
    }

    public void error(Object o) {
        log(4,o);
    }

    public void fatal(Object o) {
        log(5,o);
    }
    
    abstract protected void log(int level, Object o);
}
