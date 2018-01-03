#!/bin/bash

cp /home/pi/OpenCV/gearError.txt /home/pi/OpenCV/prevGearError.txt
while true
do
	ping -w 1 10.16.40.10 #10.16.40.183

	if [ $? -eq 0 ]; then
		echo "pinged"
		break;
	fi
done

while true
do
	ping -w 1 10.16.40.10 #roboRIO-1640-FRC.local
	
	if [ $? -eq 0 ]; then
		echo "pinged"
		break;
	fi
done

env LD_LIBRARY_PATH=/home/pi/OpenCV java -jar /home/pi/OpenCV/visionGear.jar 2>/home/pi/OpenCV/gearError.txt &
