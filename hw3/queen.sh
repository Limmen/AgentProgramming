#!/bin/bash

AGENT_CLASS=kth.se.id2209.limmen.queen.QueenAgent;

if [[ "$#" == 2 ]]; then
    HOST=$1
    AGENT=$2
fi

if [[ "$#" == 1 ]]; then
    HOST=$1
    AGENT="queen-agent-1"
fi

if [[ "$#" == 0 ]]; then
    HOST="localhost"
    AGENT="queen-agent-1"
fi

if [[ "$#" < 3 ]]; then
    AGENTS="-agents '$AGENT:$AGENT_CLASS(0,1)'"
fi

if [[ "$#" == 3 ]]; then
    HOST=$1
    AGENT=$2
    AGENTS="-agents '"
    for i in `seq 1 $3`;
        do
                AGENTS="$AGENTS$AGENT-$(($i-1)):$AGENT_CLASS($(($i-1)),$3);"
        done
     AGENTS="$AGENTS'"
fi

if [[ "$#" > 3 ]]; then
    echo "Usage: $0 (main container host) (number of agents)" >&2
    exit 1
fi

mvn compile exec:java \
    -Dexec.mainClass=jade.Boot \
    -Dexec.args="-container -host $HOST $AGENTS"
