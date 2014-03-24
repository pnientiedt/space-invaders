package de.pnientiedt.simulation;


public class Shot {
	private static float SHOT_VELOCITY = 10;
	private final Vector position = new Vector();
	private boolean isInvaderShot;
	private boolean hasLeftField = false;

	public Shot(Vector position, boolean isInvaderShot) {
		this.position.set(position);
		this.isInvaderShot = isInvaderShot;
	}

	public void update(float delta) {
		if (isInvaderShot)
			position.z += SHOT_VELOCITY * delta;
		else
			position.z -= SHOT_VELOCITY * delta;

		if (position.z > Simulation.PLAYFIELD_MAX_Z)
			hasLeftField = true;
		if (position.z < Simulation.PLAYFIELD_MIN_Z)
			hasLeftField = true;
	}

	public static float getSHOT_VELOCITY() {
		return SHOT_VELOCITY;
	}

	public static void setSHOT_VELOCITY(float sHOT_VELOCITY) {
		SHOT_VELOCITY = sHOT_VELOCITY;
	}

	public boolean isInvaderShot() {
		return isInvaderShot;
	}

	public void setInvaderShot(boolean isInvaderShot) {
		this.isInvaderShot = isInvaderShot;
	}

	public boolean isHasLeftField() {
		return hasLeftField;
	}

	public void setHasLeftField(boolean hasLeftField) {
		this.hasLeftField = hasLeftField;
	}

	public Vector getPosition() {
		return position;
	}
}
