package de.pnientiedt.main;

import javax.microedition.khronos.opengles.GL10;


import de.pnientiedt.generic.GameActivity;
import de.pnientiedt.generic.GameScreen;
import de.pnientiedt.simulation.Simulation;
import de.pnientiedt.simulation.SimulationListener;

public class GameLoop implements GameScreen, SimulationListener {
	private Simulation simulation;
	private Renderer renderer;
	private SoundManager soundManager;

	public Simulation getSimulation() {
		return simulation;
	}

	public void setSimulation(Simulation simulation) {
		this.simulation = simulation;
	}

	public GameLoop(GL10 gl, GameActivity activity) {
		simulation = new Simulation();
		simulation.setListener(this);
		renderer = new Renderer(gl, activity);
		soundManager = new SoundManager(activity);
	}

	public GameLoop(GL10 gl, GameActivity activity, Simulation simulation) {
		this.simulation = simulation;
		this.simulation.setListener(this);
		renderer = new Renderer(gl, activity);
		soundManager = new SoundManager(activity);
	}

	@Override
	public void update(GameActivity activity) {
		processInput(activity);
		simulation.update(activity.getDeltaTime());
	}

	private void processInput(GameActivity activity) {
		if (activity.getAccelerationOnYAxis() < 0)
			simulation.moveShipLeft(activity.getDeltaTime(), Math.abs(activity.getAccelerationOnYAxis()) / 10);
		else
			simulation.moveShipRight(activity.getDeltaTime(), Math.abs(activity.getAccelerationOnYAxis()) / 10);

		if (activity.isTouched())
			simulation.shot();
	}

	public boolean isDone() {
		return simulation.getShip().getLives() == 0;
	}

	@Override
	public void render(GL10 gl, GameActivity activity) {
		renderer.render(gl, activity, simulation);
	}

	@Override
	public void dispose() {
		renderer.dispose();
		soundManager.dispose();
	}
	
	@Override
	public void explosion() 
	{
	   soundManager.playExplosionSound();
	}

	@Override
	public void shot() 
	{	
	   soundManager.playShotSound();
	}
}
