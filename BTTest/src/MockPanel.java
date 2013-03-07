import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class MockPanel {
	private static boolean testing = false;
	
	private static String pin;
	private static Queue<Snapshot> historyData = new LinkedList<Snapshot>();
	private static float time;
	private static float latitude;
	private static float longitude;
	private static Hashtable<String, Event> events;
	private static int maxCharge;
	private static int minCharge;

	public static void main(String[] args) {
		for(int i = 1; i < args.length; i++) {
			if ("-t".equals(args[i])) testing = true;
		}
		startHistory();
		startBluetoothServer();
	}

	private static void startBluetoothServer() {
		RequestHandler echoserver = new RequestHandler();
	}

	private static void startHistory() {
		Thread thread = new Thread(historyRunnable);
		thread.start();
	}

	private static Runnable historyRunnable = new Runnable() {

		@Override
		public void run() {
			while (true) {
				Snapshot snap;
				if (testing) {
					snap = new Snapshot(System.currentTimeMillis(), 0.5, 0.5, 0.5, 0.5);
				} else {
					snap = new Snapshot(System.currentTimeMillis(), Math.random(), Math.random(), Math.random(), Math.random());
				}

				if (historyData.size() > 10) {
					historyData.poll();
				}
				historyData.offer(snap);

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	public static class RequestHandler {

		public final UUID uuid = new UUID( // the uid of the service, it has to
											// be
											// unique,
				"27012f0c68af4fbf8dbe6bbaf7aa432a", false); // it can be
															// generated
															// randomly
		public final String name = "Echo Server"; // the name of the service
		public final String url = "btspp://localhost:" + uuid // the service url
				+ ";name=" + name + ";authenticate=false;encrypt=false;";
		LocalDevice local = null;
		StreamConnectionNotifier server = null;
		StreamConnection conn = null;

		private String getResponse(String request) {
			// System.out.println("Request: " + request);
			JSONObject json = (JSONObject) JSONValue.parse(request);
			String type = (String) json.get("type");

			String response = "{}";
			if (MessageTypes.PIN_UPDATE.equals(type)) {
				response = handlePinUpdate(json);
			} if (MessageTypes.TIME_UPDATE.equals(type)) {
				response = handleTimeUpdate(json);
			} else if (MessageTypes.LOCATION_UPDATE.equals(type)) {
				response = handleLocationUpdate(json);
			} else if (MessageTypes.SNAPSHOT.equals(type)) {
				response = handleSnapshot();
			} else if (MessageTypes.HISTORY.equals(type)) {
				response = handleHistory();
			} else if (MessageTypes.SCHEDULE_EVENT.equals(type)) {
				response = handleScheduleEvent(json);
			} else if (MessageTypes.UNSCHEDULE_EVENT.equals(type)) {
				response = handleUnscheduleEvent(json);
			} else if (MessageTypes.EVENTS.equals(type)) {
				response = handleViewEvents();
			} else if (MessageTypes.SET_CHARGE_CONSTRAINTS.equals(type)) {
				response = handleSetChargeConstraints(json);
			} else if (MessageTypes.VIEW_CHARGE_CONSTRAINTS.equals(type)) {
				response = handleViewChargeConstraints();
			} else {
				response = handleUnknownRequest(json);
			}

			response += "\n";

			return response;
		}

		public RequestHandler() {
			try {
				System.out.println("Setting device to be discoverable...");
				local = LocalDevice.getLocalDevice();
				System.out.println("Local address: " + local.getBluetoothAddress());
				local.setDiscoverable(DiscoveryAgent.GIAC);
				System.out.println("Start advertising service...");
				server = (StreamConnectionNotifier) Connector.open(url);
				System.out.println("Waiting for incoming connection...");
				conn = server.acceptAndOpen();
				System.out.println("Client Connected...");
				DataInputStream din = new DataInputStream(conn.openInputStream());
				DataOutputStream out = new DataOutputStream(conn.openOutputStream());

				while (true) {
					String cmd = "";

					byte b;
					while (((b = din.readByte()) > 0) && (b != 0x0a)) {
						System.out.println(b);
						cmd += (char) b;
					}

					System.out.println("Received: " + cmd);
					String response = getResponse(cmd);
					out.write(response.getBytes());
				}

			} catch (Exception e) {
				System.out.println("Exception Occured: " + e.toString());
			}
		}
		
		private String handlePinUpdate(JSONObject json) {
			try {
				String p = (String) json.get("pin");
				pin = p;
				return ResponseCreator.buildDefaultOK(MessageTypes.PIN_UPDATE_RESPONSE);
			} catch (Exception e) {
				return ResponseCreator.buildDefaultInternalError(MessageTypes.PIN_UPDATE_RESPONSE, "Pin error: " + e.getMessage());
			}
		}
		
		private String handleTimeUpdate(JSONObject json) {
			try {
				long t = (Long) json.get("timestamp");
				time = t;
				return ResponseCreator.buildDefaultOK(MessageTypes.TIME_UPDATE_RESPONSE);
			} catch (Exception e) {
				return ResponseCreator.buildDefaultInternalError(MessageTypes.TIME_UPDATE_RESPONSE, "Time error: " + e.getMessage());
			}
		}

		private String handleLocationUpdate(JSONObject json) {
			try {
				float lon = (Float) json.get("longitude");
				float lat = (Float) json.get("latitude");
				longitude = lon;
				latitude = lat;
				return ResponseCreator.buildDefaultOK(MessageTypes.LOCATION_UPDATE_RESPONSE);
			} catch (Exception e) {
				return ResponseCreator.buildDefaultInternalError(MessageTypes.LOCATION_UPDATE_RESPONSE, "Location error: " + e.getMessage());
			}
		}

		private String handleSnapshot() {
			try {
				Snapshot snap;
				if (testing) {
					snap = new Snapshot(System.currentTimeMillis(), 0.5, 0.5, 0.5, 0.5);
				} else {
					snap = new Snapshot(System.currentTimeMillis(), Math.random(), Math.random(), Math.random(), Math.random());
				}
				return ResponseCreator.buildSnapshot(snap);
			} catch (Exception e) {
				return ResponseCreator.buildDefaultInternalError(MessageTypes.SNAPSHOT_RESPONSE, "Snapshot error: " + e.getMessage());
			}
		}

		private String handleHistory() {
			try {
				ArrayList<Snapshot> snaps = new ArrayList<Snapshot>(historyData);
				History history = new History(snaps);
				return ResponseCreator.buildHistory(history);
			} catch (Exception e) {
				return ResponseCreator.buildDefaultInternalError(MessageTypes.HISTORY_RESPONSE, "History error: " + e.getMessage());
			}

		}

		private String handleScheduleEvent(JSONObject json) {
			try {
				String id = (String) json.get("identifier");
				long firstRun = (Long) json.get("first-run-timestamp");
				long duration = (Long) json.get("run-duration");
				long interval = (Long) json.get("interval-durations");

				Event e = new Event(id, firstRun, duration, interval);
				events.put(id, e);
				return ResponseCreator.buildDefaultOK(MessageTypes.SCHEDULE_EVENT_REPONSE);
			} catch (Exception e) {
				return ResponseCreator.buildDefaultInternalError(MessageTypes.SCHEDULE_EVENT_REPONSE, "Schedule error: " + e.getMessage());
			}
		}

		private String handleUnscheduleEvent(JSONObject json) {
			try {
				String id = (String) json.get("identifier");
				events.remove(id);
				return ResponseCreator.buildDefaultOK(MessageTypes.UNSCHEDULE_EVENT_REPONSE);
			} catch (Exception e) {
				return ResponseCreator.buildDefaultInternalError(MessageTypes.UNSCHEDULE_EVENT_REPONSE, "Unschedule error: " + e.getMessage());
			}
		}
		
		private String handleViewEvents() {
			try {
				ArrayList<Event> e = new ArrayList<Event>(events.values());
				EventsList ev = new EventsList(e);
				return ResponseCreator.buildEventsList(ev);
			} catch (Exception e) {
				return ResponseCreator.buildDefaultInternalError(MessageTypes.EVENTS_RESPONSE, "View events error: " + e.getMessage());
			}
		}
		
		private String handleSetChargeConstraints(JSONObject json) {
			try {
				maxCharge = (Integer) json.get("max");
				minCharge = (Integer) json.get("min");
				return ResponseCreator.buildDefaultOK(MessageTypes.SET_CHARGE_CONSTRAINTS_RESPONSE);				
			} catch (Exception e) {
				return ResponseCreator.buildDefaultInternalError(MessageTypes.SET_CHARGE_CONSTRAINTS_RESPONSE, "Set charge constrints error: " + e.getMessage());
			}
		}
		
		private String handleViewChargeConstraints() {
			try {
				return ResponseCreator.buildViewChargeConstraints(maxCharge, minCharge);
			} catch (Exception e) {
				return ResponseCreator.buildDefaultInternalError(MessageTypes.VIEW_CHARGE_CONSTRAINTS_RESPONSE, "View charge constrints error: " + e.getMessage());
			}
		}
		
		private String handleUnknownRequest(JSONObject json) {
			return ResponseCreator.buildDefaultNotFound();
		}

	}
}
