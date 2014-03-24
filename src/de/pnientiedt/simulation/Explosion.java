package de.pnientiedt.simulation;


public class Explosion {
	public static final float EXPLOSION_LIVE_TIME = 1;
	private float aliveTime = 0;
	private final Vector position = new Vector();

	public Explosion(Vector position) {
		this.position.set(position);
	}

	public void update(float delta) {
		aliveTime += delta;
	}

	public float getAliveTime() {
		return aliveTime;
	}

	public void setAliveTime(float aliveTime) {
		this.aliveTime = aliveTime;
	}

	public static float getExplosionLiveTime() {
		return EXPLOSION_LIVE_TIME;
	}

	public Vector getPosition() {
		return position;
	}
}
