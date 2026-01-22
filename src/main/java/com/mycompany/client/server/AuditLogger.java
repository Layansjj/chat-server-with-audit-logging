
package com.mycompany.client.server;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

public class AuditLogger {
    private final String path;

    public AuditLogger(String path) {
        this.path = path;
    }

    public synchronized void log(String event, String actor, String action, boolean allowed, String meta) {
        String line = String.format(
            "%s event=%s actor=%s action=%s allowed=%s meta=%s%n",
            Instant.now().toString(),
            safe(event), safe(actor), safe(action), allowed, safe(meta)
        );
        try (FileWriter fw = new FileWriter(path, true)) {
            fw.write(line);
        } catch (IOException ignored) { }
    }

    private String safe(String s) {
        if (s == null) return "";
        return s.replace("\n", " ").replace("\r", " ").trim();
    }
}
