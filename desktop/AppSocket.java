import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.regex.*;

class AppSocket{ 
	
	public static void main(String[] args){
		
		int portNumber = 8002;
		
		try ( 
			DatagramSocket datagramSocket = new DatagramSocket(portNumber);
		){

			byte[] buffer = new byte[25];
			
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			
			String input;
			
			Robot robot = new Robot();
			Point pt;
			
			int x;
			int y;
			
			int changeX;
			int changeY;
			
			/*
			double accelX;
			double accelY;
			double velX=0.0;
			double velY=0.0;
			*/
			
			while(true){
				
				datagramSocket.receive(packet);
			
				input = new String(packet.getData(), "UTF-8");

				System.out.println(input);
				
				if(input.indexOf("Click") != -1){
					if(input.indexOf("Right") != -1){
						robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
						robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
					}else if(input.indexOf("Left") != -1){
						robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
						robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
					}
					continue;
				}
				
				
				String tempX = "";
				String tempY = "";
				
				int indexX = input.indexOf("x:");
				int indexY = input.indexOf("y:");
				int indexEnd = input.indexOf("end");
				
				if(indexX != -1 && indexY != -1 && indexEnd != -1 ){
					tempX = input.substring(indexX + 2, indexY - 1);

					/*
					Pattern pattern = Pattern.compile("(^\\-?\\d+)");
					Matcher matcher = pattern.matcher(input);
					if (matcher.find())
					{
						tempX  = matcher.group(1);
					}
					*/
					
					tempY = input.substring(input.indexOf("y:") + 2, input.indexOf("end"));
									
					System.out.println("tempX: " + tempX + " tempY: " + tempY + "...");
					
					changeX = Integer.parseInt(tempX);
					changeY = Integer.parseInt(tempY);
					
					/*
					accelX = Double.parseDouble(st.nextToken());
					accelY = Double.parseDouble(st.nextToken());

					velX += accelX;
					velY += accelY;
					*/
					
					
					pt = MouseInfo.getPointerInfo().getLocation();
					
					x = (int) pt.getX();
					y = (int) pt.getY();
					
					//System.out.println("current: x" + x + " y:" + y);
					
					
					int newX = x + changeX;
					int newY = y - changeY;
					
					if(newX < 1365){
						x = newX;
						
					}
					if(newY < 767){
						y = newY;
					}
					
					//System.out.println("change: x" + x + " y:" + y);
					
					robot.mouseMove(x, y);
				}
			}
		} catch(AWTException e){
			System.out.println("Got Mouse AWT Exception: " + e.getMessage());
		}catch(SocketException e){
			System.out.println("Got SocketException: " + e.getMessage());
		}catch(UnsupportedEncodingException e){
			System.out.println("Got UnsupportedEncodingException: " + e.getMessage());
		}catch(IOException e){
			System.out.println("Got IOException for datagramSocket.recieve(): " + e.getMessage());
		}
	}
}