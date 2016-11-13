#!/bin/bash
AGENT_CLASS=kth.se.id2209.limmen.profiler.ProfilerAgent;
if [[ "$#" == 2 ]]; then
    HOST=$1
    AGENT=$2
fi

if [[ "$#" == 1 ]]; then
    HOST=$1
    AGENT="profiler-agent-1"
fi

if [[ "$#" == 0 ]]; then
    HOST="localhost"
    AGENT="profiler-agent-1"
fi

if [[ "$#" < 3 ]]; then
    AGENTS="-agents '$AGENT:$AGENT_CLASS()'"
fi

if [[ "$#" == 3 ]]; then
    HOST=$1
    AGENT=$2
    AGENTS="-agents '"
    for i in `seq 1 $3`;
        do
                AGENTS="$AGENTS$AGENT-$i:$AGENT_CLASS();"
        done
     AGENTS="$AGENTS'"
fi

if [[ "$#" > 3 ]]; then
    echo "Usage: $0 (main container host) (profiler agent name) (number of agents)" >&2
    exit 1
fi

mvn compile exec:java \
    -Dexec.mainClass=jade.Boot \
    -Dexec.args="-container -host $HOST $AGENTS"
