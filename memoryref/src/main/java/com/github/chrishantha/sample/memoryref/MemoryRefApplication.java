/*
 * Copyright 2019 M. Isuru Tharanga Chrishantha Perera
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
package com.github.chrishantha.sample.memoryref;

import com.github.chrishantha.sample.base.SampleApplication;

import java.io.IOException;

public class MemoryRefApplication implements SampleApplication {

    @Override
    public void start() {
        A a = new A();
        long size = a.getSize();
        System.out.format("The retained heap size of object A is %d bytes (~%d MiB).%n",
                size, (size / (1024 * 1024)));
        long objectSize = JavaAgent.getObjectSize(a);
        if (objectSize > 0) {
            System.out.format("The shallow heap size of object A is %d bytes.%n", objectSize);
        } else {
            System.out.println("WARNING: Java Agent is not initialized properly.");
        }
        Thread backgroundThread = new Thread() {

            private long getSize() {
                G g = new G();
                return g.getSize();
            }

            @Override
            public void run() {
                long size = getSize();
                System.out.format("The size of object allocated within the background thread was %d bytes (~%d MiB).%n",
                        size, (size / (1024 * 1024)));
                try {
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        backgroundThread.setName("Background Thread");
        backgroundThread.setDaemon(true);
        backgroundThread.start();
        try {
            System.out.println("Press Enter to exit.");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "MemoryRefApplication";
    }
}
