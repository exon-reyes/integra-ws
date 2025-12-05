package integra.globalexception;

import lombok.Getter;

@Getter
public class LoginException extends RuntimeException {
    private final String title;

    public LoginException(String title, String message) {
        super(message);
        this.title = title;
    }
}
