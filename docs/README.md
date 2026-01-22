
# Secure Multi-Client Chat Server (Java)

This project implements a concurrent client–server chat system using Java sockets.
Multiple clients can connect to a single server and exchange messages in real time.

In addition to basic functionality, I approached this project from a security perspective.
During development, I focused on visibility, accountability, and understanding how the system behaves when multiple users interact concurrently.

To support this, the system was extended with audit logging, command monitoring, and basic operational controls.
Security-related documentation (risk analysis, threat modeling, and controls) is included in the `docs/` folder.


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


This documentation reflects the final state of the system at submission time.

