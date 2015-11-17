package executorsAndThreadPools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class SomeRunnable implements Runnable {

	public void run() {
		for (int i = 0; i < 10; i++) {
			System.out.println("Counter - " + i + " / Thread Name : " + Thread.currentThread().getName());
		}
	}

}

public class ExecutorServiceRunnable {
	public static void main(String[] args) {

		// Create objects of Runnable

		SomeRunnable obj1 = new SomeRunnable();
		SomeRunnable obj2 = new SomeRunnable();
		SomeRunnable obj3 = new SomeRunnable();
		// Step 1: Create a fixed Thread pool of 2 thread
		ExecutorService pool = Executors.newFixedThreadPool(2);
		// Step 2: to execute Runnable/Callable objects, which creates a new
		// thread and launches it immediately
		pool.execute(obj1);
		pool.execute(obj2);
		pool.execute(obj3);
		// Step 3: shutdown of the ExecutorService:
		pool.shutdown();

	}

}
