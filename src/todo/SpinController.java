package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class SpinController extends PeriodicThread {
	AbstractWashingMachine washingMachine;

	public SpinController(AbstractWashingMachine mach, double speed) {
		super((long) (1000/speed)); // TODO: replace with suitable period
		washingMachine = mach;
	}

	public void perform() {
		// TODO: implement this method
	}
}
