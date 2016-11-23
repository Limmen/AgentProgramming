package kth.se.id2209.limmen.hw3.queen.behaviours;

import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.abs;

/**
 * Behaviour that listens for messages from other queens and selects positions on the board
 *
 * @author Kim Hammar on 2016-11-22.
 */
public class QueenServer extends SimpleBehaviour {
    private int slot = -1;
    private ArrayList<Integer> triedSlots = new ArrayList<>();
    private int[][] board;
    private int id;
    private int numberOfAgents;
    private boolean done = false;

    /**
     * Class constructor that initializes the behaviour
     *
     * @param board initial board for the puzzle
     * @param numberOfAgents number of queen-agents in puzzle
     * @param id id of this agent in the puzzle
     */
    public QueenServer(int[][] board, int numberOfAgents, int id) {
        this.board = board;
        this.numberOfAgents = numberOfAgents;
        this.id = id;
        initializeBoard();
    }

    /**
     * Main action of the behaviour, will check the mailbox for messages and act upon them.
     */
    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchOntology("queens");
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            /**
             * Message from preceeding queen
             */
            if (msg.getPerformative() == ACLMessage.INFORM) {
                System.out.println("------------------------------------------------------------------------------------------------------------------");
                System.out.println("Agent " + id + " received board from preceeding agent");
                System.out.println("------------------------------------------------------------------------------------------------------------------");
                try {
                    board = (int[][]) msg.getContentObject();
                    triedSlots = new ArrayList<>();
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
                gameRound();
            }
            /**
             * Message from next queen
             */
            if (msg.getPerformative() == ACLMessage.FAILURE) {
                System.out.println("------------------------------------------------------------------------------------------------------------------");
                System.out.println("Agent " + id + " received request to change previously selected slot");
                gameRound();
                System.out.println("------------------------------------------------------------------------------------------------------------------");
            }
        } else {
            block();
        }
    }

    /**
     * Method that simulates a round in the puzzle game, it will try to find a safe slot on the board to move to.
     * If no slot is found we ask the preceeding queen to move.
     * If no safe slot can be found and there is no preceeding queen (i.e. id = 0) then the puzzle is not solvable.
     * If a safe slot is found and this queen is not the last one in turn to move on the board the agent will forward
     * the board with the selected position to the queen in front.
     * If a safe slot is found and this queen is the last on to pick a slot on the board it will exit.
     */
    private void gameRound() {
        DFAgentDescription[] queens = (DFAgentDescription[]) getDataStore().get(QueenFinder.SORTED_QUEENS);
        if (selectSafeSlot()) {
            if (id == numberOfAgents - 1) {
                System.out.println("------------------------------------------------------------------------------------------------------------------");
                System.out.println("Game finnished");
                prettyPrintBoard();
                System.out.println("------------------------------------------------------------------------------------------------------------------");
                exitAndShutDown();
                done = true;
            } else {
                forwardBoardToNextQueen(queens);
            }
        } else {
            if (id == 0) {
                System.out.println("------------------------------------------------------------------------------------------------------------------");
                System.out.println("Game over");
                prettyPrintBoard();
                System.out.println("------------------------------------------------------------------------------------------------------------------");
                exitAndShutDown();
                done = true;
            } else {
                askPreceedingQueenToChangeSlot(queens);
            }

        }
    }

    /**
     * Method that sends message to preceeding queen and asks it to move
     *
     * @param queens array of queens in the M.A.S.
     */
    private void askPreceedingQueenToChangeSlot(DFAgentDescription[] queens) {
        DFAgentDescription receiver = queens[id - 1];
        ACLMessage backwardMsg = new ACLMessage(ACLMessage.FAILURE);
        backwardMsg.setOntology("queens");
        backwardMsg.addReceiver(receiver.getName());
        myAgent.send(backwardMsg);
    }

    /**
     * Method that sends a message to queen in front and informs it about the updated board
     *
     * @param queens array of queens in the M.A.S
     */
    private void forwardBoardToNextQueen(DFAgentDescription[] queens) {
        DFAgentDescription receiver = queens[id + 1];
        ACLMessage forwardMsg = new ACLMessage(ACLMessage.INFORM);
        forwardMsg.setOntology("queens");
        forwardMsg.addReceiver(receiver.getName());
        try {
            forwardMsg.setContentObject(board);
        } catch (IOException e) {
            e.printStackTrace();
        }
        myAgent.send(forwardMsg);
    }

    /**
     * Method for initializing the board with -1 on every slot
     */
    private void initializeBoard() {
        for (int i = 0; i < board.length; i++) {
            Arrays.fill(board[i], -1);
        }
    }

    /**
     * Method that tries to find a safe slot on the board, it also checks that it does'nt try a slot that it previously
     * have used.
     *
     * @return true if a safe slot was found, otherwise false
     */
    private boolean selectSafeSlot() {
        ArrayList<Integer> possibleSlots = (ArrayList<Integer>) IntStream.rangeClosed(0, board[id].length-1).boxed().collect(Collectors.toList());
        Collections.shuffle(possibleSlots);
        Arrays.fill(board[id], -1);
        slot = -1;
        for(int i : possibleSlots){
            if (safeDiagonally(i) && safeVertically(i) && !triedSlots.contains(i)) {
                slot = i;
                break;
            }
        }
        if (slot != -1) {
            System.out.println("------------------------------------------------------------------------------------------------------------------");
            System.out.println("Agent " + id + " selected slot " + "(" + id + "," + slot + ")" + " which is safe");
            System.out.println("------------------------------------------------------------------------------------------------------------------");
            board[id][slot] = id;
            triedSlots.add(slot);
            return true;
        } else {
            System.out.println("------------------------------------------------------------------------------------------------------------------");
            System.out.println("Agent " + id + " failed to find a safe slot that have not previously been tried");
            prettyPrintBoard();
            System.out.println("Tried slots: ");
            for (int i : triedSlots) {
                System.out.print("(" + id + "," + i + ") ");
            }
            System.out.println();
            System.out.println("------------------------------------------------------------------------------------------------------------------");
            return false;
        }
    }

    /**
     * Checks whether a given slot on the board is safe vertically
     *
     * @param index slot on the board
     * @return true if it is safe otherwise false
     */
    private boolean safeVertically(int index) {
        for (int j = 0; j < board.length; j++) {
            if (board[j][index] != -1)
                return false;
        }
        return true;
    }

    /**
     * Checks whether a given slot on the board is safe diagonally
     *
     * @param index slot on the board
     * @return true if it is safe otherwise false
     */
    private boolean safeDiagonally(int index) {
        for (int i = 0; i < id; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != -1) {
                    if (abs(j - index) == abs(i - id))
                        return false;
                }
            }
        }
        return true;
    }

    /**
     * Method for prettyprinitng the board
     */
    private void prettyPrintBoard() {
        for (int i = 0; i < board.length; i++) {
            System.out.println();
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != -1)
                    System.out.print("(" + board[i][j] + ")");
                else
                    System.out.print("( )");
            }
        }
        System.out.println();
    }

    /**
     * Method for shutting down the container
     */
    private void exitAndShutDown(){
        final AgentContainer containerController = myAgent.getContainerController();
        myAgent.doDelete();
        new Thread(() -> {
            try {
                containerController.kill();
            } catch (StaleProxyException e) {
            }
        }).start();
    }

    /**
     * Checks whether this behaviour has terminated.
     *
     * @return true if terminated otherwise false
     */
    @Override
    public boolean done() {
        return done;
    }
}
