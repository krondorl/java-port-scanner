<img src="assets/java-port-scanner-logo.svg" width="256" height="256">

# Java Port Scanner

Java library and CLI tool for port scanning.

## Tech Stack

- Java 25
- Apache Maven 3.9.16
- picocli

## Features

- Virtual threads
- Compatible with both IPv4 and IPv6
- CLI parameters
- Cancellable search
- Host name
- Starting port number
- Ending port number
- Timeout

## Usage and Docs

Inside the repo root folder use these commands:

1. `mvn clean package`
1. `java -jar target/port-scanner.jar --host localhost --start-port 5170 --end-port 5180`

Use parameters accordingly (host, start-port, end-port).

## CLI Screen Example

```bash
> mvn clean package

[INFO] ----------------------------------------
[INFO] BUILD SUCCESS
[INFO] ----------------------------------------
[INFO] Total time:  1.846 s
[INFO] Finished at: 2026-09-22T17:23:03+02:00
[INFO] ----------------------------------------

> java -jar target/port-scanner.jar --host localhost --start-port 5170 --end-port 5180 --timeout 300

scanning ports...
Finished scanning ports!
Results 11

Port 5170 -> CLOSED
Port 5171 -> CLOSED
Port 5172 -> CLOSED
Port 5173 -> OPEN
Port 5174 -> CLOSED
Port 5175 -> CLOSED
Port 5176 -> CLOSED
Port 5177 -> CLOSED
Port 5178 -> CLOSED
Port 5179 -> CLOSED
Port 5180 -> CLOSED
```

## Resources

- [Port Scanning With Java](https://www.baeldung.com/java-port-scanning)
- [Using Executors.newVirtualThreadPerTaskExecutor() in Java](https://www.javacodegeeks.com/using-executors-newvirtualthreadpertaskexecutor-in-java.html)
- [sacn_port_java](https://github.com/yangganmu/sacn_port_java)
- [Port Scanner](https://github.com/naveen-98/Port-Scanner)
- [jports](https://github.com/mattwright324/jports)

## License

Please see the [LICENSE file](LICENSE).

## History

Started in September, 2026.
