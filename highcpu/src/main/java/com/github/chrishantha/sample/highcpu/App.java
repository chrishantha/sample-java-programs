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

import java.util.Timer;
import java.util.TimerTask;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class App {

    @Parameter(names = "--run-hashing", description = "Run Hashing Worker", arity = 1)
    private boolean runHashing = true;

    @Parameter(names = "--run-sleeping", description = "Run Sleeping Worker", arity = 1)
    private boolean runSleeping = true;

    @Parameter(names = "--run-math", description = "Run Math Worker", arity = 1)
    private boolean runMath = true;

    @Parameter(names = "--hashing-workers", description = "Hashing Workers")
    private int hashingWorkers = 1;

    @Parameter(names = "--sleeping-workers", description = "Sleeping Workers")
    private int sleepingWorkers = 3;

    @Parameter(names = "--math-workers", description = "Math Workers")
    private int mathWorkers = 2;

    @Parameter(names = "--hash-data-length", description = "Hash Data Length")
    private long hashDataLength = 2000;

    @Parameter(names = "--hashing-algo", description = "Hashing Algorithm")
    private String hashingAlgorithm = "SHA-1";

    @Parameter(names = "--help", description = "Display Help", help = true)
    private boolean help;

    @Parameter(names = "--exit-timeout", description = "Exit Timeout in Seconds (Default 2 minutes. Use 0 to run forever)")
    private int exitTimeoutInSeconds = 2 * 60;

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

    private void start() {
        System.out.println("Starting Application...");
        if (exitTimeoutInSeconds > 0) {
            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    System.out.println("Exiting now...");
                    System.exit(0);
                }
            }, exitTimeoutInSeconds * 1000);
        }
        if (runHashing) {
            for (int i = 0; i < hashingWorkers; i++) {
                startThread(i, "Hashing", new HashingWorker(hashDataLength, hashingAlgorithm));
            }
        }
        if (runSleeping) {
            for (int i = 0; i < sleepingWorkers; i++) {
                startThread(i, "Sleeping", new SleepingWorker());
            }
        }
        if (runMath) {
            for (int i = 0; i < mathWorkers; i++) {
                startThread(i, "Math", new MathWorker());
            }
        }
    }

    private void startThread(int i, String name, Runnable worker) {
        Thread thread = new Thread(worker);
        thread.setName(String.format("Thread %d: %s", i, name));
        thread.start();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("App [runHashing=");
        builder.append(runHashing);
        builder.append(", runSleeping=");
        builder.append(runSleeping);
        builder.append(", runMath=");
        builder.append(runMath);
        builder.append(", hashingWorkers=");
        builder.append(hashingWorkers);
        builder.append(", sleepingWorkers=");
        builder.append(sleepingWorkers);
        builder.append(", mathWorkers=");
        builder.append(mathWorkers);
        builder.append(", hashDataLength=");
        builder.append(hashDataLength);
        builder.append(", hashingAlgorithm=");
        builder.append(hashingAlgorithm);
        builder.append(", exitTimeoutInSeconds=");
        builder.append(exitTimeoutInSeconds);
        builder.append("]");
        return builder.toString();
    }
}
