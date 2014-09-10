package org.bencodej.exception;

/**
 * Created by bedeho on 10.09.2014.
 *
 * Thrown by BencodableObject.decode() when delimiter starting
 * bencoding field is not valid, that is among 'i','d','l','0',...,'9'.
 */
public class InvalidDelimiterException extends DecodingBencodingException {

    /**
     * Invalid delimiter found
     */
    private char invalidDelimiter;

    public InvalidDelimiterException(char invalidDelimiter) {
        this.invalidDelimiter = invalidDelimiter;
    }

    public char getInvalidDelimiter() {
        return invalidDelimiter;
    }
}
