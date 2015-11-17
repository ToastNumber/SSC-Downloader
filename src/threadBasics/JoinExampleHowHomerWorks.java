package threadBasics;

public class JoinExampleHowHomerWorks {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread workactivity1 = new Thread(new Napping(), "sleep");	
		Thread workactivity2 = new Thread(new EatDonut(workactivity1), "eat donut");	
		workactivity1.start();
		workactivity2.start();
	}
}

	class EatDonut implements Runnable{
		public EatDonut(Thread t){this.threadBRef=t;}
		Thread threadBRef;
		@Override
		public void run(){
			try{
				System.out.println("EatDonut_thread: I want to " + Thread.currentThread().getName() + 
						" but I am " + threadBRef.getName() + "ing");
				threadBRef.join();//wait until threadB is alive
				System.out.println("EatDonut_thread: I can eat donut now!");
			} catch (InterruptedException e) {//like the wait and sleep, join could get the interrupt signal too
				System.out.println("EatDonut_thread: interrupt signal has received!\n");
			}
		}
	}
	
	class Napping implements Runnable{
		@Override
		public void run() {
			try{
				Thread.sleep(10);//wait a bit, let the ThreadA goes first (not recommended)
				System.out.println("Napping_thread: I am working");
				for(int i=1;i<5;i++){
					System.out.println("Zzzz");
					Thread.sleep(1500);
				}
				System.out.println("Napping_thread: I have finished napping!");
				Thread.sleep(2500);
			}catch(InterruptedException e)
			{
				System.out.println("Napping_thread: interrupt signal has received!\n");
			}

		}	
	}

