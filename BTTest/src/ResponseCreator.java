import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class ResponseCreator {

	private static final int RESULT_OK = 200;
	private static final int NOT_FOUND = 404;
	private static final int PERMISSION_DENIED = 403;
	private static final int INTERNAL_ERROR = 500;

	private static JSONObject snapshot(Snapshot snap) {
		JSONObject json = new JSONObject();
		json.put("timestamp", snap.getTime());
		json.put("battery-voltage", snap.getBatteryV());
		json.put("pv-current", snap.getPanelI());
		json.put("pv-voltage", snap.getPanelV());
		json.put("battery-current", snap.getBatteryI());
		return json;
	}
	
	private static JSONObject event(Event e) {
		JSONObject json = new JSONObject();
		json.put("id", e.getId());
		json.put("first-run", e.getFirstTime());
		json.put("duration", e.getDuration());
		json.put("interval", e.getInterval());
		return json;
	}
	
	public static String buildDefaultOK(String type) {
		JSONObject json = new JSONObject();
		json.put("type", type);
		json.put("result", RESULT_OK);
		json.put("message", "OK");
		return json.toJSONString();
	}
	
	public static String buildDefaultNotFound() {
		JSONObject json = new JSONObject();
		json.put("result", NOT_FOUND);
		json.put("message", "Not Found");
		return json.toJSONString();
	}
	
	public static String buildDefaultPermissionDenied(String type) {
		JSONObject json = new JSONObject();
		json.put("type", type);
		json.put("result", PERMISSION_DENIED);
		json.put("message", "Permission Denied");
		return json.toJSONString();
	}
	
	public static String buildDefaultInternalError(String type, String message) {
		JSONObject json = new JSONObject();
		json.put("type", type);
		json.put("result", INTERNAL_ERROR);
		json.put("message", message);
		return json.toJSONString();
	}

	public static String buildSnapshot(Snapshot snap) {
		JSONObject json = snapshot(snap);
		json.put("type", "snapshot-response");
		json.put("result", RESULT_OK);
		return json.toJSONString();
	}

	public static String buildHistory(History history) {
		JSONObject json = new JSONObject();
		json.put("type", "history-response");
		json.put("result", RESULT_OK);
		JSONArray array = new JSONArray();
		for (Snapshot snap : history.getHistories()) {
			array.add(snapshot(snap));
		}
		json.put("history-data", array);
		return json.toJSONString();
	}
	
	public static String buildEventsList(EventsList events) {
		JSONObject json = new JSONObject();
		json.put("type", "events-response");
		json.put("result", RESULT_OK);
		JSONArray array = new JSONArray();
		for (Event e : events.getEvents()) {
			array.add(event(e));
		}
		json.put("events-data", array);
		return json.toJSONString();
	}
	
	public static String buildViewChargeConstraints(int max, int min) {
		JSONObject json = new JSONObject();
		json.put("type", "view-charge-constraints-response");
		json.put("result", RESULT_OK);
		json.put("max", max);
		json.put("min", min);
		return json.toJSONString();
	}
}
