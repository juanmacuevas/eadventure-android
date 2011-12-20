package es.eucm.eadandroid.ecore.control.functionaldata;

import java.util.ArrayList;
import android.graphics.Canvas;
import android.graphics.Paint;
import es.eucm.eadandroid.common.data.chapter.book.BookParagraph;

/**
 * This is a block of text that can be put in a book scene
 */
public class FunctionalBookTitle extends FunctionalBookParagraph {

    /**
     * The text book
     */
    private BookParagraph bookTitle;

    /**
     * The text of the book
     */
    private ArrayList<String> textLines;
    
    /**
     * Extra height necessary for drawing the book
     */
    private int extraHeight = 0;
    
    private Paint p;

    /**
     * Creates a new FunctionalBookText
     * 
     * @param text
     *            the text to be rendered
     */
    public FunctionalBookTitle( BookParagraph title ) {

        this.bookTitle = title;
        this.p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextSize(32);
        this.init( );
    }

    private void init( ) {

        textLines = new ArrayList<String>( );

        //Get the text of the book
        String text = bookTitle.getContent( );
        String word = "";
        String line = "";

        //while there is still text to be process
        while( !text.equals( "" ) ) {
            //get the first char
            char c = text.charAt( 0 );
            //and the rest of the text (without that char)
            text = text.substring( 1 );
            //If the first char is a new line
            if( c == '\n' ) {
                if( p.measureText(line + " " + word) < FunctionalTextBook.TEXT_WIDTH ) {
                    //finish the line with the current word
                    line = line + word;
                    //add the line to the text of the bullet book
                    textLines.add( line );
                    //empty line and word
                    word = "";
                    line = "";
                }
                else {
                    textLines.add( line );
                    textLines.add( word.substring( 1 ) );
                    line = "";
                    word = "";
                }
            }
            //if its a white space
            else if( Character.isWhitespace( c ) ) {
                //get the width of the line and the word
                //if its width size don't go out of the line text width
                if( p.measureText(line + " " + word) < FunctionalTextBook.TEXT_WIDTH ) {
                    //add the word to the line
                    line = line + word;
                    word = " ";
                }
                //if it goes out
                else {
                    //and the line to the text of the bullet book
                    textLines.add( line );
                    //the line is now the word
                    line = word.substring( 1 ) + " ";
                    word = "";
                }
            }
            //else we add it to the current word
            else {
                if( p.measureText(line + word + c) < FunctionalTextBook.TEXT_WIDTH )
                    word = word + c;
                else {
                    if( line != "" )
                        textLines.add( line );
                    line = "";
                    if( p.measureText( word + c ) < FunctionalTextBook.TEXT_WIDTH )
                        word = word + c;
                    else {
                        textLines.add( word );
                        word = "" + c;
                    }
                }
            }
        }
        //All the text has been process except the last line and last word
        if( p.measureText(line + " " + word) < FunctionalTextBook.TEXT_WIDTH ) {
            line = line + word;
        }
        else {
            textLines.add( line );
            line = word.substring( 1 );
        }
        textLines.add( line );
    }

    /*
     *  (non-Javadoc)
     * @see es.eucm.eadventure.engine.core.control.functionaldata.FunctionalBookParagraph#canBeSplitted()
     */
    @Override
    public boolean canBeSplitted( ) {

        return true;
    }

    /*
     *  (non-Javadoc)
     * @see es.eucm.eadventure.engine.core.control.functionaldata.FunctionalBookParagraph#draw(java.awt.Graphics2D, int, int)
     */
    @Override
    public void draw( Canvas c, int xIni, int yIni ) {

        //X and Y coordinates
        int x = xIni;
        int y = yIni;
        //for each line of the text book
        for( int i = 0; i < textLines.size( ); i++ ) {
            //draw the line string
            String line = textLines.get( i );
            
            // If the line doesn't fit, we change the line and add extra height
            // to paragraph height
            if ( FunctionalTextBook.PAGE_TEXT_HEIGHT - ( y % FunctionalTextBook.PAGE_TEXT_HEIGHT ) < FunctionalTextBook.TITLE_HEIGHT ){
                extraHeight += ( FunctionalTextBook.PAGE_TEXT_HEIGHT - ( y % FunctionalTextBook.PAGE_TEXT_HEIGHT ) );
                y += ( FunctionalTextBook.PAGE_TEXT_HEIGHT - ( y % FunctionalTextBook.PAGE_TEXT_HEIGHT ) );
            }

            // TODO ¿parche?
            
            c.drawText( line, x, y + FunctionalTextBook.TITLE_HEIGHT - 15, p);
 

            //add the line height to the Y coordinate for the next line
            y = y + FunctionalTextBook.TITLE_HEIGHT;

            /*if( i == 0 + FunctionalBook.TEXT_LINES ) {
                x = FunctionalBook.TEXT_X_2;
                y = FunctionalBook.TEXT_Y;
            }*/
        }
    }

    /*
     *  (non-Javadoc)
     * @see es.eucm.eadventure.engine.core.control.functionaldata.FunctionalBookParagraph#getHeight()
     */
    @Override
    public int getHeight( ) {

        return textLines.size( ) * FunctionalTextBook.TITLE_HEIGHT + extraHeight;
    }


}

