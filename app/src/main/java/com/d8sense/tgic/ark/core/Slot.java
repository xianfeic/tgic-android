package com.d8sense.tgic.ark.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Jason.z on 2018/9/24.
 * Copyright (c) 2016 BeikePay. All rights reserved.
 */
public class Slot {

    static Date beginEpoch;

    static {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            beginEpoch = dateFormat.parse("2017-03-21 13:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    static int getTime(Date date){
        if(date == null)
            date = new Date();

        return (int)(date.getTime() - beginEpoch.getTime())/1000;

    }

}
