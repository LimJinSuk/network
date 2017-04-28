package http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;

public class RequestHandler extends Thread {
	private Socket socket;
	
	public RequestHandler( Socket socket ) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			// get IOStream
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
			OutputStream os = socket.getOutputStream();

			// logging Remote Host IP Address & Port
			InetSocketAddress inetSocketAddress = ( InetSocketAddress )socket.getRemoteSocketAddress();
			consoleLog( "connected from " + inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort() );
			
			String request = null;
			while(true){
				String line = br.readLine();
			
				//클라이언트가 끊거나 헤더부분넘어가면 break
				if(line == null || "".equals(line))
					break;
				//헤더 첫번째 부분만 걸러낼게여ㅎㅎ
				if(request==null)
					request=line;
			}
			consoleLog(request);//헤더 첫번재 라인
			
			//요청 분석
			//명령어,URL,프로토콜 버전 나누기
			String[] tokens = request.split(" ");
			
			if("GET".equals(tokens[0]))//get
				responseStaticResource(os,tokens[1],tokens[2]);
			else//post,delete,put
				//심플 웹 서버에서는 잘못된 요청(bad request 400)으로 처리
				response400Error(os,tokens[2]);
				
			// 예제 응답입니다.
			// 서버 시작과 테스트를 마친 후, 주석 처리 합니다.
			//binary,이미지 등 여러가지 보낼 수 있음
			//os.write( "HTTP/1.1 200 OK\r\n".getBytes( "UTF-8" ) );//헤더 : 뒤에항상 개행문자
			//os.write( "Content-Type:text/html; charset=utf-8\r\n".getBytes( "UTF-8" ) );
			//os.write( "\r\n".getBytes() );//내용없는 개행 뒤로 Body
			//os.write( "<h1>이 페이지가 잘 보이면 실습과제 SimpleHttpServer를 시작할 준비가 된 것입니다.</h1>".getBytes( "UTF-8" ) );

		} catch( Exception ex ) {
			consoleLog( "error:" + ex );
		} finally {
			// clean-up
			try{
				if( socket != null && socket.isClosed() == false ) {
					socket.close();
				}
			} catch( IOException ex ) {
				consoleLog( "error:" + ex );
			}
		}			
	}
		// url에 해당하는 파일을 읽음
		// HTTP/1.0 200OK
		// Content-Type:text/html; charset=utf-8
		//
		// 응답
	public void responseStaticResource(OutputStream os,String url,String protocol) throws IOException{
		if("/".equals(url))
			url="/index.html";//welcome file 처리
		
		// 현재 디렉토리밑에 문서(./webapp) document root
		File file = new File("./webapp"+url);
		
		if(file.exists()==false){
			//404 : File Not Found response
			response404Error(os,protocol);//응답하기위한 아웃풋스트림,프로토콜
			return;
		}
		//통째로 읽어들임
		byte[] body = Files.readAllBytes(file.toPath());
		//CSS 반영
		String mimeType = Files.probeContentType(file.toPath());
		
		//헤더 응답 전송
		os.write((protocol+" 200 OK\r\n").getBytes("utf-8"));
		os.write(("Content-Type:"+mimeType+"; carset=utf-8\r\n").getBytes("utf-8"));
		os.write("\r\n".getBytes("utf-8"));
		
		//바디 응답 전송
		os.write(body);
		
	}
	
	public void response404Error(OutputStream os,String protocol) throws IOException{
		File file = new File("./webapp/error/404.html");
		byte[] body = Files.readAllBytes(file.toPath());
		
		os.write((protocol+"404 File Not Found\r\n").getBytes("utf-8"));
		os.write("Content-Type:text.html; charset=utf-8\r\n".getBytes("utf-8"));
		os.write("\r\n".getBytes("utf-8"));
		os.write(body);
		
	}
	
	public void response400Error(OutputStream os,String protocol) throws IOException{
		File file = new File("./webapp/error/400.html");
		byte[] body = Files.readAllBytes(file.toPath());
		
		os.write((protocol+"400 File Not Found\r\n").getBytes("utf-8"));
		os.write("Content-Type:text.html; charset=utf-8\r\n".getBytes("utf-8"));
		os.write("\r\n".getBytes("utf-8"));
		os.write(body);
	}
	
	public void consoleLog( String message ) {
		System.out.println( "[RequestHandler#" + getId() + "] " + message );
	}
}