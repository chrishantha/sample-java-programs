/*
 * Copyright 2015 M. Isuru Tharanga Chrishantha Perera
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
package com.github.chrishantha.sample.highcpu;

import java.util.Random;

public class SleepingWorker implements Runnable {

    private Random random = new Random();

    public SleepingWorker() {
    }

    @Override
    public void run() {
        Long l = 0l;
        while (true) {
            l++;
            try {
                Thread.sleep(random.nextInt(20));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (l == Long.MAX_VALUE) {
                l = 0l;
            }
        }
    }
}
