/*package es.eucm.eadandroid.ecore.gui.hud;

import android.graphics.Canvas;

 TODO - Game instance not ported -> it is commented
 

public class HUD {

    *//**
     * Reference to the game main class
     *//*
	//protected Game game;

    *//**
     * Reference to the GUI of the game
     *//*
    protected GUI gui;

    *//**
     * If the HUD is shown
     *//*
    protected boolean showHud;

    *//**
     * Function that initializes the HUD class
     *//*
    public void init( ) {

     //   game = Game.getInstance( );
        gui = GUI.getInstance( );
        showHud = true;
    }

    *//**
     * Returns the width of the playable area of the screen
     * 
     * @return Width of the playable area
     *//*
    public abstract int getGameAreaWidth( );

    *//**
     * Returns the height of the playable area of the screen
     * 
     * @return Height of the playable area
     *//*
    public abstract int getGameAreaHeight( );

    *//**
     * Returns the X point of the response block text
     * 
     * @return X point of the response block text
     *//*
    public abstract int getResponseTextX( );

    *//**
     * Returns the Y point of the response block text
     * 
     * @return Y point of the response block text
     *//*
    public abstract int getResponseTextY( );

    *//**
     * Returns the number of lines of the response text block
     * 
     * @return Number of response lines
     *//*
    public abstract int getResponseTextNumberLines( );

    *//**
     * Tells the HUD that there is a change in the action selected
     *//*
    public abstract void newActionSelected( );
    
    
    *//**
     * Draw the HUD with the action button, action and element selected
     * 
     * @param g
     *            Graphics2D where will be drawn
     *//*
    public abstract void draw( Canvas c);

    *//**
     * Updates the HUD representation
     * 
     * @param elapsedTime
     *            Elapsed time since last update
     *//*
    public abstract void update( long elapsedTime );

    *//**
     * Toggle the HUD on or off
     * 
     * @param show
     *            If the Hud is shown or not
     *//*
    public void toggleHud( boolean show ) {

        showHud = show;
    }

    // OLD 
    *//**
     * There has been a mouse moved in the HUD in that coordinates
     * 
     * @param e
     *            Mouse event
     * @return boolean If the move is in the HUD
     *//*
    public abstract boolean mouseMoved( MouseEvent e );

    *//**
     * There has been a click in the HUD in that coordinates
     * 
     * @param e
     *            Mouse event
     * @return boolean If the click is in the HUD
     *//*
    public abstract boolean mouseClicked( MouseEvent e );

    *//**
     * Processes KeyEvents for the HUD. Its main purpose is to support the use
     * of Esc for canceling an action
     * 
     * @param e
     *            Key Event
     * @return True if any changes made. False otherwise
     *//*
    public abstract boolean keyTyped( KeyEvent e );

    public abstract boolean mouseReleased( MouseEvent e );

    public abstract boolean mousePressed( MouseEvent e );

    public abstract boolean mouseDragged( MouseEvent e );

    public void setLastMouseMove( MouseEvent e ) {

    }


}
*/