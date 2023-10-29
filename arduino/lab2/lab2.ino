const int sensorPin = A0;  // Analog input pin
const int ledPin = 13;     // Digital output pin

int interference = 0; // 0 - false, 1 - true
int count = 0;
const int sample = 8;
int values[sample] = {0,0,0,0,0,0,0,0};

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
    // interference
    digitalWrite(ledPin, HIGH);
    Serial.println("1");
  } else {
    // signal detected
    digitalWrite(ledPin, LOW);
    Serial.println("0");
  }
}

void determineInterference(int value) {
  if (count == sample) {
    interference = 1;
    count = 0;
    // check values for a value below 100
    for (int i=0; i<sample; i++) {
      if (values[i] < 950) {
        interference = 0;
      }
    }
  } else {
    values[count] = value;
    count++;
  }
}
