package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class WaterController extends PeriodicThread {
    private AbstractWashingMachine washingMachine;
    private int mode;
    private WashingProgram program;
    private double level;
    private boolean ackSent;
    private double lastLevel;
    private double delta;

	public WaterController(AbstractWashingMachine mach, double speed) {
		super((long) (1000/speed)); // TODO: replace with suitable period
        washingMachine = mach;
	}

	public void perform() {
        //System.out.println("entering waterC loop");
        WaterEvent event = (WaterEvent) mailbox.tryFetch();
        //event is null if there is no new task given
        if (event != null) { //aka if new task given
            System.out.println("new water event!: " + event);
            mode = event.getMode();
            program = (WashingProgram) event.getSource();
            ackSent = false;
            level = event.getLevel();
        }


        double currLevel = washingMachine.getWaterLevel();
        //System.out.println("water level: " + currLevel);
        switch (mode) {
            case WaterEvent.WATER_IDLE:
                washingMachine.setFill(false);
                washingMachine.setDrain(false);
                break;
            case WaterEvent.WATER_FILL:
                if (!ackSent){
                    if(currLevel < level - (delta + 0.02)) {
                        washingMachine.setDrain(false);
                        washingMachine.setFill(true);
                        if(lastLevel < currLevel) {
                            delta = currLevel - lastLevel;
                        }
                        lastLevel = currLevel;
                    } else {
                        washingMachine.setFill(false);
                        program.putEvent(new AckEvent(this));
                        ackSent = true;
                    }
                }
                break;
            case WaterEvent.WATER_DRAIN:
                if(!ackSent) {
                    if(currLevel > 0) {
                        washingMachine.setFill(false);
                        washingMachine.setDrain(true);
                    } else {
                        program.putEvent(new AckEvent(this));
                        ackSent = true;
                    }
                }
                break;
        }
	}
}
