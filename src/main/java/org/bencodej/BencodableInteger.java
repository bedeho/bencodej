package org.bencodej;

import org.bencodej.exception.EmptyIntegerException;
import org.bencodej.exception.InvalidDelimiterException;

import java.nio.ByteBuffer;

/**
 * Created by bedeho on 10.09.2014.
 */
public class BencodableInteger extends BencodableObject {

    /**
     * Integer value
     */
    private int value;

    public BencodableInteger(int value) {
        this.value = value;
    }

    public BencodableInteger(ByteBuffer src) throws InvalidDelimiterException, EmptyIntegerException {

        // Get leading byte
        char delimiter = src.getChar();

        // Check that we have correct delimiter
        if(delimiter != 'i')
            throw new InvalidDelimiterException(delimiter, InvalidDelimiterException.DelimiterType.START);

        // Get possible negative sign
        char possiblyNegativeSign = src.getChar();
        boolean isNegativeInteger = false;

        if(possiblyNegativeSign == '-')
            isNegativeInteger = true;
        else
            src.position(src.position() - 1); // rewind buffer position one step, since we read leading digit

        // Find end of integer
        String digits = "";
        char lastDigit = src.getChar();
        while(lastDigit != 'e')
            digits += src.getChar();

        // Check that we read at least one digit
        if(digits.length() == 0)
            throw new EmptyIntegerException();

        // Convert to integer
        this.value = Integer.parseInt(digits) * (isNegativeInteger ? -1 : 1);
    }

    public byte [] bencode() {

        // Build bencoding representation of integer
        String bencoding = "i" + this.value + "e";

        // Put in buffer and return
        return bencoding.getBytes();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
