import java.util.ArrayList;
import java.util.List;

public class OutputLayer extends Layer{
    OutputLayer()
    {
    }

    @Override
    public List<Double> process(List<Double> aData) {
        List<Double> resilt = new ArrayList<>();
        resilt.add(0.);
        for(int i = 0; i < aData.size(); ++i)
        {
            Integer num = resilt.get(0).intValue();
            if (aData.get(num) < aData.get(i))
            {
                resilt.set(0, i * 1.);
            }
        }
        return resilt;
    }
}

