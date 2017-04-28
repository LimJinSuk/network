package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ChatServerThread extends Thread {
	
	private static final String PROTOCOL_DIVIDER = ":";
	private String nickname;
	private Socket socket;
	private List<Writer> listWriters;
	
	public ChatServerThread(Socket socket,List<Writer> listWriters){
		this.socket=socket;
		this.listWriters=listWriters;
	}

	@Override
	public void run() {

		BufferedReader bufferedReader = null;
		PrintWriter printWriter = null;

		try {
			InetSocketAddress inetSocketAddress = 
					(InetSocketAddress) socket.getRemoteSocketAddress();
		//호스트의 IP,PORT나눔
		int hostport = inetSocketAddress.getPort();
		String hostAddress = inetSocketAddress.getAddress().getHostAddress();
		
		System.out.println("[server]connected from "+hostAddress+" : "+hostport);
		
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			
			bufferedReader = new BufferedReader(new InputStreamReader(is,StandardCharsets.UTF_8));
			printWriter = new PrintWriter(new OutputStreamWriter(os,StandardCharsets.UTF_8),true);
			
			while(true){
				
				String request = bufferedReader.readLine();//blocking
				if(request==null){
					//클라이언트가 소켓을 닫음
					ChatServer.log( "클라이언트로 부터 연결 끊김" );
					doQuit(printWriter);
					break;
				}
				
				String[] tokens = request.split( ":" );
				
				if( "join".equals( tokens[0] ) )
				   doJoin( tokens[1], printWriter );
				
				else if( "message".equals( tokens[0] ) )
				   doMessage( tokens[1] );
				
				else if( "quit".equals( tokens[0] ) )
				   doQuit(printWriter);
				
				else 
				   ChatServer.log( "에러:알수 없는 요청(" + tokens[0] + ")" );
				
			}
			
			
		} catch(SocketException e){
		//클라이언트가 소켓을 정상적으로 닫지 않고 종료한 경우
			System.out.println("closed by client");
			doQuit( printWriter );
		}
		catch(IOException e) {
			e.printStackTrace();
		}finally{
			try{
				bufferedReader.close();
				printWriter.close();
				if( socket.isClosed() == false ){
				//데이터 통신용 닫기
					socket.close();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	
	//서버에 연결된 모든 클라이언트에 메시지를 보내는(브로드캐스트) 메소드
	private void broadcast(String data){
		//스레드간 공유 객체인  listWriters 에 접근 -동기화 필요
		synchronized( listWriters ) {
			for(int i=0; i<listWriters.size(); i++){
				PrintWriter printWriter = (PrintWriter) listWriters.get(i);
				printWriter.println(data);
				printWriter.flush();
			}
		   }
	}
	
	//doJoin은 한 사용자가 채팅 방에 참여 했을 때, 다른 사용자들에게  “OOO님이 입장하셨습니다.” 브로드캐스팅
  	private void doJoin( String nickName, Writer writer ) throws IOException {
		   this.nickname = nickName;
		   
		   String data = nickName + "님이 참여하였습니다."; 
		   broadcast( data );
				
		   /* writer pool에  저장 */
		   addWriter( writer );

		   // ack
		   //ack를 보내 방 참여가 성공했다는 것을 클라이언트에게 알려줌
		   ((PrintWriter) writer).println( "join:ok" );
		   writer.flush();
		}
	
	private void doMessage(String message){
		String data=nickname+":"+message;
		broadcast(data);
	}
	
	private void doQuit(  Writer writer ) {
		   removeWriter( writer );
				
		   String data = nickname + "님이 퇴장 하였습니다."; 
		   broadcast( data );
		}

	private void removeWriter( Writer writer ) {
		synchronized( listWriters ){
			listWriters.remove(writer);
		}
	}
		
	private void addWriter( Writer writer ) {
		//synchronized 키워드는  여러 스레드가  하나의 공유 객체에 접근할  때,동기화를 보장 해준다. 
		   synchronized( listWriters ){ 
		      listWriters.add( writer );
		   }
		}

}
