package integra.asistencia.util;

public interface HandlerExecutor<T, K> {
    T execute(K command);
}