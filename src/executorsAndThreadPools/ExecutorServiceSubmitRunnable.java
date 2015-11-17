package executorsAndThreadPools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class NewRunnable implements Runnable {
	private String taskname;
	NewRunnable(String name) {
		this.taskname = name;
		
	}
	public void run() {
		for (int i = 0; i < 10; i++) {
			System.out.println("Thread Name : "
					+ Thread.currentThread().getName() 
					+ " is working on Task " + taskname);
		}
		System.out.println("Thread Name : "
					+ Thread.currentThread().getName()
					+ " has completed Task " + taskname + "!!");
	}

}

public class ExecutorServiceSubmitRunnable {
	public static void main(String[] args) {

		//Step 1: Create a fixed Thread pool of 2 thread 
		ExecutorService pool = Executors.newFixedThreadPool(2);
		NewRunnable runnableobj = null;
		for (int counter=0; counter<5; counter++) {
		//Create objects of Runnable		
		runnableobj = new NewRunnable(String.valueOf(counter));		
		// Step 2: to execute Runnable/Callable objects, which creates a new thread and launches it immediately 
		pool.submit(runnableobj);

		}
		// Step 3: shutdown of the ExecutorService:
		pool.shutdown();

	}

}
