#define BUZZER 4

int incomingByte;

void setup() {
  pinMode(BUZZER, OUTPUT); 
  Serial.begin(115200); 
}

void loop() {
    //if we get 1 as a message, turn on tone 
    if (Serial.available()) {
       incomingByte = Serial.read();
       if (incomingByte == '1') {
          tone(BUZZER, 1500, 20000);  
          Serial.write("Ringing\n");
       }
    }
}
