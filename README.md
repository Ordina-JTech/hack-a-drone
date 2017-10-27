# Introduction
The goal of this manual is to give a short and easy to follow overview of the JTech Hack-A-Drone project. As of now, in this project, the drone can be controlled via the app or the Java Hack-A-Drone project. In the Java code the user can created a fixed path for the drone to fly or control the drone via the computer with either a keyboard or controller. The drone works on windows, unix and  OSX.

- Added [presentatie] slides
- Based on [voorbeeldapplicatie] van [Orfeo Ciano]


## Requirements

- A drone
- Laptop with the following software installed:
  - Java 8
  - Maven 3
  - Your favorite IDE
- Java Code to control the drone


## The drone

*Cheerson CX-10WD-TX*

- 2.4 GHz WiFi Remote Control
- 3.7 Volt 150 mAh
- 0.3 Mega Pixel Camera (640x480)
- Weight 18 gram
- Flying time 4–5 minutes
- Charging time 20–30 minutes


# Set up

## The Java-code

- Haal de Java-code op van GitHub:
  - https://github.com/Ordina-JTech/hack-a-drone.git
- Open de code in jouw favoriete IDE


## Drone WiFi connection

- Turn the drone on
- Connect to the WiFi of the drone
  - The drone can be recognized with the name: "CX-10WD-******"
  - Set the drone close the laptop for easy detection
- Start the Java-code with nl.ordina.jtech.hackadrone.gui.GUI


## Functions


## General User Interface (GUI)
  
- Click on "Connect" connect with the drone via the wifi
- Click on "Start Controls" to controle the drone with the keyboard
- Click on "Start Camera" to activate the camera
- Click on "Start Recorder" to record camera images
- Click on "Start AP" to start the auto pilot
- Click on "Start Deep Learning" activate the deep learning function

## Drone controles

- Use the left arrow to take off
- Use the right  arrow to land
- Use the up arrow to increase the hight during flight
- Use the down arrow to decrease the hight during the flight
- Use the W key to go forward
- Use the S key to go backwards
- Use the A key to go left
- Use the D key to go right
- Use the Q key to turn left
- Use the E key to turn right

# Future goals

- Write a functional AI component
  - Try this with: nl.ordina.jtech.hackadrone.core.io.AI
- Apply swarm theory to drone

[presentatie]: https://ordina-jtech.github.io/hack-a-drone
[voorbeeldapplicatie]: https://github.com/Otacon/wifi_china_drone_controller
[Orfeo Ciano]: https://github.com/Otacon
