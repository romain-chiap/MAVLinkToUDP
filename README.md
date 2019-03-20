# MAVLinkToUDP
MAVLink to UDP bridge to display PX4 Simulation-In-Hardware (SIH) into FlightGear.
Written in Java using [jMAVlib](https://github.com/PX4/jMAVlib) for the [MAVLink](https://mavlink.io/en/) interface.

Supports the following vehicles:
- Quadrotor X (using SIH QuadX)
- Vogi, the [vogi](http://www.vogi-vtol.com/) is a pitch decoupled VTOL aircraft.

### Using this software
A user willing to use this software can download MAVLinkToUDP.jar and common.xml from the [executable folder](https://github.com/romain-chiap/MAVLinkToUDP/tree/master/executable).
The jar file can be executed by typing `java -jar MAVLinkToUDP.jar` and the flag `-h` can be used to display the help messages.

### About the implementation
The PX4 SIH periodically sends the MAVLink message [HIL_STATE_QUATERNION](https://github.com/romain-chiap/MAVLinkToUDP/blob/master/MAVLinkToUDP/jMAVlib/common.xml#L3651). 
This program reads it when avalable and sends the position and orientation of the vehicle through UDP compatible with FlightGear. 
For the UDP message definition, see [net_fdm.hxx](https://github.com/romain-chiap/MAVLinkToUDP/blob/master/MAVLinkToUDP/lib/net_fdm.hxx)
(The UDP message must be 408 Bytes in length).

### Command-line options:
```
-h
		Display help
-d
      Display debug messages during execution
-udp [<ip_address> <udp_port>]
		Specifies the ip address <ip_address> and the udp port <udp_port> for the udp connection to FlightGear.
		If not specified, the default IP address is 127.0.0.1 and the default UDP port is 5502.
-serial [<path> <baud_rate>]
		Specifies the serial port <path> and the baud rate <baud_rate> for the MAVLink connection.
		If not specified, the default port path is /dev/ttyACM0 and the default baud rate is 230400.
		The MAVLink supports any baud rate.
		The standard USB MAVLink path are:
   		- /dev/ttyACM0 on Linux;
   		- /dev/tty.usbmodem1 on Mac OS;
   		- COM15 on Windows.
-vehicle {<quad> or <vogi>}
		Specifies the vehicle setup, it can be either quad or vogi.
		If not specified, the default vehicle is a quad.
```
