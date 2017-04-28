package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
	
	private static final String SERVER_IP="192.168.1.7";
	private static final int SERVER_PORT=5500;
	
	public static void main(String[] args) {
		Scanner scanner = null;
		Socket socket = null;
		
		try {
			scanner = new Scanner(System.in);
			socket = new Socket();
			
			//2.서버 연결
			socket.connect(new InetSocketAddress(SERVER_IP,SERVER_PORT));
			
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"),true);	
			
			System.out.print("닉네임 >> ");
			String nickname = scanner.nextLine();
			pw.println("join:"+nickname);
			pw.flush();
			//br.readLine();
			
			new ChatClientThread(br).start();
			
			while(true){
				System.out.print(">>");
				String input = scanner.nextLine();
				
				if("quit".equals(input)==true){
					pw.println("quit: ");
					break;
				}
				
				else
					pw.println("message:"+input);
				
				/*
				//에코 받기
				String echoMessage = br.readLine();
				if(echoMessage==null){//서버가 닫은경우
					System.out.println("[client]disconnected by server");
					break;
				}
				//출력
				System.out.println("<<"+echoMessage);
				*/
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
