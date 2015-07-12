/*
 *
 *  * Copyright 2015 Skymind,Inc.
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 *
 */

package org.nd4j.linalg.api.ops.impl.accum;

import org.nd4j.linalg.api.complex.IComplexNDArray;
import org.nd4j.linalg.api.complex.IComplexNumber;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.BaseAccumulation;
import org.nd4j.linalg.api.ops.Op;

/**
 * Calculate the max over a vector
 *
 * @author Adam Gibson
 */
public class IAMax extends BaseAccumulation {
    private int currIndexOfMax = 0;

    public IAMax() {
    }

    public IAMax(INDArray x, INDArray y, int n) {
        super(x, y, n);
    }

    public IAMax(INDArray x) {
        super(x);
    }

    public IAMax(INDArray x, INDArray y) {
        super(x, y);
    }

    @Override
    public void update(Number result) {
        if (result.doubleValue() > currentResult().doubleValue()) {
            this.currentResult = result;
            currIndexOfMax = numProcessed;
        }
        numProcessed++;
        if(numProcessed() == n())  {
            currentResult = currIndexOfMax;
        }
    }

    @Override
    public void update(IComplexNumber result) {
        if (result.absoluteValue().doubleValue() > currentResultComplex().absoluteValue().doubleValue()) {
            this.currentComplexResult = result;
            this.currIndexOfMax = numProcessed;
        }
        numProcessed++;
        if(numProcessed() == n())  {
            currentResult = currIndexOfMax;
        }
    }


    @Override
    public String name() {
        return "iamax";
    }

    @Override
    public void init(INDArray x, INDArray y, INDArray z, int n) {
        super.init(x, y, z, n);
        if (x instanceof IComplexNDArray) {
            IComplexNDArray complexX = (IComplexNDArray) x;
            currentComplexResult = complexX.getComplex(0);
        } else {
            currentResult = x.getDouble(0);
            initial = x.getDouble(0);
        }


    }

    @Override
    public Op opForDimension(int index, int dimension) {
        INDArray xAlongDimension = x.vectorAlongDimension(index, dimension);

        if (y() != null)
            return new IAMax(xAlongDimension, y.vectorAlongDimension(index, dimension), xAlongDimension.length());
        else
            return new IAMax(x.vectorAlongDimension(index, dimension));

    }

    @Override
    public Op opForDimension(int index, int... dimension) {
        INDArray xAlongDimension = x.tensorAlongDimension(index, dimension);

        if (y() != null)
            return new IAMax(xAlongDimension, y.tensorAlongDimension(index, dimension), xAlongDimension.length());
        else
            return new IAMax(x.tensorAlongDimension(index, dimension));
    }
}
