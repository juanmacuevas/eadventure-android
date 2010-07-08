package es.eucm.eadandroid.ecore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.Bitmap.CompressFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import es.eucm.eadandroid.R;
import es.eucm.eadandroid.common.auxiliar.ReleaseFolders;
import es.eucm.eadandroid.common.data.adventure.DescriptorData;
import es.eucm.eadandroid.common.gui.TC;
import es.eucm.eadandroid.common.loader.Loader;
import es.eucm.eadandroid.ecore.control.Game;
import es.eucm.eadandroid.ecore.control.GpsManager;
import es.eucm.eadandroid.ecore.control.Options;
import es.eucm.eadandroid.ecore.control.QrcodeManager;
import es.eucm.eadandroid.ecore.control.config.ConfigData;
import es.eucm.eadandroid.ecore.control.gamestate.GameStateConversation;
import es.eucm.eadandroid.ecore.control.gamestate.GameStatePlaying;
import es.eucm.eadandroid.ecore.gui.GUI;
import es.eucm.eadandroid.homeapp.HomeTabActivity;
import es.eucm.eadandroid.multimedia.MultimediaManager;
import es.eucm.eadandroid.res.pathdirectory.Paths;
import es.eucm.eadandroid.res.resourcehandler.ResourceHandler;

public class ECoreActivity extends Activity implements SurfaceHolder.Callback {

	public static String TAG = "ECoreActivity";

	private GameSurfaceView gameSurfaceView = null;

	private View assesmentLayout;
	private View close_button;
	private View report_button;
	private WebView webview;

	private View conversationLayout;
	private ListView conversationList; 
	private ArrayAdapter<String> conversationAdapter;
	private String adventureName;
	private boolean fromvideo = false;

	ProgressDialog dialog;
	ProgressDialog dialog2;

	boolean onescaled = false;
	boolean gpsGame=false;
	boolean qrCodeGame=false;

	private static String languageFile = ReleaseFolders.LANGUAGE_UNKNOWN;

	/**
	 * Local games activity handler messages . Handled by
	 * {@link LGActivityHandler} Defines the messages handled by this Activity
	 */
	public class ActivityHandlerMessages {

		public static final int ASSESSMENT = 0;
		public static final int VIDEO = 1;
		public static final int GAME_OVER = 2;
		public static final int LOAD_GAMES = 3;
		public static final int FINISH_DIALOG = 4;
		public static final int SHOW_DIALOG = 7;
		public static final int REGISTRATE_GPS = 5;
		public static final int CONVERSATION = 6;
		public static final int SHOW_TOAST = 8;

	}

	/**
	 * activity Handler
	 */
	public Handler ActivityHandler = new Handler() {
		@Override
		/**    * Called when a message is sent to Engines Handler Queue **/
		public void handleMessage(Message msg) {
			
			Bundle bundle;

			switch (msg.what) {

			case ActivityHandlerMessages.ASSESSMENT: {
				 bundle = msg.getData();
				String text = bundle.getString("html");

				conversationLayout.setVisibility(View.INVISIBLE);
				conversationList.setVisibility(View.INVISIBLE);
				webview.loadData(text, "text/html", "utf-8");
				assesmentLayout.setVisibility(View.VISIBLE);
				close_button.setVisibility(View.VISIBLE);
				report_button.setVisibility(View.VISIBLE);

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
				startLoadApplication();
				finish();
				break;
			case ActivityHandlerMessages.FINISH_DIALOG:
				  bundle = msg.getData();
				
				int w= bundle.getInt("dialog");
				
				if (w==0)
				{
					if (dialog != null) {
						dialog.setIndeterminate(false);
						dialog.dismiss();
						dialog = null;
					}
				}
				else{
				if (dialog2 != null) {
					dialog2.setIndeterminate(false);
					dialog2.dismiss();
					dialog2 = null;
				}
				}
				break;
			case ActivityHandlerMessages.SHOW_DIALOG:
				 bundle = msg.getData();
				String text = bundle.getString("content");
				ECoreActivity.this.dialog = ProgressDialog.show(
						ECoreActivity.this, "", text, true);
				break;
			case ActivityHandlerMessages.SHOW_TOAST:
				
				Bundle e = msg.getData();
				text = e.getString("toast");
				showToast(text);
				
				break;
			
		/*	 case ActivityHandlerMessages.REGISTRATE_GPS:
				 activateGps();
			
			 break;
			*/ 

				
			case ActivityHandlerMessages.CONVERSATION:
				Bundle c = msg.getData();
							
				showConversationOptions(c);
				
				break;
				
			}

		}

		

	};

	private void startLoadApplication() {
		Intent i = new Intent(this, HomeTabActivity.class);
		i.putExtra("tabstate", HomeTabActivity.LOAD_GAMES);
		i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		overridePendingTransition(R.anim.fade, R.anim.hold);
	}

/*	
	 protected void activateGps() { 
		 LocationManager locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
	  
	  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
	  0, GpsManager.getInstance().getListener());
	  
	  GpsManager.getInstance().setLocationManager(locationManager);
	  
	  }
*/	 

	
	private void showConversationOptions(Bundle b) {


		if(conversationList.getVisibility()!=View.VISIBLE){
			
		
			conversationAdapter.clear();
			int size = b.getInt("size");
			for (int i=0;i<size;i++){
				conversationAdapter.add((String) b.getString(Integer.toString(i)));
			}
			
			conversationLayout.setVisibility(View.VISIBLE);
			conversationList.setVisibility(View.VISIBLE);
			
			

		}

	}

	private void activityvideo() {
		Intent i = new Intent(this, EcoreVideo.class);
		startActivity(i);
		overridePendingTransition(R.anim.fade, R.anim.hold);
	}

	private void finishapplication() {
		Intent i = new Intent(this, HomeTabActivity.class);
		i.putExtra("tabstate", HomeTabActivity.GAMES);
		i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		overridePendingTransition(R.anim.fade, R.anim.hold);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		// DEBUG
		Log.e("Inicio core1", String
				.valueOf(Debug.getNativeHeapAllocatedSize()));

		// gets information from the intent to solve next questions
		// are you starting the game?
		// are you loading a saved game?
		// are you returning from the videoscene?

		// turn off the window's title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
//				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		SensorManager sm = (SensorManager) this
				.getSystemService(Context.SENSOR_SERVICE);
		sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		// tell system to use the layout defined in our XML file
		setContentView(R.layout.game_activity_canvas);	

		// to know if we are going to load a saved game
		boolean loadingfromsavedgame = this.getIntent().getExtras().getBoolean(
				"savedgame");

		// we will create our main thread always except when the thread is
		// already
		// created and comes from another videoactivity
		if (GameThread.getInstance()==null) {

			adventureName = (String) this.getIntent().getExtras().get(
					"AdventureName");
			String advPath = Paths.eaddirectory.GAMES_PATH + adventureName
					+ "/";
			
			ResourceHandler.getInstance().setGamePath(advPath);
			DescriptorData gameDescriptor = Loader
					.loadDescriptorData(ResourceHandler.getInstance());
			
			this.gpsGame=gameDescriptor.isGpsMode();
			this.qrCodeGame=gameDescriptor.isQrCodeMode();
			
			if (qrCodeGame)
			QrcodeManager.create();	
			
			if (gpsGame) {
				GpsManager.create();
				LocationManager locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);

				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 0, 0, GpsManager
								.getInstance().getListener());

				GpsManager.getInstance().setLocationManager(locationManager);

				dialog = new ProgressDialog(this);
				dialog.setTitle("eAdventure Geolocated Game");
				dialog.setIcon(R.drawable.dialog_icon);
				dialog.setMessage("Waiting for GPS signal...");
				dialog.setIndeterminate(true);
				dialog.show();
				// dialog.setCancelable(false);

			} else {
				dialog2 = new ProgressDialog(this);
				dialog2.setTitle("eAdventure");
				dialog2.setIcon(R.drawable.dialog_icon);
				dialog2.setMessage("Loading game...");
				dialog2.setIndeterminate(true);
				dialog2.show();
				
			}
			
			
			if (qrCodeGame)
			{
				
				
				
				
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("You are about to start a QRcode game \n Press the magnifier button to capture QRCodes")
				       .setCancelable(false)
				       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				               
				           }
				       });
				
				builder.setTitle("eAdventure");
				builder.setIcon(R.drawable.dialog_icon);
				      
				       
				AlertDialog alert = builder.create();
				
				alert.show();
				
				
				
				
				
				/*
				
				
				
				AlertDialog dialog3 = new AlertDialog(this);
				dialog3.setTitle("eAdventure");
				dialog3.setIcon(R.drawable.dialog_icon);
				dialog3.setMessage("You are about to start a QRcode game ");
				//dialog3.setIndeterminate(true);
				dialog3.setButton("Continue", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                
			           }
			       });
				dialog3.show();
				*/
			}

			gameSurfaceView = (GameSurfaceView) findViewById(R.id.canvas_surface);
			SurfaceHolder canvasHolder = gameSurfaceView.getHolder();
			// register our interest in hearing about changes to our surface
			// TODO tengo que descomentar esta linea
			canvasHolder.addCallback(this);
			
			
			
			if (loadingfromsavedgame) {
				String loadfile = this.getIntent().getExtras().getString(
						"restoredGame");
				GameThread
						.create(canvasHolder, this, ActivityHandler, loadfile);
			} else
				GameThread.create(canvasHolder, this, ActivityHandler, null);

			GameThread.getInstance().setAdventurePath(advPath);
			GameThread.getInstance().setAdventureName(adventureName);
			
			

		} else {		
			this.fromvideo = this.getIntent().getExtras().getBoolean("before_video");
			GameThread.getInstance().setHandler(ActivityHandler);
		}

		assesmentLayout = findViewById(R.id.hidecontainer);
		close_button = findViewById(R.id.close_button);
		report_button = findViewById(R.id.report_button);
		webview = (WebView) findViewById(R.id.webview);
		webview.setVerticalScrollBarEnabled(true);
		webview.setVerticalScrollbarOverlay(true);
		assesmentLayout.setBackgroundColor(Color.BLACK);
		// webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		close_button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Game.getInstance().getAssessmentEngine().setStateDone();
				assesmentLayout.setVisibility(View.INVISIBLE);
				close_button.setVisibility(View.INVISIBLE);
				report_button.setVisibility(View.INVISIBLE);

			}
		});

		
		conversationLayout = findViewById(R.id.conversationLayout);
		
		conversationList = (ListView) findViewById(R.id.ListView01);		
		
		conversationAdapter = new ArrayAdapter<String>(this, R.layout.conversation_line, new ArrayList<String>());
		conversationList.setAdapter(conversationAdapter);
		conversationLayout.setVisibility(View.INVISIBLE);
		conversationList.setVisibility(View.INVISIBLE);
				
		conversationList.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View view,
		  	        int position, long id) {

				((GameStateConversation) Game.getInstance().getCurrentState()).selectDisplayedOption(position);
				conversationLayout.setVisibility(View.INVISIBLE);
				conversationList.setVisibility(View.INVISIBLE);
				
				//conversationAdapter.clear();
				
				

		  	    }

		

			
		});
		
		
		
		report_button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				try {

					Picture p = webview.capturePicture();
					Bitmap report = Bitmap.createBitmap(p.getWidth(), p
							.getHeight(), Bitmap.Config.RGB_565);
					Canvas c = new Canvas(report);
					p.draw(c);

					FileOutputStream temp = new FileOutputStream(
							Paths.eaddirectory.REPORTS_PATH + "/report.jpeg");
					report.compress(CompressFormat.JPEG, 100, temp);

					final Intent emailIntent = new Intent(
							android.content.Intent.ACTION_SEND);

					emailIntent.setType("text/html");
					// emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
					// new String[] { address.getText().toString() });
					emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
							"Report");
					emailIntent.setType("image/jpeg");
					emailIntent.putExtra(Intent.EXTRA_STREAM, Uri
							.parse("file://" + Paths.eaddirectory.REPORTS_PATH
									+ "/report.jpeg"));

					ECoreActivity.this.startActivityForResult(emailIntent, 0);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

			}
		});

		// Load the configuration
		ConfigData.loadFromXML(ReleaseFolders.configFileEngineRelativePath());

		/* We�e got to set the language from the device locale ;D */
		setLanguage(ReleaseFolders.getLanguageFromPath(ConfigData
				.getLanguangeFile()));

		// DEBUG
		Log.e("Inicio core2", String
				.valueOf(Debug.getNativeHeapAllocatedSize()));

	}

	/**
	 * Sets the current language of the editor. Accepted values are
	 * {@value #LANGUAGE_ENGLISH} & {@value #LANGUAGE_ENGLISH}. This method
	 * automatically updates the about, language strings, and loading image
	 * parameters.
	 * 
	 * The method will reload the main window if reloadData is true
	 * 
	 * @param language
	 */
	private static void setLanguage(String language) {

		if (true) {
			ConfigData.setLanguangeFile(ReleaseFolders
					.getLanguageFilePath(language), ReleaseFolders
					.getAboutFilePath(language));
			languageFile = language;
			TC.loadStrings(ReleaseFolders
					.getLanguageFilePath4Engine(languageFile));
		}
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

			Options options = Game.getInstance().getOptions();
			if (Game.getInstance().getFunctionalScene() != null)
				if (options.isMusicActive())
					Game.getInstance().getFunctionalScene()
							.playBackgroundMusic();
				else
					Game.getInstance().getFunctionalScene()
							.stopBackgroundMusic();
		}

	}



	/**
	 * Invoked when the Activity loses user focus.
	 */
	@Override
	protected void onPause() {
		super.onPause();


		// to control if the game has finish or the user has done a quick exit
		// making the game capable of being restored
		if (GameThread.getInstance() != null) {
			GameThread.getInstance().pause(); // pause game when Activity pauses
			this.gameSurfaceView = null;
		}
		// we have to be aware of two circumstances
		// 1) the game can be quiting bacase the user wants to go to menu so
		// there is not game instance
		// 2)we are going to pause the thread but can be recovered,
		// in this case not all states have functional scenes so we have to
		// control them
		if (Game.getInstance() != null)
			if (Game.getInstance().getFunctionalScene() != null)
				// es una �apa pero es q no hay otra, esto es xq
				// hay video que tienen el sonido aparte entonces no podemos
				// quitarle el sonido solo en este caso
					Game.getInstance().getFunctionalScene()
							.stopBackgroundMusic();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// Log.d("cambiandooooooooo", "XXXXXXXXXXXXXXXXXXXXXXXXXXXX");
	}

	public void surfaceCreated(SurfaceHolder holder) {

		// to control if the thread is already started
		if (!GameThread.getInstance().isAlive())
			GameThread.getInstance().start();

		// to change currentstate
		if (this.fromvideo) {
			fromvideo = false;
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
	public boolean processKeyEvent(KeyEvent event) {

		return GameThread.getInstance().processKeyEvent(event);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (assesmentLayout.getVisibility() != View.VISIBLE)
				showQuitDialog(false);
			return true;
		} else

		if (event.getKeyCode() == KeyEvent.KEYCODE_CAMERA
				|| event.getKeyCode() == KeyEvent.KEYCODE_SEARCH) {

			if (QrcodeManager.getInstance() != null) {

				if (QrcodeManager.getInstance().isGameStatePlaying()) {
					Intent intent = new Intent(
							"com.google.zxing.client.android.SCAN");
					intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
					startActivityForResult(intent, 0);
					return true;
				} else
					return true;

			} else
				return true;

		} else
			return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 0)
			if (resultCode == RESULT_OK) {
				String contents = data.getStringExtra("SCAN_RESULT");
				String format = data.getStringExtra("SCAN_RESULT_FORMAT");
				QrcodeManager.getInstance().updateQRcode(contents);
			}

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

	public boolean onPrepareOptionsMenu(Menu menu) {

		boolean prepare = false;

		if (assesmentLayout.getVisibility() != View.VISIBLE) {

			prepare = true;

			Options options = Game.getInstance().getOptions();
			menu.removeItem(0);
			menu.removeItem(1);
			menu.removeItem(2);
			menu.removeItem(3);
			menu.removeItem(4);
			menu.removeItem(5);

			if (Game.getInstance().getCurrentState() instanceof GameStatePlaying) {
				menu.add(0, 0, 0, "Save").setIcon(
						android.R.drawable.ic_menu_save);
			}

			if (Game.getInstance().getFunctionalScene() != null)
				if (options.isMusicActive())
					menu.add(0, 1, 0, "Music off").setIcon(
							android.R.drawable.ic_lock_silent_mode);
				else
					menu.add(0, 1, 0, "Music on").setIcon(
							android.R.drawable.ic_lock_silent_mode_off);

			menu.add(0, 3, 0, "Quit Game").setIcon(
					android.R.drawable.ic_menu_close_clear_cancel);
			menu.add(0, 4, 0, "Load game").setIcon(
					android.R.drawable.ic_menu_directions);
			menu.add(0, 5, 0, "Resize").setIcon(
					android.R.drawable.ic_menu_directions);

		}

		return prepare;
	}

	private boolean saveGame(String fileName) {

		Boolean saved = true;

		try {

			if (!new File(Paths.eaddirectory.SAVED_GAMES_PATH).exists())
				(new File(Paths.eaddirectory.SAVED_GAMES_PATH)).mkdir();

			String adventurename = GameThread.getInstance().getAdventurePath()
					.split("/")[GameThread.getInstance().getAdventurePath()
					.split("/").length - 1];
			File folder = new File(Paths.eaddirectory.SAVED_GAMES_PATH
					+ adventurename + "/");

			if (!folder.exists())
				folder.mkdir();

			Game.getInstance().save(
					Paths.eaddirectory.SAVED_GAMES_PATH + adventurename + "/"
							+ fileName + ".txt");

			FileOutputStream sshot;

			sshot = new FileOutputStream(Paths.eaddirectory.SAVED_GAMES_PATH
					+ adventurename + "/" + fileName + ".txt.png");

			int width = 200;
			int height = (GUI.FINAL_WINDOW_HEIGHT * 200)
					/ GUI.FINAL_WINDOW_WIDTH;

			Bitmap temp = Bitmap.createScaledBitmap(GUI.getInstance()
					.getBmpCpy(), width, height, false);

			temp.compress(CompressFormat.PNG, 50, sshot);

			temp = null;

		} catch (Exception e) {
			e.printStackTrace();
			saved = false;
		}

		return saved;

	}

	private void showToast(String msg) {

		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

	}

	private void showSaveDialog(boolean quit, boolean load) {

		final EditText input = new EditText(this);

		final boolean aux_quit = quit;
		final boolean aux_load = load;

		new AlertDialog.Builder(this).setTitle("Save game").setIcon(R.drawable.dialog_icon).setMessage(
				"Set slot name").setView(input).setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Editable value = input.getText();
						if (saveGame(value.toString())) {
							showToast("Game saved");
							if (aux_quit)
								finishthread(aux_load);
						} else
							showToast("Error saving game");
					}
				}).setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				}).show();

	}

	private void showQuitDialog(boolean load) {

		final boolean aux_load = load;

		new AlertDialog.Builder(this).setTitle("Quit game").setIcon(R.drawable.dialog_icon).setMessage(
				"Do you want to save the game?").setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						showSaveDialog(true, aux_load);
					}
				}).setNeutralButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						finishthread(aux_load);
					}
				}).setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				}).show();

	}

	public boolean onOptionsItemSelected(MenuItem item) {

		Options options = Game.getInstance().getOptions();

		switch (item.getItemId()) {
		case 0:

			GameThread.getInstance().pause();
			showSaveDialog(false, false);
			GameThread.getInstance().unpause(this.gameSurfaceView.getHolder());

			break;

		case 1:
			// TODO change music
			if (options.isMusicActive())
				options.setMusicActive(false);
			else
				options.setMusicActive(true);

			Game.getInstance().saveOptions();

			if (options.isMusicActive())
				Game.getInstance().getFunctionalScene().playBackgroundMusic();
			else
				Game.getInstance().getFunctionalScene().stopBackgroundMusic();

			if (!options.isEffectsActive())
				MultimediaManager.getInstance().stopAllSounds();

			break;
		case 3:
			showQuitDialog(false);
			break;
		case 4:

			showQuitDialog(true);
			break;

		case 5:

			GameThread.getInstance().resize(onescaled);
			if (onescaled)
				onescaled = false;
			else
				onescaled = true;

			break;

		}

		return true;
	}

	private String currentDate() {

		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ssdd-MM-yyyy");

		return formatter.format(new Date());

	}

	public void finishthread(boolean loadactivitygames) {

		// boolean retry = true;
		if (GameThread.getInstance() != null) {
			GameThread.getInstance().finish(loadactivitygames);
			/*
			 * while (retry) { try { GameThread.getInstance().join(); retry =
			 * false; } catch (InterruptedException e) { } } }
			 */
		}

	}

	@Override
	protected void onRestart() {
		
		super.onRestart();
	//	Log.d("onRestart","XXXXXXXXXXXXXXXXXXXXXX");
	}

	@Override
	protected void onStart() {
		
		super.onStart();
	//	Log.d("onStart","XXXXXXXXXXXXXXXXXXXXXX");
	}

	@Override
	protected void onStop() {
		
		super.onStop();
		//Log.d("onStop","XXXXXXXXXXXXXXXXXXXXXX");
	}

	@Override
	protected void onDestroy() {
		
		super.onDestroy();
	//	Log.d("onDestroy","XXXXXXXXXXXXXXXXXXXXXX");
	}
	
	}
