/*******************************************************************************
 * <e-Adventure> Mobile for Android(TM) is a port of the <e-Adventure> research project to 	the Android(TM) platform.
 *        
 *          Copyright 2009-2012 <e-UCM> research group.
 *        
 *          <e-UCM> is a research group of the Department of Software Engineering
 *           and Artificial Intelligence at the Complutense University of Madrid
 *           (School of Computer Science).
 *        
 *           C Profesor Jose Garcia Santesmases sn,
 *           28040 Madrid (Madrid), Spain.
 *       
 *           For more info please visit:  <http://e-adventure.e-ucm.es/android> or
 *           <http://www.e-ucm.es>
 *        
 *        	 *Android is a trademark of Google Inc.
 *       	
 *        ****************************************************************************
 *     	 This file is part of <e-Adventure> Mobile, version 1.0.
 *     
 *    	 Main contributors - Roberto Tornero
 *     
 *     	 Former contributors - Alvaro Villoria 
 *     						    Juan Manuel de las Cuevas
 *     						    Guillermo Martin 	
 *    
 *     	 Directors - Baltasar Fernandez Manjon
 *     				Eugenio Marchiori
 *     
 *         	 You can access a list of all the contributors to <e-Adventure> Mobile at:
 *                	http://e-adventure.e-ucm.es/contributors
 *        
 *        ****************************************************************************
 *             <e-Adventure> Mobile is free software: you can redistribute it and/or modify
 *            it under the terms of the GNU Lesser General Public License as published by
 *            the Free Software Foundation, either version 3 of the License, or
 *            (at your option) any later version.
 *        
 *            <e-Adventure> Mobile is distributed in the hope that it will be useful,
 *            but WITHOUT ANY WARRANTY; without even the implied warranty of
 *            MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *            GNU Lesser General Public License for more details.
 *        
 *            See <http://www.gnu.org/licenses/>
 ******************************************************************************/
package es.eucm.eadandroid.homeapp.apkinstalling;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import es.eucm.eadandroid.homeapp.repository.resourceHandler.RepoResourceHandler;
import es.eucm.eadandroid.res.pathdirectory.Paths;

/**
 * Installs the main resources of EAdventure Mobile
 * 
 * @author Roberto Tornero
 */
public class EngineResInstaller extends Thread {

	/**
	 * The context from which an instance of this class is called
	 */
	private Context con;
	/**
	 * The handler to control the installation progress
	 */
	private Handler han;
	/**
	 * A dialog to show the progress of the installation of the resources
	 */
	private ProgressDialog dialog;
	/**
	 * The handler to control the appearance and visibility of the dialog
	 */
	public Handler ActivityHandler = new Handler() {
		@Override
		/**    * Called when a message is sent to Engines Handler Queue **/
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case ActivityHandlerInstalling.FINISHISTALLING:
				dialog.setIndeterminate(false);
				dialog.dismiss();
				break;
			}

		}

	};

	/**
	 * Create new instance with parameters
	 */
	public EngineResInstaller(Context con, Handler handler) {
		super();

		this.con = con;
		this.han = handler;

	}	

	/**
	 * Use {@link init} to extract the resources and update the dialog 
	 */
	@Override
	public void run() {
		this.init();

		Message msg = han.obtainMessage();
		Bundle b = new Bundle();
		msg.what = ActivityHandlerInstalling.FINISHISTALLING;
		msg.setData(b);
		msg.sendToTarget();

	}

	/**
	 * Extract the resources to the eAdventure folder
	 */
	private void init() {
		if (!new File(Paths.eaddirectory.ROOT_PATH).exists()) {

			try {
				InputStream is = con.getAssets().open("EadAndroid.zip");
				BufferedOutputStream file;
				file = new BufferedOutputStream(new FileOutputStream(
				"/sdcard/EadAndroid.zip"));
				RepoResourceHandler.copyInputStream(is, file);

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			RepoResourceHandler.unzip(Paths.device.EXTERNAL_STORAGE,
					Paths.device.EXTERNAL_STORAGE, "EadAndroid.zip", true);
			
			try {
				for (String gameFile:con.getAssets().list("")){
					if (!gameFile.toLowerCase().endsWith(".ead"))
						continue;
					InputStream is = con.getAssets().open(gameFile);
					BufferedOutputStream file;
					file = new BufferedOutputStream(new FileOutputStream(
					"/sdcard/"+gameFile));
					RepoResourceHandler.copyInputStream(is, file);
					
					RepoResourceHandler.unzip(Paths.device.EXTERNAL_STORAGE,
							Paths.eaddirectory.GAMES_PATH, gameFile, true);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Handler messages
	 * 
	 * @author Roberto Tornero
	 */
	public class ActivityHandlerInstalling {

		public static final int FINISHISTALLING = 0;

	}

}
