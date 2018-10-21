package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class TemperatureController extends PeriodicThread {
	AbstractWashingMachine washingMachine;
	TemperatureEvent event;
	double targetTemp;
	WashingProgram program;
	boolean ackAlreadySent=false;
	int mode;
	public TemperatureController(AbstractWashingMachine mach, double speed) {
		super((long) (1000/speed)); // TODO: replace with suitable period
		washingMachine = mach;
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


		switch (mode) {
			case TemperatureEvent.TEMP_IDLE :
				if (washingMachine.getTemperature() >= targetTemp) {
					washingMachine.setHeating(false);
				} else if (targetTemp - washingMachine.getTemperature() >= 2 && washingMachine.getWaterLevel()>0) {
					washingMachine.setHeating(true);
				}

				break;


			case TemperatureEvent.TEMP_SET :
				if (washingMachine.getTemperature() >= targetTemp) {
					washingMachine.setHeating(false);
					program.putEvent(new AckEvent(this));
					ackAlreadySent=true;
				} else if (targetTemp - washingMachine.getTemperature() >= 2 && washingMachine.getWaterLevel()>0) {
					washingMachine.setHeating(true);
					System.out.println("heat on");
				}
				break;

		}
	}

}
