package com.jenkov.container.impl.factory;

import com.jenkov.container.itf.factory.ILocalFactory;

/**
 * This factory returns one of the input parameters given to the instance call as its product.
 * This is handy when another factory needs one of the input parameters. Rather than having to know
 * that one of the objects needed comes from the input array, the given factory only calls the factories
 * it depends upon, some of which may return object obtained from the input parameter array. This
 * gives a more uniform way of using other factories and input parameters. No factory except this factory
 * need be aware of the input parameters. They just need to pass them on, that's all.
 */

public class InputConsumerFactory extends LocalFactoryBase implements ILocalFactory {

    protected int inputParameterIndex = 0;
    protected Class returnType = null;    //if changed to other than null as default, remember to change
    //the FactoryBuilder allArgumentsMatch and wrapInConversionFactoriesIfNecessary
    //to check for the new default value instead of null.

    public InputConsumerFactory(int inputParameterIndex) {
        this.inputParameterIndex = inputParameterIndex;
    }

    public int getInputParameterIndex() {
        return inputParameterIndex;
    }

    // todo check if the return type can be specified more precisely? Perhaps by forcing it (casting it in the script).
    public Class getReturnType() {
        return returnType;
    }

    public void setReturnType(Class returnType) {
        this.returnType = returnType;
    }

    public Object instance(Object[] parameters, Object[] localProducts) {
        if (parameters == null) return null;
        //if no input parameter was given matching this index, return null.
        if (this.inputParameterIndex >= parameters.length) return null;
        return parameters[this.inputParameterIndex];
    }

    public String toString() {
        return "$" + this.inputParameterIndex;
    }

}
