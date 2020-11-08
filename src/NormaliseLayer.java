import java.util.ArrayList;
import java.util.List;

public class NormaliseLayer extends Layer{
    private Double mMaxValue;

    NormaliseLayer(Double aMaxValue)
    {
        mMaxValue = aMaxValue;
    }

    @Override
    public List<Double> process(List<Double> aData) {
        List<Double> resilt = new ArrayList<>();
        for(int i = 0; i < aData.size(); ++i)
        {
            resilt.add(aData.get(i) / mMaxValue);
        }
        return resilt;
    }
}
