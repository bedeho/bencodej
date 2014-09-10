package org.bencodej;

import org.bencodej.exception.DecodingBencodingException;
import org.bencodej.exception.InvalidDelimiterException;

import java.nio.ByteBuffer;

/**
 * Created by bedeho on 10.09.2014.
 */
public abstract class BencodableObject {

    /**
     * Decodes bencoding.
     * @param src bencoding
     * @return decoded object
     */
    public static BencodableObject decode(byte[] src) throws DecodingBencodingException {
        return decode(ByteBuffer.wrap(src));
    }

    /**
     * Decodes bencoding. After successful return
     * the input buffer will have position after end
     * of bencoding. If exception is thrown, the position
     * is set to where last successful read was made.
     * @param src bencoding
     * @return decoded object
     */
    public static BencodableObject decode(ByteBuffer src) throws DecodingBencodingException {

        // Get leading byte, without advancing position
        char delimiter = src.getChar(src.position());

        // Call upon the correct constructor, or throw
        // exception if delimiter is not recognized.
        if(delimiter == 'i')
            return new BencodableInteger(src);
        else if(delimiter == 'l')
            return new BencodableList(src);
        else if(delimiter == 'd')
            return new BencodableDictionary(src);
        else if(delimiter <= '9' && delimiter >= '0')
            return new BencodableByteString(src);
        else
            throw new InvalidDelimiterException(delimiter);
    }

    /**
     * Bencode this object.
     * @return buffer with bencoding
     */
    abstract public ByteBuffer bencode();
}
