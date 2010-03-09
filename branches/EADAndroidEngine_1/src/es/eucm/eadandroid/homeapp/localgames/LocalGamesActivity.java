package es.eucm.eadandroid.homeapp.localgames;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;
import android.widget.Toast;
import es.eucm.eadandroid.R;
import es.eucm.eadandroid.ecore.ECoreActivity;
import es.eucm.eadandroid.homeapp.repository.database.GameInfo;

/**
 * @author Alvaro
 * 
 */
public class LocalGamesActivity extends ListActivity {

	public static final String TAG = "LocalGamesActivity";

	private String[] advList = null;

	private ArrayList<GameInfo> m_games;
	private LocalGamesListAdapter m_adapter;

	LayoutAnimationController controller;

	private Menu mMenu;

	/**
	 * Local games activity handler messages . Handled by
	 * {@link LGActivityHandler} Defines the messages handled by this Activity
	 */
	public class LGAHandlerMessages {

		public static final int GAMES_FOUND = 0;
		public static final int NO_GAMES_FOUND = 1;
		public static final int NO_SD_CARD = 2;

	}

	/**
	 * Local games activity Handler
	 */
	private Handler LGActivityHandler = new Handler() {
		@Override
		/**    * Called when a message is sent to Engines Handler Queue **/
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case LGAHandlerMessages.GAMES_FOUND: {
				Bundle b = msg.getData();
				advList = b.getStringArray("adventuresList");
				insertAdventuresToList(advList);
				break;
			}
			case LGAHandlerMessages.NO_GAMES_FOUND:
				break;
			case LGAHandlerMessages.NO_SD_CARD:
				showAlert("No SD card mounted");
				break;

			}
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);

		setLayout();

		searchForGames();

	}
	
	private void setLayout() {
		setContentView(R.layout.local_games_activity);

		m_games = new ArrayList<GameInfo>();
		m_adapter = new LocalGamesListAdapter(this,
				R.layout.local_games_actvitiy_listitem, m_games);
		setListAdapter(m_adapter);

		AnimationSet set = new AnimationSet(true);

		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(500);
		set.addAnimation(animation);

		animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(100);
		set.addAnimation(animation);

		controller = new LayoutAnimationController(set, 0.5f);

		getListView().setLayoutAnimation(controller);
		getListView().setTextFilterEnabled(true);
		
	
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		GameInfo selectedAdventure = (GameInfo) this.getListView()
				.getItemAtPosition(position);

		Intent i = new Intent(this, ECoreActivity.class);
		i.putExtra("AdventureName", selectedAdventure.getGameTitle());

		this.startActivity(i);

	}

	private void insertAdventuresToList(String[] advList) {

		setLayout();
		
		for (int i = 0; i < advList.length; i++)
			m_games.add(new GameInfo(advList[i], "", "", null, null));

		m_adapter.notifyDataSetChanged();

	}

	/** Starts SearchGamesThread -> searches for ead games */
	private void searchForGames() {

		m_games.clear();
		SearchGamesThread t = new SearchGamesThread(this, LGActivityHandler);
		t.start();

	}

	private void showAlert(String msg) {

		// new AlertDialog.Builder(this).setMessage(msg).setNeutralButton("OK",
		// null).show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Hold on to this
		mMenu = menu;

		// Inflate the currently selected menu XML resource.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.title_icon, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		searchForGames();
		return true;

	}

}
