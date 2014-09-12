package org.bencodej;

import org.bencodej.exception.EmptyLengthFieldException;
import org.bencodej.exception.InvalidLengthFieldException;
import org.bencodej.exception.LengthFieldToGreatException;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by bedeho on 10.09.2014.
 */
public class BencodableByteString implements Bencodable, Comparable<BencodableByteString> {

    /**
     * Byte byteString value.
     */
    private byte[] byteString;

    /**
     * Do not load a byte byteString which has a length field greater
     * than this.
     */
    public final static int MAX_LENGTH_FIELD_TO_TRUST = 1000;

    public BencodableByteString(byte[] byteString) {
        this.byteString = byteString;
    }

    public BencodableByteString(ByteBuffer src) throws EmptyLengthFieldException, LengthFieldToGreatException, InvalidLengthFieldException {

        // Find length field
        String lengthFiledDigits = "";
        byte lastDigit;
        while((lastDigit = src.get()) != ':')
            lengthFiledDigits += (char)lastDigit;

        // Check that we read at least one digit
        if(lengthFiledDigits.length() == 0)
            throw new EmptyLengthFieldException();

        // Recover value
        int lengthField;
        try {
            lengthField = Integer.parseInt(lengthFiledDigits);
        } catch (NumberFormatException e) {
            throw new InvalidLengthFieldException();
        }

        // Discord length value to great
        if(lengthField > MAX_LENGTH_FIELD_TO_TRUST)
            throw new LengthFieldToGreatException();

        // Read content
        this.byteString = new byte[lengthField];

        for(int i = 0;i < lengthField;i++)
            byteString[i] = src.get();
    }

    @Override
    public byte[] bencode() {

        // Get length field
        byte [] lengthBytes = ("" + byteString.length).getBytes();

        // Length of encoding
        int bencodingLength = lengthBytes.length + 1 + byteString.length;

        // Allocate space
        byte [] bencoding = new byte[bencodingLength];

        // Fill length field
        for(int i = 0;i < lengthBytes.length;i++)
            bencoding[i] = lengthBytes[i];

        // Fill in separate ':'
        bencoding[lengthBytes.length] = (byte)':';

        // Fill in bytes
        for(int i = 0;i < byteString.length;i++)
            bencoding[lengthBytes.length + 1 + i] = byteString[i];

        // Return bencoding
        return bencoding;
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
     * in BencodableDictionary hashmap. Argument can either be
     * a String object, or an object of the same class
     * @param o the object to test equality with.
     * @return true iff both underlying byte arrays are of equal length and have equal content.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        byte[] that;

        if(o instanceof String) {
            that = ((String) o).getBytes();
        }else if(o instanceof BencodableByteString) {
            that = ((BencodableByteString) o).byteString;
        } else
            return false;

        return Arrays.equals(byteString, that);
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
