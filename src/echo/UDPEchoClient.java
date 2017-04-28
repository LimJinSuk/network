package echo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Scanner;

public class UDPEchoClient {
	
	private static final String SERVER_IP="192.168.1.7";
	private static final int SERVER_PORT=6060;
	
	public static void main(String[] args) {
		DatagramSocket socket = null;
		Scanner scanner = new Scanner(System.in);
		
		try {
			//1.소켓 생성
			socket = new DatagramSocket();
			
			while(true){
				System.out.print(">>");
				String message = scanner.nextLine();
				
				if("exit".equals(message)==true)
					break;
				
				//2.전송 패킷 생성
				byte[] sendData = message.getBytes("utf-8");
				DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length,new InetSocketAddress(SERVER_IP,SERVER_PORT));
				
				//3.전송
				socket.send(sendPacket);
				
				//4.수신
				DatagramPacket receivePacket = new DatagramPacket(new byte[1024],1024);
				socket.receive(receivePacket);//blocking
				
				//5.화면 출력
				message = new String(receivePacket.getData(),0,receivePacket.getLength(),"utf-8");
				System.out.println("<<"+message);
			}
			
			
		} catch (SocketException e) {
			e.printStackTrace();
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(socket != null && socket.isClosed()==false)
				socket.close();
		}
		
		scanner.close();
		
	}

}
