package todo;

import done.AbstractWashingMachine;

public abstract class WashWashingProgram extends WashingProgram {
    public WashWashingProgram(AbstractWashingMachine mach, double speed, TemperatureController tempController, WaterController waterController, SpinController spinController) {
        super(mach, speed, tempController, waterController, spinController);
    }

    protected void mainWash(double temperature) throws InterruptedException{
        fill();
        mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_SLOW));
        setTemp(temperature);
        Thread.sleep(simulatedMinutes(30));
        myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_IDLE, Double.NaN));
        drain();
        for(int i = 0; i < 5; i++) {
            fill();
            Thread.sleep(simulatedMinutes(2));
            drain();
        }
        mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_FAST));
        Thread.sleep(simulatedMinutes(5));
        mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_OFF));
    }

    protected long simulatedMinutes(int minutes) {
        return  (long) (minutes * 60 * 1000 / mySpeed);
    }

    protected void setTemp(double temp) {
        System.out.println("set temp: " + temp);
        myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_SET, temp));
        mailbox.doFetch();
        System.out.println("temp reached: " + myMachine.getTemperature());
    }

    protected void drain() {
        myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_DRAIN, Double.NaN));
        mailbox.doFetch();
    }

    protected void fill() {
        System.out.println("filling");
        myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_FILL, 1.0));
        mailbox.doFetch();
        System.out.println("filled");
    }
}
