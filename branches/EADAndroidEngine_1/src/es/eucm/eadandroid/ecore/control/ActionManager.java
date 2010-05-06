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
package es.eucm.eadandroid.ecore.control;

import android.util.Log;
import android.view.MotionEvent;
import es.eucm.eadandroid.common.data.chapter.Action;
import es.eucm.eadandroid.common.data.chapter.Exit;
import es.eucm.eadandroid.common.data.chapter.scenes.GeneralScene;
import es.eucm.eadandroid.ecore.control.functionaldata.FunctionalConditions;
import es.eucm.eadandroid.ecore.control.functionaldata.FunctionalElement;
import es.eucm.eadandroid.ecore.control.functionaldata.FunctionalScene;
import es.eucm.eadandroid.ecore.control.gamestate.eventlisteners.events.PressedEvent;
import es.eucm.eadandroid.ecore.control.gamestate.eventlisteners.events.ScrollPressedEvent;
import es.eucm.eadandroid.ecore.control.gamestate.eventlisteners.events.TapEvent;
import es.eucm.eadandroid.ecore.control.gamestate.eventlisteners.events.UIEvent;
import es.eucm.eadandroid.ecore.control.gamestate.eventlisteners.events.UnPressedEvent;
import es.eucm.eadandroid.ecore.gui.GUI;
import es.eucm.eadandroid.ecore.gui.hud.elements.ActionButton;
import es.eucm.eadandroid.ecore.gui.hud.elements.Magnifier;

/**
 * Updated feb 2008: cursors
 * 
 * @author Javier Torrente
 * 
 */
public class ActionManager {

	/**
	 * Constant for looking action
	 */
	public static final int ACTION_LOOK = 0;

	/**
	 * Constant for grabbing action
	 */
	public static final int ACTION_GRAB = 1;

	/**
	 * Constant for talking action
	 */
	public static final int ACTION_TALK = 2;

	/**
	 * Constant for examining action
	 */
	public static final int ACTION_EXAMINE = 3;

	/**
	 * Constant for using action
	 */
	public static final int ACTION_USE = 4;

	/**
	 * Constant for giving action
	 */
	public static final int ACTION_GIVE = 5;

	/**
	 * Constant for going to action
	 */
	public static final int ACTION_GOTO = 6;

	/**
	 * Constant for using with action
	 */
	public static final int ACTION_USE_WITH = 7;

	/**
	 * Constant for giving to action
	 */
	public static final int ACTION_GIVE_TO = 8;

	/**
	 * Constant for custom action
	 */
	public static final int ACTION_CUSTOM = 9;

	/**
	 * Constant for custom interact action
	 */
	public static final int ACTION_CUSTOM_INTERACT = 10;

	/**
	 * Constant for drag to action
	 */
	public static final int ACTION_DRAG_TO = 11;

	/**
	 * Functional element in which the cursor is placed.
	 */
	private FunctionalElement elementOver;
	
    /**
     * Element selected and current cursor
     */
    private FunctionalElement elementInCursor;

	/**
	 * Current action selected.
	 */
	private int actionSelected;

	/**
	 * The original action.
	 */
	private String customActionName;

	/**
	 * Name of the current string being selected.
	 */
	private String exit;

	/*    *//**
	 * Cursor of the current exit being selected
	 */
	/*
	 * private Cursor exitCursor;
	 *//**
	 * List of the already created cursors. Useful to avoid creating the same
	 * cursors more than once
	 */
	/*
	 * private HashMap<Exit, Cursor> cursors;
	 */
	private FunctionalElement dragElement = null;

	/**
	 * Constructor.
	 */
	public ActionManager() {

		elementOver = null;
		actionSelected = ACTION_GOTO;
		exit = "";
		// exitCursor = null;
		// cursors = new HashMap<Exit, Cursor>( );
	}

	/**
	 * Returns the selected element.
	 * 
	 * @return Selected element
	 */
	public FunctionalElement getElementOver() {

		return elementOver;
	}

	/**
	 * Sets the new selected element.
	 * 
	 * @param elementOver
	 *            New selected element
	 */
	public void setElementOver(FunctionalElement elementOver) {

		this.elementOver = elementOver;
	}

	/**
	 * Returns the action selected.
	 * 
	 * @return Action selected
	 */
	public int getActionSelected() {

		return actionSelected;
	}

	/**
	 * Sets the new action selected.
	 * 
	 * @param actionSelected
	 *            New action selected
	 */
	public void setActionSelected(int actionSelected) {

		this.actionSelected = actionSelected;
		// GUI.getInstance( ).newActionSelected( );
	}

	/**
	 * Returns the current exit.
	 * 
	 * @return Current exit
	 */
	public String getExit() {

		return exit;
	}

	/*
    *//**
	 * Returns the current exit cursor.
	 * 
	 * @return Current cursor
	 */
	/*
	 * public Cursor getExitCursor( ) {
	 * 
	 * return exitCursor; }
	 */

	/**
	 * Sets the current exit.
	 * 
	 * @param exit
	 *            Current exit
	 */
	public void setExit(String exit) {

		if (exit == null)
			this.exit = "";
		else
			this.exit = exit;
	}

	/**
	 * Sets the current exit cursor.
	 * 
	 * @param exit
	 *            Current exit cursro
	 */
	/*
	 * public void setExitCursor( Cursor cursor ) {
	 * 
	 * this.exitCursor = cursor; }
	 * 
	 * public void setExitCustomized( String exit, Cursor cursor ) {
	 * 
	 * setExit( exit ); setExitCursor( cursor ); }
	 */

	public void setExitCustomized(String exit) {

		setExit(exit);

	}
	
	public void tap(UIEvent ev) {
		
		MotionEvent e = ((TapEvent) ev).event;

		int x = (int) ((e.getX() - GUI.CENTER_OFFSET)/ GUI.SCALE_RATIOX);
		int y = (int) (e.getY() / GUI.SCALE_RATIOY) - Magnifier.CENTER_OFFSET;
		
		if (elementInCursor!=null && elementOver!=null)
			   processElementClick();
			else {				
				elementInCursor=null;
				Game.getInstance().getFunctionalScene().tap(x,y);
			}
		
	}
	
public void pressed(UIEvent ev) {

		
		MotionEvent e = null;

		if (ev instanceof PressedEvent)
			e = ((PressedEvent) ev).event;
		else
			e = ((ScrollPressedEvent) ev).eventDst;
				
		int x = (int) ((e.getX() - GUI.CENTER_OFFSET)/ GUI.SCALE_RATIOX);
		int y = (int) (e.getY() / GUI.SCALE_RATIOY) - Magnifier.CENTER_OFFSET;
		
		Game game = Game.getInstance();
		FunctionalScene functionalScene = game.getFunctionalScene();
		if (functionalScene == null)
			return;

		FunctionalElement elementInside = functionalScene.getElementInside(x,y, dragElement);
		Exit exit = functionalScene.getExitInside(x,y);
		
		if (elementInside !=null) 
		Log.d("PRESSED",elementInside.getElement().getName());
		if (exit!=null)
		Log.d("PRESSED",exit.getNextSceneId());
		
		

		if (dragElement != null) {
			dragElement.setX(x);
			dragElement.setY(y);
		}

		if (elementInside != null) {
			setElementOver(elementInside);
		} else if (exit != null && actionSelected == ACTION_GOTO) {
			// SET EXIT CURSOR ;

			GeneralScene nextScene = null;

			// Pick the FIRST valid next-scene structure
			for (int i = 0; i < exit.getNextScenes().size()
					&& nextScene == null; i++)
				if (new FunctionalConditions(exit.getNextScenes().get(i)
						.getConditions()).allConditionsOk())
					nextScene = game.getCurrentChapterData().getGeneralScene(
							exit.getNextScenes().get(i).getTargetId());

			// Check the text (customized or not)
			if (getExitText(exit) != null && !getExitText(exit).equals("")) {
				setExit(getExitText(exit));
			} else if (getExitText(exit) != null) {
				setExit(" ");
			} else if (nextScene != null)
				setExit(nextScene.getName());
		}

	}


	public void unPressed(UIEvent ev) {

		MotionEvent e = ((UnPressedEvent) ev).event;

		int x = (int) ((e.getX() - GUI.CENTER_OFFSET)/ GUI.SCALE_RATIOX);
		int y = (int) (e.getY() / GUI.SCALE_RATIOY) - Magnifier.CENTER_OFFSET;
		
		
		if (elementInCursor!=null && elementOver!=null)
		   processElementClick();
		else {
			
			elementInCursor=null;
			Game.getInstance().getFunctionalScene().unpressed(x,y);
		
		}

	}
	
	public boolean processElementClick() {
		
		Game game = Game.getInstance();
		
		if( elementInCursor != null ) {
            if( game.getFunctionalPlayer( ).getCurrentAction( ).getType( ) == Action.CUSTOM_INTERACT ) {
                setActionSelected( ActionManager.ACTION_CUSTOM_INTERACT );
            }
            else if (game.getFunctionalPlayer( ).getCurrentAction( ).getType( ) == Action.DRAG_TO) {
                setActionSelected( ActionManager.ACTION_DRAG_TO );
            }
            else {
                if( this.elementOver.canPerform( ActionManager.ACTION_GIVE_TO ) ) {
                    setActionSelected( ActionManager.ACTION_GIVE );
                    game.getFunctionalPlayer( ).performActionInElement( elementInCursor );
                    setActionSelected( ActionManager.ACTION_GIVE_TO );
                }
                else {
                    setActionSelected( ActionManager.ACTION_USE );
                    game.getFunctionalPlayer( ).performActionInElement( elementInCursor );
                    setActionSelected( ActionManager.ACTION_USE_WITH );
                }
            }
            game.getFunctionalPlayer( ).performActionInElement( this.elementOver );
            elementInCursor = null;
      //      gui.setDefaultCursor( );
        }
        else {
            setActionSelected( ActionManager.ACTION_LOOK );
            game.getFunctionalPlayer( ).performActionInElement( this.elementOver );
        }
        return true;
	}

	/**
	 * Called when a mouse move event has been triggered
	 * 
	 * @param e
	 *            Mouse event
	 */

	

	public String getExitText(Exit exit) {

		if (exit.getDefaultExitLook() != null)
			return exit.getDefaultExitLook().getExitText();
		return null;
	}

	/**
	 * Returns the cursor of the first resources block which all conditions are
	 * met
	 * 
	 * @return the cursor
	 */
	public String getCursorPath(Exit exit) {

		if (exit.getDefaultExitLook() != null) {
			return exit.getDefaultExitLook().getCursorPath();
		}
		return null;
	}

	public void setCustomActionName(String name) {

		customActionName = name;
	}

	public String getCustomActionName() {

		return customActionName;
	}
	

	public void processAction(ActionButton ab, FunctionalElement elementAction) {
		
		int type = ab.getType();
		Game game = Game.getInstance();
		
		 switch( type ) {
         case ActionButton.HAND_BUTTON:
             elementInCursor = null;
         //    gui.setDefaultCursor( );
             if( elementAction.canBeUsedAlone( ) ) {
                 setActionSelected( ActionManager.ACTION_USE );
                 game.getFunctionalPlayer( ).performActionInElement( elementAction );
             }
             else {
                 if( !elementAction.isInInventory( ) ) {
                     setActionSelected( ActionManager.ACTION_GRAB );
                     game.getFunctionalPlayer( ).performActionInElement( elementAction );
                 }
                 else {
                       elementInCursor = elementAction;
             //        gui.setCursor( Toolkit.getDefaultToolkit( ).createCustomCursor( ( (FunctionalItem) elementInCursor ).getIconImage( ), new Point( 5, 5 ), "elementInCursor" ) );
                 }
             }
             break;
         case ActionButton.EYE_BUTTON:
             setActionSelected( ActionManager.ACTION_EXAMINE );
             game.getFunctionalPlayer( ).performActionInElement( elementAction );
             break;
         case ActionButton.MOUTH_BUTTON:
             setActionSelected( ActionManager.ACTION_TALK );
             game.getFunctionalPlayer( ).performActionInElement( elementAction );
             break;
         case ActionButton.DRAG_BUTTON:
//          //   elementInCursor = elementAction;
//             this.startDragging( elementInCursor );
//             this.draggingElement.setX( pressedX );
//             this.draggingElement.setY( pressedY + this.draggingElement.getHeight( ) / 2);
//             pressedX = (int) this.originalDragX;
//             pressedY = (int) this.originalDragY - this.draggingElement.getHeight( ) / 2;
             break;
         case ActionButton.CUSTOM_BUTTON:
             if( ab.getCustomAction( ).getType( ) == Action.CUSTOM ) {
                 this.setActionSelected( ActionManager.ACTION_CUSTOM );
                 this.setCustomActionName( ab.getName( ) );
                 game.getFunctionalPlayer( ).performActionInElement( elementAction );
                 break;
             }
             else {
                 elementInCursor = elementAction;
            //     gui.setCursor( Toolkit.getDefaultToolkit( ).createCustomCursor( ( (FunctionalItem) elementInCursor ).getIconImage( ), new Point( 5, 5 ), "elementInCursor" ) );
                 this.setActionSelected( ActionManager.ACTION_CUSTOM_INTERACT );
                 this.setCustomActionName( ab.getName( ) );
                 game.getFunctionalPlayer( ).performActionInElement( elementAction );
                 break;
             }
     }
     setActionSelected( ActionManager.ACTION_GOTO );

	}

	public FunctionalElement getElementInCursor() {
		return elementInCursor;
	}

}
