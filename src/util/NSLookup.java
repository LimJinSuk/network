package util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NSLookup {

	public static void main(String[] args) {
		
		try {
			//InetAddress 객체 선언
			InetAddress inetAddress = InetAddress.getLocalHost();
			Scanner scanner = new Scanner(System.in);
			
			String line = null; 			
			while(line!="exit"){
				System.out.print(">");
				line = scanner.nextLine();
				
				//==연산자 동일성 비교
				//equals()로 동질성비교해야함
				if(line.equals("exit"))
					break;
				
				//InetAddress클래스의 static 메서드 InetAddress[]
				//getAllByName(String host):주어진 이름을 갖는 호스트의 '모든' IP주소 얻기
				InetAddress[] addresses=InetAddress.getAllByName(line);
				
				for(int i=0; i<addresses.length; i++){
					System.out.println(addresses[i]);
				}
			}
			scanner.close();
			System.exit(1);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}

}
