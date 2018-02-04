import champ2011client.*;

public class Liebre extends Controller { 

	private int stuck = 0;

	public Liebre() {
		System.out.println("Iniciando");
	}

	public void reset() {
		System.out.println("Reiniciando la carrera");
	}

	public void shutdown() {
		System.out.println("CARRERA TERMINADA");
	}

	public Action control (SensorModel sensors) {
	
		Action action = new Action();
	
		if( estoyGirado(sensors) || sensors.getTrackEdgeSensors()[9] < 0) {
			incrStuck(sensors,action);
		}
	
		if( !estoyGirado(sensors) && sensors.getTrackEdgeSensors()[9] >= 0) {
			resetStuck(sensors,action);
		}
	
		if( stuck > 25) {
			salirStuck(sensors,action);
		}
	
		if( stuck < =25) {
			setAccelerateAndBrake(sensors,action);
			setGear(sensors,action);
			setSteering(sensors,action);
		}
	
		return action;
	}

	private boolean estoyGirado(SensorModel sensors) {
	
		if(sensors.getAngleToTrackAxis() > 0 && sensors.getAngleToTrackAxis() > 0.523598775)	return true;
		if(sensors.getAngleToTrackAxis() < 0 && sensors.getAngleToTrackAxis() < -0.523598775)	return true;
	
		return false;
	}

	private void incrStuck(SensorModel sensors, Action action) {
	
		stuck = stuck + 1;
	}

	private void resetStuck(SensorModel sensors, Action action) {
	
		stuck = 0;
	}

	private void salirStuck(SensorModel sensors, Action action) {
	
		double steer = -(sensors.getAngleToTrackAxis() / 0.785398);
		int gr = -1;
	
		if(sensors.getAngleToTrackAxis() * sensors.getTrackPosition() > 0) {
			gr = 1;
			steer = -steer;
		}
	
		action.gear = gr;
		action.steering = steer;
		action.accelerate = 0.3;
		action.brake = 0;
	}

	private void setAccelerateAndBrake(SensorModel sensors, Action action) {
	
		double s90 = sensors.getTrackEdgeSensors()[9] / 90.0;
	
		if(s90 > 0) {
			action.accelerate = s90;
			action.brake = 0.0;
		}
		else {
			action.accelerate = 0.0;
			action.brake = -s90;
		}
	}

	private void setGear(SensorModel sensors, Action action) {
	
		int gr;
	
		if(sensors.getGear() < 1)	gr = 1;
		if(sensors.getGear() == 1) {
			if(sensors.getRPM() > 6000)	gr = 2;
			else 	gr = 1;
		}
		if(sensors.getGear() == 2) {
			if(sensors.getRPM() > 7000)	gr = 3;
			else {
				if(sensors.getRPM() < 2500)	gr = 1;
				else 	gr = 2;
			}
		}
		if(sensors.getGear() == 3) {
			if(sensors.getRPM() > 7000)	gr = 4;
			else {
				if(sensors.getRPM() < 3000)	gr = 2;
				else 	gr = 3;
			}
		}
		if(sensors.getGear() == 4) {
			if(sensors.getRPM() > 7500)	gr = 5;
			else {
				if(sensors.getRPM() < 3000)	gr = 3;
				else 	gr = 4;
			}
		}
		if(sensors.getGear() == 5) {
			if(sensors.getRPM() > 8000)	gr = 6;
			else {
				if(sensors.getRPM() < 3500)	gr = 4;
				else 	gr = 5;
			}
		}
		if(sensors.getGear() == 6) {
			if(sensors.getRPM() < 3500)	gr = 5;
			else 	gr = 6;
		}
	
		action.gear = gr;
	}

	private void setSteering(SensorModel sensors, Action action) {
	
		int maxIndex = 0;
		double maxSensor = sensors.getTrackEdgeSensors()[0];
	
		if(sensors.getTrackEdgeSensors()[1] > maxSensor) {
			maxIndex = 1;
			maxSensor = sensors.getTrackEdgeSensors()[1];
		}
		if(sensors.getTrackEdgeSensors()[2] > maxSensor) {
			maxIndex = 2;
			maxSensor = sensors.getTrackEdgeSensors()[2];
		}
		if(sensors.getTrackEdgeSensors()[3] > maxSensor) {
			maxIndex = 3;
			maxSensor = sensors.getTrackEdgeSensors()[3];
		}
		if(sensors.getTrackEdgeSensors()[4] > maxSensor) {
			maxIndex = 4;
			maxSensor = sensors.getTrackEdgeSensors()[4];
		}
		if(sensors.getTrackEdgeSensors()[5] > maxSensor) {
			maxIndex = 5;
			maxSensor = sensors.getTrackEdgeSensors()[5];
		}
		if(sensors.getTrackEdgeSensors()[6] > maxSensor) {
			maxIndex = 6;
			maxSensor = sensors.getTrackEdgeSensors()[6];
		}
		if(sensors.getTrackEdgeSensors()[7] > maxSensor) {
			maxIndex = 7;
			maxSensor = sensors.getTrackEdgeSensors()[7];
		}
		if(sensors.getTrackEdgeSensors()[8] > maxSensor) {
			maxIndex = 8;
			maxSensor = sensors.getTrackEdgeSensors()[8];
		}
		if(sensors.getTrackEdgeSensors()[9] > maxSensor) {
			maxIndex = 9;
			maxSensor = sensors.getTrackEdgeSensors()[9];
		}
		if(sensors.getTrackEdgeSensors()[10] > maxSensor) {
			maxIndex = 10;
			maxSensor = sensors.getTrackEdgeSensors()[10];
		}
		if(sensors.getTrackEdgeSensors()[11] > maxSensor) {
			maxIndex = 11;
			maxSensor = sensors.getTrackEdgeSensors()[11];
		}
		if(sensors.getTrackEdgeSensors()[12] > maxSensor) {
			maxIndex = 12;
			maxSensor = sensors.getTrackEdgeSensors()[12];
		}
		if(sensors.getTrackEdgeSensors()[13] > maxSensor) {
			maxIndex = 13;
			maxSensor = sensors.getTrackEdgeSensors()[13];
		}
		if(sensors.getTrackEdgeSensors()[14] > maxSensor) {
			maxIndex = 14;
			maxSensor = sensors.getTrackEdgeSensors()[14];
		}
		if(sensors.getTrackEdgeSensors()[15] > maxSensor) {
			maxIndex = 15;
			maxSensor = sensors.getTrackEdgeSensors()[15];
		}
		if(sensors.getTrackEdgeSensors()[16] > maxSensor) {
			maxIndex = 16;
			maxSensor = sensors.getTrackEdgeSensors()[16];
		}
		if(sensors.getTrackEdgeSensors()[17] > maxSensor) {
			maxIndex = 17;
			maxSensor = sensors.getTrackEdgeSensors()[17];
		}
		if(sensors.getTrackEdgeSensors()[18] > maxSensor) {
			maxIndex = 18;
			maxSensor = sensors.getTrackEdgeSensors()[18];
		}
	
		action.steering = (maxIndex - 9.0) / 9.0;
	}

}
