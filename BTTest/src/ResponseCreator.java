import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class ResponseCreator {

	static final int RESULT_OK = 200;

	private static JSONObject snapshot(Snapshot snap) {
		JSONObject json = new JSONObject();
		json.put("type", "snapshot");
		json.put("result", RESULT_OK);
		json.put("timestamp", snap.getDate());
		json.put("battery-voltage", snap.getBatteryV());
		json.put("pv-current", snap.getPanelI());
		json.put("pv-voltage", snap.getPanelV());
		json.put("battery-current", snap.getBatteryI());
		return json;
	}

	public static String buildSnapshot(Snapshot snap) {
		JSONObject json = snapshot(snap);
		return json.toJSONString();
	}

	public static String buildHistory(History history) {
		JSONObject json = new JSONObject();
		json.put("type", "history");
		json.put("result", RESULT_OK);
		JSONArray array = new JSONArray();
		for (Snapshot snap : history.getHistories()) {
			array.add(snapshot(snap));
		}
		json.put("history-data", array);
		return json.toJSONString();
	}
}
