package com.jenkov.container.script;

import junit.framework.TestCase;

import java.nio.charset.Charset;
import java.util.SortedMap;

/**

 */
public class CharsetTest extends TestCase {

    public void testCharset() {

        SortedMap<String, Charset> charsetSortedMap = Charset.availableCharsets();

        for (Charset charset : charsetSortedMap.values()) {
            System.out.println("charset.aliases() = " + charset.aliases());
        }

        Charset charset = Charset.forName("UTF-16");
        assertNotNull(charset);

        //Reader reader = new InputStreamReader(null, Charset.forName("UTF-16"));
    }
}
