package de.pnientiedt.simulation;

import java.io.Serializable;
import java.util.ArrayList;

public class Simulation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4108999014160296709L;
	public final static float PLAYFIELD_MIN_X = -14;
	public final static float PLAYFIELD_MAX_X = 14;
	public final static float PLAYFIELD_MIN_Z = -15;
	public final static float PLAYFIELD_MAX_Z = 2;

	private ArrayList<Invader> invaders = new ArrayList<Invader>();
	private ArrayList<Block> blocks = new ArrayList<Block>();
	private ArrayList<Shot> shots = new ArrayList<Shot>();
	private ArrayList<Explosion> explosions = new ArrayList<Explosion>();

	private Ship ship;
	private Shot shipShot = null;

	private SimulationListener listener;

	private float multiplier = 1;
	private int score;
	private int wave = 1;

	private ArrayList<Shot> removedShots = new ArrayList<Shot>();
	private ArrayList<Explosion> removedExplosions = new ArrayList<Explosion>();

	public Simulation() {
		populate();
	}

	private void populate() {
		ship = new Ship();

		for (int row = 0; row < 4; row++) {
			for (int column = 0; column < 8; column++) {
				Invader invader = new Invader(new Vector(-PLAYFIELD_MAX_X / 2 + column * 2f, 0, PLAYFIELD_MIN_Z + row * 2f));
				invaders.add(invader);
			}
		}

		for (int shield = 0; shield < 3; shield++) {
			blocks.add(new Block(new Vector(-10 + shield * 10 - 1, 0, -2)));
			blocks.add(new Block(new Vector(-10 + shield * 10 - 1, 0, -3)));
			blocks.add(new Block(new Vector(-10 + shield * 10 + 0, 0, -3)));
			blocks.add(new Block(new Vector(-10 + shield * 10 + 1, 0, -3)));
			blocks.add(new Block(new Vector(-10 + shield * 10 + 1, 0, -2)));
		}
	}
	
	public void update( float delta )
	{			
	   ship.update( delta );
	   updateInvaders( delta );
	   updateShots( delta );
	   updateExplosions(delta);
	   checkShipCollision( );
	   checkInvaderCollision( );
	   checkBlockCollision( );
	   checkNextLevel( );		
	}
	
	private void updateInvaders( float delta )
	{
	   for( int i = 0; i < invaders.size(); i++ )
	   {
	      Invader invader = invaders.get(i);
	      invader.update( delta, multiplier );
	   }
	}
	
	private void updateShots( float delta )
	{
	   removedShots.clear();
	   for( int i = 0; i < shots.size(); i++ )
	   {
	      Shot shot = shots.get(i);
	      shot.update(delta);
	      if( shot.isHasLeftField() )
	         removedShots.add(shot);
	   }
		
	   for( int i = 0; i < removedShots.size(); i++ )		
	      shots.remove( removedShots.get(i) );
			
	   if( shipShot != null && shipShot.isHasLeftField() )    
	      shipShot = null;

	   if( Math.random() < 0.01 * multiplier && invaders.size() > 0 )
	   {			
	      int index = (int)(Math.random() * (invaders.size() - 1));
	      Shot shot = new Shot( invaders.get(index).getPosition(), true );			
	      shots.add( shot );
	      if( listener != null )
	         listener.shot();
	   }
	   
	}
	
	public void updateExplosions( float delta )
	{
	   removedExplosions.clear();
	   for( int i = 0; i < explosions.size(); i++ )
	   {
	      Explosion explosion = explosions.get(i);
	      explosion.update( delta );
	      if( explosion.getAliveTime() > Explosion.EXPLOSION_LIVE_TIME )
	         removedExplosions.add( explosion );
	   }

	   for( int i = 0; i < removedExplosions.size(); i++ )
	      explosions.remove( explosions.get(i) );
	}  
	
	private void checkInvaderCollision() 
	{		
	   if( shipShot == null )
	      return;							
				
	   for( int j = 0; j < invaders.size(); j++ )
	   {
	      Invader invader = invaders.get(j);
	      if( invader.getPosition().distance(shipShot.getPosition()) < Invader.INVADER_RADIUS )
	      {									
	         shots.remove( shipShot );
	         shipShot = null;
	         invaders.remove(invader);
	         explosions.add( new Explosion( invader.getPosition() ) );
	         if( listener != null )
	            listener.explosion();
	         score += Invader.INVADER_POINTS;
	         break;
	      }
	   }			
	}
	
	private void checkShipCollision() 
	{	
	   removedShots.clear();
		
	   if( !ship.isExploding() )
	   {
	      for( int i = 0; i < shots.size(); i++ )
	      {
	         Shot shot = shots.get(i);
	         if( !shot.isInvaderShot() )
	            continue;											
			
	         if( ship.getPosition().distance(shot.getPosition()) < Ship.SHIP_RADIUS )
	         {					
	            removedShots.add( shot );
	            shot.setHasLeftField(true);
	            ship.setLives(ship.getLives() - 1);
	            ship.setExploding(true);
	            explosions.add( new Explosion( ship.getPosition() ) );
	            if( listener != null )
	                listener.explosion();
	            break;
	         }			
	      }
		
	      for( int i = 0; i < removedShots.size(); i++ )		
	         shots.remove( removedShots.get(i) );
	   }
			 
	   for( int i = 0; i < invaders.size(); i++ )
	   {
	      Invader invader = invaders.get(i);
	      if( invader.getPosition().distance(ship.getPosition()) < Ship.SHIP_RADIUS )
	      {
	         ship.setLives(ship.getLives() - 1);
	         invaders.remove(invader);
	         ship.setExploding(true);
	         explosions.add( new Explosion( invader.getPosition() ) );
	         explosions.add( new Explosion( ship.getPosition() ) );
	         if( listener != null )
	            listener.explosion();
	         break;
	      }
	   }
	}  
	
	private void checkBlockCollision( )
	{
	   removedShots.clear();
		
	   for( int i = 0; i < shots.size(); i++ )
	   {
	      Shot shot = shots.get(i);			
								
	      for( int j = 0; j < blocks.size(); j++ )
	      {
	         Block block = blocks.get(j);
	         if( block.getPosition().distance(shot.getPosition()) < Block.BLOCK_RADIUS )
	         {					
	            removedShots.add( shot );
	            shot.setHasLeftField(true);
	            blocks.remove(block);
	            break;
	         }
	      }			
	   }
		
	   for( int i = 0; i < removedShots.size(); i++ )		
	      shots.remove( removedShots.get(i) );
	}
	
	private void checkNextLevel( )
	{
	   if( invaders.size() == 0 && ship.getLives() > 0 )
	   {
	      blocks.clear();
	      shots.clear();
	      shipShot = null;
	      Vector shipPosition = ship.getPosition();
	      int lives = ship.getLives();
	      populate();
	      ship.setLives(lives);
	      ship.getPosition().set(shipPosition);
	      multiplier += 0.1f;
	      wave++;
	   }
	}
	
	public void moveShipLeft(float delta, float scale) 
	{	
	   if( ship.isExploding() )
	      return;
		
	   ship.getPosition().x -= delta * Ship.SHIP_VELOCITY * scale;
	   if( ship.getPosition().x < PLAYFIELD_MIN_X )
	      ship.getPosition().x = PLAYFIELD_MIN_X;
	} 
	
	public void moveShipRight(float delta, float scale ) 
	{	
		if( ship.isExploding() )
			return;
		
		ship.getPosition().x += delta * Ship.SHIP_VELOCITY * scale;
		if( ship.getPosition().x > PLAYFIELD_MAX_X )
			ship.getPosition().x = PLAYFIELD_MAX_X;
	}
	
	public void shot() 
	{	
	   if( shipShot == null && !ship.isExploding() )
	   {
	      shipShot = new Shot( ship.getPosition(), false );			
	      shots.add( shipShot );
	      if( listener != null )
	         listener.shot();
	   }
	}

	public ArrayList<Invader> getInvaders() {
		return invaders;
	}

	public void setInvaders(ArrayList<Invader> invaders) {
		this.invaders = invaders;
	}

	public ArrayList<Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(ArrayList<Block> blocks) {
		this.blocks = blocks;
	}

	public ArrayList<Shot> getShots() {
		return shots;
	}

	public void setShots(ArrayList<Shot> shots) {
		this.shots = shots;
	}

	public ArrayList<Explosion> getExplosions() {
		return explosions;
	}

	public void setExplosions(ArrayList<Explosion> explosions) {
		this.explosions = explosions;
	}

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}

	public Shot getShipShot() {
		return shipShot;
	}

	public void setShipShot(Shot shipShot) {
		this.shipShot = shipShot;
	}

	public SimulationListener getListener() {
		return listener;
	}

	public void setListener(SimulationListener listener) {
		this.listener = listener;
	}

	public float getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(float multiplier) {
		this.multiplier = multiplier;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getWave() {
		return wave;
	}

	public void setWave(int wave) {
		this.wave = wave;
	}

	public ArrayList<Shot> getRemovedShots() {
		return removedShots;
	}

	public void setRemovedShots(ArrayList<Shot> removedShots) {
		this.removedShots = removedShots;
	}

	public ArrayList<Explosion> getRemovedExplosions() {
		return removedExplosions;
	}

	public void setRemovedExplosions(ArrayList<Explosion> removedExplosions) {
		this.removedExplosions = removedExplosions;
	}

	public static float getPlayfieldMinX() {
		return PLAYFIELD_MIN_X;
	}

	public static float getPlayfieldMaxX() {
		return PLAYFIELD_MAX_X;
	}

	public static float getPlayfieldMinZ() {
		return PLAYFIELD_MIN_Z;
	}

	public static float getPlayfieldMaxZ() {
		return PLAYFIELD_MAX_Z;
	}
}
