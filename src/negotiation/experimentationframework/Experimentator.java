package negotiation.experimentationframework;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jdom.JDOMException;

import negotiation.experimentationframework.Laborantin.NotEnoughMachinesException;
import negotiation.faulttolerance.experimentation.ReplicationExperimentationProtocol;
import dima.basicagentcomponents.AgentIdentifier;
import dima.basicagentcomponents.AgentName;
import dima.introspectionbasedagents.APIAgent;
import dima.introspectionbasedagents.APILauncherModule;
import dima.introspectionbasedagents.BasicCompetentAgent;
import dima.introspectionbasedagents.annotations.Competence;
import dima.introspectionbasedagents.annotations.MessageHandler;
import dima.introspectionbasedagents.annotations.ProactivityInitialisation;
import dima.introspectionbasedagents.services.CompetenceException;
import dima.introspectionbasedagents.services.core.loggingactivity.LogService;
import dima.introspectionbasedagents.services.core.observingagent.NotificationMessage;
import dima.introspectionbasedagents.services.core.observingagent.NotificationEnvelopeClass.NotificationEnvelope;
import dima.kernel.FIPAPlatform.AgentManagementSystem;
import dima.kernel.communicatingAgent.OntologyBasedAgent;
import dimaxx.server.HostIdentifier;


/**
 * The experimentator is in charge of sequencially distribute the different experiences  launch
 * that are modelled as Laborantin.
 * The different experiences to launch are provided with the experimentation protocol  
 * 
 * @author Sylvain Ductor
 *
 */
public class Experimentator extends APIAgent{
	private static final long serialVersionUID = 6985131313855716524L;

	//
	// Accessors
	//

	final ExperimentationProtocol myProtocol;
	final File f;

//	//Integer represent the sum of the number of agent of each simulation that uses the given machine 
//	public final MachineNetwork machines;
	public static final AgentIdentifier myId = new AgentName("zi experimentator");

	/*
	 * 
	 */

	public final LinkedList<ExperimentationParameters> simuToLaunch;

	public final Map<AgentIdentifier, Laborantin> launchedSimu =
			new HashMap<AgentIdentifier, Laborantin>();
	public int awaitingAnswer=-1;

	//
	// Constructor
	//

	public Experimentator(ExperimentationProtocol myProtocol) throws CompetenceException {
		super(myId);
//		this.machines = new MachineNetwork(machines);
		this.f = new File(ReplicationExperimentationProtocol.resultPath);
		//		Writing.log(
		//				this.f,
		//				myProtocol.getDescription(),
		//				true, false);
		this.myProtocol=myProtocol;
		simuToLaunch = myProtocol.generateSimulation();

		this.logMonologue("Experimentator created for:\n"+myProtocol.getDescription());//+" will use :"+getApi().getAvalaibleHosts());
	}

	//
	// Methods
	//	



	//Executed initially then called by collect result
	@ProactivityInitialisation
	public boolean launchSimulation() throws CompetenceException{
		this.logMonologue("Launching simulations --> Available Memory :"+Runtime.getRuntime().freeMemory()+"/"+Runtime.getRuntime().totalMemory(),LogService.onBoth);
		if (this.awaitingAnswer==0){
			System.out.println("1");
			this.logMonologue("yyyyyyyyeeeeeeeeeeeeaaaaaaaaaaaaahhhhhhhhhhh!!!!!!!!!!!",LogService.onBoth);
			this.setAlive(false);
			this.logMonologue(myProtocol.getDescription(),LogService.onBoth);
			System.exit(1);
		} else if (!this.simuToLaunch.isEmpty()){
			this.logMonologue("launching new exp",LogService.onBoth);
			ExperimentationParameters nextSimu = null;
			try {				
				//				while (!this.simuToLaunch.isEmpty()){
				nextSimu = this.simuToLaunch.pop();
				Laborantin l = myProtocol.createNewLaborantin(nextSimu, getApi());
				launch(l);
				startActivity(l);
				this.launchedSimu.put(l.getId(), l);
				//				}
			} catch (NotEnoughMachinesException e) {
				this.simuToLaunch.add(nextSimu);
				this.logMonologue("aaaaaaaaarrrrrrrrrrrrrrrrggggggghhhhhhhhhh",LogService.onBoth);
			}
		}
		return true;
	}




	@MessageHandler
	@NotificationEnvelope
	public void collectResult(final NotificationMessage<SimulationEndedMessage> n) throws CompetenceException{
		logMonologue(n.getSender()+" is finished",LogService.onBoth);
		this.launchedSimu.get(n.getSender()).kill();
		this.launchedSimu.remove(n.getSender());
		//		laborantinLauncher.destroy(n.getSender());
		this.awaitingAnswer--;
		this.logMonologue("Available Memory Before GC :"+Runtime.getRuntime().freeMemory()+"/"+Runtime.getRuntime().totalMemory()
				+" free (ko): "+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()/1024),LogService.onBoth);
		System.gc();
		this.logMonologue("... After GC :"+Runtime.getRuntime().freeMemory()+"/"+Runtime.getRuntime().totalMemory()
				+" free (ko): "+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()/1024),LogService.onBoth);
		this.launchSimulation();
	}

	/*
	 * 
	 */

	public static void main(final String[] args)
			throws CompetenceException, IllegalArgumentException, IllegalAccessException{
		Experimentator exp = new Experimentator(new ReplicationExperimentationProtocol());
//		exp.initAPI(true);//FIPA
				exp.initAPI(false);//SCHEDULED
//				exp.initAPI(7779,7778);//DARX LOCAL
		exp.launchMySelf();
	}
}


//		final List machines = new LinkedList<HostIdentifier>();
//		machines.add(new HostIdentifier("localhost", 7777));
