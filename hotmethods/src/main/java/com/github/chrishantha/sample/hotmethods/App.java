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
package com.github.chrishantha.sample.hotmethods;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

public class App {

    @Parameter(names = "--count", description = "Random Numbers Count")
    private int randomNumbersCount = 1000000;

    @Parameter(names = "--max", description = "Maximum limit to generate prime numbers")
    private int max = 100000;

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

    private void start() {
        Collection<Integer> primeNumbers = new LinkedList<>();
        System.out.println("Generating Prime numbers between 1 and " + max);
        for (int i = 1; i < max; i++) {
            boolean isPrimeNumber = true;
            // Check whether the number is prime
            for (int j = 2; j < i; j++) {
                if (i % j == 0) {
                    isPrimeNumber = false;
                    break;
                }
            }

            if (isPrimeNumber) {
                primeNumbers.add(i);
            }
        }

        Random random = new Random();
        System.out.println("Checking Random Prime numbers");
        int count = 0;
        for (int i = 1; i < randomNumbersCount; i++) {
            int randomNumber = random.nextInt(max);
            if (primeNumbers.contains(randomNumber)) {
                count++;
            }
        }
        System.out.format("Found %d prime numbers from %d random numbers%n", count, randomNumbersCount);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("App [randomNumbersCount=");
        builder.append(randomNumbersCount);
        builder.append(", max=");
        builder.append(max);
        builder.append("]");
        return builder.toString();
    }
}
