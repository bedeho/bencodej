package org.bencodej;

import org.bencodej.exception.EmptyLengthFieldException;
import org.bencodej.exception.LengthFieldToGreatException;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class BencodableByteStringTest {

    private BencodableByteString FIRST;
    private final byte[] FIRST_BYTESTRING = {1,2,34,12,52,86,5};
    private final byte[] FIRST_BENCODING = {'7',':',1,2,34,12,52,86,5};

    private BencodableByteString SECOND;
    private final byte[] SECOND_BYTESTRING = {1,2,34,12,52,86,4,8};
    private final byte[] SECOND_BENCODING = {'8',':',1,2,34,12,52,86,4,8};

    @Before
    public void setUp() throws Exception {

        FIRST = new BencodableByteString(FIRST_BYTESTRING);
        SECOND = new BencodableByteString(SECOND_BYTESTRING);
    }

    @Test
    public void testBencode() throws Exception {

        assertArrayEquals(FIRST_BENCODING, FIRST.bencode());
        assertArrayEquals(SECOND_BENCODING, SECOND.bencode());
    }


    /*
    @Test
    public void testBufferConstructor() throws Exception {

        BencodableList decodedFirst = new BencodableList(ByteBuffer.wrap(FIRST.bencode()));
        assertEquals(decodedFirst.getList(), FIRST.getList());

        BencodableList decodedSecond = new BencodableList(ByteBuffer.wrap(SECOND.bencode()));
        assertEquals(decodedSecond.getList(), SECOND.getList());
    }
    */

    @Test
    public void testCompareTo() throws Exception {

        // Correctly compare equals
        assertEquals(0, FIRST.compareTo(FIRST));
        assertEquals(0, SECOND.compareTo(SECOND));

        //Correctly compare non equals: '1' - '0' = 1
        assertEquals(1, FIRST.compareTo(SECOND));
    }

    @Test
    public void testEquals() throws Exception {

        // Correctly compare equals
        assertEquals(FIRST, FIRST);
        assertEquals(SECOND, SECOND);

        // Correctly compare non equals
        assertNotEquals(FIRST, SECOND);
        assertNotEquals(SECOND, FIRST);
    }

    @Test(expected=EmptyLengthFieldException.class)
    public void testEmptyLengthFieldException() throws Exception {
        new BencodableByteString(ByteBuffer.wrap(":".getBytes()));
    }

    @Test(expected=LengthFieldToGreatException.class)
    public void testLengthFieldToGreatException() throws Exception {
        new BencodableByteString(ByteBuffer.wrap(((BencodableByteString.MAX_LENGTH_FIELD_TO_TRUST + 1) + ":").getBytes()));
    }

    @Test
    public void testGetByteString() throws Exception {

        assertArrayEquals(FIRST_BYTESTRING, FIRST.getByteString());
        assertArrayEquals(SECOND_BYTESTRING, SECOND.getByteString());
    }

    @Test
    public void testSetByteString() throws Exception {

        FIRST.setByteString(SECOND_BYTESTRING);
        assertArrayEquals(SECOND_BYTESTRING, FIRST.getByteString());

        SECOND.setByteString(FIRST_BYTESTRING);
        assertArrayEquals(FIRST_BYTESTRING, SECOND.getByteString());
    }
}