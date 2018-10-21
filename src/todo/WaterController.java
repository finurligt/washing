package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class WaterController extends PeriodicThread {
    private AbstractWashingMachine washingMachine;
    private int mode;
    private WashingProgram program;
    private boolean spinLeft;
    private double level;
    private boolean ackSent;

	public WaterController(AbstractWashingMachine mach, double speed) {
		super((long) (1000/speed)); // TODO: replace with suitable period
        washingMachine = mach;
	}

	public void perform() {
        WaterEvent event = (WaterEvent) mailbox.tryFetch();
        //event is null if there is no new task given
        if (event != null) { //aka if new task given
            mode = event.getMode();
            program = (WashingProgram) event.getSource();
            ackSent = false;
            level = event.getLevel();
        }


        double currLevel = washingMachine.getWaterLevel();
        switch (mode) {
            case WaterEvent.WATER_IDLE:
                washingMachine.setFill(false);
                washingMachine.setDrain(false);
                break;
            case WaterEvent.WATER_FILL:
                if (!ackSent){
                    if(currLevel < level) {
                        washingMachine.setDrain(false);
                        washingMachine.setFill(true);
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
