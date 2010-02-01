package es.eucm.eadventure.prototypes.control;

import es.eucm.eadventure.prototypes.control.functionaldata.FunctionalElement;

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


    /**
     * Constructor.
     */
    public ActionManager( ) {

        elementOver = null;
        actionSelected = ACTION_GOTO;
        exit = "";
    }

    /**
     * Returns the selected element.
     * 
     * @return Selected element
     */
    public FunctionalElement getElementOver( ) {

        return elementOver;
    }

    /**
     * Sets the new selected element.
     * 
     * @param elementOver
     *            New selected element
     */
    public void setElementOver( FunctionalElement elementOver ) {

        this.elementOver = elementOver;
    }

    /**
     * Returns the action selected.
     * 
     * @return Action selected
     */
    public int getActionSelected( ) {

        return actionSelected;
    }

    /**
     * Sets the new action selected.
     * 
     * @param actionSelected
     *            New action selected
     */
    public void setActionSelected( int actionSelected ) {

        this.actionSelected = actionSelected;
    //    GUI.getInstance( ).newActionSelected( );
    }

    /**
     * Returns the current exit.
     * 
     * @return Current exit
     */
    public String getExit( ) {

        return exit;
    }


    /**
     * Sets the current exit.
     * 
     * @param exit
     *            Current exit
     */
    public void setExit( String exit ) {

        if( exit == null )
            this.exit = "";
        else
            this.exit = exit;
    }



  /*  *//**
     * Called when a mouse click event has been triggered
     * 
     * @param e
     *            Mouse event
     *//*
    public void mouseClicked( MouseEvent e ) {

        Game game = Game.getInstance( );

        //if( //this.elementOver!=null && isBinaryAction ( actionSelected ) || 
        //		e.getButton( ) == MouseEvent.BUTTON1 ) {
        DebugLog.user( "Mouse clicked in scene: " + e.getX( ) + " , " + e.getY( ) );
        game.getFunctionalScene( ).mouseClicked( e.getX( ), e.getY( ) );
        //}
    }

    *//**
     * Called when a mouse move event has been triggered
     * 
     * @param e
     *            Mouse event
     *//*
    public void mouseMoved( MouseEvent e ) {

        Game game = Game.getInstance( );
        FunctionalScene functionalScene = game.getFunctionalScene( );
        if( functionalScene == null )
            return;
        
        FunctionalElement elementInside = functionalScene.getElementInside( e.getX( ), e.getY( ), dragElement );
        Exit exit = functionalScene.getExitInside( e.getX( ), e.getY( ) );

        if (dragElement != null) {
            dragElement.setX( e.getX( ) );
            dragElement.setY( e.getY( ) );
        }
        
        if( elementInside != null ) {
            setElementOver( elementInside );
        }
        else if( exit != null && actionSelected == ACTION_GOTO ) {
            boolean isCursorSet = getCursorPath( exit ) != null && !getCursorPath( exit ).equals( "" );

            if( isCursorSet && !cursors.containsKey( exit ) ) {
                Cursor newCursor;
                try {
                    newCursor = Toolkit.getDefaultToolkit( ).createCustomCursor( MultimediaManager.getInstance( ).loadImageFromZip( getCursorPath( exit ), MultimediaManager.IMAGE_MENU ), new Point( 0, 0 ), "exitCursor(" + exit + ")" );
                }
                catch( Exception exc ) {
                    newCursor = Toolkit.getDefaultToolkit( ).createCustomCursor( MultimediaManager.getInstance( ).loadImageFromZip( "gui/cursors/nocursor.png", MultimediaManager.IMAGE_MENU ), new Point( 0, 0 ), "exitCursor(" + exit + ")" );
                }
                this.cursors.put( exit, newCursor );
                setExitCursor( newCursor );
            }
            else if( isCursorSet && cursors.containsKey( exit ) )
                setExitCursor( cursors.get( exit ) );
            else
                setExitCursor( null );

            GeneralScene nextScene = null;

            // Pick the FIRST valid next-scene structure
            for( int i = 0; i < exit.getNextScenes( ).size( ) && nextScene == null; i++ )
                if( new FunctionalConditions( exit.getNextScenes( ).get( i ).getConditions( ) ).allConditionsOk( ) )
                    nextScene = game.getCurrentChapterData( ).getGeneralScene( exit.getNextScenes( ).get( i ).getTargetId( ) );

            //Check the text (customized or not)
            if( getExitText( exit ) != null && !getExitText( exit ).equals( "" ) ) {
                setExit( getExitText( exit ) );
            }
            else if( getExitText( exit ) != null ) {
                setExit( " " );
            }
            else if( nextScene != null )
                setExit( nextScene.getName( ) );
        }
    }

    public String getExitText( Exit exit ) {

        if( exit.getDefaultExitLook( ) != null )
            return exit.getDefaultExitLook( ).getExitText( );
        return null;
    }*/


    public void setCustomActionName( String name ) {

        customActionName = name;
    }

    public String getCustomActionName( ) {

        return customActionName;
    }

}
