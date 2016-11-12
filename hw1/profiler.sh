#!/bin/bash

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

if [[ "$#" > 2 ]]; then
    echo "Usage: $0 (main container host) (profiler agent name)" >&2
    exit 1
fi

AGENT_CLASS=kth.se.id2209.limmen.profiler.ProfilerAgent;

mvn compile exec:java \
    -Dexec.mainClass=jade.Boot \
    -Dexec.args="-container -host $HOST -agents '$AGENT:$AGENT_CLASS(kim,painting,sthlm,22,male)'"
