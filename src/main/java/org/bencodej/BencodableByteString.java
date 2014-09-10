package org.bencodej;

import org.bencodej.exception.EmptyLengthFieldException;
import org.bencodej.exception.LenthFieldToGreatException;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by bedeho on 10.09.2014.
 */
public class BencodableByteString extends BencodableObject implements Comparable<BencodableByteString> {

    /**
     * Byte byteString value.
     */
    private byte[] byteString;

    /**
     * Do not load a byte byteString which has a length field greater
     * than this.
     */
    private final static int MAX_LENGTH_FIELD_TO_TRUST = 1000;

    public BencodableByteString(byte[] byteString) {
        this.byteString = byteString;
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
        this.byteString = new byte[lengthField];

        for(int i = 0;i < lengthField;i++)
            byteString[i] = src.get();
    }

    @Override
    public byte[] bencode() {

        // Build bencoding representation of integer
        String bencoding = byteString.length + ":" + byteString;

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
    public int compareTo(BencodableByteString s) {

        byte[] otherByteString = s.getByteString();

        // Iterate part where both are long enough
        int shortestLength = Math.min(byteString.length, otherByteString.length);

        for(int i = 0;i < shortestLength;i++) {

            // Byte comparison
            int diff = (int)byteString[i] - (int)otherByteString[i];

            if(diff != 0)
                return diff;
        }

        // Compare by length
        return byteString.length - otherByteString.length;
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

        if (!Arrays.equals(byteString, that.byteString)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(byteString);
    }

    public byte[] getByteString() {
        return byteString;
    }

    public void setByteString(byte[] byteString) {
        this.byteString = byteString;
    }
}
