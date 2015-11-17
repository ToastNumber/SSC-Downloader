package executorsAndThreadPools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class ExecutorServiceCallable {

	public static class WordLengthCallable implements Callable {
		private String word;
		private Integer pangramID;
		// Constructor for passing arguments
		public WordLengthCallable(String word, Integer pangramID) {
			this.word = word;
			this.pangramID = pangramID;
		}
		public Integer call() {
			System.out.println("Thread " + Thread.currentThread().getName() +" is working on pangram " + pangramID.toString());
			try {
				Thread.sleep((int)(Math.random() * 1000));
			} catch (InterruptedException e) {
			}
			return Integer.valueOf(word.length());
		}
	}

	public static void main(String args[]) throws Exception {
		// Several pangrams. A pangram is a sentence that contains all letters of the alphabet
		String pangrams_array[] = {"Forsaking monastic tradition, twelve jovial friars gave up their vocation for a questionable existence on the flying trapeze",
				"No kidding -- Lorenzo called off his trip to visit Mexico City just because they told him the conquistadores were extinct",
				"Jelly-like above the high wire, six quaking pachyderms kept the climax of the extravaganza in a dazzling state of flux",
				"Ebenezer unexpectedly bagged two tranquil aardvarks with his jiffy vacuum cleaner",
				"We quickly seized the black axle and just saved it from going past him",
				"Crazy Fredericka bought many very exquisite opal jewels."
		};
		System.out.println("The totoal number of pangrams is: " + pangrams_array.length);
		// Step 1: to create an a fix thread pool of size 3
		ExecutorService pool = Executors.newFixedThreadPool(3);
		// We use List to preserve the order of the returned values 
		List<Future<Integer>> list = new ArrayList<Future<Integer>>();
		Integer pangramCount = 1;
		for (String pangram: pangrams_array) {
			Callable<Integer> callable = new WordLengthCallable(pangram, pangramCount);
			// Step 2 (better): to submit a Runnable/Callable object (a job), 
			// which returns a Future object to retrieve the Callable 
			// return value and to manage the status of both Callable and Runnable
			Future<Integer> future = pool.submit(callable);
			list.add(future);
			pangramCount++;

		}
		int sum = 0;
		pangramCount = 1;
		for (Future<Integer> future : list) {
			// future.get() returns the computed result, but waits if necessary for the computation to complete, and then retrieves its result.					
			System.out.println("The " + pangramCount + "th string has " + future.get() + " words.");
			sum += future.get();
			pangramCount++;
		}
		System.out.printf("The total number of words is %s%n", sum);
		// Step 3: shutdown of the ExecutorService:
		pool.shutdown();
		System.exit(sum);
	}
}
