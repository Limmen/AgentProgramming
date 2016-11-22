package kth.se.id2209.limmen.queen.behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import kth.se.id2209.limmen.queen.gui.BoardFrame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.abs;

/**
 * @author Kim Hammar on 2016-11-22.
 */
public class QueenServer extends CyclicBehaviour {
    private int slot = -1;
    private ArrayList<Integer> triedSlots = new ArrayList<>();
    private int[][] board;
    private BoardFrame boardFrame;
    private int id;
    private int numberOfAgents;

    public QueenServer(int[][] board, BoardFrame boardFrame, int numberOfAgents, int id) {
        this.board = board;
        this.boardFrame = boardFrame;
        this.numberOfAgents = numberOfAgents;
        this.id = id;
        initializeBoard();
    }

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

    private void gameRound() {
        DFAgentDescription[] queens = (DFAgentDescription[]) getDataStore().get(QueenFinder.SORTED_QUEENS);
        if (selectSafeSlot()) {
            if (id == numberOfAgents - 1) {
                System.out.println("------------------------------------------------------------------------------------------------------------------");
                System.out.println("Game finnished");
                prettyPrintBoard();
                System.out.println("------------------------------------------------------------------------------------------------------------------");
            } else {
                forwardBoardToNextQueen(queens);
            }
        } else {
            if (id == 0) {
                System.out.println("------------------------------------------------------------------------------------------------------------------");
                System.out.println("Game over");
                prettyPrintBoard();
                System.out.println("------------------------------------------------------------------------------------------------------------------");
            } else {
                askPreceedingQueenToChangeSlot(queens);
            }

        }
    }

    private void askPreceedingQueenToChangeSlot(DFAgentDescription[] queens) {
        DFAgentDescription receiver = queens[id - 1];
        ACLMessage backwardMsg = new ACLMessage(ACLMessage.FAILURE);
        backwardMsg.setOntology("queens");
        backwardMsg.addReceiver(receiver.getName());
        myAgent.send(backwardMsg);
    }

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


    private void initializeBoard() {
        for (int i = 0; i < board.length; i++) {
            Arrays.fill(board[i], -1);
        }
    }

    private boolean selectSafeSlot() {
        Arrays.fill(board[id], -1);
        slot = -1;
        for (int i = 0; i < board[id].length; i++) {
            if (safeDiagonally(i) && safeVertically(i) && !triedSlots.contains(i)){
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
            for(int i : triedSlots){
                System.out.print("(" + id + "," + i + ") ");
            }
            System.out.println("------------------------------------------------------------------------------------------------------------------");
            return false;
        }

    }

    private boolean safeVertically(int index) {
        for (int j = 0; j < board.length; j++) {
            if (board[j][index] != -1)
                return false;
        }
        return true;
    }

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

    private void prettyPrintBoard(){
        for(int i = 0; i < board.length; i ++){
            System.out.println();
            for(int j = 0; j < board[i].length; j++){
                if(board[i][j] != -1)
                System.out.print("(" + board[i][j] + ")");
                else
                    System.out.print("( )");
            }
        }
        System.out.println();
    }
}
