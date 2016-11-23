package kth.se.id2209.limmen.hw3.artistmanager.behaviours.auctioneer.subbehaviours;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.Location;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.JADEAgentManagement.WhereIsAgentAction;
import jade.domain.mobility.MobilityOntology;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.Iterator;
import kth.se.id2209.limmen.hw3.artistmanager.ArtistManagerAgent;


/**
 * Behaviour for searching at the DF for registered art-bidders.
 *
 * @author Kim Hammar on 2016-11-17.
 */
public class FindBidders extends OneShotBehaviour {

    /**
     * Action of the behaviour.
     * Searches the yellow pages (aka DF) for registered art-bidders and updates the datastore.
     */
    @Override
    public void action() {
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        ServiceDescription bidderDescription = new ServiceDescription();
        bidderDescription.setType("art-bidder");
        dfAgentDescription.addServices(bidderDescription);
        DFAgentDescription[] bidders = new DFAgentDescription[0];
        try {
            bidders = DFService.search(myAgent, dfAgentDescription);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        findLocations(bidders);
        getDataStore().put(ArtistManagerAgent.BIDDERS, bidders);
    }

    private void findLocations(DFAgentDescription[] bidders) {
        ACLMessage request = prepareRequestToAMS(bidders[0].getName());
        System.out.println("Sending request to AMS: " + myAgent.getAMS().getName());
        myAgent.send(request);
        //Receive response from AMS
       /* MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchSender(myAgent.getAMS()),
                MessageTemplate.MatchPerformative(ACLMessage.INFORM)); */
        MessageTemplate mt = MessageTemplate.MatchAll();
        System.out.println("request sent, entering blocking receive");
        ACLMessage resp = myAgent.blockingReceive(mt);
        System.out.println("Received response: " + resp.toString());
        Location location = parseAMSResponse(resp);
        System.out.println("Received location name: " + location.getName() + " | address:" + location.getAddress());
    }

    private ACLMessage prepareRequestToAMS(AID agent) {
        myAgent.getContentManager().registerLanguage(new SLCodec());
        myAgent.getContentManager().registerOntology(MobilityOntology.getInstance());
        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.addReceiver(myAgent.getAMS());
        request.setLanguage(new SLCodec().getName());
        request.setOntology(MobilityOntology.getInstance().getName());
        Action act = new Action();
        act.setActor(myAgent.getAMS());
        WhereIsAgentAction action = new WhereIsAgentAction();
        action.setAgentIdentifier(agent);
        act.setAction(action);
        try {
            myAgent.getContentManager().fillContent(request, act);
        } catch (Codec.CodecException | OntologyException ignore) {
            ignore.printStackTrace();
        }
        return request;
    }

    private Location parseAMSResponse(ACLMessage response) {
        Result results = null;
        try {
            results = (Result) myAgent.getContentManager().extractContent(response);
        } catch (Codec.CodecException | OntologyException e) {
        }
        Iterator it = results.getItems().iterator();
        Location loc = null;
        if (it.hasNext())
            loc = (Location) it.next();
        return loc;
    }
}
