package de.pnientiedt.simulation;


public class Block {
	public final static float BLOCK_RADIUS = 0.5f;
	private Vector position = new Vector();

	public Block(Vector position) {
		this.position.set(position);
	}

	public Vector getPosition() {
		return position;
	}

	public void setPosition(Vector position) {
		this.position = position;
	}

	public static float getBlockRadius() {
		return BLOCK_RADIUS;
	}
}
