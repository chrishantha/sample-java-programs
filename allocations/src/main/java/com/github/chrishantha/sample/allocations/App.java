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
package com.github.chrishantha.sample.allocations;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class App {

    @Parameter(names = "--max", description = "Max Numbers")
    private long max = 10_000_000L;

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

    boolean isPrime(Long n) {
        //check if n is a multiple of 2
        if (n % 2 == 0) return false;
        //if not, then just check the odds
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0)
                return false;
        }
        return true;
    }

    private void start() {
        Long primeCount = 0L;
        for (long i = 0; i < max; i++) {
            if (isPrime(i)) {
                primeCount++;
            }
        }
        System.out.format("Found %d prime numbers%n", primeCount);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("App [max=");
        builder.append(max);
        builder.append("]");
        return builder.toString();
    }
}
