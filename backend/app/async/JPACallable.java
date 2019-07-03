package async;

import play.db.jpa.JPAPlugin;

import java.util.concurrent.Callable;

public class JPACallable<T> implements Callable<T> {

	private final Callable<T> callable;
	private final boolean readonly;

	public JPACallable(Callable<T> callable) {
		this(callable, false);
	}

	public JPACallable(Callable<T> callable, boolean readonly) {
		this.callable = callable;
		this.readonly = readonly;
	}

	@Override
	public T call() throws Exception {
		try {
			JPAPlugin.startTx(readonly);
			T ret = callable.call();
			JPAPlugin.closeTx(readonly);
			return ret;
		} catch (Throwable e){
			JPAPlugin.closeTx(true);
			throw e;
		}
	}
}
