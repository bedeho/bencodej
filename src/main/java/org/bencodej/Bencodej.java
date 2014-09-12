package org.bencodej;

import org.bencodej.exception.DecodingBencodingException;
import org.bencodej.exception.InvalidDelimiterException;

import java.nio.ByteBuffer;
import java.util.LinkedList;

/**
 * Created by bedeho on 12.09.2014.
 */
public abstract class Bencodej {

    /**
     * Decodes bencoding.
     * @param src bencoding
     * @return decoded object
     * @throws DecodingBencodingException if bencoding is malformed
     */
    public static Bencodable decode(byte[] src) throws DecodingBencodingException {
        return decode(ByteBuffer.wrap(src));
    }

    /**
     * Decodes bencoding. After successful return
     * the input buffer will have position after end
     * of bencoding. If exception is thrown, the position
     * is set to where last successful read was made.
     * @param src bencoding
     * @return decoded object
     * @throws DecodingBencodingException if bencoding is malformed
     */
    public static Bencodable decode(ByteBuffer src) throws DecodingBencodingException {

        // Get leading byte, without advancing position
        byte delimiter = src.get(src.position());

        // Call upon the correct constructor, or throw
        // exception if delimiter is not recognized.
        if(delimiter == 'i')
            return new BencodableInteger(src);
        else if(delimiter == 'l')
            return new BencodableList(src);
        else if(delimiter == 'd')
            return new BencodeableDictionary(src);
        else if(delimiter <= '9' && delimiter >= '0')
            return new BencodableByteString(src);
        else
            throw new InvalidDelimiterException(delimiter);
    }

    /**
     * Bencodes list of objects as specified by paramter. This is a routines for list and dictionary subclasses of this class.
     * @param startingDelimiter delimiter, either 'l' or 'd',  that should be used in this encoding
     * @param bencodableList list of bencodable objects
     * @return final bencoding
     */
    public static byte [] concatenateBencodingsIntoBencoding(byte startingDelimiter, LinkedList<Bencodable> bencodableList) {

        // Count total size
        int totalByteLength = 2;
        for(Bencodable o: bencodableList)
            totalByteLength += o.bencode().length;

        // Allocate space for bencodableList
        byte [] totalBencoding = new byte[totalByteLength];

        // Set delimiters
        totalBencoding[0] = (byte)startingDelimiter;
        totalBencoding[totalBencoding.length - 1] = 'e';

        // Copy bencoding of list objects into buffer
        int position = 1;
        for(Bencodable o: bencodableList) {

            // Bencodej object
            byte [] bencoding = o.bencode();

            // Copy into bigger buffer
            System.arraycopy(bencoding, 0, totalBencoding, position, bencoding.length);

            // Keep track of position
            position += bencoding.length;
        }

        // Return result
        return totalBencoding;
    }
}
