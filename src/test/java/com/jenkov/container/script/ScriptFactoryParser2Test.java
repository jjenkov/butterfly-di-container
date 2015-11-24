
package com.jenkov.container.script;

import junit.framework.TestCase;

import java.io.IOException;
import java.util.List;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class ScriptFactoryParser2Test extends TestCase {

    ScriptFactoryParser parser        = new ScriptFactoryParser();

    public void testParseDecimalNumber(){
        String inputString = "decimal = * 1.45;";
        ParserInput input = parserInput(inputString);
        FactoryDefinition definition = parser.parseFactory(input);
        assertEquals("1.45", definition.getIdentifier());
        assertTrue(definition.isValueFactory());
        assertEquals("*", definition.getMode());

        inputString = "decimal = 1.45;";
        input = parserInput(inputString);
        definition = parser.parseFactory(input);
        assertEquals("1.45", definition.getIdentifier());
        assertTrue(definition.isValueFactory());
        assertEquals("1", definition.getMode());

    }

    public void testParseNameWithPeriod() {
        String inputString = "/myApp/subDir/myAction.html = * com.jenkov.container.script.SomeFactoryProduct($0);";
        ParserInput input = parserInput(inputString);

        FactoryDefinition definition = parser.parseFactory(input);

        assertEquals("/myApp/subDir/myAction.html", definition.getName());
        assertNotNull(definition.getInstantiationArgFactories());

        FactoryDefinition inputParamDefinition = definition.getInstantiationArgFactories().get(0);
        assertEquals("0", inputParamDefinition.getIdentifier());
    }

    public void testParseMode(){
        String inputString = "test3 = * com.jenkov.container.TestProduct.createProduct(test).getInternalProduct();";
        ParserInput input = parserInput(inputString);

        FactoryDefinition definition = parser.parseFactory(input);
        assertEquals("*", definition.getMode());
        assertFalse(definition.isSingleton());
    }


    public void testParseInputParameters() {
        String inputString = "name = * com.jenkov.container.script.SomeFactoryProduct($0);";
        ParserInput input = parserInput(inputString);

        FactoryDefinition definition = parser.parseFactory(input);

        assertEquals("name", definition.getName());
        assertNotNull(definition.getInstantiationArgFactories());

        FactoryDefinition inputParamDefinition = definition.getInstantiationArgFactories().get(0);
        assertEquals("0", inputParamDefinition.getIdentifier());


        inputString = "name2 = * name(\"hello world\");";
        definition = parser.parseFactory(parserInput(inputString));
        assertEquals("name2", definition.getName());
        assertEquals("name", definition.getIdentifier());
        assertEquals(1, definition.getInstantiationArgFactories().size());
        inputParamDefinition = definition.getInstantiationArgFactories().get(0);
        assertEquals("\"hello world\"", inputParamDefinition.getIdentifier());
    }

    public void testParseFactory() throws IOException {
        String inputString =
                "name = * com.jenkov.container.script.SomeFactoryProduct(other, someOther);" +
                "   config{ name.setText(\"someText\");" +
                "           name.setName(\"someId\");" +
                "   }" +
                "   dispose{$name.dispose();}";

        ParserInput input = parserInput(inputString);

        FactoryDefinition definition = parser.parseFactory(input);
        assertEquals("name", definition.getName());
        assertEquals("com.jenkov.container.script.SomeFactoryProduct", definition.getIdentifierOwnerClass());
        assertEquals(2, definition.getInstantiationArgFactories().size());
        assertEquals("other"    , definition.getInstantiationArgFactories().get(0).getIdentifier());
        assertEquals("someOther", definition.getInstantiationArgFactories().get(1).getIdentifier());

//        assertEquals(2, definition.getConfigurationFactories().size());
        assertEquals(2, definition.getPhaseFactories().get("config").size());
        assertEquals("setText", definition.getPhaseFactories().get("config").get(0).getIdentifier());
        assertEquals("setName", definition.getPhaseFactories().get("config").get(1).getIdentifier());
        assertEquals("name", definition.getPhaseFactories().get("config").get(0).getIdentifierTargetFactory().getIdentifier());
        assertEquals("name", definition.getPhaseFactories().get("config").get(1).getIdentifierTargetFactory().getIdentifier());


//        assertEquals("dispose", definition.getDisposeFactory().getIdentifier());
//        assertEquals("name", definition.getDisposeFactory().getIdentifierTargetFactory().getIdentifier());
        //fail("not finished...");

    }


    public void testParseConfigFactoryChains() throws IOException {
        String inputString = "smtp.setName(); \n" +
                             "com.jenkov.container.TestProduct.config(smtp); }";

        ParserInput input = parserInput(inputString);

        List<FactoryDefinition> definitions = parser.parsePhaseFactoryChains(input);

        FactoryDefinition definition1 = definitions.get(0);
        assertEquals("setName", definition1.getIdentifier());
        assertEquals("smtp", definition1.getIdentifierTargetFactory().getIdentifier());
        assertNull(definition1.getIdentifierOwnerClass());

        FactoryDefinition definition2 = definitions.get(1);
        assertEquals("config", definition2.getIdentifier());
        assertEquals("com.jenkov.container.TestProduct", definition2.getIdentifierOwnerClass());
        assertNull(definition2.getIdentifierTargetFactory());
        assertEquals("smtp", definition2.getInstantiationArgFactories().get(0).getIdentifier());
    }

    public void testParseFactoryChainList() throws IOException {
        String expectedClassName = "com.jenkov.container.script.SomeFactoryProduct";
        String inputString       = "com.jenkov.container.TestProduct, " +
                                   "com.jenkov.container.script.SomeFactoryProduct()," +
                                   "someId)";

        ParserInput input       = parserInput(inputString);
        List<FactoryDefinition> definitions = parser.parseFactoryChainList(input);
        assertEquals(Token.PARENTHESIS_RIGHT, input.nextToken());
        assertEquals(3, definitions.size());

        FactoryDefinition definition1 = definitions.get(0);
        assertEquals("com.jenkov.container.TestProduct", definition1.getIdentifier());

        FactoryDefinition definition2 = definitions.get(1);
        assertEquals("com.jenkov.container.script.SomeFactoryProduct", definition2.getIdentifierOwnerClass());

        FactoryDefinition definition3 = definitions.get(2);
        assertEquals("someId", definition3.getIdentifier());

    }

    public void testParseFactoryChain() throws Exception {
        String expectedClassName = "com.jenkov.container.script.SomeFactoryProduct";
        String inputString = null;

        inputString = "com.jenkov.container.script.SomeFactoryProduct.CONSTANT.method().OTHER.otherMethod();";
        ParserInput input      = parserInput(inputString);
        FactoryDefinition definition = parser.parseFactoryChain(input);
        assertNotNull(definition);

        assertEquals("otherMethod", definition.getIdentifier());
        assertNotNull(definition.getIdentifierTargetFactory());
        assertNull(definition.getIdentifierOwnerClass());

        definition = definition.getIdentifierTargetFactory();
        assertEquals("OTHER", definition.getIdentifier());
        assertNotNull(definition.getIdentifierTargetFactory());
        assertNull(definition.getIdentifierOwnerClass());

        definition = definition.getIdentifierTargetFactory();
        assertEquals("method", definition.getIdentifier());
        assertNotNull(definition.getIdentifierTargetFactory());
        assertNull(definition.getIdentifierOwnerClass());

        definition = definition.getIdentifierTargetFactory();
        assertEquals("CONSTANT", definition.getIdentifier());
        assertEquals(expectedClassName, definition.getIdentifierOwnerClass());
        assertNull(definition.getIdentifierTargetFactory());

        assertEquals(Token.SEMI_COLON, input.nextToken());   //factory chain eats the delimiter after it... no use for it...?


        inputString = "com.jenkov.container.script.SomeFactoryProduct;";
        input      = parserInput(inputString);
        definition = parser.parseFactoryChain(input);
        assertNotNull(definition);
        assertEquals(expectedClassName, definition.getIdentifier());
        assertNull(definition.getIdentifierOwnerClass());

        assertEquals(Token.SEMI_COLON, input.nextToken());

    }

    public void testParseHeadFactory() throws IOException {
        String inputString = null;
        String expectedClassName = "com.jenkov.container.script.SomeFactoryProduct";


        inputString = "com.jenkov.container.script.SomeFactoryProduct;";
        ParserInput input      = parserInput(inputString);
        FactoryDefinition definition = parser.parseHeadFactory(input, null);
        assertNotNull(definition);
        assertEquals(expectedClassName, definition.getIdentifier());
        assertEquals(Token.SEMI_COLON, input.nextToken());

        inputString = "com.jenkov.container.script.SomeFactoryProduct(other, someOther);";
        input       = parserInput(inputString);
        definition  = parser.parseHeadFactory(input, null);
        assertNotNull(definition);
        assertEquals(expectedClassName, definition.getIdentifierOwnerClass());
        assertEquals(Token.SEMI_COLON, input.nextToken());

        inputString = "com.jenkov.container.script.SomeFactoryProduct.CONSTANT;";
        input       = parserInput(inputString);
        definition  = parser.parseHeadFactory(input, null);
        assertNotNull(definition);
        assertEquals(expectedClassName, definition.getIdentifierOwnerClass());
        assertEquals("CONSTANT", definition.getIdentifier());
        assertEquals(Token.SEMI_COLON, input.nextToken());

        inputString = "com.jenkov.container.script.SomeFactoryProduct.CONSTANT.method()";
        input       = parserInput(inputString);
        definition  = parser.parseHeadFactory(input, null);
        assertNotNull(definition);
        assertEquals(expectedClassName, definition.getIdentifierOwnerClass());
        assertEquals("CONSTANT", definition.getIdentifier());
        assertEquals(Token.DOT, input.nextToken());

        inputString = "com.jenkov.container.script.SomeFactoryProduct.method()";
        input       = parserInput(inputString);
        definition  = parser.parseHeadFactory(input, null);
        assertNotNull(definition);
        assertEquals(expectedClassName, definition.getIdentifierOwnerClass());
        assertEquals("method", definition.getIdentifier());
        assertNull(input.nextToken());

    }

    public void testParseClass() throws IOException{
        String className = null;
        ParserInput input = parserInput("com.jenkov.Fun");

        className = parser.parseClass(input);
        assertNull(className);
        assertEquals(new Token("com"), input.nextToken());

        String inputString       = "com.jenkov.container.script.SomeFactoryProduct";
        String expectedClassName = "com.jenkov.container.script.SomeFactoryProduct";
        input = parserInput(inputString);
        className = parser.parseClass(input);
        assertEquals(expectedClassName, className);
        assertNull(input.nextToken());

        inputString = "com.jenkov.container.script.SomeFactoryProduct.CONSTANT";
        input = parserInput(inputString);
        className = parser.parseClass(input);
        assertEquals(expectedClassName, className);
        assertEquals(Token.DOT, input.nextToken());
        assertEquals(new Token("CONSTANT"), input.nextToken());
        assertNull(input.nextToken());

        inputString = "com.jenkov.container.script.SomeFactoryProduct.method()";
        input = parserInput(inputString);
        className = parser.parseClass(input);
        assertEquals(expectedClassName, className);
        assertEquals(Token.DOT, input.nextToken());
        assertEquals(new Token("method"), input.nextToken());
        assertEquals(Token.PARENTHESIS_LEFT, input.nextToken());
        assertEquals(Token.PARENTHESIS_RIGHT, input.nextToken());
        assertNull(input.nextToken());

    }

    private ParserInput parserInput(String inputString) {
        return new ParserInput(inputString);
//        return new ParserInput(new ByteArrayInputStream(inputString.getBytes()));
    }

}