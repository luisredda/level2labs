#!/bin/bash

DIR="$( cd "$( dirname "$0" )" && pwd )"

########## Variation to run ##########
# file -> run file transport
# chunked -> run chunked transport
# anything else -> run socket transport

if [ "" = "${1}" ]
then
    transport="socket"
elif [ "proto" = "${1}" ]
then
    transport="proto"
else
    transport="socket"
fi

echo "Using $transport transport."

########## APPD instrumentation ##########

# Edit this property to point to the javaagent directory
AGENT=/Users/trader/agents/4.3-prerelease

APP=L2-${transport}
STIER=Sender
RTIER=Receiver
SNODE=SenderNode
RNODE=ReceiverNode

APPJAR=target/level2labs-1.0-SNAPSHOT.jar

echo "Receiver command: java -javaagent:${AGENT}/javaagent.jar -Dappdynamics.agent.applicationName=${APP} -Dappdynamics.agent.tierName=${RTIER} -Dappdynamics.agent.nodeName=${RNODE} -cp ${APPJAR} com.appdynamics.javacert.level2.Receiver ${transport}"
nohup java -javaagent:${AGENT}/javaagent.jar -Dappdynamics.agent.applicationName=${APP} -Dappdynamics.agent.tierName=${RTIER} -Dappdynamics.agent.nodeName=${RNODE} -cp ${APPJAR} com.appdynamics.javacert.level2.Receiver ${transport} > ${DIR}/../rcvr.out 2>&1 &
echo "Started receiver."
sleep 2
echo "Sender command: java -javaagent:${AGENT}/javaagent.jar -Dappdynamics.agent.applicationName=${APP} -Dappdynamics.agent.tierName=${STIER} -Dappdynamics.agent.nodeName=${SNODE} -cp ${APPJAR} com.appdynamics.javacert.level2.Sender ${transport}"
nohup java -javaagent:${AGENT}/javaagent.jar -Dappdynamics.agent.applicationName=${APP} -Dappdynamics.agent.tierName=${STIER} -Dappdynamics.agent.nodeName=${SNODE} -cp ${APPJAR} com.appdynamics.javacert.level2.Sender ${transport} > ${DIR}/../sndr.out 2>&1 &
echo "Started sender."