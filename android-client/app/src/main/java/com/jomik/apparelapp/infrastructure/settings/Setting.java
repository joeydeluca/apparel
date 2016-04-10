package com.jomik.apparelapp.infrastructure.settings;

/**
 * Created by Joe Deluca on 4/6/2016.
 */
public enum Setting {
    MODE("mode", "test");

    private final String settingName;
    private final String defaultValue;

    Setting(String settingName, String defaultValue) {
        this.settingName = settingName;
        this.defaultValue = defaultValue;
    }

    public String getSettingName() {
        return settingName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
