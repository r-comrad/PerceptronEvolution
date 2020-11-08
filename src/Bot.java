import java.util.ArrayList;
import java.util.List;

public class Bot extends WorldObject {
    enum Commands {
        Up, Down, Left, Right
    }

    private int mLife;

    private List<Layer> mNeuronNet;

    public Bot() {
        mLife = 30;

        setType(ObjectsType.Bot);

        mNeuronNet = new ArrayList<>();
        mNeuronNet.add(new NormaliseLayer(3.));
        mNeuronNet.add(new PerceptronLayer(8, 25));
        mNeuronNet.add(new OutputLayer());
    }

    public Bot(Bot aOther)
    {
        mLife = 30;
        setType(ObjectsType.Bot);
        mNeuronNet = aOther.mNeuronNet;
    }

    @Override
    public void reset()
    {
        mLife = 30;
    }

    @Override
    protected List<Commands> move(List<Double> aSensors) {
        List<Double> command = new ArrayList<>(aSensors);
        for (int i = 0; i < mNeuronNet.size(); ++i) {
            command = mNeuronNet.get(i).process(command);
        }
        Double value = command.get(0);
        List<Commands> result = new ArrayList<>();

        if (value < 2 || value == 7) result.add(Commands.Up);
        else if (value > 2 && value < 6) result.add(Commands.Down);

        if (value > 0 && value < 4) result.add(Commands.Right);
        else if (value > 4 && value < 8) result.add(Commands.Left);

        return result;
    }

    @Override
    protected ObjectsState update(int aChanges) {
        mLife += aChanges;
        if (mLife > 30) mLife = 30;
        --mLife;
        if (mLife < 1) return ObjectsState.Death;
        return ObjectsState.Normal;
    }

    @Override
    public void mutate(int aMutationSize) {
        mNeuronNet.get(1).mutate(aMutationSize);
    }
}