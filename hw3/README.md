# Homework 3 ID2209 - Agent Coordination and Agent Mobility

## Task 1 Queens Puzzle
### How to run

Example on how you can do a test-run of the M.A.S:

Open up a 2 shells/cmds and execute in the following order:

---

In the first shell start the maincontainer on localhost port 1099 by typing:
 
 `./main.sh`
 
---
 
In the second shell start a queens puzzle with N queens with name *queen-agent-i* that connects with the main container on localhost port 1099:

`./queen.sh localhost queen-agent 8`

Then wait for the puzzle to finnish and afterwards if you want a different solution you can run the puzzle again (some randomness is involved in choosing the slots so if there exists multiple solutions you should be able to find all of them by trying a few times)

## Task 2 Mobile dutch auctioneers

### How to run

Example on how you can do a test-run of the M.A.S:

Open up a 2 shells/cmds and execute in the following order:

---

In the first shell start the maincontainer on localhost port 1099 by typing:
 
 `./main.sh`
 
---
 
In the second shell start a controller agent that connects with the main container on localhost port 1099 by typing:

`./controller.sh localhost`

Then you can create ArtistManagerAgents/CuratorAgents and move/clone them between containers and also start auctions and synthesize multiple auction results.
 
## Commands

**build** 

`mvn install`

**run main container**

(starts the main container on localhost port 1099)

`./main.sh`


**run queen agent(s) on a container**

`./queen.sh`

running queen with arguments (otherwise default values are chosen):

`./queen.sh` (hostname of main container) (agent basename) (number of queens)

**run controller agent**

`./controller.sh`

running controller with arguments (otherwise default values are chosen):

`./controller.sh` (hostname of main container) (agentname)

## Author

Kim Hammar  <kimham@kth.se>
