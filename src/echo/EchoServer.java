package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class EchoServer {
	private static final int SERVER_PORT=6060;
	
	public static void main(String[] args) {

		//finally로 소켓 닫아줄거야
		ServerSocket serverSocket=null;
		try {
			//1.서버 소켓 생성
			serverSocket = new ServerSocket();
			
			//2.바인딩 (만들어진 서버소켓에 주소를 binding)
			InetAddress inetAddress = InetAddress.getLocalHost();
			//서버 IP를 얻어옴
			String localhostAddress = inetAddress.getHostAddress();
			serverSocket.bind( new InetSocketAddress(localhostAddress, SERVER_PORT ) );
			System.out.println("[server"+Thread.currentThread().getId()+"]binding"+localhostAddress+":"+SERVER_PORT);
			
			//3. accept(연결 요청을 기다림)
			while( true ) {
				Socket socket = serverSocket.accept();   // blocking
				new EchoServerReceiveThread( socket ).start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				//자원정리
				if( serverSocket != null && serverSocket.isClosed() == false ){
					serverSocket.close();
				}
			} catch( IOException e ) {
				e.printStackTrace();
			}
		}		
	}
}