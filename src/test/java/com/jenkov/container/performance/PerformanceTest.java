package com.jenkov.container.performance;

import com.jenkov.container.Container;
import com.jenkov.container.IContainer;
import com.jenkov.container.TestProduct;
import com.jenkov.container.script.ScriptFactoryBuilder;

import java.io.*;

/**

 */
public class PerformanceTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        int iterations = 100000;

        String fileName = "large-script.bcs";
        writeLargeFile(iterations, fileName);


        long startTime = System.currentTimeMillis();

        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(fileName), 1024 * 1024 );

        builder.addFactories(input);
        input.close();

        long parseTime = System.currentTimeMillis();

        for(int i=0; i<iterations; i++){
            container.instance("i" + i );
        }

        long instantiationTime = System.currentTimeMillis();

        System.out.println("iterations:        " + iterations);
        System.out.println("parseTime:         " + (parseTime - startTime));
        System.out.println("instantiationTime: " + (instantiationTime - parseTime));

        startTime = System.currentTimeMillis();
        TestProduct product = new TestProduct();
        for(int i=0; i<iterations; i++){
            product = new TestProduct();
        }
        instantiationTime = System.currentTimeMillis();
        System.out.println("instantiationTime*: " + (instantiationTime - startTime));

        Thread.currentThread().sleep(5000);

        
    }

    private static void writeLargeFile(int iterations, String fileName) throws IOException {
        FileOutputStream output = new FileOutputStream(fileName);

        output.write("factoryNameWhichIsQuiteLong = * com.jenkov.container.TestProduct();".getBytes());

        for(int i=0; i<iterations; i++){
            output.write(("i" + i + " = * factoryNameWhichIsQuiteLong;").getBytes());
//            output.write(("i" + i + " = * com.jenkov.container.TestProduct();").getBytes());
            if(i<iterations -1){
                output.write("\n".getBytes());
            }
        }

        output.flush();
        output.close();
    }
}
