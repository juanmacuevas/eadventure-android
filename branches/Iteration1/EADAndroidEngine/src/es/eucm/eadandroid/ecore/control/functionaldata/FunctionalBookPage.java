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
package es.eucm.eadandroid.ecore.control.functionaldata;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import javax.swing.text.rtf.RTFEditorKit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.Point;
import es.eucm.eadandroid.common.auxiliar.ReportDialog;
import es.eucm.eadandroid.common.data.chapter.book.BookPage;
import es.eucm.eadandroid.ecore.control.Game;
import es.eucm.eadandroid.ecore.gui.GUI;
import es.eucm.eadandroid.multimedia.MultimediaManager;
import es.eucm.eadandroid.res.resourcehandler.ResourceHandler;

public class FunctionalBookPage extends JPanel {

    private static final long serialVersionUID = 1L;

    private BookPage bookPage;

    private boolean isValid;

    private Bitmap background, currentArrowLeft, currentArrowRight;
    
    private Point previousPage, nextPage;

    private Bitmap image;
    
    private FunctionalStyledBook fBook;

    private BookEditorPane editorPane;

    public FunctionalBookPage( Bitmap background ) {

        this.background = background;
    }

    public FunctionalBookPage( BookPage bookPage, FunctionalStyledBook fBook, Bitmap background, Point previousPage, Point nextPage,  boolean listenHyperLinks ) {

        super( );
        editorPane = new BookEditorPane( bookPage );
        isValid = true;
        this.bookPage = bookPage;
        this.fBook = fBook;
        this.background = background;
        this.previousPage = previousPage;
        this.nextPage = nextPage;
        
        FunctionalBookMouseListener bookListener = new FunctionalBookMouseListener( );
        this.addMouseListener( bookListener );
        this.addMouseMotionListener( bookListener );
        
        if( bookPage.getType( ) == BookPage.TYPE_URL ) {
            URL url = null;
            try {
                url = new URL( bookPage.getUri( ) );
                url.openStream( ).close( );
            }
            catch( Exception e ) {
                isValid = false;
                //System.out.println( "[LOG] FunctionalBookPage - Constructor - Error creating URL "+bookPage.getUri( ) );
            }

            try {
                if( isValid ) {
                    editorPane.setPage( url );
                    editorPane.setEditable( false );
                    if( listenHyperLinks )
                        editorPane.addHyperlinkListener( new BookHyperlinkListener( ) );
                    if( !( editorPane.getEditorKit( ) instanceof HTMLEditorKit ) && !( editorPane.getEditorKit( ) instanceof RTFEditorKit ) ) {
                        isValid = false;
                        //System.out.println( "[LOG] FunctionalBookPage - Constructor - Type of page not valid "+bookPage.getUri( ) );
                    }
                    else {
                        //System.out.println( "[LOG] FunctionalBookPage - Constructor - Page OK "+bookPage.getUri( ) );
                    }

                }
            }
            catch( IOException e ) {
            }

        }
        else if( bookPage.getType( ) == BookPage.TYPE_RESOURCE ) {
            String uri = bookPage.getUri( );
            String ext = uri.substring( uri.lastIndexOf( '.' ) + 1, uri.length( ) ).toLowerCase( );
            if( ext.equals( "html" ) || ext.equals( "htm" ) || ext.equals( "rtf" ) ) {

                //Read the text
                StringBuffer textBuffer = new StringBuffer( );
                InputStream is = ResourceHandler.getInstance( ).getResourceAsStreamFromZip( uri );//null;
                try {
                    int c;
                    while( ( c = is.read( ) ) != -1 ) {
                        textBuffer.append( (char) c );
                    }
                }
                catch( IOException e ) {
                    isValid = false;
                }
                finally {
                    if( is != null ) {
                        try {
                            is.close( );
                        }
                        catch( IOException e ) {
                            isValid = false;
                        }
                    }
                }

                //Set the proper content type
                if( ext.equals( "html" ) || ext.equals( "htm" ) ) {
                    editorPane.setContentType( "text/html" );
                    ProcessHTML processor = new ProcessHTML( textBuffer.toString( ) );
                    String htmlProcessed = processor.start( );
                    editorPane.setText( htmlProcessed );
                }
                else {
                    editorPane.setContentType( "text/rtf" );
                    editorPane.setText( textBuffer.toString( ) );
                }
                isValid = true;

            }

        }
        else if( bookPage.getType( ) == BookPage.TYPE_IMAGE ) {
            image = MultimediaManager.getInstance( ).loadImageFromZip( bookPage.getUri( ), MultimediaManager.IMAGE_SCENE );
        }

        if( editorPane != null ) {
            FunctionalBookMouseListener bookListener2 = new FunctionalBookMouseListener( );
            editorPane.addMouseListener( bookListener2 );
            editorPane.addMouseMotionListener( bookListener2 );
            editorPane.setOpaque( false );
            editorPane.setEditable( false );

            this.setOpaque( false );

            this.setLayout( null );

           editorPane.setBounds( bookPage.getMargin( ), bookPage.getMarginTop( ), GUI.WINDOW_WIDTH - bookPage.getMargin( ) - bookPage.getMarginEnd( ), GUI.WINDOW_HEIGHT - bookPage.getMarginTop( ) - bookPage.getMarginBottom( ) );
            if( bookPage.getScrollable( ) )
                this.add( new JScrollPane( editorPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED ) );
            else
                this.add( editorPane );
        }
    }

    private class FunctionalBookMouseListener extends MouseAdapter implements MouseMotionListener {

        @Override
        public void mouseClicked( MouseEvent evt ) {
            MouseEvent nEvt = createMouseEvent( evt );
            Game.getInstance( ).mouseClicked( nEvt );
        }
        
        
        @Override
        public void mouseMoved( MouseEvent evt ){
            MouseEvent nEvt = createMouseEvent( evt );
            Game.getInstance( ).mouseMoved( nEvt );
        }
        
        private MouseEvent createMouseEvent( MouseEvent evt ){
            int x = evt.getX( );
            int y = evt.getY( );
            if( evt.getSource( ) == editorPane ) {
                //Spread the call gauging the positions so the margin is taken into account
                x += bookPage.getMargin( );
                y += bookPage.getMarginTop( );
            }

            MouseEvent nEvt = new MouseEvent( (Component) evt.getSource( ), evt.getID( ), evt.getWhen( ), evt.getModifiers( ), x, y, evt.getClickCount( ), evt.isPopupTrigger( ), evt.getButton( ) );    
            return nEvt;
        }

    }

    /**
     * Listener for the Hyperlinks in the HTML books
     * 
     * @author Javier Torrente
     * 
     */
    private class BookHyperlinkListener implements HyperlinkListener {

        public void hyperlinkUpdate( HyperlinkEvent e ) {

            if( e.getEventType( ) == HyperlinkEvent.EventType.ACTIVATED ) {
                JEditorPane pane = (JEditorPane) e.getSource( );
                if( e instanceof HTMLFrameHyperlinkEvent ) {
                    HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent) e;
                    HTMLDocument doc = (HTMLDocument) pane.getDocument( );
                    doc.processHTMLFrameHyperlinkEvent( evt );
                }
                else {
                    try {
                        pane.setPage( e.getURL( ) );
                    }
                    catch( Exception t ) {
                        ReportDialog.GenerateErrorReport( t, Game.getInstance( ).isFromEditor( ), "UNKNOWERROR" );
                    }
                }
            }
        }
    }

    @Override
    public void paint( Graphics g ) {

        g.drawImage( background, 0, 0, background.getWidth( null ), background.getHeight( null ), null );
        if( image != null )
            g.drawImage( image, bookPage.getMargin( ), bookPage.getMarginTop( ), this.getWidth( ) - bookPage.getMarginEnd( ), this.getHeight( ) - bookPage.getMarginBottom( ), 0, 0, image.getWidth( null ), image.getHeight( null ), null );
        if ( currentArrowLeft != null && currentArrowRight != null ){
            if ( !fBook.isInFirstPage( ) )
                g.drawImage( currentArrowLeft, previousPage.x, previousPage.y, currentArrowLeft.getWidth( null ), currentArrowLeft.getHeight( null ), null );
            
            if ( !fBook.isInLastPage( ) )
                g.drawImage( currentArrowRight, nextPage.x, nextPage.y, currentArrowLeft.getWidth( null ), currentArrowLeft.getHeight( null ), null );
        }
        super.paint( g );
    }

    /**
     * @return the bookPage
     */
    public BookPage getBookPage( ) {

        return bookPage;
    }

    /**
     * @param bookPage
     *            the bookPage to set
     */
    public void setBookPage( BookPage bookPage ) {

        this.bookPage = bookPage;
    }

    /**
     * @return the isValid
     */
    @Override
    public boolean isValid( ) {

        return isValid;
    }

    /**
     * @param isValid
     *            the isValid to set
     */
    public void setValid( boolean isValid ) {

        this.isValid = isValid;
    }

    public class ProcessHTML {

        private String html;

        private int currentPos;

        private int state;

        private final int STATE_NONE = 0;

        private final int STATE_LT = 1;

        private final int STATE_SRC = 2;

        private final int STATE_EQ = 3;

        private final int STATE_RT = 4;

        private final int STATE_RTQ = 5;

        private String reference;

        public ProcessHTML( String html ) {

            this.html = html;
            currentPos = 0;
            state = STATE_NONE;
        }

        public String start( ) {

            state = STATE_NONE;
            String lastThree = "";
            reference = "";
            for( currentPos = 0; currentPos < html.length( ); currentPos++ ) {
                char current = html.charAt( currentPos );
                if( lastThree.length( ) < 3 )
                    lastThree += current;
                else
                    lastThree = lastThree.substring( 1, 3 ) + current;

                if( state == STATE_NONE ) {
                    if( current == '<' ) {
                        state = STATE_LT;
                    }
                }
                else if( state == STATE_LT ) {
                    if( lastThree.toLowerCase( ).equals( "src" ) ) {
                        state = STATE_SRC;
                    }
                    else if( current == '>' ) {
                        state = STATE_NONE;
                    }
                }

                else if( state == STATE_SRC ) {
                    if( current == '=' ) {
                        state = STATE_EQ;
                    }
                    else if( current != ' ' ) {
                        state = STATE_NONE;
                    }
                }
                else if( state == STATE_EQ ) {
                    if( current == '"' ) {
                        state = STATE_RTQ;
                    }
                    else if( current != ' ' ) {
                        reference += current;
                        state = STATE_RT;
                    }
                }
                else if( state == STATE_RTQ ) {
                    if( current != '>' && current != '"' ) {
                        reference += current;
                    }
                    else {
                        state = STATE_NONE;
                        replaceReference( currentPos - reference.length( ), reference.length( ) );
                    }
                }
                else if( state == STATE_RT ) {
                    if( current != '>' && current != ' ' ) {
                        reference += current;
                    }
                    else {
                        state = STATE_NONE;
                        replaceReference( currentPos - reference.length( ), reference.length( ) );
                    }
                }
            }

            return html;
        }

        private void replaceReference( int index, int length ) {

            try {
                int lastSlash = Math.max( bookPage.getUri( ).lastIndexOf( "/" ), bookPage.getUri( ).lastIndexOf( "\\" ) );
                String assetPath = bookPage.getUri( ).substring( 0, lastSlash ) + "/" + reference;
                String destinyPath = ResourceHandler.getInstance( ).getResourceAsURL( assetPath ).toURI( ).getPath( );
                if( destinyPath != null ) {
                    String leftSide = html.substring( 0, index );
                    String rightSide = html.substring( index + length, html.length( ) );
                    File file = new File( destinyPath );
                    html = leftSide + file.toURI( ).toURL( ).toString( ) + rightSide;
                }
                reference = "";
            }
            catch( Exception e ) {
                ReportDialog.GenerateErrorReport( e, Game.getInstance( ).isFromEditor( ), "UNKNOWERROR" );
            }
        }
    }

    public void setCurrentArrowLeft( Image currentArrowLeft ) {
        this.currentArrowLeft = currentArrowLeft;
        this.repaint( );
    }

    public void setCurrentArrowRight( Image currentArrowRight ) {
        this.currentArrowRight = currentArrowRight;
        this.repaint( );
    }

}