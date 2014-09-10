package org.bencodej;


import org.bencodej.exception.DecodingBencodingException;
import org.bencodej.exception.InvalidDelimiterException;

import java.nio.ByteBuffer;
import java.util.LinkedList;

/**
 * Created by bedeho on 10.09.2014.
 */
public class BencodableList extends BencodableObject {

    /**
     * List of bencodable objects
     */
    private LinkedList<BencodableObject> list;

    public BencodableList(LinkedList<BencodableObject> list) {
        this.list = list;
    }

    public BencodableList(ByteBuffer src) throws DecodingBencodingException {

        // Get leading byte
        char delimiter = src.getChar();

        // Check that we have correct delimiter
        if(delimiter != 'l')
            throw new InvalidDelimiterException(delimiter, InvalidDelimiterException.DelimiterType.START);

        // Read objects until we find end of list
        this.list = new LinkedList<BencodableObject>();
        while(src.getChar(src.position()) != 'e') { // read without consuming

            // Decode next object
            BencodableObject o = BencodableObject.decode(src);

            // Add to list
            list.add(o);
        }

        // Consume ending 'e'
        delimiter = src.getChar();
        if(!(delimiter == 'e'))
            throw new InvalidDelimiterException(delimiter, InvalidDelimiterException.DelimiterType.STOP);
    }

    public ByteBuffer bencode() {

        // Start bencoding with list delimiter
        String bencoding = "l";

        // Iterate objects in list and add bencoding of each item
        for(BencodableObject o: list)
            bencoding += o.bencode().toString();

        // end bencoding with list ender
        bencoding += "e";

        // Put in buffer and return
        return ByteBuffer.wrap(bencoding.getBytes());
    }

    public LinkedList<BencodableObject> getList() {
        return list;
    }

    public void setList(LinkedList<BencodableObject> list) {
        this.list = list;
    }
}
