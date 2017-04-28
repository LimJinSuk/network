package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class ChatClientThread extends Thread {
	private BufferedReader bufferedReader=null;

	public ChatClientThread(BufferedReader br){
		bufferedReader = br;
	}
	
	@Override
	public void run() {
		try {
			while(true){
				String response = bufferedReader.readLine();
				
				if(response==null){//서버가 소켓을 닫음
					ChatServer.log( "서버로부터 연결 끊김" );
					break;
				}
			
			} 
			
		}catch (IOException e) {
				
				e.printStackTrace();
		}
		
	}
	public void console(String message){
		System.out.println(message);
	}

}
