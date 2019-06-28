Sample to show a memory leak in Java Application
================================================

This sample application implements a simple caching solution using a `HashMap`.

For example, see following code.

    Map<Key, String> map = new HashMap<>();
    while (true) {
        for (int i = 0; i < max; i++) {
            Key key = new Key(i);
            if (!map.containsKey(key)) {
                map.put(key, "Number:" + i);
            }
        }
    }

The map is not expected to grow beyond `max` value as all keys will be there in the map after the first `while` loop.

However, if the `Key` class does not contain a proper `equals()` implementation along with the `hashCode()`
 implementation, the key elements will be keep getting added as the `!map.containsKey(key)` will always return `false`.
 This is a good example of a memory leak and this sample was inspired by the blog post:
 "[How to create a memory leak](https://plumbr.io/blog/memory-leaks/how-to-create-a-memory-leak)"

### How to run

The application will throw Out of Memory error after some time when you run following command

`java -Xms1g -Xmx1g -XX:+PrintGCDetails -XX:+PrintGC -XX:+PrintGCDateStamps -Xloggc:gc.log
 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath="/tmp/heap-dump.hprof"
 -XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints
 -XX:+UnlockCommercialFeatures -XX:+FlightRecorder
 -XX:StartFlightRecording=settings=profile,duration=5m,name=MemoryLeak,filename=memoryleak.jfr
 -XX:FlightRecorderOptions=loglevel=info
 -jar target/memoryleak.jar`

### Analyzing Java Flight Recording

In Memory -> Memory tab, you should see a high memory growth rate.

Also see Memory -> Garbage Collections tab and check the frequency of GC events.

### Analyzing Garbage Collection Logs (GC)

You can analyze the GC logs using [GCViewer](https://github.com/chewiebug/GCViewer)

### How to fix the Memory Leak

Run the application with `--key-type GOOD` parameter, which will use a Key object with proper `equals()` implementation.
