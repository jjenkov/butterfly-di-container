package com.jenkov.container.impl.factory;

import com.jenkov.container.impl.factory.convert.*;
import com.jenkov.container.itf.factory.ILocalFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**

 */
public class FactoryUtil {

    public static Class getClassForName(String className) {
        if ("String".equals(className)) return String.class;
        if ("byte".equals(className)) return byte.class;
        if ("Byte".equals(className)) return Byte.class;
        if ("short".equals(className)) return short.class;
        if ("Short".equals(className)) return Short.class;
        if ("char".equals(className)) return char.class;
        if ("Character".equals(className)) return Character.class;
        if ("int".equals(className)) return int.class;
        if ("Integer".equals(className)) return Integer.class;
        if ("long".equals(className)) return long.class;
        if ("Long".equals(className)) return Long.class;
        if ("float".equals(className)) return float.class;
        if ("Float".equals(className)) return Float.class;
        if ("double".equals(className)) return double.class;
        if ("Double".equals(className)) return Double.class;
        if ("BigInteger".equals(className)) return BigInteger.class;
        if ("BigDecimal".equals(className)) return BigDecimal.class;
        if ("URL".equals(className)) return URL.class;

        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }

    }

    public static Class[] createEmptyClassArray(int size) {
        Class[] forcedArgumentTypes = new Class[size];
        for (int i = 0; i < forcedArgumentTypes.length; i++) forcedArgumentTypes[i] = null;
        return forcedArgumentTypes;
    }

    //todo add ISO date formats 
    public static ILocalFactory createConversionFactory(ILocalFactory sourceFactory, Class returnType) {
        if (int.class.equals(returnType)) return new IntFactory(sourceFactory);
        if (Integer.class.equals(returnType)) return new IntegerFactory(sourceFactory);
        if (long.class.equals(returnType)) return new LongFactory(sourceFactory);
        if (Long.class.equals(returnType)) return new LongObjectFactory(sourceFactory);
        if (byte.class.equals(returnType)) return new ByteFactory(sourceFactory);
        if (Byte.class.equals(returnType)) return new ByteObjectFactory(sourceFactory);
        if (short.class.equals(returnType)) return new ShortFactory(sourceFactory);
        if (Short.class.equals(returnType)) return new ShortObjectFactory(sourceFactory);
        if (float.class.equals(returnType)) return new FloatFactory(sourceFactory);
        if (Float.class.equals(returnType)) return new FloatObjectFactory(sourceFactory);
        if (double.class.equals(returnType)) return new DoubleFactory(sourceFactory);
        if (Double.class.equals(returnType)) return new DoubleObjectFactory(sourceFactory);
        if (char.class.equals(returnType)) return new CharFactory(sourceFactory);
        if (Character.class.equals(returnType)) return new CharacterFactory(sourceFactory);
        if (URL.class.equals(returnType)) return new UrlFactory(sourceFactory);
        if (Boolean.class.equals(returnType)) return new BooleanObjectFactory(sourceFactory);
        if (boolean.class.equals(returnType)) return new BooleanFactory(sourceFactory);

        return sourceFactory;
    }

    public static boolean isDownCastableTo(Class downCastCandiate, Class targetType) {
        if (targetType.isInterface()) return true;

        Class superClass = targetType.getSuperclass();
        while (superClass != null) {
            if (downCastCandiate.equals(superClass)) return true;
            superClass = superClass.getSuperclass();
        }
        return false;
    }


    //todo add ISO date formats
    public static boolean isConvertibleTo(Class conversionCandidate, Class type) {
        if (String.class.equals(conversionCandidate) && int.class.equals(type)) return true;
        if (String.class.equals(conversionCandidate) && Integer.class.equals(type)) return true;
        if (String.class.equals(conversionCandidate) && long.class.equals(type)) return true;
        if (String.class.equals(conversionCandidate) && Long.class.equals(type)) return true;
        if (String.class.equals(conversionCandidate) && byte.class.equals(type)) return true;
        if (String.class.equals(conversionCandidate) && Byte.class.equals(type)) return true;
        if (String.class.equals(conversionCandidate) && short.class.equals(type)) return true;
        if (String.class.equals(conversionCandidate) && Short.class.equals(type)) return true;
        if (String.class.equals(conversionCandidate) && float.class.equals(type)) return true;
        if (String.class.equals(conversionCandidate) && Float.class.equals(type)) return true;
        if (String.class.equals(conversionCandidate) && double.class.equals(type)) return true;
        if (String.class.equals(conversionCandidate) && Double.class.equals(type)) return true;
        if (String.class.equals(conversionCandidate) && char.class.equals(type)) return true;
        if (String.class.equals(conversionCandidate) && Character.class.equals(type)) return true;
        if (String.class.equals(conversionCandidate) && URL.class.equals(type)) return true;
        return String.class.equals(conversionCandidate) && Boolean.class.equals(type) || String.class.equals(conversionCandidate) && boolean.class.equals(type);

    }

    public static boolean isSubstitutableFor(Class substituteCandidate, Class type) {
        return getAllTypes(substituteCandidate).contains(type);
    }

    public static Set<Class> getAllTypes(Class targetClass) {
        Set<Class> types = getSuperClasses(targetClass);
        types.addAll(getAllInterfaces(targetClass));
        types.add(getPrimitiveOrObjectEquivalent(targetClass));
        types.add(targetClass);
        return types;
    }

    private static Class getPrimitiveOrObjectEquivalent(Class targetClass) {
        if (int.class.equals(targetClass)) return Integer.class;
        if (Integer.class.equals(targetClass)) return int.class;
        if (long.class.equals(targetClass)) return Long.class;
        if (Long.class.equals(targetClass)) return long.class;
        if (short.class.equals(targetClass)) return Short.class;
        if (Short.class.equals(targetClass)) return short.class;
        if (char.class.equals(targetClass)) return Character.class;
        if (Character.class.equals(targetClass)) return char.class;
        if (byte.class.equals(targetClass)) return Byte.class;
        if (Byte.class.equals(targetClass)) return byte.class;
        if (float.class.equals(targetClass)) return Float.class;
        if (Float.class.equals(targetClass)) return float.class;
        if (double.class.equals(targetClass)) return Double.class;
        if (Double.class.equals(targetClass)) return double.class;
        return targetClass;
    }

    public static boolean implementsInterface(Class implementorCandidate, Class theinterface) {
        return getAllInterfaces(implementorCandidate).contains(theinterface);
    }

    public static Set<Class> getAllInterfaces(Class implementorCandidate) {
        Set<Class> implementedInterfaces = new HashSet<>();

        while (implementorCandidate != null) {
            for (Class anInterface : implementorCandidate.getInterfaces()) {
                implementedInterfaces.add(anInterface);
                Class anInterfaceSuperclass = anInterface.getSuperclass();
                while (anInterfaceSuperclass != null) {
                    implementedInterfaces.add(anInterfaceSuperclass);
                    anInterfaceSuperclass = anInterfaceSuperclass.getSuperclass();
                }
            }
            implementorCandidate = implementorCandidate.getSuperclass();
        }
        return implementedInterfaces;
    }

    public static boolean isClassOrSuperclass(Class superclassCandidate, Class subclassCandidate) {
        return subclassCandidate.equals(superclassCandidate) || isSuperclass(superclassCandidate, subclassCandidate);
    }

    public static boolean isSuperclass(Class superclassCandidate, Class subclassCandidate) {
        Class superclass = subclassCandidate.getSuperclass();
        while (superclass != null) {
            if (superclassCandidate.equals(superclass)) return true;
            superclass = superclass.getSuperclass();
        }
        return false;
    }

    public static Set<Class> getSuperClasses(Class targetClass) {
        Set<Class> superclasses = new HashSet<>();
        if (targetClass == null) return superclasses;

        Class superclass = targetClass.getSuperclass();
        while (superclass != null) {
            superclasses.add(superclass);
            superclass = superclass.getSuperclass();
        }
        return superclasses;
    }


    public static Object[] toArgumentArray(List<ILocalFactory> argumentFactories, Object[] parameters, Object[] localProducts) {
        Object[] arguments = new Object[argumentFactories.size()];

        int i = 0;
        for (ILocalFactory factory : argumentFactories) {
            arguments[i++] = factory.instance(parameters, localProducts);
        }
        return arguments;
    }

    public static Class[] factoriesToArgumentTypeArray(List<ILocalFactory> argumentFactories) {
        Class[] argumentTypes = new Class[argumentFactories.size()];

        int i = 0;
        for (ILocalFactory factory : argumentFactories) {
            argumentTypes[i++] = factory.getReturnType();
        }
        return argumentTypes;
    }

    public static Class[] classesToArgumentTypeArray(List<Class> argumentTypeList) {
        Class[] argumentTypes = new Class[argumentTypeList.size()];
        for (int i = 0; i < argumentTypes.length; i++) {
            argumentTypes[i] = argumentTypeList.get(i);
        }
        return argumentTypes;
    }

}
