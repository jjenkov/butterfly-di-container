package com.jenkov.container.script;

//import com.jenkov.container.itf.factory.FactoryException;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class FactoryDefinition {

    /* todo Pool instances of FactoryDefinition like done with Token, to limit the number of FactoryDefinition instances created during parsing. */

    /*
     * These constants are only used while building the factories. After that they are forgotten.
     * They are strings to ease debugging of the parser/builder.
     */
    public static final String CONSTRUCTOR_FACTORY               = "Constructor Factory";
    public static final String INSTANCE_METHOD_FACTORY           = "Instance Method Factory";
    public static final String STATIC_METHOD_FACTORY             = "Static Method Factory";
    public static final String INSTANCE_FIELD_FACTORY            = "Instance Field Factory";
    public static final String INSTANCE_FIELD_ASSIGNMENT_FACTORY = "Instance Field Assignment Factory";
    public static final String STATIC_FIELD_FACTORY              = "Static Field Factory";
    public static final String STATIC_FIELD_ASSIGNMENT_FACTORY   = "Static Field Assignment Factory";
    public static final String VALUE_FACTORY                     = "Value Factory";
    public static final String FACTORY_CALL_FACTORY              = "Factory Call Factory";
    public static final String FACTORY_FACTORY                   = "Factory Factory";
    public static final String LOCAL_PRODUCT_FACTORY             = "Local Product Factory";
    public static final String INPUT_PARAMETER_FACTORY           = "Input Parameter Factory";
    public static final String COLLECTION_FACTORY                = "Collection Factory";
    public static final String MAP_FACTORY                       = "Map Facfory";


    String                                    name                          = null;
    String                                    mode                          = "1";  //*,  1, 5, 6,10   (new, singleton, pool, dynamic pool)
    String                                    factoryType                   = null;    //name ref, factory, constructor, value, input parameter, method, field, collection, map
    Class                                     returnType                    = null;
    String                                    forcedReturnType              = null;

    String                                    identifier                    = null;
    String                                    identifierOwnerClass          = null;
    FactoryDefinition                         identifierTargetFactory       = null;

    int                                       creationInputParameterCount   = 0;

//    List<FactoryDefinition>                   collection                    = null;
//    Map<FactoryDefinition, FactoryDefinition> map                           = null; //what is this?

    List<FactoryDefinition>                   instantiationArgKeyFactories  = null;
    List<FactoryDefinition>                   instantiationArgFactories     = null; //todo change this name to argumentFactories?
    Map<String, List<FactoryDefinition>>      phaseFactories                = null;

    FactoryDefinition                         parent                        = null;
    Map<String, Integer>                      localFactoryNameIndexMap      = null;
    Map<String, Class>                        localFactoryNameTypeMap       = null;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isGlobalFactory(){
        return this.parent == null && getName() != null;
    }

    public boolean isAnonymousLocalFactory(){
        return this.parent != null && getName() == null;
    }

    public boolean isNamedLocalFactory(){
        return this.parent != null && getName() != null;
    }

    public boolean isNewInstance(){
        if(getName() == null) return true; //anonymous factories are always new instance factories.
        return "*".equals(getMode());
    }

    public boolean isSingleton(){
        if(isInputParameterFactory()) return false;
        return "1".equals(getMode());
    }

    public boolean isFlyweight(){
        return "1F".equals(mode.toUpperCase());
    }

    public boolean isThreadSingleton(){
        return "1T".equals(mode.toUpperCase());
    }

    public boolean isLocalizedMap(){
        return "L".equals(mode.toUpperCase());
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Class getReturnType() {
        return returnType;
    }

    public void setReturnType(Class returnType) {
        this.returnType = returnType;
    }

    public String getForcedReturnType() {
        return forcedReturnType;
    }

    public void setForcedReturnType(String forcedReturnType) {
        this.forcedReturnType = forcedReturnType;
    }

    public String getFactoryType() {
        return factoryType;
    }

    public void setFactoryType(String factoryType) {
        this.factoryType = factoryType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifierOwnerClass() {
        return identifierOwnerClass;
    }

    public void setIdentifierOwnerClass(String identifierOwnerClass) {
        this.identifierOwnerClass = identifierOwnerClass;
    }

    public FactoryDefinition getIdentifierTargetFactory() {
        return identifierTargetFactory;
    }

    public void setIdentifierTargetFactory(FactoryDefinition identifierTargetFactory) {
        this.identifierTargetFactory = identifierTargetFactory;
    }

    public boolean isConstructorFactory(){
        return this.factoryType == CONSTRUCTOR_FACTORY;
    }

    public boolean isStaticMethodFactory(){
        return this.factoryType == STATIC_METHOD_FACTORY;
    }

    public boolean isInstanceMethodFactory(){
        return this.factoryType == INSTANCE_METHOD_FACTORY;
    }

    public boolean isStaticFieldFactory(){
        return this.factoryType == STATIC_FIELD_FACTORY;
    }

    public boolean isStaticFieldAssignmentFactory(){
        return this.factoryType == STATIC_FIELD_ASSIGNMENT_FACTORY;
    }

    public boolean isInstanceFieldFactory(){
        return this.factoryType == INSTANCE_FIELD_FACTORY;
    }

    public boolean isInstanceFieldAssignmentFactory(){
        return this.factoryType == INSTANCE_FIELD_ASSIGNMENT_FACTORY;
    }

    public boolean isValueFactory(){
        return this.factoryType == VALUE_FACTORY;
    }

    public boolean isFactoryCallFactory(){
        return this.factoryType == FACTORY_CALL_FACTORY;
    }

    public boolean isFactoryFactory(){
        return this.factoryType == FACTORY_FACTORY;
    }

    public boolean isLocalProductFactory(){
       return this.factoryType == LOCAL_PRODUCT_FACTORY;
    }

    public boolean isInputParameterFactory(){
        return this.factoryType == INPUT_PARAMETER_FACTORY;
    }

   public boolean isCollectionFactory(){
       return this.factoryType == COLLECTION_FACTORY;
   }

   public boolean isMapFactory(){
       return this.factoryType == MAP_FACTORY;
   }


    public int getCreationInputParameterCount() {
        return creationInputParameterCount;
    }

    public void setCreationInputParameterCount(int creationInputParameterCount) {
        this.creationInputParameterCount = creationInputParameterCount;
    }

    public List<FactoryDefinition> getInstantiationArgKeyFactories() {
        return instantiationArgKeyFactories;
    }

    public void setInstantiationArgKeyFactories(List<FactoryDefinition> instantiationArgKeyFactories) {
        this.instantiationArgKeyFactories = instantiationArgKeyFactories;
    }

    public List<FactoryDefinition> getInstantiationArgFactories() {
        return instantiationArgFactories;
    }

    public void setInstantiationArgFactories(List<FactoryDefinition> instantiationArgFactories) {
        this.instantiationArgFactories = instantiationArgFactories;
    }

    public Map<String, List<FactoryDefinition>> getPhaseFactories() {
        return phaseFactories;
    }

    public void setPhaseFactories(Map<String, List<FactoryDefinition>> phaseFactories) {
        this.phaseFactories = phaseFactories;
    }

    public FactoryDefinition getRoot(){
        if(getParent() == null || getParent() == this) return this;
        return getParent().getRoot();
    }

    public FactoryDefinition getParent() {
        return parent;
    }

    public void setParent(FactoryDefinition parent) {
        this.parent = parent;
    }

    public int getNamedLocalProductCount(){
        return getLocalFactoryNameIndexMap().size();
    }

    public Map<String, Integer> getLocalFactoryNameIndexMap() {
        if(getRootAncestor() ==  this) return this.localFactoryNameIndexMap;
        return getRootAncestor().getLocalFactoryNameIndexMap();
    }

    public void setLocalFactoryNameIndexMap(Map<String, Integer> localFactoryNameIndexMap) {
        this.localFactoryNameIndexMap = localFactoryNameIndexMap;
    }

    public Map<String, Class> getLocalFactoryNameTypeMap() {
        if(getRootAncestor() == this) return localFactoryNameTypeMap;
        return getRootAncestor().getLocalFactoryNameTypeMap();
    }

    public void setLocalProductType(String localProductName, Class type){
        getLocalFactoryNameTypeMap().put(localProductName, type);
    }

    public Class getLocalProductType(){
        return getLocalFactoryNameTypeMap().get(getIdentifier());
    }

    public int getLocalProductIndex(){
        if(getName() == null && getIdentifier() == null)
            throw new IllegalStateException("Only a factory with a name or local product reference has a local product index");

        Map<String, Integer> nameIndexMap = getLocalFactoryNameIndexMap();

        if(getName() != null) {
            if(nameIndexMap.get(getName()) == null) throw
                    new ParserException("FactoryDefinition", "UNKNOWN_FACTORY", "Uknown factory: " + getName());
            return nameIndexMap.get(getName());
        } else {
            if(nameIndexMap.get(getIdentifier()) == null) throw
                    new ParserException("FactoryDefinition", "UNKNOWN_FACTORY", "Uknown factory: " + getIdentifier());
            return nameIndexMap.get(getIdentifier());
        }
    }

    public FactoryDefinition getRootAncestor(){
        if(getParent() == null) return this;

        FactoryDefinition ancestor = getParent();
        while(ancestor.getParent() != null){
            ancestor = ancestor.getParent();
        }
        return ancestor;
    }

    public void init(){
        countCreationInputParameters();
        linkFactoriesToParents();
        mapLocalFactoryNamesToIndex();
    }

    protected void countCreationInputParameters(){
        this.creationInputParameterCount = countCreationInputParameters(this);
    }

    private int countCreationInputParameters(FactoryDefinition definition) {
        if(definition == null) return 0;

        int count = 0;
        if(definition.isInputParameterFactory()){
            return Math.max(count, Integer.parseInt(definition.getIdentifier()));
        }

        if(definition.getPhaseFactories() != null){
            for(String phase : definition.getPhaseFactories().keySet()){
                count = Math.max(count, countCreationInputParameters(definition.getPhaseFactories().get(phase)));
            }
        }
        count = Math.max(count, countCreationInputParameters(definition.getIdentifierTargetFactory()));
        count = Math.max(count, countCreationInputParameters(definition.getInstantiationArgFactories()));

        return count;
    }

    private int countCreationInputParameters(Collection<FactoryDefinition> definitions){
        if(definitions == null) return 0;
        int count = 0;
        for(FactoryDefinition definition : definitions){
            count = Math.max(count, countCreationInputParameters(definition));
        }
        return count;
    }

    protected void linkFactoriesToParents(){
        linkFactoryToParent(getIdentifierTargetFactory());

        if(getPhaseFactories() != null){
            for(String phase : getPhaseFactories().keySet()){
                linkFactoriesToParents(getPhaseFactories().get(phase));
            }
        }

        linkFactoriesToParents(getInstantiationArgFactories());

    }

    private void linkFactoryToParent(FactoryDefinition child){
        if(child == null) return;
        child.setParent(this);
        child.init();
    }

    private void linkFactoriesToParents(Collection<FactoryDefinition> children){
        if(children == null) return;
        for(FactoryDefinition child : children){
            linkFactoryToParent(child);
        }
    }

    protected void mapLocalFactoryNamesToIndex(){
        Map<String, Integer> nameIndexMap = new HashMap<String, Integer>();
        nameIndexMap.put(".index", 0);
        mapLocalFactoryNamesToIndex(this, nameIndexMap);
        this.localFactoryNameIndexMap = nameIndexMap;
        this.localFactoryNameTypeMap  = new HashMap<String, Class>();
    }

    private void mapLocalFactoryNamesToIndex(FactoryDefinition definition, Map<String, Integer> nameIndexMap){
        if(definition == null) return;
        if(isNewNamedLocalProduct(definition, nameIndexMap)){
            mapNamedLocalProductToIndex(nameIndexMap, definition);
        }

        mapLocalFactoryNamesToIndex(definition.getIdentifierTargetFactory(), nameIndexMap);

        if(definition.getPhaseFactories() != null){
            for(String phase : definition.getPhaseFactories().keySet()){
                mapLocalFactoryNamesToIndex(definition.getPhaseFactories().get(phase), nameIndexMap);
            }
        }

        mapLocalFactoryNamesToIndex(definition.getInstantiationArgFactories(), nameIndexMap);

    }

    private void mapNamedLocalProductToIndex(Map<String, Integer> nameIndexMap, FactoryDefinition definition) {
        int index = nameIndexMap.get(".index");
        nameIndexMap.put(definition.getName(), index);
        index++;
        nameIndexMap.put(".index", index);
    }

    private boolean isNewNamedLocalProduct(FactoryDefinition definition, Map<String, Integer> nameIndexMap) {
        return definition.getName() != null && nameIndexMap.get(definition.getName())==null;
    }

    private void mapLocalFactoryNamesToIndex(Collection<FactoryDefinition> definitions, Map<String, Integer> nameIndexMap){
       if(definitions == null) return;
       for(FactoryDefinition definition : definitions){
           mapLocalFactoryNamesToIndex(definition, nameIndexMap);
       }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if(getName() !=  null){
            builder.append(getName() + " = " + getMode() + " ");
        } else {
            builder.append(getIdentifier());
        }
        return builder.toString();
    }


}
