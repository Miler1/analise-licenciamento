package utils;

import play.mvc.Http;
import play.mvc.Scope;

import java.util.concurrent.Callable;


public class PlayCallable<T> implements Callable<T> {
    private final Http.Request request;
    private final Callable<T> callable;
    private final Scope.Session session;
    private final Scope.Flash flash;
    private final Scope.Params params;
    private final Scope.RenderArgs renderArgs;

    public PlayCallable(Callable<T> callable) {
        request = Http.Request.current();
        session = Scope.Session.current();
        flash = Scope.Flash.current();
        params = Scope.Params.current();
        renderArgs = Scope.RenderArgs.current();
        this.callable = callable;
    }

    @Override
    public T call() throws Exception {

        Http.Request.current.set(request);
        Scope.Session.current.set(session);
        Scope.Flash.current.set(flash);
        Scope.Params.current.set(params);
        Scope.RenderArgs.current.set(renderArgs);

        return callable.call();
    }
}