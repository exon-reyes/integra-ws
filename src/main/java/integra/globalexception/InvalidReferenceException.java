package integra.globalexception;// Archivo: InvalidReferenceException.java

import lombok.Getter;

@Getter
public class InvalidReferenceException extends RuntimeException {

    private final String businessRule;
    private final String module;
    private final String auditCode;

    public InvalidReferenceException(String message, String businessRule, String module, String auditCode) {
        super(message);
        this.businessRule = businessRule;
        this.module = module;
        this.auditCode = auditCode;
    }
}