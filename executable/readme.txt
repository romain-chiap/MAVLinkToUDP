MAVLinkToUDP readme - Romain Chiappinelli - March 2019

This software is a bridge between MAVLink and FlightGear UDP.
It is intended to be used with the SIH (for quad or for the vogi).

- To run it for quad, type in the terminal:
   java -jar MAVLinkToUDP.jar
- To run it for the vogi, type in
   java -jar MAVLinkToUDP.jar -vehicle vogi
- To display the help use the flag '-h'
- To sepecify the serial port path, use the flag '-serial [<path> <baud_rate>]'
     The MAVLink supports any baud rate.
     The standard USB MAVLink path are:
        - /dev/ttyACM0 on Linux;
        - /dev/tty.usbmodem1 on Mac OS;
        - COM15 on Windows.
