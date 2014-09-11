package org.bencodej;

import org.bencodej.exception.EmptyIntegerException;
import org.bencodej.exception.InvalidDelimiterException;
import org.bencodej.exception.InvalidIntegerDigits;
import org.bencodej.exception.NegativeZeroException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.ByteBuffer;

public class BencodableIntegerTest {

    private BencodableInteger POSITIVE;
    private final int POSITIVE_TEST_VALUE = 12;
    private final byte [] POSITIVE_TEST_BENCODING = ("i" + POSITIVE_TEST_VALUE + "e").getBytes();

    private BencodableInteger NEGATIVE;
    private final int NEGATIVE_TEST_VALUE = -2;
    private final byte [] NEGATIVE_TEST_BENCODING = ("i" + NEGATIVE_TEST_VALUE + "e").getBytes();

    @Before
    public void setUp() throws Exception {

        POSITIVE = new BencodableInteger(POSITIVE_TEST_VALUE);
        NEGATIVE = new BencodableInteger(NEGATIVE_TEST_VALUE);
    }

    @Test
    public void testBencode() throws Exception {

        assertArrayEquals(POSITIVE_TEST_BENCODING, POSITIVE.bencode());
        assertArrayEquals(NEGATIVE_TEST_BENCODING, NEGATIVE.bencode());
    }

    @Test
    public void testBufferConstructor() throws Exception {

        BencodableInteger decodedPositiveInteger = new BencodableInteger(ByteBuffer.wrap(POSITIVE.bencode()));
        assertEquals(decodedPositiveInteger.getValue(), POSITIVE.getValue());

        BencodableInteger decodedNegativeInteger = new BencodableInteger(ByteBuffer.wrap(NEGATIVE.bencode()));
        assertEquals(decodedNegativeInteger.getValue(), NEGATIVE.getValue());
    }

    @Test(expected=InvalidDelimiterException.class)
    public void testInvalidDelimiterException() throws Exception {
        new BencodableInteger(ByteBuffer.wrap(new byte[] {'?','0','e'}));
    }

    @Test(expected=EmptyIntegerException.class)
    public void testEmptyIntegerException() throws Exception {
        new BencodableInteger(ByteBuffer.wrap("ie".getBytes()));
    }

    @Test(expected=NegativeZeroException.class)
    public void testNegativeZeroException() throws Exception {
        new BencodableInteger(ByteBuffer.wrap("i-0e".getBytes()));
    }

    @Test(expected=InvalidIntegerDigits.class)
    public void testInvalidIntegerDigits() throws Exception {
        new BencodableInteger(ByteBuffer.wrap(new byte[] {'i','?','e'}));
    }

    @Test
    public void testGetValue() throws Exception {

        assertEquals(POSITIVE_TEST_VALUE, POSITIVE.getValue());
        assertEquals(NEGATIVE_TEST_VALUE, NEGATIVE.getValue());
    }

    @Test
    public void testSetValue() throws Exception {
        POSITIVE.setValue(NEGATIVE_TEST_VALUE);
        assertEquals(NEGATIVE_TEST_VALUE, POSITIVE.getValue());

        NEGATIVE.setValue(POSITIVE_TEST_VALUE);
        assertEquals(POSITIVE_TEST_VALUE, NEGATIVE.getValue());
    }
}