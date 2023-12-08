import javax.bluetooth.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.lang.Object;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BluetoothServer {
    // Original UUID with hyphens (36 characters)
    // UUID (must match the UUID on the Android client)
    private static final String SPP_UUID = "00000000-AAAA-1111-BBBB-222222222222";

    // Remove hyphens to conform to the 32-character hexadecimal format expected by javax.bluetooth.UUID
    private static String sppUuidWithoutHyphens = SPP_UUID.replace("-", "");
    private static boolean isRunning = true;

    public static void main(String[] args) throws IOException {

        // Display local device address and name
        LocalDevice localDevice = LocalDevice.getLocalDevice();
        System.out.println("Address: " + localDevice.getBluetoothAddress());
        System.out.println("Name: " + localDevice.getFriendlyName());

        // Set the device to be discoverable
        boolean discoverable = localDevice.setDiscoverable(DiscoveryAgent.GIAC);
        System.out.println("Discoverable: " + discoverable);

        // Create a UUID for SPP and start the service
        UUID uuid = new UUID(sppUuidWithoutHyphens, false);
        String url = "btspp://localhost:" + uuid + ";name=BluetoothServer";

        StreamConnectionNotifier streamConnNotifier = (StreamConnectionNotifier)Connector.open(url);
        System.out.println("Server Started. Waiting for clients to connect...");

        StreamConnection connection;

        // Wait for client connection
        while (isRunning && (connection = streamConnNotifier.acceptAndOpen()) != null) {
            processClientConnection(connection);
        }
    }

    private static void processClientConnection(StreamConnection connection) {
        try {
            // Open input stream to read messages sent by the client
            BufferedReader bReader = new BufferedReader(new InputStreamReader(connection.openInputStream()));
            Robot robot = new Robot(); // Robot to control the mouse

            System.out.println("Client Connected...");
            String lineRead;
            while (isRunning && (lineRead = bReader.readLine()) != null) {
                System.out.println("Received: " + lineRead);
                handleCommand(robot, lineRead); // Move the mouse based on the received data
                
            }
        } catch (IOException | AWTException e) {
            e.printStackTrace();
        } finally {
            // Close the connection
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void handleCommand(Robot robot, String lineRead) {
        if (lineRead.startsWith("MOVE")) {
            String[] parts = lineRead.split(" ");
            if (parts.length == 3) {
                try {
                    float dx = Float.parseFloat(parts[1]);
                    float dy = Float.parseFloat(parts[2]);

                    int intDx = (int) dx;
                    int intDy = (int) dy;

                    Point currentLocation = MouseInfo.getPointerInfo().getLocation();
                    robot.mouseMove(currentLocation.x + intDx, currentLocation.y + intDy);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        if(lineRead.startsWith("PRESS")){
            try {
                robot.mousePress(InputEvent.BUTTON1_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }
}

