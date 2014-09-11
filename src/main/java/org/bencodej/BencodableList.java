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

    public BencodableList(BencodableObject[] a) {
        setList(a);
    }

    public BencodableList(ByteBuffer src) throws DecodingBencodingException {

        // Get leading byte
        byte delimiter = src.get();

        // Check that we have correct delimiter
        if(delimiter != 'l')
            throw new InvalidDelimiterException(delimiter);

        // Read objects until we find end of list
        this.list = new LinkedList<BencodableObject>();
        while(src.get(src.position()) != 'e') { // read without consuming

            // Decode next object
            BencodableObject o = BencodableObject.decode(src);

            // Add to list
            list.add(o);
        }

        // Consume ending 'e'
        delimiter = src.get();
    }

    public byte [] bencode() {
        return super.concatenateBencodingsIntoBencoding((byte)'l', list);
    }

    /**
     * Get list of bencodable objects.
     * Altering list alters this object correspondingly.
     * @return
     */
    public LinkedList<BencodableObject> getList() {
        return list;
    }

    public void setList(LinkedList<BencodableObject> list) {
        this.list = list;
    }

    public void setList(BencodableObject [] a) {
        this.list = new LinkedList<BencodableObject>();

        for(BencodableObject o: a)
            list.add(o);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BencodableList)) return false;

        BencodableList that = (BencodableList) o;

        if (!list.equals(that.list)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }
}
