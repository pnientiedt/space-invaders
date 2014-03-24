package de.pnientiedt.simulation;


public class Ship {
	public static final float SHIP_RADIUS = 1;
	public static final float SHIP_VELOCITY = 20;
	private final Vector position = new Vector();
	private int lives = 3;
	private boolean isExploding = false;
	private float explodeTime = 0;

	public void update(float delta) {
		if (isExploding) {
			explodeTime += delta;
			if (explodeTime > Explosion.EXPLOSION_LIVE_TIME) {
				isExploding = false;
				explodeTime = 0;
			}
		}
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public boolean isExploding() {
		return isExploding;
	}

	public void setExploding(boolean isExploding) {
		this.isExploding = isExploding;
	}

	public float getExplodeTime() {
		return explodeTime;
	}

	public void setExplodeTime(float explodeTime) {
		this.explodeTime = explodeTime;
	}

	public static float getShipRadius() {
		return SHIP_RADIUS;
	}

	public static float getShipVelocity() {
		return SHIP_VELOCITY;
	}

	public Vector getPosition() {
		return position;
	}
}