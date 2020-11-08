import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WorldObject extends Object implements Serializable {
    enum ObjectsType
    {
        Void, Plant, Bot
    }

    enum ObjectsState
    {
        Non, Normal, Death
    }

    private ObjectsType mType;

    public WorldObject()
    {
        mType = WorldObject.ObjectsType.Void;
    }

    public ObjectsType getmType() {
        return mType;
    }

    protected void setType(ObjectsType aType)
    {
        mType = aType;
    }

    protected List<Bot.Commands> move(List<Double> aSensors) {
        List<Bot.Commands> result = new ArrayList<>();
        return result;
    }

    protected ObjectsState update(int aChanges) {
        return ObjectsState.Non;
    }

    public void reset()
    {
    }

    public void mutate(int aMutationSize) {
    }
}

