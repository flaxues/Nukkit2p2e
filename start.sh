#!/bin/bash

# This is the shell startup file for Nukkit2p2e.
# Input ./start.sh while in the server directory
# to start the server.

#Change this to "true" to 
#loop Nukkit2p2e after restart!

DO_LOOP="true"

###############################
# DO NOT EDIT ANYTHING BELOW! #
###############################

clear

echo "Changing maven max heap size..."
export MAVEN_OPTS="-Xmx3500m"
echo Done! $MAVEN_OPTS

if git pull | grep -q 'Already up-to-date.'; then
    clear
    echo "Nothing  changed, starting..."
else
    ./compile.sh
    clear
    echo "Compiled, starting..." 
fi

sleep 2

clear 

while [ "$DO_LOOP" == "true" ]; do
	mvn exec:java -Dexec.mainClass="cn.nukkit.Nukkit" -Dexec.classpathScope=runtime
	echo "Press Ctrl+c to stop" 
	sleep 3
        if git pull | grep -q 'Already up-to-date.'; then
	    clear
            echo "Nothing  changed, starting..."
	else
	    ./compile.sh
	    sleep 2
	    clear
	    echo "Compiled, starting..." 
	fi
done
