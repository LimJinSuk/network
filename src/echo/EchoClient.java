package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

import org.omg.CORBA.portable.OutputStream;

public class EchoClient {

	private static final String SERVER_IP="192.168.1.7";
	private static final int SERVER_PORT=6060;
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Socket socket = null;
		
		try {
			//1.소켓 생성
			socket = new Socket();
			
			//2.서버 연결
			socket.connect(new InetSocketAddress(SERVER_IP,SERVER_PORT));
			
			//3.IOStream 받아오기
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"),true);	
			
			while(true){
				System.out.println(">>");
				String message = scanner.nextLine();
				if("exit".equals(message))
					break;
				
				//메세지 보내기
				pw.println(message);
				
				//에코 받기
				String echoMessage = br.readLine();
				if(echoMessage==null){//서버가 닫은경우
					System.out.println("[client]disconnected by server");
					break;
				}
				//출력
				System.out.println("<<"+echoMessage);
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try{
				if(socket!=null && socket.isClosed()==false)
					socket.close();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			scanner.close();
		}
		
		
	
	}

}
