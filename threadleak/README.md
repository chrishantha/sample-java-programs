Sample to show thread leaks in a Java Application
=================================================

This sample application implements merge sort algorithm using multiple threads.

The algorithm for parallel merge sort was taken from the [Merge Sort example available online from the University
of Washington](https://courses.cs.washington.edu/courses/cse373/13wi/lectures/03-13/MergeSort.java)

The original example uses threads directly. This sample application uses an `ExecutorService` to run threads.

This application runs continuously and prints an interval summary statistics of algorithm run time for multiple random
number arrays. The application will also print final summary statistics of algorithm run time at the program exit.

### How to run

The application will throw Out of Memory error after some time when you run following command

`java -Xmx1g -XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints
 -XX:+UnlockCommercialFeatures -XX:+FlightRecorder
 -XX:StartFlightRecording=settings=profile,duration=2m,name=ThreadLeak,filename=threadleak.jfr
 -XX:FlightRecorderOptions=loglevel=info
 -jar target/threadleak.jar`

### Analyzing Java Flight Recording

In Threads -> Overview tab, you should see thread count is increasing steadily.

### How to fix the Thread Leak

Run the application with `--stop-leakage` parameter, which will use a single `ExecutorService` throughout the
application.
