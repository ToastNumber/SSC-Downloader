package executorsAndThreadPools;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceServerSocket {
	ExecutorService executor = Executors.newFixedThreadPool(3);

	public void start(int port) throws IOException {
		final ServerSocket ss = new ServerSocket(port);
		while (!executor.isShutdown())
			executor.submit(new TinyHttpdConnection(ss.accept()));
	}

	public void shutdown() throws InterruptedException {
		executor.shutdown();
		executor.awaitTermination(30, TimeUnit.SECONDS);
		executor.shutdownNow();
	}

	public static void main(String argv[]) throws Exception {
		Integer portnum = 1090;
		new ExecutorServiceServerSocket().start(portnum);
	}
}

class TinyHttpdConnection implements Runnable {
	Socket client;

	TinyHttpdConnection(Socket client) throws SocketException {
		this.client = client;
	}

	public void run() {
		try {


			//  Create I/O streams for communicating to the client
			DataOutputStream os =
				new DataOutputStream(client.getOutputStream());

			//  Perform communication with client
			String ThreadName = Thread.currentThread().getName();
			os.writeBytes("Thread " + ThreadName + " says no...");

			client.close();
		} catch (IOException e) {
			System.out.println("I/O error " + e);
		}
	}
}
