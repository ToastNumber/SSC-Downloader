package threadBasics;

public class FirstExtendingThread extends Thread { 
	public void run() {
		System.out.println("this thread is running...");
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FirstExtendingThread t = new FirstExtendingThread();
		t.start();
		FirstExtendingThread t2 = new FirstExtendingThread();
		t2.start();		
	}

}
