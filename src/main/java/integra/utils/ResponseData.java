package integra.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseData<T> {
    private boolean success;
    private T data;
    private String message;
    private Map<String, ?> meta;

    public ResponseData() {
    }

    public ResponseData(boolean success, T data, String message, Map<String, ?> meta) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.meta = meta;
    }

    private ResponseData(T data, String message, Map<String, ?> meta) {
        this.success = true;
        this.data = data;
        this.message = message;
        this.meta = meta;
    }

    private ResponseData(boolean estatus, String message) {
        this.success = estatus;
        this.message = message;
    }

    public static <T> ResponseData<T> of(T data, String message) {
        return new ResponseData<T>(data, message, null);
    }

    public static <T> ResponseData<T> of(boolean success, String message) {
        return new ResponseData<T>(success, message);
    }

    public static <T> ResponseData<T> paginated(T data, int page, int size, Long totalItems, int totalPages) {
        Map<String, ?> meta = Map.of("page", page, "size", size, "totalItems", totalItems, "totalPages", totalPages);
        return new ResponseData<T>(data, "Información paginada", meta);
    }

    public static <T> ResponseData<T> success(String message, T data) {
        return new ResponseData<T>(true, data, message, null);
    }

    public static <T> ResponseData<T> error(String message) {
        ResponseData<T> response = new ResponseData<>();
        response.success = false;
        response.message = message;
        return response;
    }

}