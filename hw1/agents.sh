#!/bin/bash

if [[ "$#" == 4 ]]; then
    HOST=$1
    AGENT1=$2
    AGENT2=$3
    AGENT3=$4
fi

if [[ "$#" == 3 ]]; then
    HOST=$1
    AGENT1=$2
    AGENT2=$3
    AGENT3="tourguide-agent-1"
fi

if [[ "$#" == 2 ]]; then
    HOST=$1
    AGENT1=$2
    AGENT2="profiler-agent-1"
    AGENT3="tourguide-agent-1"
fi

if [[ "$#" == 1 ]]; then
    HOST=$1
    AGENT1="curator-agent-1"
    AGENT2="profiler-agent-1"
    AGENT3="tourguide-agent-1"
fi

if [[ "$#" == 0 ]]; then
    HOST="localhost"
    AGENT1="curator-agent-1"
    AGENT2="profiler-agent-1"
    AGENT3="tourguide-agent-1"
fi

if [[ "$#" > 4 ]]; then
    echo "Usage: $0 (main container host) (curator agent name) (profiler agent name) (tourguide agent name)" >&2
    exit 1
fi

AGENT_CLASS1=kth.se.id2209.limmen.curator.CuratorAgent;
AGENT_CLASS2=kth.se.id2209.limmen.profiler.ProfilerAgent;
AGENT_CLASS3=kth.se.id2209.limmen.tourguide.TourGuideAgent;

mvn compile exec:java \
    -Dexec.mainClass=jade.Boot \
    -Dexec.args="-container -host $HOST \
    -agents '\
    $AGENT1:$AGENT_CLASS1();\
    $AGENT2:$AGENT_CLASS2();\
    $AGENT3:$AGENT_CLASS3()'"
