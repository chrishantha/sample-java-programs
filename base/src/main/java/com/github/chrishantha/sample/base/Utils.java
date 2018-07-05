package com.github.chrishantha.sample.base;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Some basic utilities for sample applications
 */
public class Utils {

    /**
     * Exit application after a given number of seconds.
     *
     * @param timeout Timeout in seconds
     */
    public static void exitApplication(int timeout) {
        if (timeout > 0) {
            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    System.out.println("Exiting now...");
                    System.exit(0);
                }
            }, timeout * 1000);
        }
    }
}
