# Hack a Drone

- Bijhorende [presentatie] slides
- Gebaseerd op de [voorbeeldapplicatie] van [Orfeo Ciano]
- Werkend en getest op Windows, Unix en OSX


## Benodigdheden

- Een drone
- Laptop met daarop geïnstalleerd:
  - Java 8
  - Maven 3
  - IDE
- Java-code om de drone aan te sturen


## De drone

*Cheerson CX-10WD-TX*

- 2.4 GHz WiFi Remote Control
- 3.7 Volt 150 mAh
- 0.3 Mega Pixel Camera (640x480)
- Weight 18 gram
- Flying time 4–5 minutes
- Charging time 20–30 minutes


# Opzetten

## De Java-code

- Haal de Java-code op van GitHub:
  - https://github.com/Ordina-JTech/hack-a-drone.git
- Open de code in jouw favoriete IDE


## Verbinding maken met de drone

- Zet de drone aan
- Maak verbinding met de drone over WiFi
  - Je herkent de drone aan de benaming "CX-10WD-******"
  - Leg de drone dicht bij je laptop om hem makkelijk te vinden
- Start de Java-code op vanuit:
  - nl.ordina.jtech.hackadrone.gui.GUI


## Het gebruik van de GUI
  
- Klik op "Connect" om verbinding met de drone te maken
- Klik op "Start Controls" om de toetsenbordbesturing in te schakelen
- Klik op "Start Camera" om de camera in te schakelen
- Klik op "Start Recorder" om een video-opname te maken
- Klik op "Start AI" om de automatische piloot te starten


## Het besturen van de drone

- Gebruik de linker pijltjestoets om op te stijgen
- Gebruik de rechter pijltjestoets om te landen
- Gebruik de bovenste pijltjestoets om omhoog te gaan
- Gebruik de onderste pijltjestoets om omlaag te gaan
- Gebruik de toetsen W en S om voor- of achteruit te gaan
- Gebruik de toetsen A en D om links of rechts te gaan
- Gebruik de toetsen Q en E om links- of rechtsom te draaien


# Uitdagingen

## Uitbreiden van de drone

- Schrijf je eigen AI om de drone aan te sturen
- Gebruik hiervoor de basis Java-code van de AI:
  - nl.ordina.jtech.hackadrone.core.io.AI


[presentatie]: https://ordina-jtech.github.io/hack-a-drone
[voorbeeldapplicatie]: https://github.com/Otacon/wifi_china_drone_controller
[Orfeo Ciano]: https://github.com/Otacon
