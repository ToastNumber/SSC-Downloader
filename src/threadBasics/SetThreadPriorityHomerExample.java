package threadBasics;

public class SetThreadPriorityHomerExample {
	public static void main(String[] args) {

		Thread Activity1 = new Thread(new HomersActivity(), "eating donut");	
		Activity1.setPriority(Thread.MAX_PRIORITY);
		Activity1.start();

		Thread Activity2 = new Thread(new HomersActivity(), "sleeping");	
		Activity2.start();


		Thread Activity3 = new Thread(new HomersActivity(), "working");	
		Activity3.setPriority(Thread.MIN_PRIORITY);
		Activity3.start();		

	}
}

class HomersActivity implements Runnable{
	@Override
	public void run() {
		Thread t=Thread.currentThread();//returns the running thread, this thread.

		try{		
			System.out.println("I am " + t.getName()+ " and its priority is " +t.getPriority() );
			Thread.sleep(5000);// wait for 5 seconds here
			System.out.println("Hallelujah! I've finished " + t.getName() );
		}catch(InterruptedException e){
			/*if there is an interrupt signal
			 * it defines by InterruptedException
			 * so this catch determine whenever this thread got interrupt ex.
			 */
			System.out.println("Doh, I've got interrupt message!");
		}
	}
}