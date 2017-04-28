package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class EchoServerReceiveThread extends Thread {
	private Socket socket;
	
	public EchoServerReceiveThread(Socket socket){
		this.socket=socket;
	}
	
	@Override
	public void run() {
		InetSocketAddress remoteAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
		int remoteHostPort = remoteAddress.getPort();
		//inetAddress로 호스트ip뽑아옴
		String remoteHostAddress = remoteAddress.getAddress().getHostAddress();
		System.out.println("[server]connected from "+remoteHostAddress+" : "+remoteHostPort);
			
		try{
			//5.IOStream 받아오기
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"),true);
			
			while(true){
				String message = br.readLine();//blocking
				if(message==null){
					//클라이언트가 소켓을 닫음
					consoleLog("disconnected by client");
					break;
				}
				consoleLog("received : "+message);
				
				//데이터쓰기
				pw.println(message);
			}
			
		}catch(SocketException e){
		//클라이언트가 소켓을 정상적으로 닫지 않고 종료한 경우
			consoleLog("closed by client");
		}
		catch(IOException e) {
			e.printStackTrace();
		}finally{
			try{
				if(socket != null && socket.isClosed()==false){
				//데이터 통신용 닫기
				socket.close();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	
	}
	private void consoleLog(String message){
		System.out.println("[server:"+getId()+"]"+message);
	}
	
}
