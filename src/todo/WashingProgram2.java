package todo;

import done.AbstractWashingMachine;

public class WashingProgram2 extends WashWashingProgram {
    public WashingProgram2(AbstractWashingMachine mach, double speed, TemperatureController tempController, WaterController waterController, SpinController spinController) {
        super(mach, speed, tempController, waterController, spinController);
    }

    @Override
    protected void wash() throws InterruptedException {
        myMachine.setLock(true);
        fill();
        mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_SLOW));
        setTemp(40.0);
        Thread.sleep(simulatedMinutes(15));
        mainWash(90.0);
        myMachine.setLock(false);
    }
}
