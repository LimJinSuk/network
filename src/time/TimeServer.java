package time;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeServer {
	private static final int PORT=7070;
	
	public static void main(String[] args) {
		DatagramSocket datagramSocket = null;
		try {
			//1.소켓 생성
			datagramSocket = new DatagramSocket(PORT);
			while( true ) {
				//2.데이터 수신 패킷 생성
				DatagramPacket receivePacket = new DatagramPacket(new byte[1024],1024);
				
				//3.데이터 수신 대기
				//receive():호출 후 패킷이 수신될 때까지 대기 하게 함
				datagramSocket.receive(receivePacket);//blocking
				
				//시간 전송
				SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss a" );
				//String 클래스의 format메서드 : 지정된 위치에 값을 대입해서 문자열을 만들어 내는 용도
				String data = format.format( new Date() );
				
				byte[] sendData = data.getBytes("utf-8");
				
				DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length,receivePacket.getSocketAddress());//받는 것의 소켓주소
				datagramSocket.send(sendPacket);
			}
			
		}catch (SocketException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(datagramSocket != null &&datagramSocket.isClosed()==false)
				datagramSocket.close();
		}
	}

}
