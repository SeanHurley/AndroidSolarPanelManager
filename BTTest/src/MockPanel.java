import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Date;
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
	private static Queue<Snapshot> historyData = new LinkedList<Snapshot>();

	public static void main(String[] args) {
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
				Snapshot snap = new Snapshot(new Date(), Math.random(), Math.random(), Math.random(), Math.random());
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
			System.out.println("Request: " + request);
			JSONObject json = (JSONObject) JSONValue.parse(request);
			String type = (String) json.get("type");
			if (type.equals("snapshot")) {
				Snapshot snap = new Snapshot(new Date(), Math.random(), Math.random(), Math.random(), Math.random());
				String response = ResponseFactory.buildSnapshot(snap);
				return response;
			} else if (type.equals("history")) {
				ArrayList<Snapshot> snaps = new ArrayList<Snapshot>(historyData);
				History history = new History(snaps);
				String response = ResponseFactory.buildHistory(history);
				return response;
			}
			return "{}\n";
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
	}
}
