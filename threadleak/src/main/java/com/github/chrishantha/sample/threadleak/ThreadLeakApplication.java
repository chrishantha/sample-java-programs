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
package com.github.chrishantha.sample.threadleak;

import com.beust.jcommander.Parameter;
import com.github.chrishantha.sample.base.SampleApplication;
import com.github.chrishantha.sample.base.Utils;
import org.HdrHistogram.Histogram;
import org.HdrHistogram.Recorder;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SuppressWarnings("FieldCanBeLocal")
public class ThreadLeakApplication implements SampleApplication {

    // Random number generator
    private static final Random random = new Random();

    // Get the Thread MXBean.
    private final static ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

    // Formatter to print current time
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Parameter(names = "--threads", description = "Number of threads to be used for merge sorting algorithm.")
    private int threads = Runtime.getRuntime().availableProcessors();

    @Parameter(names = "--initial-length", description = "Initial length of random number array to sort.")
    private int length = 1_000;

    @Parameter(names = "--runs", description = "How many times to grow the array by 2")
    private int runs = 16;

    @Parameter(names = "--bound", description = "Upper bound of values in array.")
    private int bound = 1_000_000;

    @Parameter(names = "--delay", description = "Initial delay in seconds to print statistics. Default: 10 seconds.")
    private int delay = 10;

    @Parameter(names = "--period", description = "Period in seconds to print statistics. Default: 30 seconds.")
    private int period = 30;

    @Parameter(names = "--stop-leakage", description = "Stop leaking threads.", arity = 0)
    private boolean stopLeakage = false;

    @Parameter(names = "--exit-timeout", description = "Exit timeout in seconds " +
            "(Default 2 minutes. Use 0 to run forever)")
    private int exitTimeoutInSeconds = 2 * 60;

    private volatile ExecutorService executorService;

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void start() {
        System.out.println("Starting Application...");
        Utils.exitApplication(exitTimeoutInSeconds);

        // Map of stats for each array size used for merge sorting
        final Map<Integer, StatsRecorder> recorderMap = new ConcurrentSkipListMap<>();

        // Schedule the printing of statistics
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                printHeader("Interval");
                for (Map.Entry<Integer, StatsRecorder> entry : recorderMap.entrySet()) {
                    StatsRecorder statsRecorder = entry.getValue();
                    Histogram histogram = statsRecorder.recorder.getIntervalHistogram();
                    statsRecorder.accumulatedHistogram.add(histogram);
                    printRow(entry.getKey(), histogram);
                }
            }
        }, delay * 1000, period * 1000);

        // Print statistics at exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            printHeader("Final");
            for (Map.Entry<Integer, StatsRecorder> entry : recorderMap.entrySet()) {
                StatsRecorder statsRecorder = entry.getValue();
                Histogram histogram = statsRecorder.accumulatedHistogram;
                printRow(entry.getKey(), histogram);
            }
        }));

        // Run continuously.
        while (true) {
            // Initial array length
            int arrayLength = length;
            for (int i = 1; i <= runs; i++) {
                int[] numbers = createRandomArray(arrayLength);

                // run the algorithm and measure how long it takes
                long startTime = System.nanoTime();
                parallelMergeSort(numbers);
                long endTime = System.nanoTime();
                StatsRecorder statsRecorder = recorderMap.compute(arrayLength, (integer, existingRecorder) -> {
                    if (existingRecorder != null) {
                        return existingRecorder;
                    } else {
                        return new StatsRecorder();
                    }
                });
                statsRecorder.recorder.recordValue(endTime - startTime);

                if (!isSorted(numbers)) {
                    throw new RuntimeException("Array is not sorted: " + Arrays.toString(numbers));
                }

                arrayLength *= 2;   // double size of array for next time
            }
        }
    }

    private static class StatsRecorder {
        // HdrHistogram recorder to record algorithm run time.
        Recorder recorder = new Recorder(2);
        // Accumulated values
        Histogram accumulatedHistogram = new Histogram(2);
    }

    private void printHeader(String summary) {
        System.out.printf("Time: %20s, Summary: %8s, Thread Count: %3d, Unit: milliseconds (ms)\n",
                LocalDateTime.now().format(formatter),
                summary,
                threadMXBean.getThreadCount());
        System.out.printf("%8s, %7s, " +
                        "%7s, %7s, %7s, " +
                        "%7s, %7s, " +
                        "%7s, %7s, %7s, %7s\n",
                "Elements", "Samples",
                "Avg", "Min", "Max",
                "StdDev", "Median",
                "p75", "p98", "p99", "p99.9");
    }

    private void printRow(int elements, Histogram histogram) {
        System.out.printf("%8d, %7d," +
                        " %7.2f, %7.2f, %7.2f," +
                        " %7.2f, %7.2f, " +
                        "%7.2f, %7.2f, %7.2f, %7.2f\n", elements,
                histogram.getTotalCount(),
                histogram.getMean() / 1E6,
                histogram.getMinValue() / 1E6,
                histogram.getMaxValue() / 1E6,
                histogram.getStdDeviation() / 1E6,
                histogram.getValueAtPercentile(0.5) / 1E6,
                histogram.getValueAtPercentile(0.75) / 1E6,
                histogram.getValueAtPercentile(0.98) / 1E6,
                histogram.getValueAtPercentile(0.99) / 1E6,
                histogram.getValueAtPercentile(0.999) / 1E6);
    }

    private ExecutorService getExecutorService() {
        if (stopLeakage) {
            // Return a single executor service.
            if (executorService == null) {
                synchronized (this) {
                    if (executorService == null) {
                        executorService = Executors.newFixedThreadPool(threads);
                    }
                }
            }
            return executorService;
        } else {
            // Always to return a new executor service to leak threads.
            return Executors.newFixedThreadPool(threads);
        }
    }

    /**
     * Run parallel merge sort algorithm
     *
     * @param numbers The numbers to sort.
     */
    private void parallelMergeSort(int[] numbers) {
        parallelMergeSort(numbers, getExecutorService(), threads);
    }

    /**
     * This is a recursive method.
     *
     * @param numbers         The numbers to sort.
     * @param executorService The {@link ExecutorService} to submit parallel tasks.
     * @param threadCount     The number of threads to use
     */
    private void parallelMergeSort(int[] numbers, ExecutorService executorService, int threadCount) {
        if (threadCount <= 1) {
            mergeSort(numbers);
        } else if (numbers.length >= 2) {
            // split array in half
            int[] left = Arrays.copyOfRange(numbers, 0, numbers.length / 2);
            int[] right = Arrays.copyOfRange(numbers, numbers.length / 2, numbers.length);
            Runnable leftSorter = () -> parallelMergeSort(left, executorService, threadCount / 2);
            Runnable rightSorter = () -> parallelMergeSort(right, executorService, threadCount / 2);
            Future<?> leftResult = executorService.submit(leftSorter);
            Future<?> rightResult = executorService.submit(rightSorter);
            try {
                leftResult.get();
                rightResult.get();
            } catch (Exception e) {
                throw new RuntimeException("Unexpected error", e);
            }
            // merge them back together
            merge(left, right, numbers);
        }
    }

    /**
     * Arranges the elements of the given array into sorted order
     * using the "merge sort" algorithm, which splits the array in half,
     * recursively sorts the halves, then merges the sorted halves.
     * It is O(N log N) for all inputs.
     *
     * @param numbers The numbers to sort.
     */
    private void mergeSort(int[] numbers) {
        if (numbers.length >= 2) {
            // split array in half
            int[] left = Arrays.copyOfRange(numbers, 0, numbers.length / 2);
            int[] right = Arrays.copyOfRange(numbers, numbers.length / 2, numbers.length);

            // sort the halves
            mergeSort(left);
            mergeSort(right);

            // merge them back together
            merge(left, right, numbers);
        }
    }

    /**
     * Combines the contents of sorted left/right arrays into output array numbers.
     * Assumes that left.length + right.length == numbers.length.
     *
     * @param left    The left array.
     * @param right   The right array.
     * @param numbers The output array.
     */
    private void merge(int[] left, int[] right, int[] numbers) {
        int i1 = 0;
        int i2 = 0;
        for (int i = 0; i < numbers.length; i++) {
            if (i2 >= right.length || (i1 < left.length && left[i1] < right[i2])) {
                numbers[i] = left[i1];
                i1++;
            } else {
                numbers[i] = right[i2];
                i2++;
            }
        }
    }

    /**
     * Creates an array of the given length, fills it with random
     * non-negative integers, and returns it.
     *
     * @param length The length of the array.
     * @return An array of random numbers with given length.
     */
    private int[] createRandomArray(int length) {
        int[] numbers = new int[length];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = random.nextInt(bound);
        }
        return numbers;
    }

    /**
     * Returns true if the given array is sorted in ascending order.
     *
     * @param numbers The number array.
     * @return {@code true} if the array is  sorted in ascending order, otherwise {@code false}
     */
    private boolean isSorted(int[] numbers) {
        for (int i = 0; i < numbers.length - 1; i++) {
            if (numbers[i] > numbers[i + 1]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "ThreadLeakApplication{" +
                "threads=" + threads +
                ", length=" + length +
                ", runs=" + runs +
                ", bound=" + bound +
                ", delay=" + delay +
                ", period=" + period +
                ", stopLeakage=" + stopLeakage +
                ", exitTimeoutInSeconds=" + exitTimeoutInSeconds +
                '}';
    }
}
