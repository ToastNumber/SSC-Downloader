package threadBasics;

public class SleepInterruptExample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread HomerSimpson = new Thread(new Homer(), "Homer");	
		Thread MrBurns = new Thread(new Burns(HomerSimpson), "Mr. Burns");	
		HomerSimpson.start();
		MrBurns.start();
	}

}

class Homer implements Runnable{
	@Override
	public void run() {
		String threadName = Thread.currentThread().getName(); 
		//.....some work
		System.out.println(threadName + ": I'm working...");		
		try{// try to sleep
			System.out.println(threadName + ": Zzzz");
			Thread.sleep(10000); // sleep for 10 sec
		}catch(InterruptedException e)//while in sleep mode, interruption would happened
		{//handle if interrupted, do alternative tasks
			System.out.println(e.getMessage());
			System.out.println(threadName + ": Doh, Mr. Burns!");
			System.out.println(threadName + ": Escape! Run!");
			return;//terminate the thread
		}
		System.out.println(threadName + ": I have no interruption today! Let's eat donut!");
	}
}


class Burns implements Runnable{
	public Burns(Thread t)
	{
		this.HomerSimpsonref=t;
	}

	Thread HomerSimpsonref;
	@Override
	public void run(){
		String threadName = Thread.currentThread().getName(); 
		try {			
			Thread.sleep(5000);//sleeps for 5 sec to simulate doing other things
			System.out.println(threadName + ": Homer is not working! Let me do something.");
			System.out.println(threadName + ": Homer wake up!!!");
			//HomerSimpsonref.interrupt();//comment it and see the differences
			Thread.sleep(1000);//sleeps for 5 sec
		} catch (InterruptedException e) {
			System.out.println(threadName + ": I've got an interruption signal but I don't care!");
		}
		System.out.println(threadName + ": *&*&*&^&*%%^");
	}
}