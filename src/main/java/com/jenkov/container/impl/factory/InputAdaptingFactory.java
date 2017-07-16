package com.jenkov.container.impl.factory;

import com.jenkov.container.itf.factory.IGlobalFactory;
import com.jenkov.container.itf.factory.ILocalFactory;

import java.util.List;

/**

 */
public class InputAdaptingFactory extends LocalFactoryBase implements ILocalFactory {

    IGlobalFactory targetFactory = null;
    List<ILocalFactory> inputFactories = null;

    public InputAdaptingFactory(IGlobalFactory targetFactory, List<ILocalFactory> inputFactories) {
        this.targetFactory = targetFactory;
        this.inputFactories = inputFactories;
    }

    public Class getReturnType() {
        return this.targetFactory.getReturnType();
    }

    public Object instance(Object[] parameters, Object[] localProducts) {
        Object[] adaptedParameters = new Object[inputFactories.size()];
        for (int i = 0; i < adaptedParameters.length; i++) {
            adaptedParameters[i] = this.inputFactories.get(i).instance(parameters, localProducts);
        }
        //here we do NOT want to pass the localProducts on to the global factory called.
        // The called factory will create its own
        //array of local products and pass around internally. To the called global factory this
        //call looks like a call directly from the container, or directly to the instance(Object[] parameters) method.
        return this.targetFactory.instance(adaptedParameters);
    }
}
