/*!
 * Java Port Scanner
 *
 * Copyright (c) 2026- Adam Burucs
 *
 * MIT Licensed
 */

package io.github.krondorl;

import java.util.List;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
        name = "port-scanner",
        version = "0.1.0",
        description = "Scans TCP ports on a target host.",
        mixinStandardHelpOptions = true
)
public class Main implements Runnable {
    @Option(
            names = {"-H", "--host"},
            description = "Host to scan.",
            required = true
    )
    private String host;

    @Option(
            names = {"-s", "--start-port"},
            description = "First port to scan.",
            defaultValue = "1"
    )
    private int startPort;

    @Option(
            names = {"-e", "--end-port"},
            description = "Last port to scan.",
            defaultValue = "65535"
    )
    private int endPort;

    @Option(
            names = {"-t", "--timeout"},
            description = "Last port to scan.",
            defaultValue = "250"
    )
    private int timeout;

    /**
     * Runs the port-scanner demonstration.
     *
     * @param args command-line arguments, which are currently ignored
     */
    @Override
    public void run() {
        System.out.println();
        System.out.println("Java Port Scanner");
        System.out.println();
        System.out.println("scanning ports...");

        PortScanner portScanner = new PortScanner(timeout);
        List<PortScanner.ScanResult> scanResult = portScanner.scan(host, startPort, endPort);

        System.out.println("Finished scanning ports!");
        System.out.println("Results " + scanResult.size());
        System.out.println();

        for (PortScanner.ScanResult result : scanResult) {
            System.out.println(
                    "Port " + result.port() + " -> " + result.status()
            );
        }
    }

    static void main(String[] args) {
        int exitCode = new CommandLine(new Main())
                .execute(args);

        System.exit(exitCode);
    }
}
