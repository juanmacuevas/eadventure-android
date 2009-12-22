package es.eucm.eadandroid.gameLauncher;

import java.io.File;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SearchGamesThread extends Thread {

	private Context context;
	private Handler handler;

	/**
	 * @param ctx
	 *            -> contains the system context
	 * @param ha
	 *            -> Thread Handle Queue to send messages to.
	 */
	public SearchGamesThread(Context ctx, Handler ha) {
		context = ctx;
		handler = ha;
	}

	/**
	 * Starts the thread and searches for ead games in externalstorage SDCard ,
	 * when task is finished it sends a GAMES_FOUND message through "ha" handler
	 * .
	 */

	@Override
	public void run() {

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Message msg = new Message();
		File sdCard = Environment.getExternalStorageDirectory();

		Log.d("SearcgGamesThread", "SDCard state : "
				+ Environment.getExternalStorageState().toString());

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			String adventures[] = sdCard.list(new EADFileFilter());

			if (adventures != null && adventures.length > 0) {
				Log.d("SearchGamesThread", "EAD files in sdCard : "
						+ adventures[0]);

				Bundle b = new Bundle();
				b.putStringArray("adventuresList", adventures);
				msg.what = SearchHandlerMessages.GAMES_FOUND;
				msg.setData(b);

			}

			else {

				Log.d(this.getClass().getSimpleName(), "No adventures in SD");

				msg.what = SearchHandlerMessages.NO_GAMES_FOUND;

			}

		}

		else {

			msg.what = SearchHandlerMessages.NO_SD_CARD;
		}

		handler.sendMessage(msg);

	}
}