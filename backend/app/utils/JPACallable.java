package utils;

import java.util.concurrent.Callable;

import play.db.jpa.JPA;

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

        //para usar oT he persistence unit name default do play framework 1.4.3
        String name = "default";

        try {
            JPA.startTx(name , readonly);
            T ret = callable.call();
            JPA.closeTx(name);
            return ret;
        } catch (Throwable e){
            JPA.closeTx(name);
            try {
                throw e;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return null;
    }
}