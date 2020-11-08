import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Perceptron  implements Serializable {
    private Double mOffset;
    private List<Double> mWeights;

    public Perceptron(int aConnectionCount) {
        mWeights = new ArrayList<>();
        //for (int i = 0; i < aConnectionCount; ++i) {
        //    mWeights.add(Math.random() - 0.5);
       // }
        //mOffset = Math.random() - 0.5;

        for (int i = 0; i < aConnectionCount; ++i) {
            mWeights.add(0.01);
        }
        mOffset = 0.01;
    }

    public double process(List<Double> aData) {
        Double summ = 0.;
        for (int i = 0; i < mWeights.size(); ++i) {
            summ += mWeights.get(i) * aData.get(i);
        }
        summ += mOffset;

        summ = activationFunction(summ);
        return summ;
    }

    private double activationFunction(double aS) {
        return 1. / (1. + Math.exp(-aS));
    }

    public void mutate() {
        int number = (int) (Math.random() * mWeights.size());
        double value = (Math.random()-0.5) / 100;
        value = mWeights.get(number) + value;
        mWeights.set(number, value);
    }
}