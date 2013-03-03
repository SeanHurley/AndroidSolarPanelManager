import java.util.ArrayList;

public class History {
	private ArrayList<Snapshot> history;

	public History(ArrayList<Snapshot> history) {
		this.history = history;
	}

	public ArrayList<Snapshot> getHistories() {
		return this.history;
	}
}
