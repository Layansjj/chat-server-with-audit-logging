### Secure Multi-Client Chat Server (Java)

This project implements a concurrent client-server chat system using Java sockets.  
The system was extended with security-focused features including audit logging, command monitoring, and operational controls.

Beyond functionality, I treated this project as a small security governed system, with documented risks, threat modeling, and implemented controls.

 ### Key Features
- Multi-client concurrent connections using threads
- Centralized broadcast messaging
- Command handling (/users, /log, /alert, /quit)
- Persistent server logging
- Security audit logging for user actions and system events

### Security Focus
This project emphasizes:
- Accountability and traceability
- Abuse monitoring
- Operational visibility
- Secure system behavior under concurrent use

Security documentation can be found in the `docs/` folder.

## How Audit Logs Are Generated

The system implements basic audit logging to track security-relevant actions during runtime.

Audit logs are generated on the server side using the `AuditLogger` class. Each log entry records:
- the event type (e.g., message sent, command issued, client connection)
- the actor (username or system component)
- the action performed
- whether the action was allowed or successful
- optional metadata such as message length or client address

Logs are written to a persistent file using synchronized file access to ensure consistency when multiple clients are connected concurrently.

This mechanism provides basic accountability and visibility into system behavior, which can be useful for monitoring misuse, debugging, or security review.


Documentation finalized.
