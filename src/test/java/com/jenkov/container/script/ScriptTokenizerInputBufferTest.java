package com.jenkov.container.script;

import junit.framework.TestCase;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class ScriptTokenizerInputBufferTest extends TestCase {

    public void testFactoryStart_noCopy() throws IOException {

        Reader reader = new StringReader("0123456789ABCDEFGHIJ");
        ScriptTokenizerInputBuffer buffer = new ScriptTokenizerInputBuffer(reader, 10);

        assertEquals(0, buffer.index);
        assertEquals(10, buffer.endIndex);
        assertEquals(0, buffer.factoryStartIndex);

        buffer.factoryStart();
        assertEquals(0, buffer.index);
        assertEquals(10, buffer.endIndex);
        assertEquals(0, buffer.factoryStartIndex);

        assertEquals('0', buffer.read());
        assertEquals('1', buffer.read());
        assertEquals('2', buffer.read());
        assertEquals('3', buffer.read());
        assertEquals('4', buffer.read());
        assertEquals('5', buffer.read());
        assertEquals('6', buffer.read());
        assertEquals('7', buffer.read());
        assertEquals('8', buffer.read());
        assertEquals('9', buffer.read());

        buffer.factoryStart();
        assertEquals(10, buffer.factoryStartIndex);
        assertEquals(10, buffer.index);
        assertEquals(10, buffer.endIndex);
    }

    public void testFactoryStart_copy() throws IOException {

        Reader reader = new StringReader("0123456789ABCDEFGHIJ");
        ScriptTokenizerInputBuffer buffer = new ScriptTokenizerInputBuffer(reader, 1);

        assertEquals('0', buffer.read());
        buffer.factoryStart();
        assertEquals('1', buffer.read());
        buffer.factoryStart();
        assertEquals('2', buffer.read());
        buffer.factoryStart();
        assertEquals('3', buffer.read());

        buffer.factoryStart();
        assertEquals('4', buffer.read());
        buffer.factoryStart();
        assertEquals('5', buffer.read());
        buffer.factoryStart();
        assertEquals('6', buffer.read());
        buffer.factoryStart();
        assertEquals('7', buffer.read());

        buffer.factoryStart();
        assertEquals('8', buffer.read());
        buffer.factoryStart();
        assertEquals('9', buffer.read());
        buffer.factoryStart();
        assertEquals('A', buffer.read());
        buffer.factoryStart();
        assertEquals('B', buffer.read());

        buffer.factoryStart();
        assertEquals('C', buffer.read());
        buffer.factoryStart();
        assertEquals('D', buffer.read());
        buffer.factoryStart();
        assertEquals('E', buffer.read());
        buffer.factoryStart();
        assertEquals('F', buffer.read());

        buffer.factoryStart();
        assertEquals('G', buffer.read());
        buffer.factoryStart();
        assertEquals('H', buffer.read());
        buffer.factoryStart();
        assertEquals('I', buffer.read());
        buffer.factoryStart();
        assertEquals('J', buffer.read());
        buffer.factoryStart();
        assertEquals(-1, buffer.read());
        assertTrue(buffer.endOfStreamReached);
        assertEquals(-1, buffer.read());
    }

}
