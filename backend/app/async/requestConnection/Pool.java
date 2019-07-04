package async;

import play.Logger;
import play.Play;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Pool {

	private static Pool GLOBAL = null;

	public static final String PROPRIEDADE_POOL = "async.pool";
	private ExecutorService POOL;
	private static int pool = 0;
	private static int countPoll = 0;

	static {
		GLOBAL = new Pool();
	}

	public static Pool global() {
		return GLOBAL;
	}

	public Pool() {
		this(0);
	}

	public Pool(int pool) {
		if (pool > 0)
			this.pool = pool;
		else if (Play.configuration.containsKey(PROPRIEDADE_POOL))
			this.pool = Integer.parseInt(Play.configuration.getProperty(PROPRIEDADE_POOL));
		else
			this.pool = Runtime.getRuntime().availableProcessors();

		POOL = Executors.newFixedThreadPool(this.pool);
		//POOL = Executors.newScheduledThreadPool(this.pool);
	}

	private class Log<V> implements Callable<V> {

		private final Callable<V> callable;

		public Log(Callable<V> callable) {
			this.callable = callable;
		}

		@Override
		public V call() throws Exception {

			synchronized (POOL) {
				countPoll++;
				Logger.info("[ASYNC_POOL] Max[" + pool + "] Now[" + countPoll + "]");
			}

			try {
				return callable.call();
			} finally {

				synchronized (POOL) {
					countPoll--;
					Logger.info("[ASYNC_POOL] Max[" + pool + "] Now[" + countPoll + "]");
				}
			}
		}
	}

	public <V> Future<V> submit(Callable<V> callable) {
		return POOL.submit(new Log(callable));
	}
}
