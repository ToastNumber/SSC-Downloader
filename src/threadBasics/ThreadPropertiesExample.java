package threadBasics;

public class ThreadPropertiesExample {
	public static void main(String[] args) {

		Thread thread1 = new Thread(new SecondRunnable(), "Shan He");	
		System.out.println("thread1 current state is " + thread1.getState());
		thread1.start();
		System.out.println("After start(), thread1 current state is " + thread1.getState());
		System.out.println("-------------------------------------------------------");
		
		
		System.out.println("The thread name is " + Thread.currentThread().getName());
		System.out.println("-------------------------------------------------------");
		

		Thread thread2 = new Thread(new ThirdRunnable(), "Bob Hendley");	
		thread2.start();
		

	}
}


class SecondRunnable implements Runnable{

	@Override
	public void run() {

		for(int i=0;i<20;i++){
			System.out.println("SecondRunnable thread name is " + Thread.currentThread().getName());
			System.out.println("SecondRunnable thread ID is " + Thread.currentThread().getId());
			
		}

	}
}


class ThirdRunnable implements Runnable{

	@Override
	public void run() {
		for(int i=0;i<20;i++){
			System.out.println("ThirdRunnable thread name is " + Thread.currentThread().getName());
			System.out.println("ThirdRunnable thread ID is " + Thread.currentThread().getId());
		}

	}
}
