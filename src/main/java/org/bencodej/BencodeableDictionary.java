package org.bencodej;

import org.bencodej.exception.DecodingBencodingException;
import org.bencodej.exception.InvalidDelimiterException;
import org.bencodej.exception.NonLexicographicalKeyOrderException;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by bedeho on 10.09.2014.
 */
public class BencodeableDictionary {

    /**
     * Map of bencodable objects
     */
    private HashMap<BencodableByteString,BencodableObject> map;

    public BencodeableDictionary(HashMap<BencodableByteString,BencodableObject> map) {
        this.map = map;
    }

    public BencodeableDictionary(ByteBuffer src) throws DecodingBencodingException {

        // Get leading byte
        char delimiter = src.getChar();

        // Check that we have correct delimiter
        if(delimiter != 'd')
            throw new InvalidDelimiterException(delimiter, InvalidDelimiterException.DelimiterType.START);

        // Read objects until we find end of list
        this.map = new HashMap<BencodableByteString,BencodableObject>();

        // Last key read, is used to check lexicographic ordering
        BencodableByteString lastKey = null;

        while(src.getChar(src.position()) != 'e') { // read without consuming

            // Decode key object
            BencodableByteString key = new BencodableByteString(src);

            // Check that lexical ordering is preserved
            if(lastKey != null && key.compareTo(lastKey) >> 0)
                throw new NonLexicographicalKeyOrderException();

            // Decode value object
            BencodableObject value = BencodableObject.decode(src);

            // Add to list
            map.put(key, value);
        }

        // Consume ending 'e'
        delimiter = src.getChar();
        if(!(delimiter == 'e'))
            throw new InvalidDelimiterException(delimiter, InvalidDelimiterException.DelimiterType.STOP);
    }

    public String bencode() {

        // Start bencoding with list delimiter
        String bencoding = "d";

        // Recover keys and order them
        LinkedList<BencodableByteString> orderedKeyList = new LinkedList<BencodableByteString>();

        for(BencodableByteString s : map.keySet())
                orderedKeyList.add(s);

        orderedKeyList.sort();

        // Iterate keys and dump bencoding of values
        for(BencodableByteString s : orderedKeyList) {

            bencoding += s.bencode();

            BencodableObject o = map.get(s);

            bencoding += o;

        }

        // end bencoding with list ender
        bencoding += "e";

        // Put in buffer and return
        return bencoding.getBytes();
    }

    public HashMap<BencodableByteString,BencodableObject> getDictionary() {
        return map;
    }

    public void setDictionary(HashMap<BencodableByteString,BencodableObject> map) {
        this.map = map;
    }
}
