package kth.se.id2209.limmen.hw3.artistmanager.behaviours.auctioneer.subbehaviours;

import jade.content.OntoAID;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.JADEAgentManagement.QueryAgentsOnLocation;
import jade.domain.mobility.MobilityOntology;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import kth.se.id2209.limmen.hw3.artistmanager.ArtistManagerAgent;

import java.util.ArrayList;
import java.util.List;


/**
 * Behaviour for searching at the DF for registered art-bidders and sending request to the AMS to see the locations
 * of the agents in order to know which agents are located on the same container as the auctioneer.
 *
 * @author Kim Hammar on 2016-11-17.
 */
public class FindBidders extends OneShotBehaviour {

    /**
     * Action of the behaviour.
     * Searches the yellow pages (aka DF) for registered art-bidders, sends request to AMS for locations
     * and updates the datastore.
     */
    @Override
    public void action() {
        ArrayList<AID> allBidders = findAllBidders();
        ArrayList<OntoAID> agentsOnContainer = (ArrayList<OntoAID>) findAgentsOnContainer();
        ArrayList<AID> bidders = new ArrayList<>();
        for (OntoAID ontoAID : agentsOnContainer) {
            AID aid = ontoAID;
            if (allBidders.contains(aid)) {
                bidders.add(aid);
            }
        }
        ((ArtistManagerAgent) myAgent).updateLog("Found " + agentsOnContainer.size() + " agents on container, where " + bidders.size() + " are bidders");
        getDataStore().put(ArtistManagerAgent.BIDDERS, bidders);
    }

    private List findAgentsOnContainer() {
        ACLMessage request = prepareRequestToAMS();
        myAgent.send(request);
        MessageTemplate mt = MessageTemplate.MatchAll();
        ACLMessage resp = myAgent.blockingReceive(mt);
        return parseAMSResponse(resp);
    }

    private ACLMessage prepareRequestToAMS() {
        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.addReceiver(myAgent.getAMS());
        request.setLanguage(new SLCodec().getName());
        request.setOntology(MobilityOntology.getInstance().getName());
        Action act = new Action();
        act.setActor(myAgent.getAMS());
        QueryAgentsOnLocation action = new QueryAgentsOnLocation();
        action.setLocation(myAgent.here());
        act.setAction(action);
        try {
            myAgent.getContentManager().fillContent(request, act);
        } catch (Codec.CodecException | OntologyException ignore) {
            ignore.printStackTrace();
        }
        return request;
    }

    private List parseAMSResponse(ACLMessage response) {
        Result results = null;
        try {
            results = (Result) myAgent.getContentManager().extractContent(response);
        } catch (Codec.CodecException | OntologyException e) {
        }
        return ((jade.util.leap.ArrayList) results.getItems()).toList();
    }

    private ArrayList<AID> findAllBidders() {
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        ServiceDescription bidderDescription = new ServiceDescription();
        bidderDescription.setType("art-bidder");
        dfAgentDescription.addServices(bidderDescription);
        ArrayList<AID> allBidders = new ArrayList<>();
        DFAgentDescription[] bidders = new DFAgentDescription[0];
        try {
            bidders = DFService.search(myAgent, dfAgentDescription);
            for (int i = 0; i < bidders.length; i++) {
                allBidders.add(bidders[i].getName());
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        return allBidders;
    }
}
