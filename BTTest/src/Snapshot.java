import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Snapshot {
	private Date date;
	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private double panelV, panelI, batteryV, batteryI;

	public Snapshot(Date d, double pV, double pI, double bV, double bI) {
		this.date = d;
		this.panelI = pI;
		this.panelV = pV;
		this.batteryI = bI;
		this.batteryV = bV;
	}

	public Date getDate() {
		return date;
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

}
