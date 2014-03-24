package de.pnientiedt.simulation;


public class Invader {
	public final static float INVADER_RADIUS = 0.75f;
	public final static float INVADER_VELOCITY = 1;
	public final static int INVADER_POINTS = 40;
	public final static int STATE_MOVE_LEFT = 0;
	public final static int STATE_MOVE_DOWN = 1;
	public final static int STATE_MOVE_RIGHT = 2;

	private final Vector position = new Vector();
	private int state = STATE_MOVE_LEFT;
	private boolean wasLastStateLeft = true;
	private float movedDistance = Simulation.PLAYFIELD_MAX_X / 2;

	public Invader(Vector position) {
		this.position.set(position);
	}

	public void update(float delta, float speedMultiplier) {
		movedDistance += delta * INVADER_VELOCITY * speedMultiplier;
		if (state == STATE_MOVE_LEFT) {
			position.x -= delta * INVADER_VELOCITY * speedMultiplier;
			if (movedDistance > Simulation.PLAYFIELD_MAX_X) {
				state = STATE_MOVE_DOWN;
				movedDistance = 0;
				wasLastStateLeft = true;
			}
		}
		if (state == STATE_MOVE_RIGHT) {
			position.x += delta * INVADER_VELOCITY * speedMultiplier;
			if (movedDistance > Simulation.getPlayfieldMaxX()) {
				state = STATE_MOVE_DOWN;
				movedDistance = 0;
				wasLastStateLeft = false;
			}
		}
		if (state == STATE_MOVE_DOWN) {
			position.z += delta * INVADER_VELOCITY * speedMultiplier;
			if (movedDistance > 1) {
				if (wasLastStateLeft)
					state = STATE_MOVE_RIGHT;
				else
					state = STATE_MOVE_LEFT;
				movedDistance = 0;
			}
		}
	}

	public static float getINVADER_RADIUS() {
		return INVADER_RADIUS;
	}

	public static float getINVADER_VELOCITY() {
		return INVADER_VELOCITY;
	}

	public static int getINVADER_POINTS() {
		return INVADER_POINTS;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public boolean isWasLastStateLeft() {
		return wasLastStateLeft;
	}

	public void setWasLastStateLeft(boolean wasLastStateLeft) {
		this.wasLastStateLeft = wasLastStateLeft;
	}

	public float getMovedDistance() {
		return movedDistance;
	}

	public void setMovedDistance(float movedDistance) {
		this.movedDistance = movedDistance;
	}

	public static int getStateMoveLeft() {
		return STATE_MOVE_LEFT;
	}

	public static int getStateMoveDown() {
		return STATE_MOVE_DOWN;
	}

	public static int getStateMoveRight() {
		return STATE_MOVE_RIGHT;
	}

	public Vector getPosition() {
		return position;
	}
}
