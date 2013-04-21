import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class ResponseCreator {

	private static final int RESULT_OK = 200;
	private static final String OK_MESSAGE = "OK";

	private static final int NOT_FOUND = 404;
	private static final String NOT_FOUND_MESSAGE = "Not Found";

	private static final int PERMISSION_DENIED = 403;
	private static final String PERMISSION_DENIED_MESSAGE = "Permission Denied";

	private static final int INTERNAL_ERROR = 500;

	private static JSONObject snapshot(Snapshot snap) {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.SNAPSHOT_TIMESTAMP, snap.getTime());
		json.put(MessageKeys.SNAPSHOT_BATTERY_VOLTAGE, snap.getBatteryV());
		json.put(MessageKeys.SNAPSHOT_BATTERY_CURRENT, snap.getBatteryI());
		json.put(MessageKeys.SNAPSHOT_BATTERY_PERCENT, snap.getPercent());
		json.put(MessageKeys.SNAPSHOT_PANEL_VOLTAGE, snap.getPanelV());
		json.put(MessageKeys.SNAPSHOT_PANEL_CURRENT, snap.getPanelI());
		json.put(MessageKeys.SNAPSHOT_INTAKE, snap.getIntake());
		json.put(MessageKeys.SNAPSHOT_OUTTAKE, snap.getOutake());
		json.put(MessageKeys.CHARGE_MIN, snap.getMin());
		json.put(MessageKeys.CHARGE_MAX, snap.getMax());
		return json;
	}

	private static JSONObject event(Event e) {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.EVENT_ID, e.getId());
		json.put(MessageKeys.EVENT_NAME, e.getName());
		json.put(MessageKeys.EVENT_FIRST_TIME, e.getFirstTime());
		json.put(MessageKeys.EVENT_DURATION, e.getDuration());
		json.put(MessageKeys.EVENT_INTERVAL, e.getInterval());
		return json;
	}

	public static String buildDefaultOK(String type) {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, type);
		json.put(MessageKeys.RESPONSE_CODE, RESULT_OK);
		json.put(MessageKeys.RESPONSE_MESSAGE, OK_MESSAGE);
		return json.toJSONString();
	}

	public static String buildEventCreated(String id) {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.SCHEDULE_EVENT_REPONSE);
		json.put(MessageKeys.RESPONSE_CODE, RESULT_OK);
		json.put(MessageKeys.RESPONSE_MESSAGE, id);
		return json.toJSONString();
	}

	public static String buildDefaultNotFound() {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.RESPONSE_CODE, NOT_FOUND);
		json.put(MessageKeys.RESPONSE_MESSAGE, NOT_FOUND_MESSAGE);
		return json.toJSONString();
	}

	public static String buildDefaultPermissionDenied(String type) {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, type);
		json.put(MessageKeys.RESPONSE_CODE, PERMISSION_DENIED);
		json.put(MessageKeys.RESPONSE_MESSAGE, PERMISSION_DENIED_MESSAGE);
		return json.toJSONString();
	}

	public static String buildDefaultInternalError(String type, String message) {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, type);
		json.put(MessageKeys.RESPONSE_CODE, INTERNAL_ERROR);
		json.put(MessageKeys.RESPONSE_MESSAGE, message);
		return json.toJSONString();
	}

	public static String buildSnapshot(Snapshot snap) {
		JSONObject json = snapshot(snap);
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.SNAPSHOT_RESPONSE);
		json.put(MessageKeys.RESPONSE_CODE, RESULT_OK);
		json.put(MessageKeys.RESPONSE_MESSAGE, OK_MESSAGE);
		return json.toJSONString();
	}

	public static String buildHistory(History history) {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.HISTORY_RESPONSE);
		json.put(MessageKeys.RESPONSE_CODE, RESULT_OK);
		json.put(MessageKeys.RESPONSE_MESSAGE, OK_MESSAGE);
		JSONArray array = new JSONArray();
		for (Snapshot snap : history.getHistories()) {
			array.add(snapshot(snap));
		}
		json.put(MessageKeys.HISTORY_DATA, array);
		return json.toJSONString();
	}

	public static String buildEventsList(List<Event> events) {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.EVENTS_RESPONSE);
		json.put(MessageKeys.RESPONSE_CODE, RESULT_OK);
		json.put(MessageKeys.RESPONSE_MESSAGE, OK_MESSAGE);
		JSONArray array = new JSONArray();
		for (Event e : events) {
			array.add(event(e));
		}
		json.put(MessageKeys.EVENTS_DATA, array);
		return json.toJSONString();
	}

	public static String buildViewChargeConstraints(int max, int min) {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.VIEW_CHARGE_CONSTRAINTS_RESPONSE);
		json.put(MessageKeys.RESPONSE_CODE, RESULT_OK);
		json.put(MessageKeys.RESPONSE_MESSAGE, OK_MESSAGE);
		json.put(MessageKeys.CHARGE_MAX, max);
		json.put(MessageKeys.CHARGE_MIN, min);
		return json.toJSONString();
	}
}