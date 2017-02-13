
This code serves as the foundation for three labs:
- a simple correlation exercise that can be accomplished using XML correlation
- a correlation exercise to be handled with the legacy SDK
- a correlation exercise that cannot be handled using XML correlation but can with the 4.3 iSDK (or the legacy SDK)

The app is simple: a sender sends a message over a socket.  A receiver picks up the message and prints to stdout.

There is only one BT, a POJO on com.appdynamics.javacert.level2.Sender, method sendOneMessage.  In the XML correlation
exercise you can configure the POJO in the UI; in the other SDK-based exercises the POJO should be configured in SDK code.
