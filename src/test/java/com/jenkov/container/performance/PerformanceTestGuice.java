package com.jenkov.container.performance;

//import com.google.inject.*;

/**

 */
public class PerformanceTestGuice {


    public static int iterationsPerTest = 10 * 1000 * 1000;
    public static int iterationsOfAllTests = 10;

    //public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        /* NEW INSTANCE TEST */
        /*
        for(int i=0; i<iterationsOfAllTests; i++){
            long javaNewTime         = javaNewTest();
            long javaReflectionTime  = javaReflectionTest();
            long butterflyScriptTime = butterflyScriptTest();
            long butterflyScript2Time= butterflyScriptTest2();
            long butterflyJavaTime   = butterflyJavaTest();
            long butterflyJava2Time  = butterflyJava2Test();
            long guiceTime           = guiceTest();
            long guice2Time          = guiceTest2();
            long guiceProviderTime   = guiceProviderTest();
            long guiceProvider2Time  = guiceProvider2Test();

            long javaReflectionTimeRatio  = (javaReflectionTime  * 100) / javaNewTime;
            long butterflyJavaTimeRatio   = (butterflyJavaTime   * 100) / javaNewTime;
            long butterflyJava2TimeRatio  = (butterflyJava2Time  * 100) / javaNewTime;
            long butterflyScript2TimeRatio = (butterflyScript2Time * 100) / javaNewTime;
            long butterflyScriptTimeRatio = (butterflyScriptTime * 100) / javaNewTime;
            long guiceTimeRatio           = (guiceTime           * 100) / javaNewTime;
            long guiceTime2Ratio          = (guice2Time          * 100) / javaNewTime;
            long guiceProviderTimeRatio   = (guiceProviderTime   * 100) / javaNewTime;
            long guiceProvider2TimeRatio  = (guiceProvider2Time  * 100) / javaNewTime;


            System.out.println("-- NEW BASED INSTANTIATION ----------------------");
            System.out.println("Java new           :  100%,   " + javaNewTime);
            System.out.println("Butterfly Java     :  " + butterflyJavaTimeRatio  + "%,  " + butterflyJavaTime);
            System.out.println("Butterfly Java 2   :  " + butterflyJava2TimeRatio  + "%,  " + butterflyJava2Time);
            System.out.println("Guice Provider     : " + guiceProviderTimeRatio + "%, " + guiceProviderTime);
            System.out.println("Guice Provider 2   : " + guiceProvider2TimeRatio + "%, " + guiceProvider2Time);
            System.out.println("-- REFLECTION BASED INSTANTIATION ----------------");
            System.out.println("Java Reflection    : " + javaReflectionTimeRatio + "%,  " + javaReflectionTime);
            System.out.println("Butterfly Script   : " + butterflyScriptTimeRatio + "%, " + butterflyScriptTime);
            System.out.println("Butterfly Script 2 : " + butterflyScript2TimeRatio + "%, " + butterflyScript2Time);
            System.out.println("Guice Reflection   : " + guiceTimeRatio + "%, " + guiceTime);
            System.out.println("Guice Reflection 2 : " + guiceTime2Ratio + "%, " + guice2Time);
            System.out.println("=================================================");
        }
    }


    protected static long javaNewTest(){
        long newStart = System.currentTimeMillis();
        for(int i=0; i< iterationsPerTest; i++){
            new TestProduct();
        }
        long newEnd = System.currentTimeMillis();
        return newEnd - newStart;
    }

    protected static long javaReflectionTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Constructor constructor = TestProduct.class.getConstructor(null);
        long newStart = System.currentTimeMillis();
        for(int i=0; i< iterationsPerTest; i++){
            constructor.newInstance(null);
        }
        long newEnd = System.currentTimeMillis();
        return newEnd - newStart;
    }

    protected static long butterflyScriptTest(){
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);
        builder.addFactory("script = * com.jenkov.container.TestProduct();");

        long butterflyScriptStart = System.currentTimeMillis();
        for(int i=0; i< iterationsPerTest; i++){
            container.instance("script");
        }
        long butterflyScriptEnd = System.currentTimeMillis();
        return butterflyScriptEnd - butterflyScriptStart;
    }

    protected static long butterflyScriptTest2(){
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);
        builder.addFactory("script = * com.jenkov.container.TestProduct();");

        IGlobalFactory<TestProduct> factory = container.getFactory("script");

        long butterflyScriptStart = System.currentTimeMillis();
        for(int i=0; i< iterationsPerTest; i++){
            factory.instance();
        }
        long butterflyScriptEnd = System.currentTimeMillis();
        return butterflyScriptEnd - butterflyScriptStart;
    }

    protected static long butterflyJavaTest(){
        IContainer container = new Container();
        JavaFactoryBuilder javaBuilder = new JavaFactoryBuilder(container);

        javaBuilder.addFactory("java", TestProduct.class, new JavaFactory(){
            public Object instance(Object... parameters) {
                return new TestProduct();
            }
        });

        long butterflyJavaStart = System.currentTimeMillis();
        for(int i=0; i< iterationsPerTest; i++){
            container.instance("java");
        }
        long butterflyJavaEnd = System.currentTimeMillis();
        return butterflyJavaEnd - butterflyJavaStart;
    }

    protected static long butterflyJava2Test(){
        IContainer container = new Container();
        JavaFactoryBuilder javaBuilder = new JavaFactoryBuilder(container);

        javaBuilder.addFactory("java", TestProduct.class, new JavaFactory(){
            public Object instance(Object... parameters) {
                return new TestProduct();
            }
        });

        IGlobalFactory<TestProduct> javaFactory = container.getFactory("java");


        long butterflyJavaStart = System.currentTimeMillis();
        for(int i=0; i< iterationsPerTest; i++){
            javaFactory.instance();
        }
        long butterflyJavaEnd = System.currentTimeMillis();
        return butterflyJavaEnd - butterflyJavaStart;
    }
    */


    //protected static long guiceTest(){
        /* GUICE TEST */
        /*
        Injector injector =  Guice.createInjector(new Module(){
            public void configure(Binder binder) {
                binder.bind(TestProduct.class);
            }
        }) ;

        long guiceStart = System.currentTimeMillis();
        for(int i=0; i< iterationsPerTest; i++){
            injector.getInstance(TestProduct.class);
        }
        long guiceEnd = System.currentTimeMillis();

        return guiceEnd - guiceStart;
    }
    */


    //protected static long guiceTest2(){
        /* GUICE TEST */
        /*
        Injector injector =  Guice.createInjector(new Module(){
            public void configure(Binder binder) {
                binder.bind(TestProduct.class);
            }
        }) ;

        Provider<TestProduct> provider = injector.getProvider(TestProduct.class);

        long guiceStart = System.currentTimeMillis();
        for(int i=0; i< iterationsPerTest; i++){
            provider.get();
        }
        long guiceEnd = System.currentTimeMillis();

        return guiceEnd - guiceStart;
    }
    */

    /*
    protected static long guiceProviderTest(){

        final Provider<TestProduct> provider = new Provider<TestProduct>(){
            public TestProduct get() {
                return new TestProduct();
            }
        };
        // GUICE TEST
        Injector injector =  Guice.createInjector(new Module(){
            public void configure(Binder binder) {
                binder.bind(TestProduct.class).toProvider(provider);
            }
        }) ;

        long guiceStart = System.currentTimeMillis();
        for(int i=0; i< iterationsPerTest; i++){
            injector.getInstance(TestProduct.class);
        }
        long guiceEnd = System.currentTimeMillis();

        return guiceEnd - guiceStart;
    }
    */

  /*
  protected static long guiceProvider2Test(){
         Injector injector =  Guice.createInjector(new Module(){
             public void configure(Binder binder) {
                 binder.bind(TestProduct.class).toProvider(new
                     Provider<TestProduct>() {
                         public TestProduct get() {
                             return new TestProduct();
                         }
                 });
             }
         }) ;

         Provider<TestProduct> provider = injector.getProvider(TestProduct.class);

         long guiceStart = System.currentTimeMillis();
         for(int i=0; i< iterationsPerTest; i++){
             provider.get();
         }
         long guiceEnd = System.currentTimeMillis();

         return guiceEnd - guiceStart;
     }
  */
}
