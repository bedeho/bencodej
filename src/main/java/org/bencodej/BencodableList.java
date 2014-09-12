package org.bencodej;

import org.bencodej.exception.DecodingBencodingException;
import org.bencodej.exception.InvalidDelimiterException;

import java.nio.ByteBuffer;
import java.util.LinkedList;

/**
 * Created by bedeho on 10.09.2014.
 */
public class BencodableList extends LinkedList<Bencodable> implements Bencodable {

    public BencodableList(){}

    public BencodableList(ByteBuffer src) throws DecodingBencodingException {

        // Get leading byte
        byte delimiter = src.get();

        // Check that we have correct delimiter
        if(delimiter != 'l')
            throw new InvalidDelimiterException(delimiter);

        // Read objects until we find end of list
        while(src.get(src.position()) != 'e') { // read without consuming

            // Decode next object
            Bencodable o = Bencodej.decode(src);

            // Add to list
            add(o);
        }

        // Consume ending 'e'
        delimiter = src.get();
    }

    public byte [] bencode() {
        return Bencodej.concatenateBencodingsIntoBencoding((byte)'l', this);
    }
}
