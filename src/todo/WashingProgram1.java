package todo;

import done.AbstractWashingMachine;

public class WashingProgram1 extends WashingProgram {
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
        fill();
        mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_SLOW));
        myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_SET, 60.0));
        mailbox.doFetch();
        Thread.sleep( (long) (30 * 60 * 1000 / mySpeed));
        myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_IDLE, Double.NaN));
        drain();
        for(int i = 0; i < 5; i++) {
            fill();
            Thread.sleep(2 * 60 * 1000);
            drain();
        }
        mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_FAST));
        Thread.sleep(5 * 60 * 1000);
        mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_OFF));
        myMachine.setLock(false);
    }

    private void drain() {
        myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_DRAIN, Double.NaN));
        mailbox.doFetch();
    }

    private void fill() {
        myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_FILL, 9000));
        mailbox.doFetch();
    }
}
