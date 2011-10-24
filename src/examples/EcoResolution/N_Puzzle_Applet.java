package examples.EcoResolution;

import java.applet.Applet;
import java.awt.Color;

public class N_Puzzle_Applet extends Applet
{

	    /**
	 *
	 */
	private static final long serialVersionUID = -3158815161739387207L;

		PuzzleFrame tqFrame;

	//{{DECLARE_CONTROLS
	java.awt.Label label1;
	java.awt.Button button1;
	//}}

	class SymMouse extends java.awt.event.MouseAdapter
	{
		@Override
		public void mouseReleased(final java.awt.event.MouseEvent event)
		{
		}

		@Override
		public void mousePressed(final java.awt.event.MouseEvent event)
		{
		}

		@Override
		public void mouseClicked(final java.awt.event.MouseEvent event)
		{
		}
	}
	class SymAction implements java.awt.event.ActionListener
	{
		@Override
		public void actionPerformed(final java.awt.event.ActionEvent event)
		{
			final Object object = event.getSource();
			if (object == N_Puzzle_Applet.this.button1)
				N_Puzzle_Applet.this.button1_ActionPerformed(event);
		}
	}

	void button1_ActionPerformed(final java.awt.event.ActionEvent event)
	{
		this.tqFrame = new PuzzleFrame( this, "N-Puzzle Solving", 4);
	}
	@Override
	public void init()
	{

		// This code is automatically generated by Visual Cafe when you add
		// components to the visual environment. It instantiates and initializes
		// the components. To modify the code, only use code syntax that matches
		// what Visual Cafe can generate, or Visual Cafe may be unable to back
		// parse your Java file into its visual environment.
		//{{INIT_CONTROLS
		this.setLayout(null);
		this.setSize(376,140);
		this.setBackground(new Color(12632256));
		this.label1 = new java.awt.Label("Welcome to the EPS implementation of the N-Puzzle");
		this.label1.setBounds(17,28,344,27);
		this.add(this.label1);
		this.button1 = new java.awt.Button();
		this.button1.setLabel("Start");
		this.button1.setBounds(301,102,60,23);
		this.add(this.button1);
		//}}

		//{{REGISTER_LISTENERS
		final SymMouse aSymMouse = new SymMouse();
		final SymAction lSymAction = new SymAction();
		this.button1.addActionListener(lSymAction);
		//}}
	}
//		public void start() {
//		tqFrame = new TaquinFrame( this, "N-Puzzle Solving", 4);	}

	@Override
	public void stop() {
		if ( this.tqFrame !=null) {
			this.tqFrame.dispose();
			this.tqFrame = null;
		}
	}
}
