import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortDataListener;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SignalTextTest {
    private SerialPort arduinoPort; // Serial port for Arduino communication
    // update but do not commit the actual values for these
    public static final String ACCOUNT_SID = "ACf5200104419f75389ab80c169cd5b6c0";
    public static final String AUTH_TOKEN = "5aa6bbba131cc143ee39a9101c23c2cd";

    public SignalTextTest() {
        // Initialize the Arduino serial port (change the port name as needed)
        arduinoPort = SerialPort.getCommPort("COM4"); // Change this to your Arduino's port
        arduinoPort.openPort();
        arduinoPort.setBaudRate(115200); // Set the correct baud rate

        // Add a data listener to receive data from Arduino
        arduinoPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;
                byte[] newData = new byte[arduinoPort.bytesAvailable()];
                int numRead = arduinoPort.readBytes(newData, newData.length);
                String receivedData = new String(newData);

                try {
                    // Parse the received data as a double and add it to the graph
                    double temperature = Double.parseDouble(receivedData.trim());

                    if (temperature > 80) {
                        String warningMessage = "Warning: There was a reported temperature of " + temperature + " at " + " seconds, please evacuate";
                        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
                        Message message = Message.creator(new PhoneNumber("+13195310040"), new PhoneNumber("+18775666567"), warningMessage).create();
                        System.out.println(message.getSid());
                        System.out.println(warningMessage);
                    }

                } catch (NumberFormatException e) {
                    System.err.println("Invalid data received from Arduino: " + receivedData);
                }
            }
        });
    }

    public static void main(final String[] args) {
        SignalTextTest demo = new SignalTextTest();
    }
}