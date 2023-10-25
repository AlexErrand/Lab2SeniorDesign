#include <arduinoFFT.h>

// const uint16_t samples = 64; // This value MUST ALWAYS be a power of 2
// const double samplingFrequency = 550; // Hz, must be less than 10000 due to ADC

// unsigned int sampling_period_us;
// unsigned long microseconds;

// double vReal[samples];
// double vImag[samples];

const int sensorPin = A0;  // Analog input pin
const int ledPin = 13;     // Digital output pin

int interference = 0; // 0 - false, 1 - true
int count = 0;
const int sample = 8;
int values[sample] = {0,0,0,0,0,0,0,0};

void setup() {
  // sampling_period_us = round(1000000*(1.0/samplingFrequency));
  Serial.begin(9600);
  pinMode(ledPin, OUTPUT);
  pinMode(sensorPin, INPUT);
}

void loop() {
  // delay(100); 
  int sensorValue = analogRead(sensorPin);

  /* SAMPLING */
  // microseconds = micros();
  // for(int i=0; i<samples; i++)
  // {
  //     vReal[i] = analogRead(sensorPin);
  //     vImag[i] = 0;
  //     while(micros() - microseconds < sampling_period_us){
  //       //empty loop
  //     }
  //     microseconds += sampling_period_us;
  // }

  // FFT = arduinoFFT(vReal, vImag, samples, samplingFrequency); /* Create FFT object */
  // FFT.Windowing(FFT_WIN_TYP_HAMMING, FFT_FORWARD);	/* Weigh data */
  // FFT.Compute(FFT_FORWARD); /* Compute FFT */
  // FFT.ComplexToMagnitude(); /* Compute magnitudes */
  // double x = FFT.MajorPeak();
  // Serial.println(x, 6); //Print out what frequency is the most dominant.
  // while(1); /* Run Once */

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
