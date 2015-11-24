package com.jenkov.container.script;

import com.jenkov.container.IContainer;
import com.jenkov.container.ContainerException;
import com.jenkov.container.impl.factory.*;
import com.jenkov.container.itf.factory.IGlobalFactory;
import com.jenkov.container.itf.factory.ILocalFactory;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * A ScriptFactoryBuilder is capable of parsing Butterfly Container Script into factories and add them
 * to an IContainer instance.
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class ScriptFactoryBuilder {

    FactoryBuilder builder = new FactoryBuilder();

    IContainer container = null;

//    public ScriptFactoryBuilder() {
//    }

    /**
     * Creates a new ScriptFactoryBuilder that adds its factories to the given container.
     * @param container  The container the ScriptFactoryBuilder is to add factories to.
     */
    public ScriptFactoryBuilder(IContainer container) {
        if(container == null) throw new IllegalArgumentException("The container parameter must be non-null");
        this.container = container;
    }

    /**
     * Parses the given script and adds the corresponding factory to the container.
     * Note: The script should only define a single factory.
     * @param factoryScript The script defining the factory to add.
     */
    public void addFactory(String factoryScript){
        validateContainer();
        buildGlobalFactory(this.container, new StringReader(factoryScript));
    }

    /**
     * Parses the given script string and replaces the corresponding factory. If no factory exists
     * with the given name, a new factory is created.
     *
     * @param factoryScript The script defining the factory to replace.
     */
    public void replaceFactory(String factoryScript){
        ScriptFactoryParser parser = new ScriptFactoryParser();
        FactoryDefinition definition = parser.parseFactory(new ParserInput(new StringReader(factoryScript)));
        IGlobalFactory factory = buildGlobalFactory(container, definition);
        container.replaceFactory(definition.getName(), factory);
    }


    /**
     * Parses the script read from the given InputStream and adds the corresponding factories
     * to the container. The script can container as many factories as you like.
     * <br/><br/>
     * This method takes a name used to identify the stream.
     * The name is only used if errors are found in the script
     * read from the InputStream. In that case an exception is thrown, and the name of the stream
     * is included. This is handy when reading scripts from more than one file or network location.
     * That way you will be told what file/location the error is found in.
     *
     * @deprecated  Use the methods that take a Reader instead, so you can control the character set of the script.
     * @param input The InputStream connected to the script to parse.
     * @param name  A name used to identify this stream. The name is only used if errors are found in the script
     *              read from the InputStream. In that case an exception is thrown, and the name of the stream
     *              is included.
     * @param closeInputStream Set to true if you want the method to close the InputStream when it is done
     *             parsing the factories. False if not.
     */
    public void addFactories(InputStream input, String name, boolean closeInputStream){
        validateContainer();

        ContainerException parserException = null;
        Exception       exception       = null;
        try{
            addFactories(this.container, input);
        } catch (ContainerException e){
            parserException = e;
            e.addInfo("ScriptFactoryBuilder", "ERROR_IN_INPUT_STREAM", "An error occurred in the stream (file?) named "  + name);
            throw e;
        } catch (Exception e){
            exception = e;
            throw new ParserException("ScriptFactoryBuilder", "ERROR_IN_INPUT_STREAM", "An error occurred in the stream (file?) named "  + name, e);
        } finally {
            if(closeInputStream){
                try {
                    input.close();
                } catch (IOException e) {
                    if(parserException == null && exception == null){
                        throw new ParserException("ScriptFactoryBuilder", "ERROR_CLOSING_INPUT_STREAM",
                                "An exception occurred when attempting to close InputStream", e);
                    } else {
                        //ignore the close exception. A more important exception has already been thrown earlier.
                    }
                }
            }
        }
    }


    /**
     * Parses the script read from the given InputStream and adds the corresponding factories
     * to the container. The script can container as many factories as you like.
     * <br/><br/>
     * Note: Look at the newer method addFactories(InputStream, String, boolean)
     * for a more user friendly method that does the same thing.
     *
     * @deprecated Use the methods that take a Reader instead, so you can control the character set of the script.
     * @param input The InputStream connected to the script to parse.
     */
    public void addFactories(InputStream input){
        validateContainer();
        addFactories(this.container, input);
    }



    /**
     * Parses the script read from the given InputStream and adds the corresponding factories
     * to the container. The script can container as many factories as you like.
     * 
     * <br/><br/>
     * Use this method when you want to control the character set used to interprete the
     * script file. For instance, if the script file is encoded in UTF-16, you can create
     * a Reader instance that understands UTF-16.
     *
     * <br/><br/>
     * This method takes a name used to identify the stream.
     * The name is only used if errors are found in the script
     * read from the InputStream. In that case an exception is thrown, and the name of the stream
     * is included. This is handy when reading scripts from more than one file or network location.
     * That way you will be told what file/location the error is found in.
     *
     * @param reader The Reader connected to the script to parse.
     * @param name   A name used to identify this stream. The name is only used if errors are found in the script
     *               read from the InputStream. In that case an exception is thrown, and the name of the stream
     *               is included.
     * @param closeReader Set to true if you want the method to close the Reader when the ScriptFactoryBuilder is done
     *               parsing the factories. False if not.
     */
    public void addFactories(Reader reader, String name, boolean closeReader){
        validateContainer();

        ContainerException parserException = null;
        Exception       exception          = null;
        try{
            addFactories(this.container, reader);
        } catch (ContainerException e){
            parserException = e;
            e.addInfo("ScriptFactoryBuilder", "ERROR_IN_INPUT_STREAM", "An error occurred in the stream (file?) named "  + name);
            throw e;
        } catch (Exception e){
            exception = e;
            throw new ParserException("ScriptFactoryBuilder", "ERROR_IN_INPUT_STREAM", "An error occurred in the stream (file?) named "  + name, e);
        } finally {
            if(closeReader){
                try {
                    reader.close();
                } catch (IOException e) {
                    if(parserException == null && exception == null){
                        throw new ParserException("ScriptFactoryBuilder", "ERROR_CLOSING_INPUT_STREAM",
                                "An exception occurred when attempting to close Reader", e);
                    } else {
                        //ignore the close exception. A more important exception has already been thrown earlier.
                    }
                }
            }
        }
    }


    /**
     * Parses the script read from the given Reader and adds the corresponding factories
     * to the container. The script can container as many factories as you like.
     *
     * <br/><br/>
     * Use this method when you want to control the character set used to interprete the
     * script file. For instance, if the script file is encoded in UTF-16, you can create
     * a Reader instance that understands UTF-16.
     *
     * <br/><br/>
     * Note: Look at the newer method addFactories(InputStream, String, boolean)
     * for a more user friendly method that does the same thing.
     *
     * @param reader The Reader connected to the script to parse.
     */
    public void addFactories(Reader reader){
        validateContainer();
        addFactories(this.container, reader);
    }



    private void addFactories(IContainer container, Reader reader){
        ScriptFactoryParser parser = new ScriptFactoryParser();
        ParserInput parserInput = new ParserInput(reader);
        FactoryDefinition definition = parser.parseFactory(parserInput);
        while(definition != null){
            IGlobalFactory factory = buildGlobalFactory(container, definition);
            container.addFactory(definition.getName(), factory);
            definition = parser.parseFactory(parserInput);
        }
    }


    /**
     * @deprecated Use the methods that take a Reader instead.
     *
     * @param container
     * @param input
     */
    private void addFactories(IContainer container, InputStream input){
        ScriptFactoryParser parser = new ScriptFactoryParser();
        ParserInput parserInput = new ParserInput(input);
        FactoryDefinition definition = parser.parseFactory(parserInput);
        while(definition != null){
            IGlobalFactory factory = buildGlobalFactory(container, definition);
            container.addFactory(definition.getName(), factory);
            definition = parser.parseFactory(parserInput);
        }
    }

    /**
     * @deprecated Use the methods that take a Reader instead.
     * @param input The InputStream from which to load the butterfly container script.
     */
    public void replaceFactories(InputStream input){
        validateContainer();
        replaceFactories(this.container, new InputStreamReader(input));
    }


    /**
     * Parses the script stream and replaces all existing factories with same
     * names as factories found in the script file. Factories in the container that have
     * no new definition found in the script file are kept as is. Factories in the script
     * file that has no counterpart in the container are just added.
     *
     * <br/><br/>
     * Use this method when you want to control the character set used to interprete the
     * script file. For instance, if the script file is encoded in UTF-16, you can create
     * a Reader instance that understands UTF-16.
     *
     * @param reader The Reader connected to the script to parse and add factories from.
     */
    public void replaceFactories(Reader reader){
        validateContainer();
        replaceFactories(this.container, reader);
    }


    private void replaceFactories(IContainer container, Reader reader){
        ScriptFactoryParser parser = new ScriptFactoryParser();
        ParserInput parserInput = new ParserInput(reader);
        FactoryDefinition definition = parser.parseFactory(parserInput);
        while(definition != null){
            IGlobalFactory factory = buildGlobalFactory(container, definition);
            container.replaceFactory(definition.getName(), factory);
            definition = parser.parseFactory(parserInput);
        }
    }

    protected void buildGlobalFactory(IContainer container, Reader input){
        ScriptFactoryParser parser = new ScriptFactoryParser();
        FactoryDefinition definition = parser.parseFactory(new ParserInput(input));
        IGlobalFactory factory = buildGlobalFactory(container, definition);
        container.addFactory(definition.getName(), factory);
    }

    protected IGlobalFactory buildGlobalFactory(IContainer container, FactoryDefinition definition){
        ILocalFactory instantiationFactory = buildLocalFactoryRecursively(container, definition);
        definition.setLocalProductType(definition.getName(), instantiationFactory.getReturnType());

        GlobalFactoryBase globalFactory = null;

        if(definition.isNewInstance() || definition.isLocalizedMap()){
            globalFactory = new GlobalNewInstanceFactory();
        } else if(definition.isSingleton()){
            globalFactory = new GlobalSingletonFactory();
        } else if(definition.isThreadSingleton()){
            globalFactory = new GlobalThreadSingletonFactory();
        } else if(definition.isFlyweight()){
            globalFactory = new GlobalFlyweightFactory();
        }

        int namedLocalProductCount = definition.getNamedLocalProductCount();

        globalFactory.setLocalProductCount(namedLocalProductCount);

        /* todo optimize this... so far all global factories have at least 1 named local product (the returned product)... but far from
          all global factories actually reference it from life cycle phases. */
        if(definition.getNamedLocalProductCount() > 0){
            instantiationFactory = new LocalProductProducerFactory(instantiationFactory, 0);
        }

        globalFactory.setLocalInstantiationFactory(instantiationFactory);

        if(definition.getPhaseFactories() != null && definition.getPhaseFactories().size() > 0){
            for(String phase : definition.getPhaseFactories().keySet()){
                List<FactoryDefinition> phaseFactories = definition.getPhaseFactories().get(phase);
                globalFactory.setPhase(phase, buildLocalFactories(container, phaseFactories));
            }
        }

        return globalFactory;
    }

    protected List<ILocalFactory> buildLocalFactories(IContainer container, List<FactoryDefinition> factoryDefinitions){
        List<ILocalFactory> factories = new ArrayList<ILocalFactory>();
        if(factoryDefinitions != null){
            for(FactoryDefinition definition : factoryDefinitions){
                factories.add(buildLocalFactoryRecursively(container, definition));
            }
        }
        return factories;
    }

    protected ILocalFactory buildLocalFactoryRecursively(IContainer container, FactoryDefinition definition){
        ILocalFactory factory = null;

        try {
            List<ILocalFactory> argumentFactories = buildLocalFactories(container, definition.getInstantiationArgFactories());

            //todo get this from castings in factory definition. Casting will force a specific return type.
            Class[] forcedArgumentTypes = getForcedArgumentTypes(argumentFactories, definition);


            if(definition.isConstructorFactory()){  //constructor factory
                factory = builder.createConstructorFactory(definition.getIdentifierOwnerClass(), argumentFactories, forcedArgumentTypes);
            } else if(definition.isStaticMethodFactory()){      //method invocation factory
                factory = builder.createStaticMethodFactory(definition.getIdentifier(), definition.getIdentifierOwnerClass(), argumentFactories, forcedArgumentTypes);
            } else if(definition.isInstanceMethodFactory()) {
                ILocalFactory methodInvocationTargetFactory = buildLocalFactoryRecursively(container, definition.getIdentifierTargetFactory());
                factory = builder.createInstanceMethodFactory(definition.getIdentifier(), methodInvocationTargetFactory, argumentFactories, forcedArgumentTypes);
            } else if(definition.isInstanceFieldFactory()){
                ILocalFactory fieldTargetFactory = buildLocalFactoryRecursively(container, definition.getIdentifierTargetFactory());
                factory = builder.createFieldFactory(definition.getIdentifier(), fieldTargetFactory);
            } else if(definition.isStaticFieldFactory()){
                factory = builder.createFieldFactory(definition.getIdentifier(), definition.getIdentifierOwnerClass());
            } else if(definition.isInstanceFieldAssignmentFactory()){
                ILocalFactory assignmentTargetFactory = buildLocalFactoryRecursively(container, definition.getIdentifierTargetFactory());
                factory = builder.createFieldAssignmentFactory(definition.getIdentifier(), assignmentTargetFactory, argumentFactories.get(0));
            } else if(definition.isStaticFieldAssignmentFactory()){
                factory = builder.createFieldAssignmentFactory(definition.getIdentifier(), definition.getIdentifierOwnerClass(), argumentFactories.get(0));
            } else if(definition.isFactoryCallFactory()){ //existing factory reference
                if(container.getFactory(definition.getIdentifier()) == null) throw
                        new ParserException(
                                "ScriptFactoryBuilder", "UNKNOWN_FACTORY",
                                "Error in factory definition " + definition.getRoot().getName() + ": Unknown Factory: " + definition.getIdentifier());
                factory = new InputAdaptingFactory(container.getFactory(definition.getIdentifier()), argumentFactories);
            } else if(definition.isFactoryFactory()){
                factory = new FactoryFactory(container, definition.getIdentifier());
            } else if(definition.isCollectionFactory()){
                factory = new CollectionFactory(argumentFactories);
            } else if(definition.isMapFactory()){
                List<ILocalFactory> keyFactories = buildLocalFactories(container, definition.getInstantiationArgKeyFactories());
                factory = new MapFactory(keyFactories, argumentFactories);

                if(definition.isLocalizedMap()){
                    ((MapFactory) factory).setFactoryMap(true);
                    IGlobalFactory localeFactory = container.getFactory("locale");
                    if(localeFactory == null){
                        new ParserException(
                                "ScriptFactoryBuilder", "NO_LOCALE_FACTORY_FOUND",
                                "Error in factory definition " + definition.getRoot().getName() + ": No 'locale' factory found. " +
                                        "A 'locale' factory must be present in order to use localized resource factories");
                    }
                    factory = new LocalizedResourceFactory(factory, localeFactory);
                }
            } else if(definition.isValueFactory()){                   // value factory
                if(isString(definition.getIdentifier()))
                    factory = new ValueFactory(definition.getIdentifier().substring(1, definition.getIdentifier().length()-1));
                else if("null".equals(definition.getIdentifier())){
                    factory = new ValueFactory(null);
                } else factory = new ValueFactory(definition.getIdentifier());
            } else if(definition.isInputParameterFactory()){          // input consuming factory
                factory = new InputConsumerFactory(Integer.parseInt(definition.getIdentifier()));
            } else if(definition.isLocalProductFactory()){
                factory = new LocalProductConsumerFactory(definition.getLocalProductType(), definition.getLocalProductIndex());
            }

            //only local factories with a name (named local factories) can be something else than "new instance" factories.
            if(definition.isNamedLocalFactory()){
                if(definition.isSingleton()){
                    factory = new LocalSingletonFactory(factory);
                } else if(definition.isThreadSingleton()){
                    factory = new LocalThreadSingletonFactory(factory);
                } else if(definition.isFlyweight()){
                    factory = new LocalFlyweightFactory(factory);
                }

                factory = new LocalProductProducerFactory(factory, definition.getLocalProductIndex());
                definition.setLocalProductType(definition.getName(), factory.getReturnType());
            }

            return factory;
        } catch (ContainerException e) {
            if(e.getCode().indexOf("ScriptFactoryBuilder") == -1){
                e.addInfo("ScriptFactoryBuilder", "ERROR_CREATING_FACTORY", "Error in factory definition " + definition.getRoot().getName());
            }
            throw e;
        }
    }

    private Class[] getForcedArgumentTypes(List<ILocalFactory> arguments, FactoryDefinition definition) {
        Class[] forcedArgumentTypes = new Class[arguments.size()];
        if(definition.getInstantiationArgFactories() != null){
            for(int i=0; i<forcedArgumentTypes.length; i++){
                String forcedReturnType = definition.getInstantiationArgFactories().get(i).getForcedReturnType();
                if(forcedReturnType != null){
                    if(forcedReturnType.endsWith("[]")){
                        Class componentType = FactoryUtil.getClassForName(forcedReturnType.substring(0, forcedReturnType.length()-2).trim());
                        if(componentType == null){
                            throw new ParserException(
                                    "ScriptFactoryBuilder", "INVALID_PARAMETER_CAST",
                                    "Error in factory definition " + definition.getRoot().getName() +
                                    ": Invalid parameter casting - class not found: " +
                                    definition.getInstantiationArgFactories().get(i).getForcedReturnType());

                        }
                        forcedArgumentTypes[i] = Array.newInstance(componentType, 0).getClass();

                    } else {
                        forcedArgumentTypes[i] = FactoryUtil.getClassForName(forcedReturnType);
                        if(forcedArgumentTypes[i] == null){
                            throw new ParserException(
                                    "ScriptFactoryBuilder", "INVALID_PARAMETER_CAST",
                                    "Error in factory definition " + definition.getRoot().getName() +
                                    ": Invalid parameter casting - class not found: " +
                                    definition.getInstantiationArgFactories().get(i).getForcedReturnType());

                        }
                    }
                }
            }
        }
        return forcedArgumentTypes;
    }


    private boolean isString(String value) {
        return value.startsWith("\"") || value.startsWith("'");
    }

    private void validateContainer() {
        if(this.container == null){
            throw new IllegalStateException("You cannot use this method unless the ScriptFactoryBuilder" +
                    " was instantiated with a Container instance in the constructor");
        }
    }

}