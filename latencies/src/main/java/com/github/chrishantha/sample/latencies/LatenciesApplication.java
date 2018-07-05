/*
 * Copyright 2017 M. Isuru Tharanga Chrishantha Perera
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
package com.github.chrishantha.sample.latencies;

import com.beust.jcommander.Parameter;
import com.github.chrishantha.sample.base.SampleApplication;

public class LatenciesApplication implements SampleApplication {

    @Parameter(names = "--count", description = "Print Count")
    private int count = 50;

    private class EvenThread extends Thread {

        public EvenThread() {
            super("Even-Thread");
        }

        @Override
        public void run() {
            for (int i = 0; i < count; i++) {
                if (isEven(i)) {
                    printNumber(i);
                }
            }
        }
    }

    private class OddThread extends Thread {

        public OddThread() {
            super("Odd-Thread");
        }

        @Override
        public void run() {
            for (int i = 0; i < count; i++) {
                if (!isEven(i)) {
                    printNumber(i);
                }
            }
        }
    }


    private void printNumber(int i) {
        System.out.format("Thread: %s, Number: %d%n", Thread.currentThread().getName(), i);
    }

    private synchronized boolean isEven(int i) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return i % 2 == 0;
    }

    @Override
    public void start() {
        new EvenThread().start();
        new OddThread().start();
    }

    @Override
    public String toString() {
        return "LatenciesApplication{" +
                "count=" + count +
                '}';
    }
}
