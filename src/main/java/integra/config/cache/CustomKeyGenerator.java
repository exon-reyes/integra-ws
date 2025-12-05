package integra.config.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CustomKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(target.getClass().getSimpleName())
                .append(".")
                .append(method.getName());

        if (params != null && params.length > 0) {
            keyBuilder.append("(");
            String paramStr = Arrays.stream(params)
                    .map(this::paramToString)
                    .collect(Collectors.joining(","));
            keyBuilder.append(paramStr);
            keyBuilder.append(")");
        } else {
            keyBuilder.append("()");
        }

        String key = keyBuilder.toString();
        log.debug("Generated cache key: {}", key);
        return key;
    }

    private String paramToString(Object param) {
        if (param == null) {
            return "null";
        }
        if (param.getClass().isArray()) {
            return Arrays.deepToString((Object[]) param);
        }
        if (param instanceof Iterable) {
            return ((Iterable<?>) param).iterator().hasNext() ?
                    "[" + ((Iterable<?>) param).iterator().next().toString() + "...]" :
                    "[]";
        }
        return param.toString();
    }
}