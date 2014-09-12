package org.bencodej;

/**
 * Created by bedeho on 10.09.2014.
 */
public interface Bencodable {

    /**
     * Bencode this object.
     * @return buffer with bencoding
     */
    abstract public byte [] bencode();
}
