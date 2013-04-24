public class Snapshot {
	private long time;
	private double panelV, panelI, batteryV, batteryI, intake, outtake;
	private int percent;
	private int min, max;

	public Snapshot(long t, int percent, int min, int max, double pV, double pI, double bV, double bI, double in,
			double out) {
		this.time = t;
		this.panelI = pI;
		this.panelV = pV;
		this.batteryI = bI;
		this.batteryV = bV;
		this.percent = percent;
		this.intake = in;
		this.outtake = out;
		this.min = min;
		this.max = max;
	}

	public long getTime() {
		return time;
	}

	public double getPanelV() {
		return panelV;
	}

	public double getPanelI() {
		return panelI;
	}

	public double getBatteryV() {
		return batteryV;
	}

	public double getBatteryI() {
		return batteryI;
	}

	public int getPercent() {
		return this.percent;
	}

	public double getIntake() {
		return this.intake;
	}

	public double getOutake() {
		return this.outtake;
	}

	public int getMin() {
		return this.min;
	}

	public int getMax() {
		return this.max;
	}

}
