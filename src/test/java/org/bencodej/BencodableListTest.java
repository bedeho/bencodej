package org.bencodej;

import org.bencodej.exception.InvalidDelimiterException;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class BencodableListTest {

    private BencodableList FIRST;
    private byte [] FIRST_BENCODING;

    private BencodableList SECOND;
    private byte [] SECOND_BENCODING;

    @Before
    public void setUp() throws Exception {

        FIRST = new BencodableList();
        FIRST.add(new BencodableInteger(1));
        FIRST.add(new BencodableByteString(new byte[] {'g','f','3','4','5'}));
        FIRST.add(new BencodableInteger(3));
        FIRST_BENCODING = new byte[] {'l','i','1','e','5',':','g','f','3','4','5','i','3','e','e'}; //l i1e 5:gf345 i3e e

        SECOND = new BencodableList();
        SECOND.add(new BencodableInteger(123));
        SECOND.add(new BencodableByteString(new byte [] {'D','D','W'}));
        SECOND.add(new BencodableInteger(-5));
        SECOND_BENCODING = new byte[] {'l','i','1','2','3','e','3',':','D','D','W','i','-','5','e','e'}; //l i123e 3:DDW i-5e e
    }

    @Test
    public void testBencode() throws Exception {

        assertArrayEquals(FIRST_BENCODING, FIRST.bencode());
        assertArrayEquals(SECOND_BENCODING, SECOND.bencode());
    }

    @Test
    public void testBufferConstructor() throws Exception {

        BencodableList decodedFirst = new BencodableList(ByteBuffer.wrap(FIRST.bencode()));
        assertEquals(decodedFirst, FIRST);

        BencodableList decodedSecond = new BencodableList(ByteBuffer.wrap(SECOND.bencode()));
        assertEquals(decodedSecond, SECOND);
    }

    @Test(expected=InvalidDelimiterException.class)
    public void testInvalidDelimiterException() throws Exception {
        new BencodableList(ByteBuffer.wrap(new byte[] {'?','e'}));
    }
}