package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

//서버 생성
public class TCPServer {
	//포트 고정 설정,IP는 직접 구하는게 좋음
	private static final int SERVER_PORT=5050;
	
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
			InetSocketAddress inetSocketAddress = 
					new InetSocketAddress(localhostAddress,SERVER_PORT);
			//서버소켓은 서버 IP주소와 PORT정보를 가지고 bind
			serverSocket.bind(inetSocketAddress);
			System.out.println("[server]binding"+localhostAddress+":"+SERVER_PORT);
			
			//3.연결요청을 기다림(accept)
			Socket socket = serverSocket.accept();//blocking
			//위 소켓안의 스트림을 사용하는것
			
			//4.연결 성공
			//다운 캐스팅 반환되는 소켓이 Inetsocketaddress의 부모이기 때문
			InetSocketAddress remoteAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
			int remoteHostPort = remoteAddress.getPort();
			//inetAddress로 호스트ip뽑아옴
			String remoteHostAddress = remoteAddress.getAddress().getHostAddress();
			System.out.println("[server]connected from "+remoteHostAddress+" : "+remoteHostPort);
				
			try{
				//5.IOStream 받아오기
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				
				while(true){
					//6.데이터 읽기
					byte[] buffer = new byte[256];
					int readByteCount = is.read(buffer);//blocking
					
					//client가 소켓을 닫은 경우
					if(readByteCount<=-1){
						System.out.println("[server]disconnetced by client");
						return;
					}
					//버퍼를 0~읽은 수 까지 한글로 인코딩 할것
					String data = new String(buffer,0,readByteCount,"utf-8");
					System.out.println("[server]received : "+data);
					
					//7.데이터 쓰기
					os.write(data.getBytes("utf-8"));
				}
			}catch(SocketException e){
			//클라이언트가 소켓을 정상적으로 닫지 않고 종료한 경우
				System.out.println("[server]closed by client");
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
		} catch (IOException e) {
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

}
