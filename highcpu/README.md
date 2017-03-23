Sample program consuming high CPU usage
=======================================

A java program consuming high CPU usage. This program was inspired by a sample found in the article "[Identifying which Java Thread is consuming most CPU](http://code.nomad-labs.com/2010/11/18/identifying-which-java-thread-is-consuming-most-cpu/)"

### How to run
`java -Xms128m -Xmx128m -XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints -XX:+UnlockCommercialFeatures -XX:+FlightRecorder -XX:StartFlightRecording=settings=profile,duration=30s,name=HighCPU,filename=highcpu.jfr -XX:FlightRecorderOptions=loglevel=info -jar target/highcpu.jar`
