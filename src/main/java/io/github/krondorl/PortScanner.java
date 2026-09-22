/*!
 * Java Port Scanner
 *
 * Copyright (c) 2026- Adam Burucs
 *
 * MIT Licensed
 */

package io.github.krondorl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Scans an inclusive range of TCP ports on a host.
 */
public final class PortScanner {
    private final int timeoutMillis;
    private final AtomicBoolean cancelled = new AtomicBoolean(false);

    /**
     * Describes the result of scanning one TCP port.
     *
     * @param port the scanned port number
     * @param status whether the port accepted a TCP connection
     */
    public record ScanResult(
            int port,
            Status status
    ) {
        /**
         * The possible outcomes of a port scan.
         */
        public enum Status {
            /** The port accepted a TCP connection. */
            OPEN,
            /** The port did not accept a TCP connection. */
            CLOSED
        }
    }

    /**
     * Creates a scanner with the given connection timeout.
     *
     * @param timeoutMillis maximum time to wait for each connection, in milliseconds
     * @throws IllegalArgumentException if {@code timeoutMillis} is not positive
     */
    public PortScanner(int timeoutMillis) {
        if (timeoutMillis <= 0) {
            throw new IllegalArgumentException(
                    "Timeout must be greater than 0"
            );
        }

        this.timeoutMillis = timeoutMillis;
    }

    /**
     * Scans every TCP port in the given inclusive range.
     *
     * @param host the host name or IP address to scan
     * @param startPort the first port to scan, from 1 through 65535
     * @param endPort the last port to scan, from 1 through 65535
     * @return scan results in ascending port order, unless the scan is interrupted
     * @throws IllegalArgumentException if the host is blank or the port range is invalid
     */
    public List<ScanResult> scan(
            String host,
            int startPort,
            int endPort
    ) {
        cancelled.set(false);

        validateHost(host);
        validatePortRange(startPort, endPort);

        List<ScanResult> results = new ArrayList<>();

        try (var executor =
                     Executors.newVirtualThreadPerTaskExecutor()) {

            Thread shutdownHook = new Thread(() -> {
                System.out.println();
                System.out.println("Stopping scan...");

                cancelled.set(true);
                executor.shutdownNow();
            });

            Runtime.getRuntime().addShutdownHook(shutdownHook);

            try {
                List<Future<ScanResult>> futures = new ArrayList<>();

                for (int port = startPort; port <= endPort; port++) {
                    if (cancelled.get()) {
                        break;
                    }

                    int currentPort = port;

                    futures.add(
                            executor.submit(
                                    () -> scanPort(host, currentPort)
                            )
                    );
                }

                for (Future<ScanResult> future : futures) {
                    if (cancelled.get()) {
                        break;
                    }

                    try {
                        ScanResult result = future.get();

                        if (result != null) {
                            results.add(result);
                        }

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        cancelled.set(true);
                        break;

                    } catch (CancellationException e) {
                        break;

                    } catch (ExecutionException e) {
                        throw new RuntimeException(
                                "Port scan task failed",
                                e.getCause()
                        );
                    }
                }

            } finally {
                try {
                    Runtime.getRuntime()
                            .removeShutdownHook(shutdownHook);

                } catch (IllegalStateException ignored) {
                    // JVM is already shutting down
                }
            }
        }

        return results;
    }

    private ScanResult scanPort(String host, int port) {
        if (cancelled.get()) {
            return null;
        }

        try {
            InetAddress[] addresses =
                    InetAddress.getAllByName(host);

            for (InetAddress address : addresses) {
                if (cancelled.get()) {
                    return null;
                }

                try (Socket socket = new Socket()) {
                    InetSocketAddress socketAddress =
                            new InetSocketAddress(address, port);

                    socket.connect(
                            socketAddress,
                            timeoutMillis
                    );

                    return new ScanResult(
                            port,
                            ScanResult.Status.OPEN
                    );

                } catch (IOException ignored) {
                    // Try the next resolved IP address.
                }
            }

            return new ScanResult(
                    port,
                    ScanResult.Status.CLOSED
            );

        } catch (IOException e) {
            return new ScanResult(
                    port,
                    ScanResult.Status.CLOSED
            );
        }
    }

    private void validateHost(String host) {
        if (host == null || host.isBlank()) {
            throw new IllegalArgumentException(
                    "Host must not be blank"
            );
        }
    }

    private void validatePortRange(
            int startPort,
            int endPort
    ) {
        if (startPort < 1 || startPort > 65535) {
            throw new IllegalArgumentException(
                    "Start port must be between 1 and 65535"
            );
        }

        if (endPort < 1 || endPort > 65535) {
            throw new IllegalArgumentException(
                    "End port must be between 1 and 65535"
            );
        }

        if (startPort > endPort) {
            throw new IllegalArgumentException(
                    "Start port must not be greater than end port"
            );
        }
    }
}
