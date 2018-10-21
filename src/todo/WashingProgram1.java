package todo;

import done.AbstractWashingMachine;

public class WashingProgram1 extends WashWashingProgram {
    /**
     * @param mach            The washing machine to control
     * @param speed           Simulation speed
     * @param tempController  The TemperatureController to use
     * @param waterController The WaterController to use
     * @param spinController  The SpinController to use
     */
    protected WashingProgram1(AbstractWashingMachine mach, double speed, TemperatureController tempController, WaterController waterController, SpinController spinController) {
        super(mach, speed, tempController, waterController, spinController);
    }

    @Override
    protected void wash() throws InterruptedException {
        myMachine.setLock(true);
        mainWash(60.0);
        myMachine.setLock(false);
    }

}
