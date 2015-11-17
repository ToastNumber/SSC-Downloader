package threadBasics;

public class ThreadPassArguments {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread Homer1 = new Thread(new CoreHomeCharater("a donut"), "Homer1");	
		Homer1.start();
		Thread Homer2 = new Thread(new CoreHomeCharater("work"), "Homer2");	
		Homer2.start();
		Thread Homer3= new Thread(new CoreHomeCharater("Math"), "Homer3");	
		Homer3.start();		

	}

}

class CoreHomeCharater implements Runnable{
	public CoreHomeCharater(String arg){
		// the argument you want to pass
		this.arg=arg;
	}
	private String arg;
	@Override
	public void run() {
		String threadName = Thread.currentThread().getName(); 
		System.out.println( threadName + ": This is " + arg);
		if(arg.contains("donut")) //using the passed(arg) value
			System.out.println(threadName + ": I love donut!");
		else
			System.out.println(threadName + ": zzz....");

	}
}