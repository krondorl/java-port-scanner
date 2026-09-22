/*!
 * Java Port Scanner
 *
 * Copyright (c) 2026- Adam Burucs
 *
 * MIT Licensed
 */

package io.github.krondorl;

import java.util.List;

/**
 * Demonstrates scanning a small range of local TCP ports.
 */
public class Main {
    /**
     * Runs the port-scanner demonstration.
     *
     * @param args command-line arguments, which are currently ignored
     */
    public static void main(String[] args) {
        System.out.println();
        System.out.println("Java Port Scanner");
        System.out.println();
        System.out.println("scanning ports...");

        PortScanner portScanner = new PortScanner(250);
        List<PortScanner.ScanResult> scanResult = portScanner.scan("localhost", 5170, 5180);

        System.out.println("Finished scanning ports!");
        System.out.println("Results " + scanResult.size());
        System.out.println();

        for (PortScanner.ScanResult result : scanResult) {
            System.out.println(
                    "Port " + result.port() + " -> " + result.status()
            );
        }
    }
}
