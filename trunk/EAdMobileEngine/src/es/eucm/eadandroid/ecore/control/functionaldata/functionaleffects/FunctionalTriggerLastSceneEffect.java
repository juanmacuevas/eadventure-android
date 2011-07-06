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
package es.eucm.eadandroid.ecore.control.functionaldata.functionaleffects;

import es.eucm.eadandroid.common.data.chapter.effects.TriggerSceneEffect;
import es.eucm.eadandroid.ecore.control.Game;

/**
 * Special case of FunctionalTriggerSceneEffect. Triggers the last scene
 * rendered on the screen
 * 
 * @author Javier
 * 
 */
public class FunctionalTriggerLastSceneEffect extends FunctionalTriggerSceneEffect {

    private static String getLastSceneId( ) {

        if( Game.getInstance( ).getLastScene( ) != null ) {
            return Game.getInstance( ).getLastScene( ).getNextSceneId( );
        }
        return null;
    }

    private static int getLastSceneX( ) {

        if( Game.getInstance( ).getLastScene( ) != null ) {
            return Game.getInstance( ).getLastScene( ).getDestinyX( );
        }
        return Integer.MIN_VALUE;
    }

    private static int getLastSceneY( ) {

        if( Game.getInstance( ).getLastScene( ) != null ) {
            return Game.getInstance( ).getLastScene( ).getDestinyY( );
        }
        return Integer.MIN_VALUE;
    }

    public FunctionalTriggerLastSceneEffect( ) {

        super( new TriggerSceneEffect( null, Integer.MIN_VALUE, Integer.MIN_VALUE ) );
    }

    @Override
    public void triggerEffect( ) {

        effect = new TriggerSceneEffect( getLastSceneId( ), getLastSceneX( ), getLastSceneY( ) );
        super.triggerEffect( );
    }

}
