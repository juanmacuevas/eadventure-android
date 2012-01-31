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
/**
 * <e-Adventure> is an <e-UCM> research project. <e-UCM>, Department of Software
 * Engineering and Artificial Intelligence. Faculty of Informatics, Complutense
 * University of Madrid (Spain).
 * 
 * @author Del Blanco, A., Marchiori, E., Torrente, F.J. (alphabetical order) *
 * @author L�pez Ma�as, E., P�rez Padilla, F., Sollet, E., Torijano, B. (former
 *         developers by alphabetical order)
 * @author Moreno-Ger, P. & Fern�ndez-Manj�n, B. (directors)
 * @year 2009 Web-site: http://e-adventure.e-ucm.es
 */

/*
 * Copyright (C) 2004-2009 <e-UCM> research group
 * 
 * This file is part of <e-Adventure> project, an educational game & game-like
 * simulation authoring tool, available at http://e-adventure.e-ucm.es.
 * 
 * <e-Adventure> is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 * 
 * <e-Adventure> is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * <e-Adventure>; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 */
package es.eucm.eadandroid.multimedia;

/**
 * This abstract class defines any kind of sound event managed in eAdventure.
 * <p>
 * Any concrete class for a concrete sound format must implement the playOnce()
 * method. stopPlaying() method should also be overriden, calling this class'
 * stopPlaying() method to stop the play loop and actually stopping the sound
 * currently being played.
 * <p>
 * The sound is played in a new thread, so execution is not stopped while
 * playing the sound.
 */
public abstract class Sound extends Thread {

    /**
     * Stores if the sound file must play as a loop
     */
    private boolean loop;

    /**
     * Stores if the sound file is stopped
     */
    protected boolean stop;

    /**
     * Creates a new Sound.
     * 
     * @param loop
     *            defines whether or not the sound must be played in a loop
     */
    public Sound( boolean loop ) {

        super( );
        this.loop = loop;
        stop = false;
    }


    @Override
    public void run( ) {

        boolean playedAtLeastOnce = false;
        while( !stop && ( loop || !playedAtLeastOnce ) ) {
            playOnce( );
            playedAtLeastOnce = true;
        }
        //finalize( );
    }

    /**
     * Starts playing the sound.
     */
    public void startPlaying( ) {

        start( );
    }

    /**
     * Plays the sound just once. This method shouldn't be called manually.
     * Instead, create a new Sound with loop = false, and call startPlaying().
     */
    public abstract void playOnce( );

    /**
     * Stops playing the sound.
     */
    public abstract void stopPlaying( );

    /**
     * Finalice and released the resources used for the sound
     */
    @Override
    public abstract void finalize( );

    /**
     * Returns whether the sound is still playing
     * 
     * @return true if the sound is playing, false otherwise
     */
    public boolean isPlaying( ) {

        return isAlive( );
    }

}
