package de.pnientiedt.main;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLU;
import android.util.Log;

import de.pnientiedt.generic.Font;
import de.pnientiedt.generic.GameActivity;
import de.pnientiedt.generic.GameScreen;
import de.pnientiedt.generic.Mesh;
import de.pnientiedt.generic.Texture;
import de.pnientiedt.generic.Font.FontStyle;
import de.pnientiedt.generic.Font.Text;
import de.pnientiedt.generic.Mesh.PrimitiveType;
import de.pnientiedt.generic.Texture.TextureFilter;
import de.pnientiedt.generic.Texture.TextureWrap;

public class StartScreen implements GameScreen {
	private Mesh backgroundMesh;
	private Texture backgroundTextur;
	private Mesh titleMesh;
	private Texture titleTextur;
	private boolean isDone = false;
	private SoundManager soundManager;
	private Font font;
	private Text text;
	private String pressText = "Touch Screen to Start!";

	public StartScreen(GL10 gl, GameActivity activity) {
		backgroundMesh = new Mesh(gl, 4, false, true, false);
		backgroundMesh.texCoord(0, 0);
		backgroundMesh.vertex(-1, 1, 0);
		backgroundMesh.texCoord(1, 0);
		backgroundMesh.vertex(1, 1, 0);
		backgroundMesh.texCoord(1, 1);
		backgroundMesh.vertex(1, -1, 0);
		backgroundMesh.texCoord(0, 1);
		backgroundMesh.vertex(-1, -1, 0);

		titleMesh = new Mesh(gl, 4, false, true, false);
		titleMesh.texCoord(0, 0);
		titleMesh.vertex(-256, 256, 0);
		titleMesh.texCoord(1, 0);
		titleMesh.vertex(256, 256, 0);
		titleMesh.texCoord(1, 0.5f);
		titleMesh.vertex(256, 0, 0);
		titleMesh.texCoord(0, 0.5f);
		titleMesh.vertex(-256, 0, 0);

		try {
			Bitmap bitmap = BitmapFactory.decodeStream(activity.getAssets().open("planet.jpg"));
			backgroundTextur = new Texture(gl, bitmap, TextureFilter.MipMap, TextureFilter.Nearest, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
			bitmap.recycle();

			bitmap = BitmapFactory.decodeStream(activity.getAssets().open("title.png"));
			titleTextur = new Texture(gl, bitmap, TextureFilter.Nearest, TextureFilter.Nearest, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
			bitmap.recycle();
		} catch (Exception ex) {
			Log.d("Space Invaders", "couldn't load Texturs");
			throw new RuntimeException(ex);
		}

		soundManager = new SoundManager(activity);

		font = new Font(gl, activity.getAssets(), "font.ttf", activity.getViewportWidth() > 480 ? 32 : 16, FontStyle.Plain);
		text = font.newText(gl);
		text.setText(pressText);
	}
	
	@Override
	public boolean isDone() 
	{	
	   return isDone;
	}
	
	@Override
	public void update(GameActivity activity) 
	{	
	   if( activity.isTouched() )
	   isDone = true;
	}
	
	@Override
	public void render(GL10 gl, GameActivity activity) 
	{	
	   gl.glViewport( 0, 0, activity.getViewportWidth(), activity.getViewportHeight() );
	   gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
	   gl.glEnable( GL10.GL_TEXTURE_2D );
	   gl.glMatrixMode( GL10.GL_PROJECTION );
	   gl.glLoadIdentity();
	   gl.glMatrixMode( GL10.GL_MODELVIEW );
	   gl.glLoadIdentity();
			
	   gl.glEnable( GL10.GL_BLEND );
	   gl.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
		
	   backgroundTextur.bind();
	   backgroundMesh.render(PrimitiveType.TriangleFan );

	   gl.glMatrixMode( GL10.GL_PROJECTION );
	   GLU.gluOrtho2D( gl, 0, activity.getViewportWidth(), 0, activity.getViewportHeight() );
	   gl.glMatrixMode( GL10.GL_MODELVIEW );
	   gl.glLoadIdentity();
		
	   gl.glLoadIdentity();
	   gl.glTranslatef( activity.getViewportWidth() / 2, activity.getViewportHeight() - 256, 0 );
	   titleTextur.bind();
	   titleMesh.render(PrimitiveType.TriangleFan);

	   gl.glLoadIdentity();
	   gl.glTranslatef( activity.getViewportWidth() / 2 - font.getStringWidth( pressText ) / 2, 100, 0 );
	   text.render();
		
	   gl.glDisable( GL10.GL_TEXTURE_2D );
	   gl.glDisable( GL10.GL_BLEND );
	}
	
	public void dispose() 
	{	
	   backgroundTextur.dispose();
	   titleTextur.dispose();
	   soundManager.dispose();
	   font.dispose();
	   text.dispose();
	   backgroundMesh.dispose();
	   titleMesh.dispose();
	}
}
