import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
                String receivedData = new String(newData).trim();
                System.out.println(numRead + " Bytes: " + receivedData);
                if (receivedData.equals("1") && !textSent) {
                    textSent = true;
                    String currentTime = getCurrentTimeAsString();
                    String currentDate = getCurrentDateAsString();
                    String textMessage = "Critical Safety Event at " + currentTime + " on " + currentDate;
                    SendText.sendATextToPhone(textMessage);
                    System.out.println("TEXT SENT");
                } else if (receivedData.equals("0")) {
                    textSent = false;
                    System.out.println("SIGNAL RECONNECTED - READY TO SEND TEXT");
                }
            }
        });
    }

    public String getCurrentTimeAsString() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public String getCurrentDateAsString() {
        LocalDate today = LocalDate.now();
        return today.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
    }

    public static void main(final String[] args) {
        SignalTextTest demo = new SignalTextTest();
        System.out.println(demo.getCurrentTimeAsString());
        System.out.println(demo.getCurrentDateAsString());
    }
}