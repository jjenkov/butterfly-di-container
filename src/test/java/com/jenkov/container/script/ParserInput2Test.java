package com.jenkov.container.script;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class ParserInput2Test extends TestCase {

    final String inputString = "A man came through the door, and {he laughed)\n" +
            "Then a (woman came} too, and ; was a real annoying incident\n" +
            "   \n" +
            "\n" +
            "\n\n" +
            "lalala";

    public void testNextToken() throws IOException {

        ParserInput input = new ParserInput(new ByteArrayInputStream(inputString.getBytes()));

        assertParse(input, "A", 1, 2);
        assertParse(input, "man", 1, 6);
        assertParse(input, "came", 1, 11);
        assertParse(input, "through", 1, 19);
        assertParse(input, "the", 1, 23);
        assertParse(input, "door", 1, 28);
        assertParse(input, ",", 1, 29);
        assertParse(input, "and", 1, 33);
        assertParse(input, "{", 1, 35);
        assertParse(input, "he", 1, 37);
        assertParse(input, "laughed", 1, 45);
        assertParse(input, ")", 1, 46);

        assertParse(input, "Then", 2, 5);
        assertParse(input, "a", 2, 7);
        assertParse(input, "(", 2, 9);
        assertParse(input, "woman", 2, 14);
        assertParse(input, "came", 2, 19);
        assertParse(input, "}", 2, 20);
        assertParse(input, "too", 2, 24);
        assertParse(input, ",", 2, 25);
        assertParse(input, "and", 2, 29);
        assertParse(input, ";", 2, 31);
        assertParse(input, "was", 2, 35);
        assertParse(input, "a", 2, 37);
        assertParse(input, "real", 2, 42);
        assertParse(input, "annoying", 2, 51);
        assertParse(input, "incident", 2, 60);

        assertParse(input, "lalala", 7, 7);

        assertNull(input.nextToken());
        assertEquals(7, input.scriptTokenizer.getLineNo());
        assertEquals(7, input.scriptTokenizer.getCharNo());

    }

    public void testPushPopMarks() throws IOException {
        ParserInput input = new ParserInput(new ByteArrayInputStream(inputString.getBytes()));

        assertParse(input, "A", 1, 2);
        assertFalse(input.hasMark());

        input.mark();
        assertParse(input, "man", 1, 6);
        assertParse(input, "came", 1, 11);
        assertParse(input, "through", 1, 19);

        input.mark();
        assertParse(input, "the", 1, 23);
        assertParse(input, "door", 1, 28);
        assertParse(input, ",", 1, 29);

        input.backtrack();
        assertEquals(19, input.scriptTokenizer.getCharNo());
        assertEquals(18, input.scriptTokenizer.inputBuffer.index);

        assertParse(input, "the", 1, 23);
        assertParse(input, "door", 1, 28);
        assertParse(input, ",", 1, 29);

        assertParse(input, "and", 1, 33);
        assertParse(input, "{", 1, 35);
        assertParse(input, "he", 1, 37);
        assertParse(input, "laughed", 1, 45);
        assertParse(input, ")", 1, 46);

        assertParse(input, "Then", 2, 5);
        assertParse(input, "a", 2, 7);
        assertParse(input, "(", 2, 9);
        assertParse(input, "woman", 2, 14);
        assertParse(input, "came", 2, 19);
        assertParse(input, "}", 2, 20);

        input.backtrack();
        assertParse(input, "man", 1, 6);
        assertParse(input, "came", 1, 11);
        assertParse(input, "through", 1, 19);
    }

    private void assertParse(ParserInput input, String expectedValue, int lineNo, int charNo) {
        Token nextToken = input.nextToken();

        Token expected = new Token();
        expected.setBuffer(expectedValue.toCharArray());
        expected.setFrom(0);
        expected.setLength(expected.getBuffer().length);

        assertEquals(expected, nextToken);
        assertEquals(lineNo, input.scriptTokenizer.getLineNo());
        assertEquals(charNo, input.scriptTokenizer.getCharNo());
    }


}