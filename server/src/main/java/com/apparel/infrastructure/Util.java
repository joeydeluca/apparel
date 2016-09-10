package com.apparel.infrastructure;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Joe Deluca on 9/10/2016.
 */
public class Util {

    public static Date getDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

}
