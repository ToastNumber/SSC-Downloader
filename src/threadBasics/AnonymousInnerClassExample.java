package threadBasics;

public class AnonymousInnerClassExample {

	// Two ways of passing arguments to anonymous inner thread claass
	// First one is to declare the variable as final 
	
	public static void main(String[] args) throws InterruptedException {    

		final long PassedArgument = 10;
		Runnable runA = new Runnable() {		
			public void run() {
				long p = PassedArgument;
				System.out.println("Passed augument is: " + p);
			}
		};


		Thread ta = new Thread(runA, "threadA");
		ta.start();

	}
}
