package todo;

import done.AbstractWashingMachine;
import se.lth.cs.realtime.PeriodicThread;

public class DeltaRegulator  {
    private double deltaSum;
    private int count;

    public void addTimeSeriesDelta(double d) {
        deltaSum += d;
        count++;
    }

    public double getDelta() {
        return count > 0 ? deltaSum / count : 0;
    }
}
