import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortDataListener;

public class SignalTextTest {

    private boolean textSent = false;
    private final SerialPort arduinoPort; // Serial port for Arduino communication

    public SignalTextTest() {
        // Initialize the Arduino serial port (change the port name as needed)
        arduinoPort = SerialPort.getCommPort("COM3"); // Change this to your Arduino's port
        arduinoPort.openPort();
        arduinoPort.setBaudRate(9600); // Set the correct baud rate

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
                System.out.println("Bytes read:" + numRead);
                String receivedData = new String(newData).trim();
                try {
                    System.out.println(receivedData);
                    if (receivedData.equals("1") && !textSent) {
                        textSent = true;
                        String textMessage = "Test";
                        SendText.sendATextToPhone(textMessage);
                    } else if (!receivedData.equals("1")) {
                        textSent = false;
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