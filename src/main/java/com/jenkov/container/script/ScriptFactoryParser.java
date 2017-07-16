package com.jenkov.container.script;

import com.jenkov.container.ContainerException;
import com.jenkov.container.impl.factory.FactoryUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class ScriptFactoryParser {

    protected FactoryDefinition parseFactory(ParserInput input) {
        input.factoryStart();
        if (input.lookAhead() == null) return null;
        String id = parseGlobalFactoryName(input);
        input.assertNextToken(Token.EQUALS);
        Token mode = parseMode(input);

        FactoryDefinition definition = null;
        try {
            definition = parseFactoryChain(input);
        } catch (ContainerException e) {
            e.addInfo("ScriptFactoryParser", "ERROR_PARSING_FACTORY", "Error in factory definition " + id);
            throw e;
        } catch (Exception e) {
            throw new ParserException("ScriptFactoryParser", "ERROR_PARSING_FACTORY", "Error in factory definition " + id, e);
        }
        definition.setName(id);
        definition.setMode(mode.toString());
        input.assertNextToken(Token.SEMI_COLON);

        boolean hasPhases = true;
        while (hasPhases) {
            input.mark();
            Token phaseToken = input.nextToken();
            if (phaseToken == null) {
                hasPhases = false;
                input.backtrack();
                break;
            }
            String phase = phaseToken.toString();
            Token delimiter = input.nextToken();
            if (!Token.CURLY_LEFT.equals(delimiter)) {
                hasPhases = false;
                input.backtrack();
                break;
            }

            List<FactoryDefinition> phaseFactories = null;
            try {
                phaseFactories = parsePhaseFactoryChains(input);
            } catch (ContainerException e) {
                e.addInfo("ScriptFactoryParser", "ERROR_PARSING_FACTORY_PHASES", "Error in factory definition " + id + " in phases definition");
                throw e;
            } catch (Exception e) {
                throw new ParserException("ScriptFactoryParser", "ERROR_PARSING_FACTORY_PHASES", "Error in factory definition " + id + " in phases definition");
            }

            if (definition.getPhaseFactories() == null) {
                definition.setPhaseFactories(new HashMap<>());
            }
            List<FactoryDefinition> currentPhaseFactories = definition.getPhaseFactories().get(phase);
            if (currentPhaseFactories == null) {
                definition.getPhaseFactories().put(phase, phaseFactories);
            } else {
                currentPhaseFactories.addAll(phaseFactories);
            }

            input.assertNextToken(Token.CURLY_RIGHT);
            input.clearMark();
        }

        definition.init();

        return definition;
    }


    protected List<FactoryDefinition> parsePhaseFactoryChains(ParserInput input) {
        List<FactoryDefinition> configDefinitions = new ArrayList<>();

        Token delimiter = input.lookAhead();
        while (!Token.CURLY_RIGHT.equals(delimiter)) {
            FactoryDefinition chainDefinition = parseFactoryChain(input);
            input.assertNextToken(Token.SEMI_COLON);
            configDefinitions.add(chainDefinition);

            input.mark();
            delimiter = input.nextToken();
            input.backtrack();
        }

        return configDefinitions;
    }


    protected FactoryDefinition parseMapFactory(ParserInput input, FactoryDefinition definition) {
        definition.setFactoryType(FactoryDefinition.MAP_FACTORY);
        definition.setIdentifier("<>");
        definition.setIdentifierOwnerClass("java.util.HashMap");

        List<FactoryDefinition> keyFactories = new ArrayList<>();
        List<FactoryDefinition> valueFactories = new ArrayList<>();

        definition.setInstantiationArgKeyFactories(keyFactories);
        definition.setInstantiationArgFactories(valueFactories);

        Token nextToken = input.lookAhead();
        while (!Token.GREATER_THAN.equals(nextToken)) {
            FactoryDefinition keyFactory = parseFactoryChain(input);
            keyFactories.add(keyFactory);

            nextToken = input.nextToken();
            if (!Token.COLON.equals(nextToken)) {
                throw new ParserException("ScriptFactoryParser", "KEY_VALUE_DELIMITER_ERROR",
                        "Keys and values in maps must be separated by colons, not " + nextToken);
            }

            FactoryDefinition valueFactory = parseFactoryChain(input);
            valueFactories.add(valueFactory);

            nextToken = input.nextToken();
            if (!Token.COMMA.equals(nextToken) && !Token.GREATER_THAN.equals(nextToken)) {
                throw new ParserException("ScriptFactoryParser", "KEY_VALUE_PAIR_DELIMITER_ERROR",
                        "Key : value pairs in maps must be separated by commas, not " + nextToken);
            }
        }

        return definition;

    }

    /**
     * Parses a comma separated list of factory chains. It will not work with semi-colon separated lists. Only comma.
     *
     * @param input The ParserInput to parse the factory chain list from.
     * @return A list of factory definitions coresponding to the factory chain lists parsed.
     */
    protected List<FactoryDefinition> parseFactoryChainList(ParserInput input) {
        List<FactoryDefinition> factoryChainList = new ArrayList<>(); //todo optimize out to a constant EMPTY_LIST for performance

        Token nextToken = input.lookAhead();
        while (!isEndOfFactoryChainList(nextToken)) {

            FactoryDefinition factoryChain = parseFactoryChain(input);
            factoryChainList.add(factoryChain);

            input.mark();
            nextToken = input.nextToken();
            if (Token.COMMA.equals(nextToken)) input.clearMark();
            else if (isEndOfFactoryChainList(nextToken)) input.backtrack();
            else throw new ParserException(
                        "FactoryBuilder", "FIELD_FACTORY_ERROR",
                        "Factory chains in lists must be separated by ','  not by '" + nextToken + "'");
        }

        return factoryChainList;
    }

    protected FactoryDefinition parseFactoryChain(ParserInput input) {
        input.mark();
        String forcedReturnType = null;

        //forced return type .. casting
        forcedReturnType = parseForcedReturnType(input, forcedReturnType);

        input.mark();
        Token name = input.nextToken();
        Token delimiter = input.nextToken();
        Token mode = Token.SINGLETON;               //default to singleton
        if (Token.EQUALS.equals(delimiter)) {
            input.clearMark();
            mode = parseMode(input);
        } else {
            name = null;
            input.backtrack();
        }


        FactoryDefinition definition = parseHeadFactory(input, null);
        definition.setName(name != null ? name.toString() : null);
        definition.setMode(mode.toString());
        definition.setForcedReturnType(forcedReturnType);

        delimiter = input.lookAhead();
        delimiter = input.lookAhead();
        delimiter = input.lookAhead();
        delimiter = input.lookAhead();
        if (Token.SEMI_COLON.equals(delimiter)) return definition;
        if (Token.DOT.equals(delimiter)) {
            definition = parseTailFactoryChain(input, definition);
        }

        // com.jenkov.TestProduct());          ;
        // com.jenkov.TestProdust().setMethod(test)     ;


        return definition;
    }

    private String parseForcedReturnType(ParserInput input, String forcedReturnType) {
        if (input.isNextElseBacktrack(Token.PARENTHESIS_LEFT)) {
            forcedReturnType = parseClass(input);

            input.mark();
            if (Token.SQUARE_LEFT.equals(input.nextToken()) && Token.SQUARE_RIGHT.equals(input.nextToken())) {
                forcedReturnType += "[]";
                input.clearMark();
            } else {
                input.backtrack();
            }
            input.assertNextToken(Token.PARENTHESIS_RIGHT);
            input.clearMark();
        }
        return forcedReturnType;
    }

    private FactoryDefinition parseTailFactoryChain(ParserInput input, FactoryDefinition definition) {
        input.mark();
        Token delimiter = input.nextToken();
        if (delimiter == null) throw new ParserException(
                "ScriptFactoryParser", "PARSE_TAIL_FACTORY_CHAIN",
                "Unexpected end-of-line encountered on line: " + input.scriptTokenizer.getLineNo() +
                        ". Perhaps () or ; (or both) missing?");
        while (!isEndOfFactoryChain(delimiter)) {
            input.clearMark();
            definition = parseTailFactory(input, definition);

            input.mark();
            delimiter = input.nextToken();
        }

        input.backtrack();  // put end-token back into input.
        return definition;
    }

    protected FactoryDefinition parseHeadFactory(ParserInput input, FactoryDefinition definition) {
        FactoryDefinition headFactory = definition;
        if (headFactory == null) headFactory = new FactoryDefinition();

        input.mark();
        Token nextToken = input.nextToken();
        Token delimiter = null;

        if (nextToken.startsWith(Token.HASH)) {
            nextToken.right(1);
            headFactory.setFactoryType(FactoryDefinition.FACTORY_FACTORY);
            headFactory.setIdentifier(nextToken.toString());
            input.clearMark();
            return headFactory;
        } else if (nextToken.startsWith(Token.DOLLAR)) {
            nextToken.right(1);
            if (nextToken.isInteger()) {
                headFactory.setFactoryType(FactoryDefinition.INPUT_PARAMETER_FACTORY);
                headFactory.setIdentifier(nextToken.toString());
            } else {
                headFactory.setFactoryType(FactoryDefinition.LOCAL_PRODUCT_FACTORY);
                headFactory.setIdentifier(nextToken.toString());
            }
            input.clearMark();
            return headFactory;
        } else if (Token.SQUARE_LEFT.equals(nextToken)) {
            headFactory.setFactoryType(FactoryDefinition.COLLECTION_FACTORY);
            headFactory.setIdentifier("[]");
            headFactory.setInstantiationArgFactories(parseFactoryChainList(input));
            input.assertNextToken(Token.SQUARE_RIGHT);
            input.clearMark();
            return headFactory;
        } else if (Token.LESS_THAN.equals(nextToken)) {
            parseMapFactory(input, headFactory);
//            input.assertNextToken(Token.GREATER_THAN);
            input.clearMark();
            return headFactory;

        } else if (isValue(nextToken)) {
            headFactory.setFactoryType(FactoryDefinition.VALUE_FACTORY);
            headFactory.setIdentifier(nextToken.toString());
            input.clearMark();
            return headFactory;
        }


        input.backtrack();
        String className = parseClass(input);
        if (className != null) {
            input.mark();
            delimiter = input.nextToken();
            if (Token.PARENTHESIS_LEFT.equals(delimiter)) {
                headFactory.setFactoryType(FactoryDefinition.CONSTRUCTOR_FACTORY);
                headFactory.setIdentifier("()");
                headFactory.setIdentifierOwnerClass(className);
                headFactory.setInstantiationArgFactories(parseFactoryChainList(input));
                input.assertNextToken(Token.PARENTHESIS_RIGHT);
                input.clearMark();
            } else if (Token.DOT.equals(delimiter)) {
                parseMethodOrFieldHeadFactory(input, headFactory, className);
                input.clearMark();
            } else if (isEndOfFactoryChain(delimiter)) {
                //factory chain is finished already. It was just a class, nothing else. Set as value factory.
                headFactory.setFactoryType(FactoryDefinition.VALUE_FACTORY);
                headFactory.setIdentifier(className);
                input.backtrack();
            }
        } else {
            nextToken = input.nextToken(); //re-take the token. it was pushed back when checking for class name earlier.
            headFactory.setFactoryType(FactoryDefinition.FACTORY_CALL_FACTORY);
            headFactory.setIdentifier(nextToken.toString());
            input.mark();
            nextToken = input.nextToken();
            if (Token.PARENTHESIS_LEFT.equals(nextToken)) {
                headFactory.setInstantiationArgFactories(parseFactoryChainList(input));
                input.assertNextToken(Token.PARENTHESIS_RIGHT);
            } else {
                input.backtrack();
            }
        }


        return headFactory;
    }

    protected FactoryDefinition parseTailFactory(ParserInput input, FactoryDefinition previousFactory) {
        FactoryDefinition tailFactory = new FactoryDefinition();
        Token identifier = input.nextToken();
        input.mark();
        Token nextDelimiter = input.nextToken();
        if (Token.PARENTHESIS_LEFT.equals(nextDelimiter)) {
            tailFactory.setFactoryType(FactoryDefinition.INSTANCE_METHOD_FACTORY);
            tailFactory.setIdentifierTargetFactory(previousFactory);
            tailFactory.setIdentifier(identifier.toString());
            tailFactory.setInstantiationArgFactories(parseFactoryChainList(input));
            input.assertNextToken(Token.PARENTHESIS_RIGHT);
            input.clearMark();
        } else if (Token.DOT.equals(nextDelimiter) || isEndOfFactoryChain(nextDelimiter)) {
            tailFactory.setFactoryType(FactoryDefinition.INSTANCE_FIELD_FACTORY);
            tailFactory.setIdentifierTargetFactory(previousFactory);
            tailFactory.setIdentifier(identifier.toString());
            input.backtrack();
        } else if (Token.EQUALS.equals(nextDelimiter)) {
            tailFactory.setFactoryType(FactoryDefinition.INSTANCE_FIELD_ASSIGNMENT_FACTORY);
            tailFactory.setIdentifierTargetFactory(previousFactory);
            tailFactory.setIdentifier(identifier.toString());
            input.clearMark();
            tailFactory.setInstantiationArgFactories(new ArrayList<>());
            tailFactory.getInstantiationArgFactories().add(parseFactoryChain(input));
        } else {
            throw new ParserException(
                    "FactoryBuilder", "FIELD_FACTORY_ERROR",
                    "Expected either a method or field, but found: " + identifier + " " + nextDelimiter);
        }
        return tailFactory;
    }

    protected FactoryDefinition parseMethodOrFieldHeadFactory(ParserInput input, FactoryDefinition headFactory, String className) {
        Token identifier = input.nextToken();
        input.mark();
        Token nextDelimiter = input.nextToken();
        if (Token.PARENTHESIS_LEFT.equals(nextDelimiter)) {
            headFactory.setFactoryType(FactoryDefinition.STATIC_METHOD_FACTORY);
            headFactory.setIdentifierOwnerClass(className);
            headFactory.setIdentifier(identifier.toString());
            headFactory.setInstantiationArgFactories(parseFactoryChainList(input));
            input.assertNextToken(Token.PARENTHESIS_RIGHT);
            input.clearMark();
        } else if (Token.DOT.equals(nextDelimiter) || isEndOfFactoryChain(nextDelimiter)) {
            headFactory.setFactoryType(FactoryDefinition.STATIC_FIELD_FACTORY);
            headFactory.setIdentifierOwnerClass(className);
            headFactory.setIdentifier(identifier.toString());
            input.backtrack();
        } else if (Token.EQUALS.equals(nextDelimiter)) {
            headFactory.setFactoryType(FactoryDefinition.STATIC_FIELD_ASSIGNMENT_FACTORY);
            headFactory.setIdentifierOwnerClass(className);
            headFactory.setIdentifier(identifier.toString());
            input.clearMark();
            headFactory.setInstantiationArgFactories(new ArrayList<>());
            headFactory.getInstantiationArgFactories().add(parseFactoryChain(input));
        } else {
            throw new ParserException(
                    "ScriptFactoryParser", "PARSE_METHOD_OR_FIELD_FACTORY",
                    "Expected either a method or field, but found: " + identifier + nextDelimiter);
        }
        return headFactory;
    }


    protected String parseClass(ParserInput input) {
        input.mark();
        String token = input.nextToken().toString();
        StringBuilder className = new StringBuilder(token);
        Class classInstance = FactoryUtil.getClassForName(className.toString());
        while (classInstance == null) {
            Token delimiterToken = input.nextToken();
            if (delimiterToken == null) {
                input.backtrack();
                return null;
            }
            String delimiter = delimiterToken.toString();
            if (!".".equals(delimiter)) {
                input.backtrack();  //backtrack to beginning of "assumed class name".
                return null;
            }
            token = input.nextToken().toString();
            className.append(delimiter);
            className.append(token);
            classInstance = FactoryUtil.getClassForName(className.toString());
        }
        input.clearMark();
        return classInstance.getName();
    }

    protected String parseName(ParserInput input) {
        return input.nextToken().toString();
    }

    protected Token parseMode(ParserInput input) {
        input.mark();
        Token mode = null;
        Token nextToken = input.nextToken();
        if (Token.NEW_INSTANCE.equals(nextToken) ||
                Token.FLYWEIGHT.equals(nextToken) ||
                Token.THREAD_SINGLETON.equals(nextToken) ||
                Token.LOCALIZED.equals(nextToken)
                ) {
            mode = nextToken;
            input.clearMark();
        } else if (Token.SINGLETON.equals(nextToken)) {
            mode = Token.SINGLETON;
            Token tokenAfterMode = input.lookAhead();
            if (Token.SEMI_COLON.equals(tokenAfterMode)) {
                // The factory definition was 'mode = 1 ;' which means 1 is not the instantiation mode,
                // but the value of a value factory, using property style configuration.
                // Therefore we backtrack the 1 parsed as instantiation mode, and let it be parsed
                // as a factory value. Note however, that instantiation mode of property style factories is as singleton!

                input.backtrack();
            }

        } else {
            //nextToken is not a mode... backtrack and default to singleton
            mode = Token.SINGLETON;
            input.backtrack();
        }

        return mode;
        //todo expand modes with pools that are numbers (min, max) e.g.  5,10
        //todo expand modes with services - started in their own thread

        //throw new ParserException("Factory mode was: " + mode + ". Mode must be either '*', '+', '1', 'n' or 'n,m'");
    }

    /**
     * Global factory names are allowed to contain a single period / dot,
     * just like a normal URI can have. E.g. <br/><br/>
     * <p>
     * /myApp/subDir/myPageOrAction.html
     * <p>
     * <br/><br/>
     * Notice the . before html.
     *
     * @param input The ParserInput instance that is currently being parsed a factory from
     * @return The global factory name containing either zero or one period.
     */
    protected String parseGlobalFactoryName(ParserInput input) {
        Token nameBeforeDot = input.nextToken();

        input.mark();
        Token delimiter = input.nextToken();
        if (!Token.DOT.equals(delimiter)) {
            input.backtrack();
            return nameBeforeDot.toString();
        }

        StringBuilder builder = new StringBuilder();
        builder.append(nameBeforeDot.toString());
        builder.append('.');
        builder.append(input.nextToken().toString());

        input.clearMark();
        return builder.toString();
    }


    //==================================================================================
    // General Utility Methods ... perhaps move to ParserInput or other utility class???
    //==================================================================================


    private boolean isValue(Token token) {
        if (isStringValue(token)) return true;
        return token.isNumber() || Token.NULL.equals(token);
    }

    /*
    private boolean isInteger(Token token){
        return token.isInteger();
    }

    private boolean isNumber(Token token) {
        return token.isNumber();
    }*/

    private boolean isStringValue(Token token) {
        if (token.isString()) return true;
        if (token.startsWith(Token.QUOTE) || token.endsWith(Token.QUOTE)) {
            throw new ParserException(
                    "ScriptFactoryParser", "IS_STRING_VALUE",
                    "Illegal String value - quote missing: " + token);
        }
        if (token.startsWith(Token.QUOTE_SINGLE) || token.endsWith(Token.QUOTE_SINGLE)) {
            throw new ParserException(
                    "ScriptFactoryParser", "IS_STRING_VALUE",
                    "Illegal String value - quote missing: " + token);
        }
        return false;
    }

    private boolean isEndOfFactoryChain(Token delimiter) {
        if (Token.CURLY_LEFT.equals(delimiter)) return true;
        if (Token.COMMA.equals(delimiter)) return true;
        if (Token.SEMI_COLON.equals(delimiter)) return true;
        if (Token.PARENTHESIS_RIGHT.equals(delimiter)) return true;
        if (Token.SQUARE_RIGHT.equals(delimiter)) return true;
        return Token.GREATER_THAN.equals(delimiter) || Token.COLON.equals(delimiter);
    }

    private boolean isEndOfFactoryChainList(Token delimiter) {
        if (Token.PARENTHESIS_RIGHT.equals(delimiter)) return true;
        return Token.SQUARE_RIGHT.equals(delimiter) || Token.GREATER_THAN.equals(delimiter);
    }
}