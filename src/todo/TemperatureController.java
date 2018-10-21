package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class TemperatureController extends PeriodicThread {
	private AbstractWashingMachine washingMachine;
	private TemperatureEvent event;
	private double targetTemp;
	private WashingProgram program;
	private boolean ackAlreadySent=false;
	private int mode;
	private double lastTemp;
	private DeltaRegulator posReg;
	private DeltaRegulator negReg;
	private boolean heating;
	public TemperatureController(AbstractWashingMachine mach, double speed) {
		super((long) (1000/speed)); // TODO: replace with suitable period
		washingMachine = mach;
		posReg = new DeltaRegulator();
		negReg = new DeltaRegulator();
		//targetTemp = washingMachine.getTemperature();
	}


	public void perform() {

		event = (TemperatureEvent) mailbox.tryFetch();


		//event is null if there is no new task given
		if (event != null) { //aka if new task given
			targetTemp = event.getTemperature();
			program = (WashingProgram) event.getSource();
			ackAlreadySent=false;
			mode=event.getMode();
		}

        double currTemp = washingMachine.getTemperature();
		switch (mode) {
			case TemperatureEvent.TEMP_IDLE :
                washingMachine.setHeating(false);
				break;


			case TemperatureEvent.TEMP_SET :
                if (!ackAlreadySent || heating) {
                    double posDelta = posReg.getDelta();
                    if(currTemp < targetTemp - (posDelta + 0.02)) {
                        washingMachine.setHeating(true);
                        if(lastTemp < currTemp) {
                            posReg.addTimeSeriesDelta(currTemp - lastTemp);
                        }
                        lastTemp = currTemp;
                        heating = true;
                    } else {
                        washingMachine.setHeating(false);
                        if(!ackAlreadySent) {
                            program.putEvent(new AckEvent(this));
                            ackAlreadySent = true;
                        }
                        heating = false;
                    }
                } else {
                    double negDelta = negReg.getDelta();
                    System.out.println("neg: " + negDelta);
                    if (targetTemp - currTemp >= 2 - (negDelta + 0.02) && washingMachine.getWaterLevel()>0) {
                        washingMachine.setHeating(true);
                        heating = true;
                    }
                    if(lastTemp > currTemp) {
                        negReg.addTimeSeriesDelta(lastTemp - currTemp);
                    }
                    lastTemp = currTemp;
                }
				/*if (washingMachine.getTemperature() >= targetTemp) {
					program.putEvent(new AckEvent(this));
					ackAlreadySent=true;
				} else if (targetTemp - washingMachine.getTemperature() >= 2 && washingMachine.getWaterLevel()>0) {
					washingMachine.setHeating(true);
					//System.out.println("heat on");
				}*/
				break;

		}
	}

}
