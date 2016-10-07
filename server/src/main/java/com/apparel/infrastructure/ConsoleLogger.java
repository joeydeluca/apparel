package com.apparel.infrastructure;

/**
 * Created by Joe Deluca on 10/5/2016.
 */
public class ConsoleLogger implements Logger {

    private Class clazz;

    public ConsoleLogger(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public void info(String message) {
        log("INFO", message);
    }

    @Override
    public void error(String message) {
        log("ERROR", message);
    }

    private void log(String level, String message) {
        System.out.println(String.format("%s: %s %s", level, clazz.getSimpleName(), message));
    }
}
