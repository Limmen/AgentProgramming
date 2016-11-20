# Homework 2 ID2209 - Dutch Auction

## How to run

Example on how you can do a test-run of the M.A.S:

Open up 5 shells/cmds and perform the following actions in the given order:

---

In the first shell start the maincontainer on localhost port 1099 by typing:
 
 `./main.sh`
 
---
 
In the second shell start a curator agents with name *curator-agent-1* that connects with the main container on localhost port 1099:

`./curator.sh localhost curator-agent 3`

Then enter the desired strategy of the agent by picking one of the listed strategies

---

In the third shell start a curator agents with name *curator-agent-2* that connects with the main container on localhost port 1099:

`./curator.sh localhost curator-agent 3`

Then enter the desired strategy of the agent by picking one of the listed strategies

---

In the fourth shell start a curator agents with name *curator-agent-3* that connects with the main container on localhost port 1099:

`./curator.sh localhost curator-agent 3`

Then enter the desired strategy of the agent by picking one of the listed strategies

---

In the fifth shell start a artistmanager agent with name *artistmanager-agent-1* that connects with the main container on localhost port 1099:

`./artistmanager.sh localhost artistmanager-agent-1`

Then choose what good to auction and initial price, rate of reduction and reserved price to use

---

For the first CFP received for each auction the curator agent will ask the user to choose a valuation of the good, if no valuation is entered before timeout then a default valuation is chosen. 

When the auction is closed the artistmanager agent will ask you to start a new auction.
 
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

**run artistmanager agent(s) on a container**

`./artistmanager.sh`

running artistmanager with arguments (otherwise default values are chosen):

`./artistmanager.sh` (hostname of main container) (agentname)

running multiple artistmanager agents (names will have attached counter-ids)

`./artistmanager.sh` (hostname of main container) (agent basename) (number of agents)


## Author

Kim Hammar  <kimham@kth.se>
