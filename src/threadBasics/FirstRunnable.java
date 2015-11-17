package threadBasics;

public class FirstRunnable implements Runnable{

	/**
	 * @param args
	 */
	
	public void run() {		
		        System.out.println("In a thread");	
		    }

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread thread = new Thread(new FirstRunnable());	
		thread.start();
		System.out.println("In the main thread");

	}

}

