/*
 * Created on Oct 27, 2004
 */
package no.ntnu.fp.net.co;

import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import no.ntnu.fp.net.admin.Log;
import no.ntnu.fp.net.cl.ClException;
import no.ntnu.fp.net.cl.ClSocket;
import no.ntnu.fp.net.cl.KtnDatagram;
import no.ntnu.fp.net.cl.KtnDatagram.Flag;

/**
 * Implementation of the Connection-interface. <br>
 * <br>
 * This class implements the behaviour in the methods specified in the interface
 * {@link Connection} over the unreliable, connectionless network realised in
 * {@link ClSocket}. The base class, {@link AbstractConnection} implements some
 * of the functionality, leaving message passing and error handling to this
 * implementation.
 * 
 * @author Sebj�rn Birkeland and Stein Jakob Nordb�
 * @see no.ntnu.fp.net.co.Connection
 * @see no.ntnu.fp.net.cl.ClSocket
 */
public class ConnectionImpl extends AbstractConnection {

    /** Keeps track of the used ports for each server port. */
    private static Map<Integer, Boolean> usedPorts = Collections.synchronizedMap(new HashMap<Integer, Boolean>());

    /**
     * Initialise initial sequence number and setup state machine.
     * 
     * @param myPort
     *            - the local port to associate with this connection
     */
    public ConnectionImpl(int myPort) {
    	// TODO check if port is already used, and add to map
    	if(usedPorts.containsKey(myPort) && usedPorts.get(myPort)){
    		//port already taken, throw exception/return?
    		System.out.println("Port taken!!!");
    	} // TODO find a way to free the port afterwards
    	else usedPorts.put(myPort, true);
		this.myPort=myPort;
		myAddress="127.0.0.1"; //or public ip?
		remoteAddress="127.0.0.1";
		remotePort=5555; //TODO should not be assigned here, for debugging only
		nextSequenceNo=(int)(Math.random()*50); // TODO replace 50 with something meaningful
		System.out.println("Connection instantiated");
    }

    private String getIPv4Address() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException e) {
            return "127.0.0.1";
        }
    }

    /**
     * Establish a connection to a remote location.
     * 
     * @param remoteAddress
     *            - the remote IP-address to connect to
     * @param remotePort
     *            - the remote portnumber to connect to
     * @throws IOException
     *             If there's an I/O error.
     * @throws java.net.SocketTimeoutException
     *             If timeout expires before connection is completed.
     * @see Connection#connect(InetAddress, int)
     */
    public void connect(InetAddress remoteAddress, int remotePort) throws IOException,
            SocketTimeoutException {
        //throw new NotImplementedException();
    }

    /**
     * Listen for, and accept, incoming connections.
     * 
     * @return A new ConnectionImpl-object representing the new connection.
     * @see Connection#accept()
     */
    public Connection accept() throws IOException, SocketTimeoutException {
    	
    	state=State.LISTEN;
    	
    	//receive internal packets until we get a syn
    	KtnDatagram synPacket;
        do{
        	synPacket=receivePacket(true);
        }
        while(synPacket==null || synPacket.getFlag()!=KtnDatagram.Flag.SYN);
        
        //TODO: check if packet is valid
        
        //send synack
        sendAck(synPacket, true);
        
        KtnDatagram datagram=receivePacket(true);
        if(datagram.getAck()== synPacket.getAck()){
        	//TODO check if the received ackpacket is acks the synpacket, do-while?
        }
        
        
        //create a new connection with correct attributes
        ConnectionImpl connection = new ConnectionImpl(myPort); //port collisions?
        connection.remoteAddress=synPacket.getSrc_addr();
        connection.remotePort=synPacket.getSrc_port();
        connection.myAddress=myAddress;
        connection.state=State.ESTABLISHED;
        
        state=State.CLOSED;
        
    	return connection;
    }

    /**
     * Send a message from the application.
     * 
     * @param msg
     *            - the String to be sent.
     * @throws ConnectException
     *             If no connection exists.
     * @throws IOException
     *             If no ACK was received.
     * @see AbstractConnection#sendDataPacketWithRetransmit(KtnDatagram)
     * @see no.ntnu.fp.net.co.Connection#send(String)
     */
    public void send(String msg) throws ConnectException, IOException {
        //throw new NotImplementedException();
    }

    /**
     * Wait for incoming data.
     * 
     * @return The received data's payload as a String.
     * @see Connection#receive()
     * @see AbstractConnection#receivePacket(boolean)
     * @see AbstractConnection#sendAck(KtnDatagram, boolean)
     */
    public String receive() throws ConnectException, IOException {
        //throw new NotImplementedException();
    	return null;
    }

    /**
     * Close the connection.
     * 
     * @see Connection#close()
     */
    public void close() throws IOException {
    	//send close message to other end
    	state=State.CLOSED;
        usedPorts.put(myPort, false);
    }

    /**
     * Test a packet for transmission errors. This function should only called
     * with data or ACK packets in the ESTABLISHED state.
     * 
     * @param packet
     *            Packet to test.
     * @return true if packet is free of errors, false otherwise.
     */
    protected boolean isValid(KtnDatagram packet) {
        // TODO implement
    	return true;
    }
}
