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

Documentation finalized.
