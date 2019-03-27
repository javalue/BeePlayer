package com.bee.player.util;

public class TimeUtils {

    /**
     * @param time 毫秒
     * @return HH:mm:ss
     */
    public static String toDuration(long time) {

        String ss = String.format("%02d", (time / 1000) % 60);
        String mm = String.format("%02d", time / 1000 / 60 % 60);
        String hh = String.format("%02d", (time / 1000) / 3600);
        return hh + ":" + mm + ":" + ss;
    }
}
