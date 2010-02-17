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
package es.eucm.eadandroid.ecore.control.gamestate;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


import es.eucm.eadandroid.common.data.adaptation.AdaptedState;
import es.eucm.eadandroid.common.data.chapter.Exit;
import es.eucm.eadandroid.ecore.control.Game;
import es.eucm.eadandroid.ecore.gui.GUI;

/**
 * A game main loop during the normal game
 */
public class GameStatePlaying extends GameState {

    /**
     * List of mouse events.
     */
    private Queue<MouseEvent> vMouse;

    /**
     * Constructor.
     */
    public GameStatePlaying( ) {
        super( );
        vMouse = new ConcurrentLinkedQueue<MouseEvent>( );
    }

    /*
     * (non-Javadoc)
     * @see es.eucm.eadventure.engine.engine.control.gamestate.GameState#EvilLoop(long, int)
     */
    @Override
    public synchronized void mainLoop( long elapsedTime, int fps ) {
 

        // Process the mouse events
        MouseEvent e;
        while( (e = vMouse.poll( )) != null ) {
            switch (e.getID( )) {
                case MouseEvent.MOUSE_CLICKED:
                    mouseClickedEvent( e );
                    break;
                case MouseEvent.MOUSE_MOVED:
                    mouseMovedEvent( e );
                    break;
                case MouseEvent.MOUSE_PRESSED:
                    mousePressedEvent( e );
                    break;
                case MouseEvent.MOUSE_RELEASED:
                    mouseReleasedEvent( e );
                    break;
                case MouseEvent.MOUSE_DRAGGED:
                    mouseMovedEvent( e );
            }
        }

        // Update the time elapsed in the functional scene and in the GUI
        game.getFunctionalScene( ).update( elapsedTime );
        GUI.getInstance( ).update( elapsedTime );

        // Get the graphics and paint the whole screen in black
        Canvas c = GUI.getInstance( ).getGraphics( );
        // TODO check, though it should give no problems with non-transparent backgrounds
        //g.clearRect( 0, 0, GUI.WINDOW_WIDTH, GUI.WINDOW_HEIGHT );

        // Draw the functional scene, and then the GUI
        game.getFunctionalScene( ).draw( );
        
        GUI.getInstance( ).drawScene( c, elapsedTime );
        

        GUI.getInstance( ).drawHUD( g );

        // Draw the FPS
        //g.setColor( Color.WHITE );
        //g.drawString( Integer.toString( fps ) + " fps", 700, 14 );

        // If there is an adapted state to be executed
        if( game.getAdaptedStateToExecute( ) != null ) {

            // If it has an initial scene, set it
            if( game.getAdaptedStateToExecute( ).getTargetId( ) != null ) {
                // check the scene is in chapter
                if( game.getCurrentChapterData( ).getScenes( ).contains( game.getAdaptedStateToExecute( ).getTargetId( ) ) ) {
                    game.setNextScene( new Exit( game.getAdaptedStateToExecute( ).getTargetId( ) ) );
                    game.setState( Game.STATE_NEXT_SCENE );
                    game.flushEffectsQueue( );
                }
            }

            // Set the flag values
            for( String flag : game.getAdaptedStateToExecute( ).getActivatedFlags( ) )
                if( game.getFlags( ).existFlag( flag ) )
                    game.getFlags( ).activateFlag( flag );
            for( String flag : game.getAdaptedStateToExecute( ).getDeactivatedFlags( ) )
                if( game.getFlags( ).existFlag( flag ) )
                    game.getFlags( ).deactivateFlag( flag );

            // Set the vars
            List<String> adaptedVars = new ArrayList<String>( );
            List<String> adaptedValues = new ArrayList<String>( );
            game.getAdaptedStateToExecute( ).getVarsValues( adaptedVars, adaptedValues );
            for( int i = 0; i < adaptedVars.size( ); i++ ) {
                String varName = adaptedVars.get( i );
                String varValue = adaptedValues.get( i );
                // check if it is a "set value" operation
                if( AdaptedState.isSetValueOp( varValue ) ) {
                    String val = AdaptedState.getSetValueData( varValue );
                    if( val != null )
                        game.getVars( ).setVarValue( varName, Integer.parseInt( val ) );
                }
                // it is "increment" or "decrement" operation, for both of them is necessary to 
                // get the current value of referenced variable
                else {
                    if( game.getVars( ).existVar( varName ) ) {
                        int currentValue = game.getVars( ).getValue( varName );
                        if( AdaptedState.isIncrementOp( varValue ) ) {
                            game.getVars( ).setVarValue( varName, currentValue + 1 );
                        }
                        else if( AdaptedState.isDecrementOp( varValue ) ) {
                            game.getVars( ).setVarValue( varName, currentValue - 1 );
                        }
                    }
                }
            }

        }


        
        // Update the data pending from the flags
        game.updateDataPendingFromState( true );

        // Ends the draw process
        GUI.getInstance( ).endDraw( );
        g.dispose( );
    }

    @Override
    public synchronized void mouseClicked( MouseEvent e ) {

        vMouse.add( e );
    }

    @Override
    public synchronized void mouseMoved( MouseEvent e ) {

        vMouse.add( e );
    }

    @Override
    public synchronized void mousePressed( MouseEvent e ) {

        vMouse.add( e );
    }

    @Override
    public synchronized void mouseReleased( MouseEvent e ) {
       
        vMouse.add( e );
    }

    @Override
    public synchronized void mouseDragged( MouseEvent e ) {

        vMouse.add( e );
    }

    /**
     * Triggers the given mouse event.
     * 
     * @param e
     *            Mouse event
     */
    public void mouseClickedEvent( MouseEvent e ) {
        if(!GUI.getInstance( ).mouseClickedInHud( e ))
            game.getActionManager( ).mouseClicked( e );
    }

    /**
     * Triggers the given mouse event.
     * 
     * @param e
     *            Mouse event
     */
    public void mouseMovedEvent( MouseEvent e ) {
        game.getActionManager( ).setExitCustomized( null, null );
        game.getActionManager( ).setElementOver( null );

        if(!GUI.getInstance( ).mouseMovedinHud( e ) ) {
            game.getActionManager( ).mouseMoved( e );
        }
    }

    private void mouseReleasedEvent( MouseEvent e ) {
        GUI.getInstance( ).mouseReleasedinHud( e );
    }

    private void mousePressedEvent( MouseEvent e ) {
        GUI.getInstance( ).mousePressedinHud( e );
    }

    @Override
    public void keyPressed( KeyEvent e ) {

        switch( e.getKeyCode( ) ) {
            case KeyEvent.VK_ESCAPE:
                if( !GUI.getInstance( ).keyInHud( e ) )
                    game.setState( Game.STATE_OPTIONS );
                break;
        }
    }
}