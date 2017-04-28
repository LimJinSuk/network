package echo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPEchoServer {
	//TCP는 또열면 안되는 데 UDP는 괜춘
	private static final int PORT=6060;
	
	public static void main(String[] args) {
		DatagramSocket datagramSocket = null;
		try {
			//1.소켓 생성
			datagramSocket = new DatagramSocket(PORT);
			
			//2.데이터 수신 패킷 생성
			DatagramPacket receivePacket = new DatagramPacket(new byte[1024],1024);
			
			//3.데이터 수신 대기
			datagramSocket.receive(receivePacket);//blocking
			
			//4.수신(data를 뽑아내어 0부터 사이즈만큼 한글로 인코딩)
			String message = new String(receivePacket.getData(),0,receivePacket.getLength(),"utf-8");
			System.out.println("[UDP Echo Server] received : "+message);
			
			//5.에코잉 리턴
			byte[] sendData = message.getBytes("utf-8");
			DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length,receivePacket.getSocketAddress());//받는 것의 소켓주소
			datagramSocket.send(sendPacket);
			
			
		} catch (SocketException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(datagramSocket != null &&datagramSocket.isClosed()==false)
				datagramSocket.close();
		}
		
	}

}
