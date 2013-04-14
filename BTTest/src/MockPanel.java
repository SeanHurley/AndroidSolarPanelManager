import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
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
	private static boolean testing = true;

	private static String pin;
	private static Queue<Snapshot> historyData = new LinkedList<Snapshot>();
	private static float time;
	private static float latitude;
	private static float longitude;
	private static Hashtable<String, Event> events = new Hashtable<String, Event>();
	private static int maxCharge;
	private static int minCharge;
	private static int currentEventID = 0;

	public static void main(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if ("-t".equals(args[i])) {
				testing = true;
				System.out.println("Entering testing mode");
			}
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
					snap = new Snapshot(System.currentTimeMillis(), 50, 0.5, 0.5, 0.5, 0.5);
				} else {
					snap = new Snapshot(System.currentTimeMillis(), (int) (Math.random() * 100), Math.random(),
							Math.random(), Math.random(), Math.random());
				}

				if (historyData.size() > 10) {
					historyData.poll();
				}
				historyData.offer(snap);

				try {
					Thread.sleep(10000);
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
			}
			if (MessageTypes.TIME_UPDATE.equals(type)) {
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
			} else if (MessageTypes.PIN_UPDATE.equals(type)) {
				response = handlePinUpdate(json);
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
				while (true) {
					conn = server.acceptAndOpen();
					System.out.println("Client Connected...");
					DataInputStream din = new DataInputStream(conn.openInputStream());
					DataOutputStream out = new DataOutputStream(conn.openOutputStream());

					String cmd = "";

					byte b;
					while (((b = din.readByte()) > 0) && (b != 0x0a)) {
						cmd += (char) b;
					}

					System.out.println("Received: " + cmd);
					String response = getResponse(cmd);
					out.write(response.getBytes());

					// Cleanup
					out.flush();
					out.close();
					din.close();
					out.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private String handlePinUpdate(JSONObject json) {
			try {
				if (testing) {
					return ResponseCreator.buildDefaultOK(MessageTypes.PIN_UPDATE_RESPONSE);
				} else {
					String p = (String) json.get(MessageKeys.PIN_PASSWORD);
					pin = p;
					return ResponseCreator.buildDefaultOK(MessageTypes.PIN_UPDATE_RESPONSE);
				}
			} catch (Exception e) {
				return ResponseCreator.buildDefaultInternalError(MessageTypes.PIN_UPDATE_RESPONSE,
						"Pin error: " + e.getMessage());
			}
		}

		private String handleTimeUpdate(JSONObject json) {
			try {
				if (testing) {
					return ResponseCreator.buildDefaultOK(MessageTypes.LOCATION_UPDATE_RESPONSE);
				} else {
					long t = (Long) json.get(MessageKeys.TIME_TIME);
					time = t;
					return ResponseCreator.buildDefaultOK(MessageTypes.TIME_UPDATE_RESPONSE);
				}
			} catch (Exception e) {
				return ResponseCreator.buildDefaultInternalError(MessageTypes.TIME_UPDATE_RESPONSE,
						"Time error: " + e.getMessage());
			}
		}

		private String handleLocationUpdate(JSONObject json) {
			try {
				if (testing) {
					return ResponseCreator.buildDefaultOK(MessageTypes.LOCATION_UPDATE_RESPONSE);
				} else {
					float lon = (Float) json.get(MessageKeys.LOCATION_LONGITUDE);
					float lat = (Float) json.get(MessageKeys.LOCATION_LATITUDE);
					longitude = lon;
					latitude = lat;
					return ResponseCreator.buildDefaultOK(MessageTypes.LOCATION_UPDATE_RESPONSE);
				}
			} catch (Exception e) {
				return ResponseCreator.buildDefaultInternalError(MessageTypes.LOCATION_UPDATE_RESPONSE,
						"Location error: " + e.getMessage());
			}
		}

		private String handleSnapshot() {
			try {
				Snapshot snap;
				if (testing) {
					snap = new Snapshot(System.currentTimeMillis(), 50, 0.5, 0.5, 0.5, 0.5);
				} else {
					snap = new Snapshot(System.currentTimeMillis(), (int) (Math.random() * 100), Math.random(),
							Math.random(), Math.random(), Math.random());
				}
				return ResponseCreator.buildSnapshot(snap);
			} catch (Exception e) {
				return ResponseCreator.buildDefaultInternalError(MessageTypes.SNAPSHOT_RESPONSE,
						"Snapshot error: " + e.getMessage());
			}
		}

		private String handleHistory() {
			try {
				ArrayList<Snapshot> snaps;
				if (!testing) {
					snaps = new ArrayList<Snapshot>(historyData);
				} else {
					snaps = new ArrayList<Snapshot>();
					snaps.add(new Snapshot(System.currentTimeMillis(), 25, 0.1, 0.2, 0.3, 0.4));
					snaps.add(new Snapshot(System.currentTimeMillis(), 30, 0.6, 0.7, 0.8, 0.9));
				}
				History history = new History(snaps);
				return ResponseCreator.buildHistory(history);
			} catch (Exception e) {
				return ResponseCreator.buildDefaultInternalError(MessageTypes.HISTORY_RESPONSE,
						"History error: " + e.getMessage());
			}

		}

		private String handleScheduleEvent(JSONObject json) {
			try {
				if (testing) {
					return ResponseCreator.buildDefaultOK(MessageTypes.SCHEDULE_EVENT_REPONSE);
				} else {
					String name = (String) json.get(MessageKeys.EVENT_NAME);
					long firstRun = (Long) json.get(MessageKeys.EVENT_FIRST_TIME);
					long duration = (Long) json.get(MessageKeys.EVENT_DURATION);
					long interval = (Long) json.get(MessageKeys.EVENT_INTERVAL);
					
					String id = String.valueOf(++currentEventID);
					
					Event e = new Event(id, name, firstRun, duration, interval);
					events.put(id, e);
					return ResponseCreator.buildDefaultOK(MessageTypes.SCHEDULE_EVENT_REPONSE);
				}
			} catch (Exception e) {
				return ResponseCreator.buildDefaultInternalError(MessageTypes.SCHEDULE_EVENT_REPONSE,
						"Schedule error: " + e.getMessage());
			}
		}

		private String handleUnscheduleEvent(JSONObject json) {
			try {
				if (testing) {
					return ResponseCreator.buildDefaultOK(MessageTypes.UNSCHEDULE_EVENT_REPONSE);
				} else {
					String id = (String) json.get(MessageKeys.EVENT_ID);
					events.remove(id);
					return ResponseCreator.buildDefaultOK(MessageTypes.UNSCHEDULE_EVENT_REPONSE);
				}
			} catch (Exception e) {
				return ResponseCreator.buildDefaultInternalError(MessageTypes.UNSCHEDULE_EVENT_REPONSE,
						"Unschedule error: " + e.getMessage());
			}
		}

		private String handleViewEvents() {
			try {
				if (testing) {
					List<Event> events = new ArrayList<Event>();
					events.add(new Event("a", "Event 1", 1000, 1000000, 3000000));
					events.add(new Event("b", "Event 2", 3000000, 1000000, 3000000));
					return ResponseCreator.buildEventsList(events);
				} else {
					List<Event> evs = new ArrayList<Event>(events.values());
					return ResponseCreator.buildEventsList(evs);
				}
			} catch (Exception e) {
				return ResponseCreator.buildDefaultInternalError(MessageTypes.EVENTS_RESPONSE, "View events error: "
						+ e.getMessage());
			}
		}

		private String handleSetChargeConstraints(JSONObject json) {
			try {
				if (testing) {
					return ResponseCreator.buildDefaultOK(MessageTypes.SET_CHARGE_CONSTRAINTS_RESPONSE);
				} else {
					maxCharge = (Integer) json.get(MessageKeys.CHARGE_MAX);
					minCharge = (Integer) json.get(MessageKeys.CHARGE_MIN);
					return ResponseCreator.buildDefaultOK(MessageTypes.SET_CHARGE_CONSTRAINTS_RESPONSE);
				}
			} catch (Exception e) {
				return ResponseCreator.buildDefaultInternalError(MessageTypes.SET_CHARGE_CONSTRAINTS_RESPONSE,
						"Set charge constrints error: " + e.getMessage());
			}
		}

		private String handleViewChargeConstraints() {
			try {
				if (testing) {
					return ResponseCreator.buildViewChargeConstraints(90, 10);
				} else {
					return ResponseCreator.buildViewChargeConstraints(maxCharge, minCharge);
				}
			} catch (Exception e) {
				return ResponseCreator.buildDefaultInternalError(MessageTypes.VIEW_CHARGE_CONSTRAINTS_RESPONSE,
						"View charge constrints error: " + e.getMessage());
			}
		}

		private String handleUnknownRequest(JSONObject json) {
			return ResponseCreator.buildDefaultNotFound();
		}

	}
}
