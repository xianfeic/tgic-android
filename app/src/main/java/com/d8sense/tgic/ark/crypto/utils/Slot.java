package com.d8sense.tgic.ark.crypto.utils;

import com.d8sense.tgic.ark.crypto.configuration.Network;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Slot {
    public static int time() {
        long now = new Date().getTime();
        long epoch = epoch();
        return (int) ((new Date().getTime() - epoch()) / 1000);
    }

    public static long epoch() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            return dateFormat.parse("2017-03-21 13:00:00").getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
