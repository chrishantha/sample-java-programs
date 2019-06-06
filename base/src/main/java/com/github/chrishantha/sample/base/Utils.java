/*
 * Copyright 2018 M. Isuru Tharanga Chrishantha Perera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
