package chat;

import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
	
	private static final int SERVER_PORT=5500;
	
	public static void main(String[] args) {
		
		ServerSocket serverSocket=null;
		List<Writer> listWriters = new ArrayList<Writer>();
		
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(InetAddress.getLocalHost(),SERVER_PORT));
			
			while(true){
				//멀티스레드 프로그래밍
				log( "[서버] 연결 기다림");
				Socket socket = serverSocket.accept();
				Thread cst = new ChatServerThread(socket,listWriters);
				cst.start();
			}		
			
		}catch (IOException e) {
			e.printStackTrace();
		}finally{
			try{
				//자원정리
				if(serverSocket != null && serverSocket.isClosed()==false)
					serverSocket.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		
		
	}

	public static void log(String string) {
		System.out.println(string);
	}

}
