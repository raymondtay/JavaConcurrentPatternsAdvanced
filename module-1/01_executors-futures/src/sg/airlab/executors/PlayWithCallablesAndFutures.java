package sg.airlab.executors;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PlayWithCallablesAndFutures {

	// You should verify that the exception thrown is wrapped in ExecutionException
	public static void main(String[] args) throws ExecutionException, InterruptedException {

		Callable<String> task = () -> {
			throw new IllegalStateException("Exception thrown in thread " + Thread.currentThread().getName());
		};

		ExecutorService executor = Executors.newFixedThreadPool(4);

		try {
			for (int i = 0; i < 10; i++) {
				Future<String> future = executor.submit(task);
				System.out.println("Result: " + future.get());
			}
		} finally {
			executor.shutdown();
		}
	}
}
