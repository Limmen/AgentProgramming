# Homework 1 ID2209 - SmartMuseum Multi Agent System

## How to run

Example on how you can do a test-run of the M.A.S:

Open up 4 shells/cmds and in perform the following actions in the given order:

---

In the first shell start the maincontainer on localhost port 1099 by typing:
 
 `./main.sh`
 
---
 
In the second shell start 3 curator agents with name *curator-agent-id* that connects with the main container on localhost port 1099:

`./curator.sh localhost curator-agent 3`

---

In the third shell start 3 tourguide agents with name *tourguide-agent-id* that connects with the main container on localhost port 1099:

`./tourguide.sh localhost tourguide-agent 3`

---

In the fourth shell start a profiler agent with name *profiler-agent-1* that connects with the main container on localhost port 1099:

`./profiler.sh localhost profiler-agent-1`

---

Now you can interact with the profiler through the profiler shell and do the following:

* Initialize User profile
* Search for tourguides
* Select tourguides
* Receive and print virtual tours from tourguides
* Visit artifacts in vitual tours

You can also add more curators/tourguides/profilers.

**Note**: A default art-gallery is created upon startup which contain arts of the following genres: painting, sculpture, conceptual art, street art, digital art

If you initialize the profiler with other interest than those, you wont find any suited virtual tours. (This can easily be extended in future by making the Curator agent interactive and able to add art-artifacts dynamically.

## Commands

**build** 

`mvn install`

**run main container**

(starts the main container on localhost port 1099)

`./main.sh`

**run curator agent(s) on a single container**

`./curator.sh`

running curator with arguments (otherwise default values are chosen):

`./curator.sh` (hostname of main container) (agentname)

running multiple curator agents (names will have attached counter-ids)

`./curator.sh` (hostname of main container) (agent basename) (number of agents)

**run single profiler agent(s) on a container**

`./profiler.sh` 

running profiler with arguments (otherwise default values are chosen):

`./profiler.sh` (hostname of main container) (agentname)

running multiple profiler agents (names will have attached counter-ids)

`./profiler.sh` (hostname of main container) (agent basename) (number of agents)

**run single tourguide agent(s) on a container**

`./tourguide.sh`

running tourguide with arguments (otherwise default values are chosen):

`./tourguide.sh` (hostname of main container) (agentname)

running multiple tourguide agents (names will have attached counter-ids)

`./tourguide.sh` (hostname of main container) (agent basename) (number of agents)



