package todo;

import done.*;
import se.lth.cs.realtime.RTThread;


public class WashingController implements ButtonListener {	
	AbstractWashingMachine washingMachine;
	double speed;
    TemperatureController temperatureController;
    WaterController waterController;
    SpinController spinController;
	
    public WashingController(AbstractWashingMachine theMachine, double theSpeed) {
		washingMachine = theMachine;
		speed = theSpeed;
		temperatureController = new TemperatureController(washingMachine,speed);
		temperatureController.start();
		waterController = new WaterController(washingMachine,speed);
		waterController.start();
		spinController = new SpinController(washingMachine,speed);
		spinController.start();
    }

    public void processButton(int theButton) {
        RTThread program;
        switch (theButton) {
            case 0: break;
            case 1:
                program = new WashingProgram1(washingMachine,speed,temperatureController,waterController,spinController);
                program.start();
                break;
            case 2:
                program = new WashingProgram2(washingMachine,speed,temperatureController,waterController,spinController);
                program.start();
                break;
            case 3: {
                program = new WashingProgram3(washingMachine,speed,temperatureController,waterController,spinController);
                program.start();
                break;
            }

        }

    }
}
