package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class TemperatureController extends PeriodicThread {
	AbstractWashingMachine washingMachine;
	public TemperatureController(AbstractWashingMachine mach, double speed) {
		super((long) (1000/speed)); // TODO: replace with suitable period
		washingMachine = mach;
	}

	public void perform() {
		//här ska man tydligen faktiskt göra sakerna, vilken tur att det definitivt framgick någonstans
	}

}
