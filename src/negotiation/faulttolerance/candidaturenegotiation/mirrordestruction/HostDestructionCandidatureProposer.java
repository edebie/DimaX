package negotiation.faulttolerance.candidaturenegotiation.mirrordestruction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import negotiation.faulttolerance.ReplicationCandidature;
import negotiation.faulttolerance.ReplicationSpecification;
import negotiation.faulttolerance.negotiatingagent.HostState;
import negotiation.faulttolerance.negotiatingagent.ReplicaState;
import negotiation.negotiationframework.SimpleNegotiatingAgent;
import negotiation.negotiationframework.interaction.ResourceIdentifier;
import negotiation.negotiationframework.interaction.candidatureprotocol.mirror.IllAnswer;
import negotiation.negotiationframework.interaction.consensualnegotiation.AbstractProposerCore;
import dima.introspectionbasedagents.NotReadyException;
import dima.introspectionbasedagents.competences.BasicAgentCompetence;
import dima.introspectionbasedagents.coreservices.observingagent.PatternObserverWithHookCompetence.EventHookedMethod;

public class HostDestructionCandidatureProposer
extends
BasicAgentCompetence<SimpleNegotiatingAgent<ReplicationSpecification, HostState, ReplicationCandidature>>
implements
AbstractProposerCore<
SimpleNegotiatingAgent<ReplicationSpecification, HostState, ReplicationCandidature>,
ReplicationSpecification, 
HostState, 
ReplicationCandidature>  {
	private static final long serialVersionUID = -7851541913056925444L;


	private Collection<ReplicationCandidature> contractsToPropose = new HashSet<ReplicationCandidature>();


	//
	// Communication
	//

	@EventHookedMethod(IllAnswer.class)
	public void receiveFullNotification(final IllAnswer<ReplicationCandidature> n) {
		Collection<ReplicationCandidature> unacceptedContracts = n.getAnswers().getRejectedContracts();
		
		if (!getMyAgent().getMyProtocol().negotiationAsInitiatorHasStarted()
				&& !unacceptedContracts.isEmpty() 
				&& n.getAnswers().getContractsAcceptedBy(getIdentifier()).isEmpty()){
			//The state of the host is stable and there is rejected contract : we try to find if there is destruction that could loccally improve the system
			ReplicationCandidature minRefused =  
					Collections.min(
							unacceptedContracts, 
							this.getMyAgent().getMyPreferenceComparator());
			Collection<ReplicationCandidature> toPutOnWait = new ArrayList<ReplicationCandidature>();

			for (ReplicationCandidature c : unacceptedContracts){
				Iterator<ReplicaState> itAg = 
						this.getMyAgent().
						getMyCurrentState().getMyAgents();

				while (itAg.hasNext()){
					ReplicaState agToKill = itAg.next();
					if (agToKill.getMyReliability() > c.getAgentInitialState().getMyReliability()){
						ReplicationDestructionCandidature replicationDestructionCandidature = new ReplicationDestructionCandidature(
								(ResourceIdentifier) this.getMyAgent().getIdentifier(), 
								agToKill.getMyAgentIdentifier(),
								minRefused);
						replicationDestructionCandidature.setSpecification(getMyAgent().getMySpecif(replicationDestructionCandidature));
						replicationDestructionCandidature.setSpecification(agToKill);
						contractsToPropose.add(replicationDestructionCandidature);
						toPutOnWait.add(c);
					}
				}
			}

			for (ReplicationCandidature c : toPutOnWait){
				n.getAnswers().removeRejection(this.getMyAgent().getIdentifier(), c);
			}
		}
	}

	//
	// Methods
	//
	@Override
	public Collection<? extends ReplicationCandidature> getNextContractsToPropose()
			throws NotReadyException {
		Collection<ReplicationCandidature> result = new ArrayList<ReplicationCandidature>();
		result.addAll(contractsToPropose);
		contractsToPropose.clear();
		return result;		
	}
}


















//	private boolean iMFull = false;
//
//	@NotificationEnvelope
//	@MessageHandler
////	public void setiMFull(final NotificationMessage<ImFull> n) {
//		// logMonologue("fullllllllllllllllllll!"+n.getNotification().isFull());
//		this.iMFull = n.getNotification().isFull();
//	}
//
//	// @NotificationEnvelope
//	// @MessageHandler
//	// public void beInformedOfCandidature(final
//	// NotificationMessage<ReplicationCandidature> notification) {
//	// throw new RuntimeException("a revoir");
//	// ((HostCandidatureProposer) myProposerCore).minCandidatedReliability =
//	// Math.min(((HostCandidatureProposer)
//	// myProposerCore).minCandidatedReliability,
//	// n.getNotification().getAgentSpecification().getMyReliability());
//	// }
//
//	@ProactivityInitialisation
//	public boolean activateObservation() {
//		// this.getMyAgent().autoObserve(ReplicationCandidature.class);
//		this.getMyAgent().autoObserve(ImFull.class);
//		return true;
//	}
//
//	@Override
//	public Collection<ReplicationCandidature> getNextContractsToPropose()
//			throws NotReadyException {
//		final Collection<ReplicationCandidature> result = 
//				new ArrayList<ReplicationCandidature>();
//		if (this.iMFull) {
////			 this.logMonologue("fulll (2)!");
//			Double minCandidatedReliability = Double.POSITIVE_INFINITY;
//			for (final ReplicationCandidature c : this.getMyAgent()
//					.getMyProtocol().getContracts()
//					.getContractsRejectedBy(this.getMyAgent().getIdentifier())){
//				minCandidatedReliability = Math.min(minCandidatedReliability, c
//						.getAgentResultingState().getMyReliability());
//			}
//			Iterator<AgentIdentifier> itAg = 
//					(((NegotiatingHost) this.getMyAgent()).
//							getMyCurrentState().getMyAgents());
//			while (itAg.hasNext()){
//				AgentIdentifier id = itAg.next();
//				if (((NegotiatingHost) this.getMyAgent()).
//						getReliability(id) > minCandidatedReliability)
//					result.add(new ReplicationCandidatureWithMinInfo(
//							(ResourceIdentifier) this.getMyAgent()
//							.getIdentifier(), id, false,
//							minCandidatedReliability));
//			}
//
////			 if (!result.isEmpty())
////			 this.logMonologue("agent destruction toPropose "+result);
////			 logMonologue("fullllllllllllllllllll! wanna kill "+result);
//		}
//		return result;
//	}
//
//}
