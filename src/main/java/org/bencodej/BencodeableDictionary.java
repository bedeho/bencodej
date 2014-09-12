package org.bencodej;

import org.bencodej.exception.DecodingBencodingException;
import org.bencodej.exception.InvalidDelimiterException;
import org.bencodej.exception.NonLexicographicalKeyOrderException;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by bedeho on 10.09.2014.
 */
public class BencodeableDictionary extends HashMap<BencodableByteString,Bencodable> implements Bencodable {

    public BencodeableDictionary() {}

    public BencodeableDictionary(ByteBuffer src) throws DecodingBencodingException {

        // Get leading byte
        byte delimiter = src.get();

        // Check that we have correct delimiter
        if(delimiter != 'd')
            throw new InvalidDelimiterException(delimiter);

        // Last key read, is used to check lexicographic ordering
        BencodableByteString lastKey = null;

        while(src.get(src.position()) != 'e') { // read without consuming

            // Decode key object
            BencodableByteString key = new BencodableByteString(src);

            // Check that lexical ordering is preserved
            if(lastKey != null && key.compareTo(lastKey) < 0)
                throw new NonLexicographicalKeyOrderException();

            // Decode value object
            Bencodable value = Bencodej.decode(src);

            // Add to list
            put(key, value);

            // Save this key as last key
            lastKey = key;
        }

        // Consume ending 'e'
        delimiter = src.get();
    }

    @Override
    public byte [] bencode() {

        // Recover keys and order them
        LinkedList<BencodableByteString> orderedKeyList = new LinkedList<BencodableByteString>();

        for(BencodableByteString s : keySet())
                orderedKeyList.add(s);

        // Sort
        Collections.sort(orderedKeyList);

        // Iterate keys and dump bencoding of values
        LinkedList<Bencodable> list = new LinkedList<Bencodable>();

        for(BencodableByteString s : orderedKeyList) {

            // Add key
            list.add(s);

            // Add Value
            list.add(get(s));
        }

        return Bencodej.concatenateBencodingsIntoBencoding((byte)'d', list);
    }
}
