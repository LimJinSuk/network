package thread;

public class MultiThreadEx {

	public static void main(String[] args) {
		/* 단일스레드
		 * for(int i=0; i<10; i++)
			System.out.print(i);
		System.out.println("");
		for(char c='a'; c<='z'; c++)
			System.out.print(c);*/
		
		//스레드 객체를 만듦
		Thread t1 = new AlphabetThread();
		Thread t2 = new DigitThread();
		Thread t3 = new DigitThread();
		
		//스레드클래스를 직접 상속받은게 아니라서 스레드 생성자에 객체로 받아옴
		Thread t4 = new Thread(new UpperCaseAlphabetTest());
		
		//스레드 객체의 run실행
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		//메인스레드는 종료되나 스레드객체들의 run은 계속 실행
	}

}
