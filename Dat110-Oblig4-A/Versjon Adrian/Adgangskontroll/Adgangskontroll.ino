  #include <Keypad.h>
  //Const
const byte PIR=10;
const byte GREEN=4;
const byte YELLOW=3;
const byte RED=2;

const int LOCKED = 1;
const int WAITING = 2;
const int UNLOCKED = 3;

const int CODELENGTH=2;

const unsigned long TIMEOUT = 1000; //the timeout in milliseconds


//var
int state;
int time;
char keymap[1][2]= {{'1', '2'}};
byte rowPins[1] ={11};
byte colPins[2] = {13,12};

/*
byte colPins[1] ={11};
byte rowPins[] = {12,13};*/



Keypad myKeypad=Keypad(makeKeymap(keymap), rowPins, colPins, 1,2);

char code[CODELENGTH]={'1','2'};
char givenCode[CODELENGTH]={};

//unsigned long time;
unsigned long starttime;

void setup()
{
  state=LOCKED;
  
  pinMode(PIR, INPUT);
  
  pinMode(GREEN, OUTPUT);
  pinMode(YELLOW, OUTPUT);
  pinMode(RED, OUTPUT);

  digitalWrite(GREEN,LOW);
  digitalWrite(YELLOW,LOW);
  
  Serial.begin(9600);
  time=0;
}
  int timeout(){
    if(time>=TIMEOUT){
      return 1;
    }else {
      return 0;
    }
  }

void blinkers(byte pin,int times){
  for(int i=0; i<times; i++){
    digitalWrite(pin,LOW);
    delay(10);
    digitalWrite(pin,HIGH);
    delay(10);
  }
}

void loop(){
  switch (state) {
    case LOCKED:
      Serial.println("Locked stage");
      digitalWrite(RED,HIGH);
      if(HIGH==digitalRead(PIR)){
            delay(100);
        state=WAITING;
          digitalWrite(RED,LOW);
      }
    break;
    case WAITING:
      Serial.println("Waiting stage");
      digitalWrite(YELLOW,HIGH);
      if(timeout()==0){
          int count=0;
         do{
        char input=myKeypad.getKey(); //Get the current input char
      
        if(input!=NO_KEY){
              Serial.println(input);
              givenCode[count]=input;
              blinkers(YELLOW,5);
              count++;
        }
           time=time+1;
          
           Serial.println(time);
      }while(count!=CODELENGTH && timeout()==0);  
          int correct=0;
        for(int i=0; i<CODELENGTH; i++){
          if(givenCode[i]==code[i]){
            correct++;
          }
        }
          if(correct==CODELENGTH){
            state=UNLOCKED;
              time=0;
              digitalWrite(YELLOW,LOW);
            }else{
              digitalWrite(YELLOW,LOW);
            state=LOCKED;
              time=0;
            }
          
        }else{
          digitalWrite(YELLOW,LOW);
          state=LOCKED;
          time=0;
        }
    break;
  
  case UNLOCKED:
      Serial.println("Unlocked stage");
      digitalWrite(GREEN,HIGH);
      delay(10000);
      state=LOCKED;
      digitalWrite(GREEN,LOW);
      time=0;
    break;
    default:
    break;
  }
    delay(10); // Delay a little bit to improve simulation performance
  }

