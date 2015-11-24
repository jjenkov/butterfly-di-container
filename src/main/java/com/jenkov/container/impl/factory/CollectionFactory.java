package com.jenkov.container.impl.factory;

import com.jenkov.container.itf.factory.ILocalFactory;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**

 */
public class CollectionFactory extends LocalFactoryBase implements ILocalFactory {

    public enum CollectionKind {
        ARRAY, LIST, SET;
    }

    protected CollectionKind collectionKind = CollectionKind.LIST;
    protected Class collectionArgumentType = null;
    protected Type  collectionGenericType  = String.class;
    protected Type  collectionRawType      = null;
    protected Class  collectionElementType  = Object.class;

    protected List<ILocalFactory> collectionContentFactories = null;

    public void setCollectionType(Class argumentType, Type genericType) {
        this.collectionArgumentType = argumentType;
        this.collectionGenericType  = genericType;

        if(genericType instanceof ParameterizedType){
            ParameterizedType parameterizedCollectionType = (ParameterizedType) genericType;
            this.collectionRawType = parameterizedCollectionType.getRawType();
            this.collectionElementType = (Class) parameterizedCollectionType.getActualTypeArguments()[0];
        }

        if(argumentType.isArray()){
            this.collectionKind = CollectionKind.ARRAY;
            this.collectionElementType = this.collectionArgumentType.getComponentType();
        } else if(FactoryUtil.isSubstitutableFor(argumentType, List.class)){
            this.collectionKind = CollectionKind.LIST;
        } else if(FactoryUtil.isSubstitutableFor(argumentType, Set.class)){
            this.collectionKind = CollectionKind.SET;
        } else if(FactoryUtil.isSubstitutableFor(argumentType, Collection.class)){
            this.collectionKind = CollectionKind.LIST;
        }

        /* todo move this code to wrapInConversionFactoryIfNecessary, if possible. Then the factory knows as little as possible about parsing*/
        FactoryBuilder builder = new FactoryBuilder();
        for(int i=0; i<this.collectionContentFactories.size(); i++){
            this.collectionContentFactories.set(i,
                    builder.wrapInConversionFactoryIfNecessary(
                       this.collectionContentFactories.get(i), this.collectionElementType
                    ));
        }

    }

    public CollectionFactory(List<ILocalFactory> collectionContentFactories) {
        this.collectionContentFactories = collectionContentFactories;
    }

    public Class getReturnType() {
        return this.collectionArgumentType;
    }

    public Object instance(Object[] parameters, Object[] localProducts) {
        if(this.collectionKind == CollectionKind.ARRAY){
            Object array = Array.newInstance(this.collectionArgumentType.getComponentType(), this.collectionContentFactories.size());
            for(int i=0; i<this.collectionContentFactories.size(); i++){
                Array.set(array, i, this.collectionContentFactories.get(i).instance(parameters, localProducts));
            }
            return array;
        } else if(this.collectionKind == CollectionKind.LIST){
            List list = new ArrayList();
            for(ILocalFactory contentFactory : this.collectionContentFactories){
                list.add(contentFactory.instance(parameters, localProducts));
            }
            return list;
        } else if(this.collectionKind == CollectionKind.SET){
            Set set = new HashSet();
            for(ILocalFactory contentFactory : this.collectionContentFactories){
                set.add(contentFactory.instance(parameters, localProducts));
            }
            return set;
        }

        return null;
    }
}
