
#include <Max3421e.h>
#include <Usb.h>
#include <FHB.h>

#define  LED_ERR        8 /* digi */


#define  LED1           6
#define  LED2           5

#define  ACT_LED        'L'

AndroidAccessory acc("telethonSommeil",
		     "telethonSommeil",
		     "telethonSommeil",
		     "1.0",
		     "http://telethon.bemyapp.com/",
		     "0000000012345678");

void setup();
void loop();

void init_leds()
{
	pinMode(LED_ERR, OUTPUT);

	pinMode(LED1, OUTPUT);
	pinMode(LED2, OUTPUT);
}


void error_led()
{
	digitalWrite(LED_ERR, 1);
	Serial.println("ERROR");
}

void clear_error()
{
	digitalWrite(LED_ERR, 0);
}

void setup()
{
	Serial.begin(9600);
	Serial.print("\r\nStart");

	init_leds();

	Serial.println("pre-power");

	acc.powerOn();

	Serial.println("post-power");
}

void loop()
{
	if (Serial.available() > 0) {
		byte incomingByte;
                incomingByte = Serial.read();
        }

	if (acc.isConnected()) {
		byte msg[3];
		int len = acc.read(msg, sizeof(msg), 1);

		clear_error();

		if (len == 3)
                {
	            if (msg[0] == ACT_LED) {
			if (msg[1] == 0x0) {
				analogWrite(LED1, msg[2]);
				Serial.print("LED1 "); Serial.print(msg[2], DEC);
			} else if (msg[1] == 0x1) {
				analogWrite(LED2, msg[2]);
				Serial.print("LED2 "); Serial.print(msg[2], DEC);
			} else {
				error_led();
			}
			Serial.println("");
		    }
		}

	}
	while(Serial.available()) {
		Serial.read();
	}
	delay(10);
}

