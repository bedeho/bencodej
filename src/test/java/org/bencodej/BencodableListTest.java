package org.bencodej;

import org.bencodej.exception.InvalidDelimiterException;
import org.bencodej.exception.InvalidLengthFieldException;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class BencodableListTest {

    private BencodableList FIRST;
    private BencodableObject [] FIRST_LIST = {new BencodableInteger(1), new BencodableByteString(new byte[] {'g','f','3','4','5'}), new BencodableInteger(3)};
    private byte [] FIRST_BENCODING = {'l','i','1','e','5',':','g','f','3','4','5','i','3','e','e'}; //"l i1e 5:gf345 i3e e"

    private BencodableList SECOND;
    private BencodableObject [] SECOND_LIST = {new BencodableInteger(123), new BencodableByteString(new byte [] {'D','D','W'}), new BencodableInteger(-5)};
    private byte [] SECOND_BENCODING = "l i123e 3:DDW i-5e e".replaceAll(" ", "").getBytes();

    @Before
    public void setUp() throws Exception {

        FIRST = new BencodableList(FIRST_LIST);
        SECOND = new BencodableList(SECOND_LIST);
    }

    @Test
    public void testBencode() throws Exception {

        assertArrayEquals(FIRST_BENCODING, FIRST.bencode());
        assertArrayEquals(SECOND_BENCODING, SECOND.bencode());
    }

    @Test
    public void testBufferConstructor() throws Exception {

        BencodableList decodedFirst = new BencodableList(ByteBuffer.wrap(FIRST.bencode()));
        assertEquals(decodedFirst.getList(), FIRST.getList());

        BencodableList decodedSecond = new BencodableList(ByteBuffer.wrap(SECOND.bencode()));
        assertEquals(decodedSecond.getList(), SECOND.getList());
    }

    @Test(expected=InvalidDelimiterException.class)
    public void testInvalidDelimiterException() throws Exception {
        new BencodableList(ByteBuffer.wrap(new byte[] {'?','e'}));
    }

    @Test
    public void testGetList() throws Exception {

        assertArrayEquals(FIRST_LIST, FIRST.getList().toArray());
        assertArrayEquals(SECOND_LIST, SECOND.getList().toArray());
    }

    @Test
    public void testSetList() throws Exception {

        FIRST.setList(SECOND_LIST);
        assertArrayEquals(SECOND_LIST, FIRST.getList().toArray());

        SECOND.setList(FIRST_LIST);
        assertArrayEquals(FIRST_LIST, SECOND.getList().toArray());
    }
}