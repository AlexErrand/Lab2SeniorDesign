const int sensorPin = A0;  // Analog input pin
const int ledPin = 13;     // Digital output pin

int interference = 0; // 0 - false, 1 - true
int count = 0;
int values[10] = {0,0,0,0,0,0,0,0,0,0};

void setup() {
  Serial.begin(9600);
  pinMode(ledPin, OUTPUT);
  pinMode(sensorPin, INPUT);
}

void loop() {
  delay(100); 
  int sensorValue = analogRead(sensorPin);

  determineInterference(sensorValue);

  // if there is an interference turn on led and send the flag to the serial monitor
  if (interference == 1) {
    digitalWrite(ledPin, HIGH);
    Serial.println("1");
  } else {
    digitalWrite(ledPin, LOW);
  }

}

void determineInterference(int value) {
  interference = 1;
  if (count == 10) {
    count = 0;
    // check values for a value below 100
    for (int i=0; i<10; i++) {
      if (values[i] < 100) {
        interference = 0;
      }
    }
  } else {
    values[count] = value;
    count++;
  }
}
