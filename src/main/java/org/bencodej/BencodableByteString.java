package org.bencodej;

import org.bencodej.exception.EmptyLengthFieldException;
import org.bencodej.exception.LenthFieldToGreatException;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by bedeho on 10.09.2014.
 */
public class BencodableByteString extends BencodableObject {

    /**
     * Byte string value.
     */
    private byte[] string;

    /**
     * Do not load a byte string which has a length field greater
     * than this.
     */
    private final static int MAX_LENGTH_FIELD_TO_TRUST = 1000;

    public BencodableByteString(byte[] string) {
        this.string = string;
    }

    public BencodableByteString(ByteBuffer src) throws EmptyLengthFieldException, LenthFieldToGreatException {

        // Find length field
        String lengthFiledDigits = "";
        char lastDigit = src.getChar();
        while(lastDigit != ':')
            lengthFiledDigits += src.getChar();

        // Check that we read at least one digit
        if(lengthFiledDigits.length() == 0)
            throw new EmptyLengthFieldException();

        // Recover value
        int lengthField = Integer.parseInt(lengthFiledDigits);

        // Discord length value to great
        if(lengthField > MAX_LENGTH_FIELD_TO_TRUST)
            throw new LenthFieldToGreatException();

        // Read content
        this.string = new byte[lengthField];

        for(int i = 0;i < lengthField;i++)
            string[i] = src.get();
    }

    public byte..[] bencode() {

        // Build bencoding representation of integer
        String bencoding = string.length + ":" + string;

        // Put in buffer and return
        return bencoding.getBytes();
    }

    /**
     * Orders objects, which is necessary so that
     * objects of this class can be assured to appear
     * lexicographically in the bencoding of dictionaries.
     * @param s the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(BencodableByteString s) {

    }

    /**
     * Tests for equality, which is necessary so
     * objects of this class can serve as keys
     * in BencodableDictionary hashmap.
     * @param o the object to test equality with.
     * @return true iff both underlying byte arrays are of equal length and have equal content.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BencodableByteString)) return false;

        BencodableByteString that = (BencodableByteString) o;

        if (!Arrays.equals(string, that.string)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(string);
    }

    public byte[] getValue() {
        return string;
    }

    public void setValue(byte[] byteString) {
        this.string = byteString;
    }
}
