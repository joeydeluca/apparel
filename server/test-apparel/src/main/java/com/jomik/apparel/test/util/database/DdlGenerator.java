package com.jomik.apparel.test.util.database;

/**
 * Run this to print the schema sql to the console - yay
 * Created by Mick on 4/16/2016.
 */
public class DdlGenerator {
    public static void main(String[] args){
        HibernateExporter exporter = new HibernateExporter("org.hibernate.dialect.MySQL5Dialect", "com.jomik.apparel.domain");
        exporter.exportToConsole();
    }
}
