# iSkipper-Software

![](https://github.com/charlescao460/iSkipper-Software/blob/master/Pictures/Capturing.png)

What's this?
-------
An Arduino and Java project based on [@wizard97](https://github.com/wizard97)'s repository [iSkipper](https://github.com/wizard97/iSkipper). With suitable arduino devices, this project can let you recieve or send any iClicker multiple choice answer, through an easy-to-use GUI program.

**NOTE: THIS PROJECT IS FOR EXPERIMENTAL USE ONLY. CONTRIBUTORS OF THIS PROJECT ARE FREE OF RESPONSIBILITY OF ANY IMPROPER USE OF THIS PROJECT**

Features
--------
* Recieve other's answer.


* Compatible with all the channels: AA, AB, AC, AD, BA, BB, BC, BD, CA, CB, CC, CD, DA, DB, DC, DD.
* Send answers with a fixed ID, just like a normal remote.
* Send answers with specific IDs, which allow you to send answers as "others".
* Automatically select the most popular choice.
* Show polling result in three different charts: Area Chart, Bar Chart, Pie Chart.
* The channel and ID list are automatically saved on your computer.
* Cross-platform friendly. (Windows, Mac OS, Linux...)
* (Any features in original repository [iSkipper](https://github.com/wizard97/iSkipper), but some "dangerous" functions are not included in the GUI program, though they are supported [here](https://github.com/charlescao460/iSkipper-Software/blob/master/Software/Java/iSkipper/src/com/csr460/iSkipper/emulator/Emulator.java))

How to use it?
-------
### First, Build the hardware ###
**Materials**

* Any [Arduino](https://www.arduino.cc/) device with SPI interface and USB-Serial support. (Or you can use a USB-TTL converter)


* [RFM69HW](https://www.hoperf.com/Home/Product/detail/id/212/term_id/7.html)/[RFM69W](https://www.hoperf.com/Home/Product/detail/id/213/term_id/7.html) in 915MHz frequency.

* (Optional) A breakout board, since RFM69 has 2.00mm pin spacing, which is different from most Arduino's 2.54mm. A breakout board is something like [this](https://modtronicsaustralia.com/shop/rfm69hw-breakout-board-bare-pcb-rf-wireless-module/), or you could just get a all-in-one modular like [this](https://www.tindie.com/products/modtronicsaustralia/rfm69hw-wireless-rf-breakout-board-1km-range/). 

* (Optional) If you're using Arduino with 5.0V logic level, you'll need a 3.3V-5.0V logic level converter. There is a tutorial of using logic level converter [here](https://learn.sparkfun.com/tutorials/retired---using-the-logic-level-converter).

**Connect your hardwares**

Basically, just read your modular's pin maps and connect the SPI pins one-by-one.
For example, you can check the tutorials of connecting Arduino Nano and RFM69 [here](https://learn.sparkfun.com/tutorials/rfm69hcw-hookup-guide/all).Though they are using RFM69HCW, it should still work fine.

### Or, make your own PCBs! ###
You can go and check my related hardware project [iSkipper-In-One-Package](https://github.com/charlescao460/iSkipper-In-One-Package) to design and manufacture your own iSkipper PCBs!

### Second, Burn the firmware (Arduino Sketch) ###

1. Get an [Arduino IDE](https://www.arduino.cc/en/Main/Software).

2. Go to the original iSkipper repository to download and install the [iSkipper Library](https://github.com/wizard97/iSkipper/tree/master/emulator/iSkipper) and related [Libraries](https://github.com/wizard97/iSkipper/tree/master/emulator/libs). Just copy them into your Arduino library folder.

3. Download the sketch of this project at `/iSkipper_Arduino_Sketch` [here](https://github.com/charlescao460/iSkipper-Software/tree/master/iSkipper_Arduino_Sketch).

4. Open `iSkipper_Arduino_Sketch/iSkipper_Arduino_Sketch.ino` and edit lines below to fit your hardware. Current setting is for Arduino Nano and RFM69HW. 
```cpp
//iSkipper_Arduino_Sketch/iSkipper_Arduino_Sketch.ino:
#define IS_RFM69HW true 	//make true if using w version
#define IRQ_PIN 3		// Must Be 3 for Nano
#define CSN 10			// NSS pin,10 for Nano
```
and
```cpp
//The Fixed ID for emulating a normal iClikcer
const uint32_t FIXED_ISKIPPER_ID = 0xCDCDCDCD;
```
Please note that the iClicker ID is NOT totally random. The last byte of the ID is an [8-bit Xor Checksum](https://www.scadacore.com/tools/programming-calculators/online-checksum-calculator/) of previous 3 bytes. For example, `ABCDEF89` is a valid ID because `0x89 = 0xAB XOR 0xCD XOR 0xEF.`

5. Burn the edited sketch into your device.

6. Open serial monitor at baudrate `115200`. If you see the output like this `Initialization Complete. Waiting for COM command...`, then congratulations! You have done the hardware/firmware part.

### Last, Complie the software ###

If you're not interesting in development of this software, you can **skip** following build process and use our binary [release](https://github.com/charlescao460/iSkipper-Software/releases).

**Requirements**

* Java Development Kit (JDK) **11**
* [Apache Maven](https://maven.apache.org/)

**Installing Apache Maven**

1. Go to the website linked above and find the "Download" link for your OS.

2. Unless you know what you're doing (in which case you aren't reading this), you want to download the .zip version.

3. Unzip the .zip using your favorite decompression tool and move the resultant folder somewhere safe (Documents or something, just get it out of Downloads). 

4. Add the "bin" folder (inside the folder you just moved) to your Path system variable. On Windows you do this by going to Control Panel > System and Secutirty > System > Advanced System Settings > Environment Variables and finding the "Path" variable. Select it, then select "edit" then "new" then "browse" and find the bin folder. Click "OK" until all the windows are gone (3 times) then click "Apply". You have now added the mvn command to your path. Restart CMD and type "mvn -v" to ensure you did this step correctly. On Mac or Linux do a google on how to do this.

**Build**

There two maven projects. One is `iSkipper` (pom.xml [here](https://github.com/charlescao460/iSkipper-Software/blob/master/Software/Java/iSkipper/pom.xml)), and another is `iSkipper-JavaFX`(pom.xml [here](https://github.com/charlescao460/iSkipper-Software/blob/master/Software/iSkipper-JavaFX/pom.xml)).

1. Complie and **install** `iSkipper` into your local maven repository (use `install` as goals).
    
    * Navigate to iSkipper-Software-master\iSkipper-Software-master\Software\Java\iSkipper and open a powershell/cmd/terminal window here. If you're on Windows you can ctrl+shift+right-click and click "Open Powershell window here". 

    * Type "`mvn install`" and you're done.

2. Compile and run `iSkipper-JavaFX` (Use `clean compile exec:java`).

    * Navigate to iSkipper-Software-master\iSkipper-Software-master\Software\iSkipper-JavaFX and open a powershell/cmd/terminal windows here. If you're on Windows you can ctrl+shift+right-click and click "Open Powershell window here".

    * Type "`mvn clean compile exec:java`" and you're done.

3. If you want export a single executable JAR, use `clean compile assembly:single`

    * Navigate to iSkipper-Software-master\iSkipper-Software-master\Software\iSkipper-JavaFX and open a powershell/cmd/terminal windows here. If you're on Windows you can ctrl+shift+right-click and click "Open Powershell window here".

    * Type "`mvn clean compile assembly:single`" and you're done.

4. (Optional) To delivery a ready-to-use package, use `jlink` to generated our customize Java Runtime Enviroment(JRE):
```shell
jlink --no-man-pages --add-modules  java.base,java.datatransfer,java.desktop,java.prefs,java.scripting,java.xml,jdk.jsobject,jdk.unsupported,jdk.unsupported.desktop,jdk.xml.dom --compress=2 --output iSkipper-JRE
```

TODOs
---------
* Implement session opening and closing.
* Implement full-auto modes.


See Also
---------
The original iSkipper repository: [wizard97/iSkipper](https://github.com/wizard97/iSkipper).

Related Hardware Project：[iSkipper-In-One-Package](https://github.com/charlescao460/iSkipper-In-One-Package).
