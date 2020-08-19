#include <NewPing.h> //Librería externa descargada para manejar el sensor ultrasónico
#include <Keypad.h> //Libreria para poder manejar el teclado matricial
#include <NewTone.h>//

//Pines para los dos ultrasónicos, disparador y eco
int trigger1 = 32;
int echo1 = 33;
int trigger2 = 34;
int echo2 = 35;
int distancia = 200; //Valor predefinido para la distancia del sensor
int sensor1 = 100; //Distancia del sensor1
int sensor2 = 100; //Distancia del sensor2

NewPing sonar1(trigger1, echo1, distancia);//Variable para obtener los valores del sensor1
NewPing sonar2(trigger2, echo2, distancia);//Variable para obtener los valores del sensor2
//------------------------------------------------------

//Pines y variables para el teclado
const byte rowsCount = 4;
const byte columsCount = 4;

char keys[rowsCount][columsCount] = {
  { '1', '2', '3', 'A' },
  { '4', '5', '6', 'B' },
  { '7', '8', '9', 'C' },
  { '#', '0', '*', 'D' }
};

const byte rowPins[rowsCount] = { 11, 10, 9, 8 }; //Filas 1-4
const byte columnPins[columsCount] = { 7, 6, 5, 4 }; //Columnas 5-8

Keypad keypad = Keypad(makeKeymap(keys), rowPins, columnPins, rowsCount, columsCount);
//------------------------------------------------------

//Variables para controlar las cosas de la casa
int LEDC = 22;
int LEDC11 = 23;
int LEDC12 = 24;
int LEDC2 = 25;
int LEDS = 26;
int VENTILADOR = 27;
//-----------------------------------------

// Variables para los tonitos
int frecuencia=220;    // frecuencia correspondiente a la nota La
int contador;          // variable para el contador
float m=1.059;         // constante para multiplicar frecuencias
int spk=28;                                           // altavoz a GND y pin 13
int c[5]={131,262,523,1046,2093};       // frecuencias 4 octavas de Do
int cs[5]={139,277,554,1108,2217};      // Do#
int d[5]={147,294,587,1175,2349};       // Re
int ds[5]={156,311,622,1244,2489};    // Re#
int e[5]={165,330,659,1319,2637};      // Mi
int f[5]={175,349,698,1397,2794};       // Fa
int fs[5]={185,370,740,1480,2960};     // Fa#
int g[5]={196,392,784,1568,3136};     // Sol
int gs[5]={208,415,831,1661,3322};   // Sol#
int a[5]={220,440,880,1760,3520};      // La
int as[5]={233,466,932,1866,3729};    // La#
int b[5]={247,494,988,1976,3951};      // Si

void nota(int a, int b);            // declaración de la función auxiliar. Recibe dos números enteros.
//-------------------------------------

String codigo_u = ""; //Para obtener el código de usuario ingresado en el teclado matricial
String ByteEntrada = ""; //El Byte que se recibe del puerto

int valorSensor = 0;  // Variable para almacenar el valor proveniente del SensorFuego
int SensorFuego = 12; // Pin para leer el sensor de fuego

void setup() {
  Serial.begin(9600);
  pinMode(SensorFuego, INPUT); //Sensor de fuego

  pinMode(trigger1, OUTPUT); //Pin del disparador del sensor1
  pinMode(echo1, INPUT);  //Pin del echo del sensor1
  pinMode(trigger2, OUTPUT); //Pin del disparador del sensor2
  pinMode(echo2, INPUT);  //Pin del echo del sensor2

  pinMode(LEDC, OUTPUT); //Led Cocina
  pinMode(LEDC11, OUTPUT); //Led Cuarto 1 foco 1
  pinMode(LEDC12, OUTPUT); //Led Cuarto 1 foco 2
  pinMode(LEDC2, OUTPUT); //Led Cuarto 2
  pinMode(LEDS, OUTPUT); //Led Sala
  pinMode(VENTILADOR, OUTPUT); //Ventilador

}
void loop() {
  
  Fuego();
  codigo_u = "@";
  char key = keypad.getKey();
  if (key && key != '#') {
    codigo_u = key;
    LeerTeclado(key);
    for (int i = 0; i < 30; i++) {
      SensoresPuerta();
      delay(100);
    }
    codigo_u="@";
  }
  else {
    if (Serial.available() > 0) { //Sabemos que algo se está enviando al puerto y lo leemos
      PrenderCosas("NO");
      RecibirCosas(); //Recibe las respuesta de Java sobre las configuraciones del usuario
      ByteEntrada = "";
    }
    else {
      SensoresPuerta();
    }
  }
}

void RecibirCosas() {
  ByteEntrada = Serial.readString();
  int j = 0;
  String aux = "";
  for (int i = 0; i < ByteEntrada.length(); i++) {
    if ((i % 2 == 0) && (i > 0)) {
      aux = ByteEntrada.substring(j, i);
      PrenderCosas(aux);
      j = j + 2;
    }
  }
  aux = ByteEntrada.substring(j, ByteEntrada.length());
  PrenderCosas(aux);
}

void PrenderCosas(String ByteEntrada) {
  if (ByteEntrada.equals("LC")) {
    digitalWrite(LEDC, HIGH);
  }

  if (ByteEntrada.equals("11")) {
    digitalWrite(LEDC11, HIGH);
  }

  if (ByteEntrada.equals("12")) {
    digitalWrite(LEDC12, HIGH);
  }

  if (ByteEntrada.equals("C2")) {
    digitalWrite(LEDC2, HIGH);
  }

  if (ByteEntrada.equals("LS")) {
    digitalWrite(LEDS, HIGH);
  }

  if (ByteEntrada.equals("VE")) {
    digitalWrite(VENTILADOR, HIGH);
  }

  if (ByteEntrada.equals("NO")) {
    digitalWrite(LEDC, LOW);
    digitalWrite(LEDC11, LOW);
    digitalWrite(LEDC12, LOW);
    digitalWrite(LEDC2, LOW);
    digitalWrite(LEDS, LOW);
    digitalWrite(VENTILADOR, LOW);
  }
  if (ByteEntrada.equals("SI")) {
    digitalWrite(LEDC, HIGH);
    digitalWrite(LEDC11, HIGH);
    digitalWrite(LEDC12, HIGH);
    digitalWrite(LEDC2, HIGH);
    digitalWrite(LEDS, HIGH);
    digitalWrite(VENTILADOR, HIGH);
  }
  
  if (ByteEntrada.equals("T1")) {
    marcha();
  }
  if (ByteEntrada.equals("T2")) {
    Intro();
  }
  if (ByteEntrada.equals("T3")) {
    HarryPotter();
  }
}

void SensoresPuerta() {

  sensor1 = 100;
  delay(10);
  sensor1 = (sonar1.ping_median() / US_ROUNDTRIP_CM); // Obtener medicion de tiempo de viaje del sonido y guardar en variable sensor1
  sensor2 = (sonar2.ping_median() / US_ROUNDTRIP_CM);

  if (sensor1 < 8) {
    bool transmision = false;
    for (int i = 0; i < 10; i++) {
      sensor2 = 100;
      sensor2 = (sonar2.ping_median() / US_ROUNDTRIP_CM); // Obtener medicion de tiempo de viaje del sonido y guardar en variable sensor2
      if (sensor2 < 8) {
        transmision = true;
      }
      delay(100);
    }
    if (transmision) {
      Serial.println("Entro;" + codigo_u);
      codigo_u = "@";
    }
  }
  else {
    bool transmision = false;
    sensor2 = 100;
    delay(10);
    sensor2 = (sonar2.ping_median() / US_ROUNDTRIP_CM); // Obtener medicion de tiempo de viaje del sonido y guardar en variable sensor2
    if (sensor2 < 8) {
      for (int i = 0; i < 10; i++) {
        sensor1 = 100;
        sensor1 = (sonar1.ping_median() / US_ROUNDTRIP_CM); // Obtener medicion de tiempo de viaje del sonido y guardar en variable sensor2
        if (sensor1 < 8) {
          transmision = true;
        }
        delay(100);
      }
      if (transmision) {
        Serial.println("Salio;" + codigo_u);
        codigo_u = "@";
      }
    }
  }
}

void Fuego(){
  valorSensor = digitalRead(SensorFuego);
  if(valorSensor == 0){
    Serial.println("LLAMA;@");
    while(valorSensor == 0){
      Alarma();
      valorSensor = digitalRead(SensorFuego);
    }
  }
  delay(50);
}

void LeerTeclado(char key) {
  while (key != '#') {
    key = keypad.getKey();
    if (key && key != '#') {
      codigo_u = codigo_u + key;
    }
    delay(100);
  }
}

void nota(int frec, int t)
{
    NewTone(spk,frec);      // suena la nota frec recibida
    delay(t);                // para despues de un tiempo t
}

void marcha(){
  nota(g[2],500); noNewTone(spk);delay(100);
  nota(g[2],500); noNewTone(spk);delay(100);
  nota(g[2],500); noNewTone(spk);delay(100);
  nota(ds[2],500); noNewTone(spk);delay(1);
  nota(as[2],125); noNewTone(spk);delay(25);
  nota(g[2],500); noNewTone(spk);delay(100);
  nota(ds[2],500); noNewTone(spk);delay(1);
  nota(as[2],125); noNewTone(spk);delay(25);
  nota(g[2],500);
  noNewTone(spk);delay(2000);
}
  void Intro(){
  nota(d[1],150); noNewTone(spk);delay(50);
  nota(d[1],150); noNewTone(spk);delay(50);
  nota(d[1],150); noNewTone(spk);delay(50);
  nota(g[1],900); noNewTone(spk);delay(150);
  nota(d[2],900); noNewTone(spk);delay(50);
  nota(c[2],150); noNewTone(spk);delay(50);
  nota(b[1],150); noNewTone(spk);delay(50);
  nota(a[1],150); noNewTone(spk);delay(50);
  nota(g[2],900); noNewTone(spk);delay(150);
  nota(d[2],900); noNewTone(spk);delay(100);
  nota(c[2],150); noNewTone(spk);delay(50);
  nota(b[1],150); noNewTone(spk);delay(50);
  nota(a[1],150); noNewTone(spk);delay(50);
  nota(g[2],900); noNewTone(spk);delay(150);
  nota(d[2],900); noNewTone(spk);delay(100);
  nota(c[2],150); noNewTone(spk);delay(50);
  nota(b[1],150); noNewTone(spk);delay(50);
  nota(c[2],150); noNewTone(spk);delay(50);
  nota(a[1],1200); noNewTone(spk);delay(2000);
}

void HarryPotter(){
  nota(b[2], 500);          
  nota(e[3],1000); 
  nota(g[3], 250);
  nota(fs[3],250);
  nota(e[3],1000);
  nota(b[3],500);
  nota(a[3],1250);
  nota(fs[3],1000);    
  nota(b[2], 500);
  nota(e[3],1000);
  nota(g[3],250);  
  nota(fs[3],250);
  nota(d[3],1000);
  nota(e[3],500 );
  nota(b[2],1000 );    
  noNewTone(spk); 
  delay(1000);   
  nota(b[2], 500);
  nota(e[3],1000);
  nota(g[3], 250);
  nota(fs[3],250);
  nota(e[3],1000);
  nota(b[3],500);
  nota(d[4],1000);
  nota(cs[4],500);
  nota(c[4],1000);
  nota(a[3],500);
  nota(c[4],1000);
  nota(b[3],250);
  nota(as[3],250);
  nota(b[2],1000);
  nota(g[3],500);
  nota(e[3],1000);
  noNewTone(spk); 
  delay(2000);
}

void Alarma(){
  nota(fs[4], 500); noNewTone(spk);delay(50);
  nota(c[4], 500); noNewTone(spk);delay(50);
}
