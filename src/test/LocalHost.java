package test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalHost {

	public static void main(String[] args) {
		try {
			//ip주소 받아오기
			InetAddress inetAddress = InetAddress.getLocalHost();
			
			String hostAddress = inetAddress.getHostAddress();
			System.out.println(hostAddress);
			
			String hostName = inetAddress.getHostName();
			System.out.println(hostName);
			
			byte[] addresses = inetAddress.getAddress();
			for(int i=0; i<addresses.length; i++){
				//int 변환시 byte수준의 문제 해결하기위해
				//앞에 MSB 살려줌
				int address = addresses[i] & 0x000000ff;
				System.out.print(address);
				if(i<3)
					System.out.print(".");
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}

}
