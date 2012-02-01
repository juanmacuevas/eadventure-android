/*******************************************************************************
 * <e-Adventure> Mobile for Android is a port of the <e-Adventure> research project to 	the Android platform.
 *     
 *      Copyright 2009-2012 <e-UCM> research group.
 *    
 *       <e-UCM> is a research group of the Department of Software Engineering
 *            and Artificial Intelligence at the Complutense University of Madrid
 *            (School of Computer Science).
 *    
 *            C Profesor Jose Garcia Santesmases sn,
 *            28040 Madrid (Madrid), Spain.
 *    
 *            For more info please visit:  <http://e-adventure.e-ucm.es/android> or
 *            <http://www.e-ucm.es>
 *    
 *    ****************************************************************************
 * 	This file is part of <e-Adventure> Mobile, version 1.0.
 * 
 * 	Main contributors - Roberto Tornero
 * 
 * 	Former contributors - Alvaro Villoria 
 * 						    Juan Manuel de las Cuevas
 * 						    Guillermo Mart�n 	
 * 
 *     	You can access a list of all the contributors to <e-Adventure> Mobile at:
 *            	http://e-adventure.e-ucm.es/contributors
 *    
 *    ****************************************************************************
 *         <e-Adventure> Mobile is free software: you can redistribute it and/or modify
 *        it under the terms of the GNU Lesser General Public License as published by
 *        the Free Software Foundation, either version 3 of the License, or
 *        (at your option) any later version.
 *    
 *        <e-Adventure> Mobile is distributed in the hope that it will be useful,
 *        but WITHOUT ANY WARRANTY; without even the implied warranty of
 *        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *        GNU Lesser General Public License for more details.
 *    
 *        See <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package es.eucm.eadandroid.homeapp.repository.database;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Implementation of the games repository database, as a list of information
 * 
 * @author Roberto Tornero
 */
public class RepositoryDatabase implements Iterable<GameInfo> {

	/**
	 * The list of games and their information
	 */
	private ArrayList<GameInfo> repoGames = new ArrayList<GameInfo>();

	/**
	 * Returns the list of games
	 */
	public ArrayList<GameInfo> getRepoData() {
		return repoGames;		
	}

	/**
	 * Adds a new game to the database
	 */
	public void addGameInfo(GameInfo ginfo){
		repoGames.add(ginfo);
	}

	/**
	 * Removes a game from the database
	 */
	public void removeGameInfo(GameInfo ginfo){
		repoGames.remove(ginfo);
	}

	/**
	 * Iterates through the database
	 */
	public Iterator<GameInfo> iterator() {
		return repoGames.iterator();
	}

	/**
	 * Clears the information on the database
	 */
	public void clear() {
		repoGames.clear();	
	}

}