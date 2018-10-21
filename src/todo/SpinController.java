package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class SpinController extends PeriodicThread {
	private AbstractWashingMachine washingMachine;
    private int mode;
	private WashingProgram program;
	private boolean spinLeft;
	private boolean isFresh;

	public SpinController(AbstractWashingMachine mach, double speed) {
		super((long) (1000/speed)); // TODO: replace with suitable period
		washingMachine = mach;
	}

	public void perform() {
        SpinEvent event = (SpinEvent) mailbox.tryFetch();
        //event is null if there is no new task given
        if (event != null) { //aka if new task given
            mode = event.getMode();
            program = (WashingProgram) event.getSource();
            isFresh = true;
            //ackAlreadySent=false; TODO: maybe send ACK?
        }


        switch (mode) {
            case SpinEvent.SPIN_OFF:
                if(isFresh) washingMachine.setSpin(AbstractWashingMachine.SPIN_OFF);
                break;
            case SpinEvent.SPIN_SLOW:
                if(spinLeft) {
                    washingMachine.setSpin(AbstractWashingMachine.SPIN_LEFT);
                } else {
                    washingMachine.setSpin(AbstractWashingMachine.SPIN_RIGHT);
                }
                spinLeft = !spinLeft;
                break;
            case SpinEvent.SPIN_FAST:
                if(isFresh) washingMachine.setSpin(AbstractWashingMachine.SPIN_FAST);
                break;
        }
        isFresh = false;
	}
}
