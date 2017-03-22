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

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class App {

    @Parameter(names = "--count", description = "Print Count")
    private int count = 50;

    @Parameter(names = "--lock", description = "Lock", arity = 1)
    private boolean lock = true;

    @Parameter(names = "--help", description = "Display Help", help = true)
    private boolean help;

    public static void main(String[] args) {
        App app = new App();
        final JCommander jcmdr = new JCommander(app);
        jcmdr.setProgramName(App.class.getSimpleName());
        jcmdr.parse(args);

        System.out.println(app);

        if (app.help) {
            jcmdr.usage();
            return;
        }

        app.start();
    }


    private class EvenThread extends Thread {

        public EvenThread() {
            super("Even-Thread");
        }

        @Override
        public void run() {
            for (int i = 0; i < count; i++) {
                printEven(i);
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
                printOdd(i);
            }
        }
    }


    private void printNumber(int i) {
        System.out.format("Thread: %s, Number: %d%n", Thread.currentThread().getName(), i);
    }

    private void printEven(int i) {
        if ((lock && isEvenLocked(i)) || isEven(i)) {
            printNumber(i);
        }
    }

    private void printOdd(int i) {
        if ((lock && !isEvenLocked(i)) || !isEven(i)) {
            printNumber(i);
        }
    }

    private synchronized boolean isEvenLocked(int i) {
        return isEven(i);
    }

    private boolean isEven(int i) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return i % 2 == 0;
    }

    private void start() {
        new OddThread().start();
        new EvenThread().start();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("App [count=");
        builder.append(count);
        builder.append("]");
        return builder.toString();
    }
}
