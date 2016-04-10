package com.jomik.apparelapp.infrastructure.settings;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Joe Deluca on 4/5/2016.
 */
public class SettingsUtil {
    private static Properties properties;

    public static String getSettingValue(Setting setting, Context context) {
        if(properties == null) {
            properties = new Properties();
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = null;
            try {
                inputStream = assetManager.open("config.properties");
                properties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // First check the properties file
        String settingValue = properties.getProperty(setting.getSettingName());
        if(settingValue != null) {
            return settingValue;
        }

        // Otherwise return the default value
        return setting.getDefaultValue();
    }
}
