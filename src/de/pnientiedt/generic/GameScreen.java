package de.pnientiedt.generic;

import javax.microedition.khronos.opengles.GL10;

public interface GameScreen 
{
   public void update( GameActivity activity );
   public void render( GL10 gl, GameActivity activity );
   public boolean isDone( );
   public void dispose( );
}  
