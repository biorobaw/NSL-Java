package robot;
public class Battery {
	private double voltage;
	private double current;
	private double averageCurrent;
	private double temperature;
	
	public static final double MAX_VOLTAGE = 7.4;
	public static final double MIN_VOLTAGE = 6; //When the batteries voltage falls under 6V, the battery cut himself the power supply to avoid a too important discharge of the cell.


	public Battery(double voltage, double current, double averageCurrent, double temperature) {
		super();
		this.voltage = voltage;
		this.current = current;
		this.averageCurrent = averageCurrent;
		this.temperature = temperature;
	}
	public void setAverageCurrent(double averageCurrent) {
		this.averageCurrent = averageCurrent;
	}
	public void setCurrent(double current) {
		this.current = current;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	public void setVoltage(double voltage) {
		this.voltage = voltage;
	}

	public double getVoltage() {
		return voltage;
	}

	@Override
	public String toString() {
		return "voltage: " + voltage + ". current: " + current + ". averageCurrent: " + averageCurrent +". temperature: " +temperature+".";
	}
	
	
}
