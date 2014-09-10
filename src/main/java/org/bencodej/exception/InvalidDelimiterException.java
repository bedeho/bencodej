package org.bencodej.exception;

/**
 * Created by bedeho on 10.09.2014.
 *
 * Thrown by BencodableObject.decode() when delimiter starting
 * bencoding field is not valid, that is among 'i','d','l','0',...,'9',
 * or delimiter ending field is not valid, that is 'e'.
 */
public class InvalidDelimiterException extends DecodingBencodingException {

    /**
     * Invalid delimiter found
     */
    private char invalidDelimiter;

    /**
     * Delimiter type
     */
    public enum DelimiterType {
        START,STOP;
    }

    private DelimiterType type;

    /**
     * Type of
     * @param invalidDelimiter
     * @param type
     */

    public InvalidDelimiterException(char invalidDelimiter, DelimiterType type) {
        this.invalidDelimiter = invalidDelimiter;
        this.type = type;
    }

    public char getInvalidDelimiter() {
        return invalidDelimiter;
    }

    public DelimiterType getType() {
        return type;
    }
}
