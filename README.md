# 🚁 Drone Simulation Interface

This project was created for our Object-Oriented Programming in Java class in our 3rd semester at the Frankfurt University of Applied Sciences.

## Table of Contents
- Prerequisites
- Setup

## Prerequisites
1. JDK: Ensure you have a JDK installed. For this, we used Java 23, but older versions should work too.
2. Maven: Ensure Maven is installed.
3. IDE: We recommend IntelliJ IDEA as the setup is very easy and all dependencies get installed automatically.
4. Ensure you are connected to the university's Wi-Fi network or accessing it through a VPN.

## Setup
1. Clone the Repository
```console
git clone https://github.com/MandoV0/DroneSimulationInterface.git
cd DroneSimulationInterface
``` 
2. Start the Project in Your IDE
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
| Shaydur       | .             | @Shadowwing11         |

## Screenshots
![image](https://github.com/user-attachments/assets/29fa1ad7-b1a3-4165-ba97-3fbd1d68c7e8)
![image](https://github.com/user-attachments/assets/6459a7b2-59d5-4f63-a7e4-13feeeffdbf9)
![image](https://github.com/user-attachments/assets/49bce8ea-4f48-4ead-b86a-45d1115d210b)
![image](https://github.com/user-attachments/assets/b73210da-d962-471b-8e2f-9de8395aca33)
![image](https://github.com/user-attachments/assets/1c61e59f-0d5a-4c3f-81a4-93d0d8204252)
![image](https://github.com/user-attachments/assets/8eaa41e9-b5ca-4652-a19e-15ad4f7dadb8)
![image](https://github.com/user-attachments/assets/42c7942b-a03c-4047-8e4e-0fda3a92902f)
![image](https://github.com/user-attachments/assets/068c126e-3638-456a-96f4-f46b85bdb582)









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











