package es.eucm.eadandroid.ecore;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import es.eucm.eadandroid.R;
import es.eucm.eadandroid.common.auxiliar.ReleaseFolders;
import es.eucm.eadandroid.common.gui.TC;
import es.eucm.eadandroid.ecore.control.Game;
import es.eucm.eadandroid.ecore.control.Options;
import es.eucm.eadandroid.ecore.control.config.ConfigData;
import es.eucm.eadandroid.ecore.gui.GUI;
import es.eucm.eadandroid.homeapp.HomeTabActivity;
import es.eucm.eadandroid.homeapp.loadsavedgames.LoadSavedGames;
import es.eucm.eadandroid.homeapp.localgames.LocalGamesActivity.LGAHandlerMessages;
import es.eucm.eadandroid.multimedia.MultimediaManager;
import es.eucm.eadandroid.res.pathdirectory.Paths;
import es.eucm.eadandroid.utils.ActivityPipe; //TODO esto a lo mejor aqui no
import es.eucm.eadandroid.ecore.control.gamestate.GameStatePlaying;
import es.eucm.eadandroid.ecore.control.gamestate.GameStateVideoscene;


public class ECoreActivity extends Activity implements SurfaceHolder.Callback {

	public static String TAG = "ECoreActivity";

	private GameSurfaceView gameSurfaceView = null;
	
	private WindowManager window;

	private View assesmentLayout;
	private View mbutton;
	private WebView webview;
	

	private String adventureName;
	private boolean fromvideo=false;
	private boolean continueAudio=false;
	
	
	

	/**
	 * Local games activity handler messages . Handled by
	 * {@link LGActivityHandler} Defines the messages handled by this Activity
	 */
	public class ActivityHandlerMessages {

		public static final int ASSESSMENT = 0;
		public static final int VIDEO = 1;
		public static final int GAME_OVER = 2;
		public static final int LOAD_GAMES = 3;

	}

	/**
	 * activity Handler
	 */
	public Handler ActivityHandler = new Handler() {
		@Override
		/**    * Called when a message is sent to Engines Handler Queue **/
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case ActivityHandlerMessages.ASSESSMENT: {
				Bundle b = msg.getData();
				String text = b.getString("html");
				webview.loadData(text, "text/html", "utf-8");
				assesmentLayout.setVisibility(View.VISIBLE);
				mbutton.setVisibility(View.VISIBLE);

				break;
			}
			case ActivityHandlerMessages.VIDEO:
				activityvideo();
				break;

			case ActivityHandlerMessages.GAME_OVER:
				finishapplication();
				finish();
				
				break;
				
			case ActivityHandlerMessages.LOAD_GAMES:
				StartLoadApplication();
				finish();
				
				break;	
			}
 
		}

		

	};

	private void StartLoadApplication() {
		Intent i = new Intent(this, HomeTabActivity.class);
		//FIXME tendre que mirar xq si cambiamos el orden de tabs cogera otro
		i.putExtra("tabstate", HomeTabActivity.LOAD_GAMES);
		startActivity(i);
		
	}
	
	private void activityvideo() {
		this.continueAudio=true;
		Intent i = new Intent(this, EcoreVideo.class);
		startActivity(i);
	}
	
	private void finishapplication()
	{
		Intent i = new Intent(this, HomeTabActivity.class);
		i.putExtra("tabstate", HomeTabActivity.GAMES);
		startActivity(i);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// gets information from the intent to solve the nexts questions
		// are you starting the game?
		// are you loading a saved game?
		// are you returning from the videoscene?

		// turn off the window's title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		SensorManager sm = (SensorManager) this
				.getSystemService(Context.SENSOR_SERVICE);
		sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		// tell system to use the layout defined in our XML file
		setContentView(R.layout.game_activity_canvas);
		
		//to know if we are going to load a saved game
		boolean loadingfromsavedgame=this.getIntent().getExtras().getBoolean("savedgame");
		
		

		// we will create our main thread always except when the thread is already
		// created and comes from another videoactivity
		if (!this.getIntent().getExtras().getBoolean("before_video")) {
			gameSurfaceView = (GameSurfaceView) findViewById(R.id.canvas_surface);
			SurfaceHolder canvasHolder = gameSurfaceView.getHolder();
			// register our interest in hearing about changes to our surface
			// TODO tengo que descomentar esta linea
			canvasHolder.addCallback(this);
			

			if (loadingfromsavedgame)
			{
				String loadfile=this.getIntent().getExtras().getString("restoredGame");
				GameThread.create(canvasHolder, this,ActivityHandler,loadfile);
			}else GameThread.create(canvasHolder, this,ActivityHandler,null);
			
			
			// GameThread.getInstance();
			adventureName = (String) this.getIntent().getExtras().get(
					"AdventureName");
			String advPath = Paths.eaddirectory.GAMES_PATH + adventureName
					+ "/";
			GameThread.getInstance().setAdventurePath(advPath);
		}else {this.fromvideo=true;
				GameThread.getInstance().setHandler(ActivityHandler);
				}

		assesmentLayout = findViewById(R.id.hidecontainer);
		mbutton = findViewById(R.id.hideme1);
		webview = (WebView) findViewById(R.id.webview);
		webview.setVerticalScrollBarEnabled(true);
		webview.setVerticalScrollbarOverlay(true);
		assesmentLayout.setBackgroundColor(Color.BLACK);
		// webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		mbutton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Game.getInstance().getAssessmentEngine().statedone();
				assesmentLayout.setVisibility(View.INVISIBLE);
				mbutton.setVisibility(View.INVISIBLE);

			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		// gameSurfaceView will only be null when the application is restored
		if (gameSurfaceView == null) {
				gameSurfaceView = (GameSurfaceView) findViewById(R.id.canvas_surface);
				gameSurfaceView.setFocusable(true);
				SurfaceHolder canvasHolder = gameSurfaceView.getHolder();
				canvasHolder.addCallback(this);
				GameThread.getInstance().unpause(canvasHolder);
				
				Options options=Game.getInstance().getOptions();
				if(Game.getInstance().getFunctionalScene()!=null)
				if( options.isMusicActive( ) )
		            Game.getInstance().getFunctionalScene( ).playBackgroundMusic( );
		        else
		        	Game.getInstance().getFunctionalScene( ).stopBackgroundMusic( );
		}

	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//Log.d("acaba la aplicacion","XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX    ");
	}

	/**
	 * Invoked when the Activity loses user focus.
	 */
	@Override
	protected void onPause() {
		super.onPause();
		
		//to control if the game has finish or the user has done a quick exit
		//making the game capable of being restored
		if (GameThread.getInstance()!=null)
		{	
		GameThread.getInstance().pause(); // pause game when Activity pauses
		this.gameSurfaceView = null;
		}
		//we have to be aware of two circumstances
		//1) the game can be quiting bacase the user wants to go to menu so there is not game instance
		//2)we are going to pause the thread but can be recovered, 
		//in this case not all states have functional scenes so we have to control them
		if (Game.getInstance()!=null)
		if(Game.getInstance().getFunctionalScene( )!=null)
		// es una �apa pero es q no hay otra, esto es xq
		//hay video que tienen el sonido aparte entonces no podemos 
		//quitarle el sonido solo en este caso
		if (!this.continueAudio)	
		Game.getInstance().getFunctionalScene( ).stopBackgroundMusic( );
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		//Log.d("cambiandooooooooo", "XXXXXXXXXXXXXXXXXXXXXXXXXXXX");
	}

	public void surfaceCreated(SurfaceHolder holder) {
		
		
		//to control if the thread is already started
		if (!GameThread.getInstance().isAlive())
			GameThread.getInstance().start();
		
		//to change currentstate
		if(this.fromvideo)
		{
			fromvideo=false;
			Game.getInstance().setvideostatefinish();
		}
		
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// we have to tell thread to shut down & wait for it to finish, or else
		// it might touch the Surface after we return and explode
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#dispatchKeyEvent(android.view.KeyEvent)
	 */
	public boolean dispatchKeyEvent(KeyEvent event) {

		return GameThread.getInstance().processKeyEvent(event);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
	 */
	public boolean onTouchEvent(MotionEvent event) {

		boolean dispatched = GameThread.getInstance().processTouchEvent(event);

		// don't allow more than 60 motion events per second
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
		}
		return dispatched;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.app.Activity#dispatchTrackballEvent(android.view.MotionEvent)
	 */
	public boolean dispatchTrackballEvent(MotionEvent event) {
		return GameThread.getInstance().processTrackballEvent(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.app.Activity#dispatchTrackballEvent(android.view.MotionEvent)
	 */
	public boolean dispatchSensorEvent(SensorEvent event) {
		return GameThread.getInstance().processSensorEvent(event);
	}

	
	public boolean onPrepareOptionsMenu (Menu menu)
	{
		Options options=Game.getInstance().getOptions();
		menu.removeItem(0);
		menu.removeItem(1);
		menu.removeItem(2);
		menu.removeItem(3);
		menu.removeItem(4);

		if (Game.getInstance().getCurrentState() instanceof GameStatePlaying )
		{menu.add(0, 0, 0, "Save").setIcon(android.R.drawable.ic_menu_save);
		}
		
		if(Game.getInstance().getFunctionalScene( )!=null)
		if (options.isMusicActive())
		menu.add(0, 1, 0, "Music off").setIcon(android.R.drawable.ic_lock_silent_mode);
		else menu.add(0, 1, 0, "Music on").setIcon(android.R.drawable.ic_lock_silent_mode_off);
		
	/*	if(Game.getInstance().getFunctionalScene( )!=null)
		if (options.isEffectsActive())
		menu.add(0, 2, 0, "Effects off").setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		else menu.add(0, 2, 0, "Effects on").setIcon(android.R.drawable.ic_menu_view);
		*/
		
		menu.add(0, 3, 0, "Quit Game").setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		menu.add(0, 4, 0, "Load game").setIcon(android.R.drawable.ic_menu_directions);
		

		return true;	
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		
		Options options=Game.getInstance().getOptions();
		AlertDialog.Builder builder;
		AlertDialog alert;
		
		switch (item.getItemId()) {
		case 0:
			
			//TODO tendremos que decir que solo salva si estas en stateplaying
			GameThread.getInstance().pause();
			if(!new File(Paths.eaddirectory.SAVED_GAMES_PATH).exists())
				(new File(Paths.eaddirectory.SAVED_GAMES_PATH)).mkdir();
			
			if(!new File(Paths.eaddirectory.SAVED_GAMES_PATH+adventureName+"/").exists())
				(new File(Paths.eaddirectory.SAVED_GAMES_PATH+adventureName+"/")).mkdir();
			
			String time=this.time();
			Game.getInstance().save(Paths.eaddirectory.SAVED_GAMES_PATH+adventureName+"/"+time+".txt");
			GameThread.getInstance().unpause(this.gameSurfaceView.getHolder());
			
			
			return true;

		case 1:
			//TODO change music
	        if( options.isMusicActive( ) )
	            options.setMusicActive( false );
	        else
	            options.setMusicActive( true );
		
	        break;
			
	/*	case 2:
		//TODO change effects
			if( options.isEffectsActive( ) )
	            options.setEffectsActive( false );
	        else
	            options.setEffectsActive( true );
			break;
		*/	
		case 3:
			//TODO exit game
		/*	getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
		             WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
			builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure you want to exit?")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   
			           }
			       })
			       .setNegativeButton("No", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			alert = builder.create();
		*/	finishthread(false);
			
		  return true;
		case 4:
		/*	getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
		             WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
			//TODO go to load games
			builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure you want to exit and go to load games?")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   finishthread(true);
			           }
			       })
			       .setNegativeButton("No", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			alert = builder.create();
			
			*/finishthread(true);
			return true;		
				
		}
		
		
		Game.getInstance().saveOptions( );
		if( options.isMusicActive( ) )
            Game.getInstance().getFunctionalScene( ).playBackgroundMusic( );
        else
        	Game.getInstance().getFunctionalScene( ).stopBackgroundMusic( );

        if( !options.isEffectsActive( ) )
        	MultimediaManager.getInstance().stopAllSounds( );
           

		return true;
	}
	
	
	public String  time()
	{
		String finalresult;
		Date now = new Date();
		String day="none";
		 switch( now.getDay() ) {
		 case 0:
			 day="sun";
			 break;
		 case 1:
			 day="mon";
			 break;
		 case 2:
			 day="tues";
			 break;
		 case 3:
			 day="wed";
			 break;
		 case 4:
			 day="thurs";
			 break;
		 case 5:
			 day="fri";
			 break;
		 case 6:
			 day="sat";
			 break;
		 
		 }
		 int month=now.getMonth()+1;
         String time=new String("MONTH_"+month+"_DAY_"+day+"_HOUR_"+now.getHours()+"_MIN_"+now.getMinutes()+"_SEC_"+now.getSeconds());
		return time;
	}
	
	public void finishthread(boolean loadactivitygames)
	{
		
	
		 // boolean retry = true;
		  if (GameThread.getInstance()!=null) {
			  GameThread.getInstance().finish(loadactivitygames);
		/*  while (retry) 
		  { try { GameThread.getInstance().join(); retry = false; } catch
		  (InterruptedException e) { } } }
		  */
		  }
		  
		 	
	}
	
	static final int DIALOG_PAUSED_ID = 0;
	static final int DIALOG_GAMEOVER_ID = 1;
	
	protected Dialog onCreateDialog(int id) {
	    Dialog dialog = null;
	    switch(id) {
	    case DIALOG_PAUSED_ID:
	        // do the work to define the pause Dialog
	        break;
	    case DIALOG_GAMEOVER_ID:
	        // do the work to define the game over Dialog
	        break;
	    default:
	        dialog = null;
	    }
	    return dialog;
	}
	

}
