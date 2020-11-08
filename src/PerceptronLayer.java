import java.util.ArrayList;
import java.util.List;

class PerceptronLayer extends Layer {
    private List<Perceptron> mNeurons;

    PerceptronLayer(int aSize, int aConnectionCount) {
        mNeurons = new ArrayList<>();
        for (int i = 0; i < aSize; ++i) {
            mNeurons.add(new Perceptron(aConnectionCount));
        }
    }

    @Override
    public List<Double> process(List<Double> aData) {
        List<Double> resilt = new ArrayList<>();
        for (int i = 0; i < mNeurons.size(); ++i) {
            resilt.add(mNeurons.get(i).process(aData));
        }
        return resilt;
    }

    @Override
    public void mutate(int aMutationSize) {
        for(int i = 0; i < aMutationSize; ++i)
        {
            int number = (int) (Math.random() * mNeurons.size());
            mNeurons.get(number).mutate();
        }
    }
}