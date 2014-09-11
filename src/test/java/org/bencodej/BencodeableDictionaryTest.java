package org.bencodej;

import org.bencodej.exception.NonLexicographicalKeyOrderException;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class BencodeableDictionaryTest {

    BencodeableDictionary FIRST;
    HashMap<BencodableByteString,BencodableObject> FIRST_MAP;
    private byte [] FIRST_BENCODING = {'d','4',':','k','e','y','1','i','2','3','e','4',':','k','e','y','2','5',':','h','e','l','l','o','e'}; // d 4:key1 i23e 4:key2 5:hello e

    BencodeableDictionary SECOND;
    HashMap<BencodableByteString,BencodableObject> SECOND_MAP;
    private byte [] SECOND_BENCODING = {'d','4',':','k','e','y','1','i','1','1','e','4',':','k','e','y','2','5',':','w','o','r','l','d','e'}; // d 4:key1 i11e 4:key2 5:world e

    @Before
    public void setUp() throws Exception {

        FIRST_MAP = new HashMap<BencodableByteString,BencodableObject>();
        FIRST_MAP.put(new BencodableByteString("key1".getBytes()), new BencodableInteger(23));
        FIRST_MAP.put(new BencodableByteString("key2".getBytes()), new BencodableByteString("hello".getBytes()));
        FIRST = new BencodeableDictionary(FIRST_MAP);

        SECOND_MAP = new HashMap<BencodableByteString,BencodableObject>();
        SECOND_MAP.put(new BencodableByteString("key1".getBytes()), new BencodableInteger(11));
        SECOND_MAP.put(new BencodableByteString("key2".getBytes()), new BencodableByteString("world".getBytes()));
        SECOND = new BencodeableDictionary(SECOND_MAP);
    }

    @Test
    public void testBencode() throws Exception {

        assertArrayEquals(FIRST_BENCODING, FIRST.bencode());
        assertArrayEquals(SECOND_BENCODING, SECOND.bencode());
    }

    @Test
    public void testBufferConstructor() throws Exception {

        BencodeableDictionary decodedFirst = new BencodeableDictionary(ByteBuffer.wrap(FIRST.bencode()));
        assertEquals(decodedFirst.getDictionary(), FIRST.getDictionary());

        BencodeableDictionary decodedSecond = new BencodeableDictionary(ByteBuffer.wrap(SECOND.bencode()));
        assertEquals(decodedSecond.getDictionary(), SECOND.getDictionary());
    }

    @Test(expected=NonLexicographicalKeyOrderException.class)
    public void testNonLexicographicalKeyOrderException() throws Exception {
        new BencodeableDictionary(ByteBuffer.wrap(new byte[] {'d','4',':','k','e','y','9','i','1','1','e','4',':','k','e','y','2','5',':','w','o','r','l','d','e'}));
    }

    @Test
    public void testGetDictionary() throws Exception {

        assertEquals(FIRST_MAP, FIRST.getDictionary());
        assertEquals(SECOND_MAP, SECOND.getDictionary());
    }

    @Test
    public void testSetDictionary() throws Exception {

        FIRST.setDictionary(SECOND_MAP);
        assertEquals(SECOND_MAP, FIRST.getDictionary());

        SECOND.setDictionary(FIRST_MAP);
        assertEquals(FIRST_MAP, SECOND.getDictionary());
    }
}