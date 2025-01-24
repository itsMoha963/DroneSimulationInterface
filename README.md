# 🚁 Drone Simulation Interface

This project was created for our Object-Oriented Programming in Java class in our 3rd semester at the Frankfurt University of Applied Sciences.

## Table of Contents
- Prerequisites
- Setup

## Prerequisites
1. JDK: Ensure you have a JDK installed. For this, we used Java 23, but older versions should work too.
2. Maven: Ensure Maven is installed.
3. IDE: We recommend IntelliJ IDEA as the setup is very easy and all dependencies get installed automatically.

## Setup
1. Clone the Repository
```console
git clone https://github.com/MandoV0/DroneSimulationInterface.git
cd DroneSimulationInterface
```
2. Ensure you are connected to the university's Wi-Fi network or accessing it through a VPN.
3. Start the Project in Your IDE
If you are using IntelliJ, simply click on the `pom.xml`. A button will appear in the top-right corner to install all required dependencies.
3. Build the Project
```console
mvn clean package
```

## Running the Program
1. Run from the IDE
- Open the `Main.java` file in your IDE.
- Right-click the file and select Run or use the IDE’s run configuration.
2. Run from Command Line
```console
java -jar target/DSI.jar
```

## Collaborators
| Name          | Role          | GitHub Profile         |
|---------------|---------------|------------------------|
| Burak         | .             | @MandoV0              |
| Gabriel       | .             | @Gibo20001119         |
| Mohammad      | .             | @itsMoha963           |

## Screenshots
XXXX

## Dependencies
1. FlatLaf (for modern UI look and feel):
```xml
<dependency>
    <groupId>com.formdev</groupId>
    <artifactId>flatlaf</artifactId>
    <version>3.5.4</version>
</dependency>
```

2. org.json (for JSON parsing):
```xml
<dependency>
  <groupId>org.json</groupId>
    <artifactId>json</artifactId>
    <version>20231013</version>
</dependency>
```











