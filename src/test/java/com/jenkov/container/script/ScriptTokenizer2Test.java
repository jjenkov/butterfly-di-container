package com.jenkov.container.script;

import junit.framework.TestCase;

import java.io.StringReader;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class ScriptTokenizer2Test extends TestCase {

    public void testNextToken(){
        String script = "   bean = * com;\n    bean2 = * blablabla();\n \n   \n\r \r\n";
        ScriptTokenizer tokenizer = new ScriptTokenizer(new ScriptTokenizerInputBuffer(new StringReader(script)));

        Token token = tokenizer.nextToken();
        assertEquals(1, token.getLineNo());
        assertEquals(4, token.length());
        assertEquals(3, token.getFrom());
        assertEquals('b', token.getBuffer()[token.getFrom()]);
        assertEquals('e', token.getBuffer()[token.getFrom() + 1]);
        assertEquals('a', token.getBuffer()[token.getFrom() + 2]);
        assertEquals('n', token.getBuffer()[token.getFrom() + 3]);

        assertEquals(4, token.getCharNoBefore());
//        assertEquals(, );

        token = tokenizer.nextToken();
        assertEquals(1, token.getLineNo());
        assertEquals(1, token.length());
        assertEquals(8, token.getFrom());
        assertEquals('=', token.getBuffer()[token.getFrom()]);

        token = tokenizer.nextToken();
        assertEquals(1, token.getLineNo());
        assertEquals(1, token.length());
        assertEquals(10, token.getFrom());
        assertEquals('*', token.getBuffer()[token.getFrom()]);

        token = tokenizer.nextToken();
        assertEquals(1, token.getLineNo());
        assertEquals(3, token.length());
        assertEquals(12, token.getFrom());
        assertEquals('c', token.getBuffer()[token.getFrom()]);
        assertEquals('o', token.getBuffer()[token.getFrom() + 1]);
        assertEquals('m', token.getBuffer()[token.getFrom() + 2]);

        token = tokenizer.nextToken();
        assertEquals(1, token.getLineNo());
        assertEquals(1, token.length());
        assertEquals(15, token.getFrom());
        assertEquals(';', token.getBuffer()[token.getFrom()]);

        //line 2
        token = tokenizer.nextToken();
        assertEquals(2, token.getLineNo());
        assertEquals(5, token.length());
        assertEquals(21, token.getFrom());
        assertEquals('b', token.getBuffer()[token.getFrom()]);
        assertEquals('e', token.getBuffer()[token.getFrom() + 1]);
        assertEquals('a', token.getBuffer()[token.getFrom() + 2]);
        assertEquals('n', token.getBuffer()[token.getFrom() + 3]);
        assertEquals('2', token.getBuffer()[token.getFrom() + 4]);

        token = tokenizer.nextToken();
        assertEquals(2, token.getLineNo());
        assertEquals(1, token.length());
        assertEquals(27, token.getFrom());
        assertEquals('=', token.getBuffer()[token.getFrom()]);

        token = tokenizer.nextToken();
        assertEquals(2, token.getLineNo());
        assertEquals(1, token.length());
        assertEquals(29, token.getFrom());
        assertEquals('*', token.getBuffer()[token.getFrom()]);

        token = tokenizer.nextToken();
        assertEquals(2, token.getLineNo());
        assertEquals(9, token.length());
        assertEquals(31, token.getFrom());
        assertEquals('b', token.getBuffer()[token.getFrom()]);
        assertEquals('l', token.getBuffer()[token.getFrom() + 1]);
        assertEquals('a', token.getBuffer()[token.getFrom() + 2]);
        assertEquals('b', token.getBuffer()[token.getFrom() + 3]);
        assertEquals('l', token.getBuffer()[token.getFrom() + 4]);
        assertEquals('a', token.getBuffer()[token.getFrom() + 5]);
        assertEquals('b', token.getBuffer()[token.getFrom() + 6]);
        assertEquals('l', token.getBuffer()[token.getFrom() + 7]);
        assertEquals('a', token.getBuffer()[token.getFrom() + 8]);

        token = tokenizer.nextToken();
        assertEquals(2, token.getLineNo());
        assertEquals(1, token.length());
        assertEquals(40, token.getFrom());
        assertEquals('(', token.getBuffer()[token.getFrom()]);

        token = tokenizer.nextToken();
        assertEquals(2, token.getLineNo());
        assertEquals(1, token.length());
        assertEquals(41, token.getFrom());
        assertEquals(')', token.getBuffer()[token.getFrom()]);

        token = tokenizer.nextToken();
        assertEquals(2, token.getLineNo());
        assertEquals(1, token.length());
        assertEquals(42, token.getFrom());
        assertEquals(';', token.getBuffer()[token.getFrom()]);

        token = tokenizer.nextToken();
        assertNull(token);



    }
}
