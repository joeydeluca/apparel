package com.apparel.infrastructure;

/**
 * Created by Joe Deluca on 10/5/2016.
 */
public class LoggerFactory {

    public static Logger getLogger(Class clazz) {
        return new ConsoleLogger(clazz);
    }

}
