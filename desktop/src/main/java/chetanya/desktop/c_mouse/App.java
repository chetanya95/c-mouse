package chetanya.desktop.c_mouse;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.json.JSONObject;

/**
 * Hello world!
 *
 */
public class App {

    private static final int SERVER_PORT = 2802;
    private static final int MAX_PACKET_SIZE = 2048;
    private static final int TIMEOUT = 2000; // milliseonds
    private static final String DISCOVERY_REQUEST = "Where are you C-Mouse Server?";
    private static final String DISCOVERY_REPLY = "I am here C-Mouse Client.";
    
    private static int resolutionX;
    private static int resolutionY;

    private static DatagramSocket socket;

	
	public static void main(String[] args) {
		
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        resolutionX = (int) screenSize.getWidth();
        resolutionY = (int) screenSize.getHeight();

        System.out.println("Screen Width: " + resolutionX);
        System.out.println("Screen Height: " + resolutionY);
        
		try {
			// 0.0.0.0 means all IPv4 address on this machine.
			InetAddress addr = InetAddress.getByName( "0.0.0.0" );
//			InetAddress addr = NetworkUtil.getMyAddress();
			socket = new DatagramSocket(SERVER_PORT, addr);
			// set flag to enable receipt of broadcast packets
			//socket.setBroadcast(true);
		} catch (Exception ex) {
			String msg = "Could not create UDP socket on port " + SERVER_PORT;
			System.err.println(msg);
			ex.printStackTrace();
			return;
		}
			
		System.out.printf("Server listening on port %d\n", SERVER_PORT);
			
		while (true) {
			byte[] receiveBuffer = new byte[MAX_PACKET_SIZE];
			DatagramPacket packet = new DatagramPacket(receiveBuffer, receiveBuffer.length);
			try {
				// wait for a packet
				socket.receive(packet);
			} catch (IOException ioe) {
				System.err.println("IOException: " + ioe.getMessage());
				ioe.printStackTrace();
				continue;
			}

			InetAddress clientAddress = packet.getAddress();
			int clientPort = packet.getPort();
	
			String message = new String(packet.getData()).trim();
			if (message.contains(DISCOVERY_REQUEST)) {
				System.out.println("Received data: " + new String(packet.getData()) );
				System.out.println(String.format("Packet received from %s:%d", clientAddress.getHostAddress(), clientPort) );
				sendIP(clientAddress, clientPort);
			}
			else {
				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(message);
				}catch (Exception e){
					System.err.println("Error in converting to JSONObject: "+ e.getMessage());
					e.printStackTrace();
					continue;
				}
				if(jsonObject.has("movement")) {
					movePointer(jsonObject);
				}else if(jsonObject.has("touch")) {
					System.out.println("Received data: " + new String(packet.getData()) );
					performTouchOperation(jsonObject);
				}
				else {
					System.err.println(String.format("Packet from %s:%d not a valid packet",
							clientAddress.getHostAddress(), clientPort) );
					continue;
				}
			}
		}
	}
	
	public static boolean sendIP(InetAddress clientAddress, int clientPort) {
		String myIP = NetworkUtil.getMyAddress().getHostAddress();
		
		String reply =  DISCOVERY_REPLY + " My IP Address: "+ myIP;
		System.out.println("Reply data: " + reply);
		byte[] sendData = reply.getBytes();

		DatagramPacket sendPacket = new DatagramPacket(sendData,
				sendData.length, clientAddress, clientPort);
		try {
			socket.send(sendPacket);
			System.out.println(String.format("Reply sent to %s:%d",
					clientAddress.getHostAddress(), clientPort) );
		} catch(IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
			ioe.printStackTrace();
			return false;
		}
		return true;
		
	}
	
	public static boolean movePointer(JSONObject jsonObject) {
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			System.err.println("AWTException: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		Point point = MouseInfo.getPointerInfo().getLocation();
		
		int currentX = (int) point.getX();
		int currentY = (int) point.getY();
		
		int movementX;
		int movementY;
		try {
			movementX = jsonObject.getJSONObject("movement").getInt("x");
			movementY = jsonObject.getJSONObject("movement").getInt("y");
		}catch (Exception e){
			System.err.println("Error in parsing movement data: "+ e.getMessage());
			e.printStackTrace();
			return false;
		}
		
		int newX = currentX + movementX;
		int newY = currentY + movementY;
		
		if(newX >= resolutionX){
			newX = resolutionX;
			
		}
		if(newY >= resolutionY){
			newY = resolutionY;
		}
		
		robot.mouseMove(newX, newY);
		return true;
	}
	
	public static boolean performTouchOperation(JSONObject jsonObject) {
		Robot robot;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			System.err.println("AWTException: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		String touchData = jsonObject.getJSONObject("touch").getString("data");
		if(touchData.equals("Right Click")) {
			robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
			robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
		}else if(touchData.equals("Left Click")){
			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		}else {
			System.err.println("Invalid touchData");
			return false;
		}
		return true;
	}

}
