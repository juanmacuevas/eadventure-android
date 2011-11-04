package es.eucm.eadandroid.ecore.control.functionaldata;

import android.graphics.Canvas;

/**
 * This class defines any kind of object that can be put in a book scene
 */
public abstract class FunctionalBookParagraph {

    /**
     * Returns whether the object can be splitted in lines
     * 
     * @return true if can be splitted, false otherwise
     */
    public abstract boolean canBeSplitted( );

    /**
     * Returns the height of the object
     * 
     * @return the height of the object
     */
    public abstract int getHeight( );

    /**
     * Draws the object in the given graphics and the given position
     * 
     * @param g
     *            the graphics where the object must be painted
     * @param x
     *            horizontal position of the object
     * @param y
     *            vertical position of the object
     */
    public abstract void draw( Canvas c, int x, int y );

}