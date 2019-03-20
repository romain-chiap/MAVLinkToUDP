package coriolis;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientUDP {
	
    private DatagramSocket socket;
    private InetAddress address;
	private int portUDP; 	

    public ClientUDP(String addressIP, int portUDP) throws Exception {

    		socket = new DatagramSocket();
    		this.portUDP=portUDP;
        	address = InetAddress.getByName(addressIP);
        	// address = InetAddress.getByName("127.0.0.1");
    }
    
    public void send(byte [] buffer) throws Exception {

        	DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, portUDP);
        	socket.send(packet);	        
    }
 
    public void close() {
        socket.close();
    }
}