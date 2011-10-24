package dima.introspectionbasedagents;

import java.util.Date;
import java.util.Map;

import dima.basicagentcomponents.AgentIdentifier;
import dima.introspectionbasedagents.shells.BasicCommunicatingShell;
import dima.kernel.FIPAPlatform.AgentManagementSystem;
import dima.kernel.communicatingAgent.BasicCommunicatingAgent;

public class BasicIntrospectedCommunicatingAgent extends BasicCommunicatingAgent{

	/**
	 *
	 */
	private static final long serialVersionUID = -7910893993695270592L;
	private BasicCommunicatingShell myShell;
	private final Date creation;

	public BasicIntrospectedCommunicatingAgent() {
		super();
		this.creation = new Date();
	}

	public BasicIntrospectedCommunicatingAgent(final AgentIdentifier newId) {
		super(newId);
		this.creation = new Date();
	}

	public BasicIntrospectedCommunicatingAgent(final Map<?, ?> mp, final AgentIdentifier newId) {
		super(mp, newId);
		this.creation = new Date();
	}

	public BasicIntrospectedCommunicatingAgent(final Map<?, ?> mp) {
		super(mp);
		this.creation = new Date();
	}

	public BasicIntrospectedCommunicatingAgent(final String newId) {
		super(newId);
		this.creation = new Date();
	}

	/*
	 *
	 */

	public BasicIntrospectedCommunicatingAgent(final Date horloge) {
		super();
		this.creation = horloge;
	}

	public BasicIntrospectedCommunicatingAgent(final AgentIdentifier newId, final Date horloge) {
		super(newId);
		this.creation = horloge;
	}

	public BasicIntrospectedCommunicatingAgent(final Map<?, ?> mp, final AgentIdentifier newId, final Date horloge) {
		super(mp, newId);
		this.creation = horloge;
	}

	public BasicIntrospectedCommunicatingAgent(final Map<?, ?> mp, final Date horloge) {
		super(mp);
		this.creation = horloge;
	}

	public BasicIntrospectedCommunicatingAgent(final String newId, final Date horloge) {
		super(newId);
		this.creation = horloge;
	}
	

	//
	// Proactivity
	//


	@Override
	public final void proactivityInitialize() {
		this.myShell =initiateMyShell();
		myShell.proactivityInitialize();
		Thread.yield();
	}

	@Override
	public final void preActivity() {
		myShell.preActivity();
			Thread.yield();
	}

	@Override
	public final void step() {
			this.myShell.step();
	}

	@Override
	public final void postActivity(){
		this.myShell.postActivity();
			Thread.yield();
	}
	
	@Override
	public void proactivityTerminate() {
		myShell.proactivityTerminate();
		if (AgentManagementSystem.getDIMAams()!=null) 
			AgentManagementSystem.getDIMAams().unregister(getIdentifier());
		this.myShell=null;
		Thread.yield();
	}
	
	//
	// Primitive
	//
	
	protected BasicCommunicatingShell initiateMyShell(){
		return  new BasicCommunicatingShell(this,this.creation);
	}
	
	
}