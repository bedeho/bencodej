package org.bencodej;

import org.bencodej.exception.NonLexicographicalKeyOrderException;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class BencodeableDictionaryTest {

    BencodeableDictionary FIRST;
    private byte [] FIRST_BENCODING;

    BencodeableDictionary SECOND;
    private byte [] SECOND_BENCODING;

    @Before
    public void setUp() throws Exception {

        FIRST = new BencodeableDictionary();
        FIRST.put(new BencodableByteString("key1".getBytes()), new BencodableInteger(23));
        FIRST.put(new BencodableByteString("key2".getBytes()), new BencodableByteString("hello".getBytes()));
        FIRST_BENCODING = new byte[] {'d','4',':','k','e','y','1','i','2','3','e','4',':','k','e','y','2','5',':','h','e','l','l','o','e'}; // d 4:key1 i23e 4:key2 5:hello e

        SECOND = new BencodeableDictionary();
        SECOND.put(new BencodableByteString("key1".getBytes()), new BencodableInteger(11));
        SECOND.put(new BencodableByteString("key2".getBytes()), new BencodableByteString("world".getBytes()));
        SECOND_BENCODING = new byte [] {'d','4',':','k','e','y','1','i','1','1','e','4',':','k','e','y','2','5',':','w','o','r','l','d','e'}; // d 4:key1 i11e 4:key2 5:world e
    }

    @Test
    public void testBencode() throws Exception {

        assertArrayEquals(FIRST_BENCODING, FIRST.bencode());
        assertArrayEquals(SECOND_BENCODING, SECOND.bencode());
    }

    @Test
    public void testBufferConstructor() throws Exception {

        BencodeableDictionary decodedFirst = new BencodeableDictionary(ByteBuffer.wrap(FIRST.bencode()));
        assertEquals(decodedFirst, FIRST);

        BencodeableDictionary decodedSecond = new BencodeableDictionary(ByteBuffer.wrap(SECOND.bencode()));
        assertEquals(decodedSecond, SECOND);
    }

    @Test(expected=NonLexicographicalKeyOrderException.class)
    public void testNonLexicographicalKeyOrderException() throws Exception {
        new BencodeableDictionary(ByteBuffer.wrap(new byte[] {'d','4',':','k','e','y','9','i','1','1','e','4',':','k','e','y','2','5',':','w','o','r','l','d','e'}));
    }
}