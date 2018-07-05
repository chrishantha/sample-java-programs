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

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import java.util.Iterator;
import java.util.ServiceLoader;

public class App {

    private static class CommonArgs {
        @Parameter(names = {"-h", "--help"}, description = "Display Help", help = true)
        private boolean help;
    }

    public static void main(String[] args) {
        // There should be only one application
        Iterator<SampleApplication> applicationIterator = ServiceLoader.load(SampleApplication.class).iterator();
        if (!applicationIterator.hasNext()) {
            throw new IllegalStateException("Could not load Sample Application");
        }
        SampleApplication sampleApplication = applicationIterator.next();
        CommonArgs commonArgs = new CommonArgs();
        final JCommander jcmdr = JCommander.newBuilder()
                .programName(sampleApplication.getClass().getSimpleName())
                .addObject(sampleApplication)
                .addObject(commonArgs)
                .build();

        try {
            jcmdr.parse(args);
        } catch (ParameterException ex) {
            System.err.println(ex.getMessage());
            return;
        }

        if (commonArgs.help) {
            jcmdr.usage();
            return;
        }

        System.out.println(sampleApplication);
        sampleApplication.start();
    }
}
