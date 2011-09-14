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

/**
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
 */
 
package es.eucm.eadandroid.common.data.animation;
//PORT
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import es.eucm.eadandroid.common.auxiliar.CreateImage;
import es.eucm.eadandroid.common.data.chapter.resources.Resources;
import es.eucm.eadandroid.common.gui.TC;
import es.eucm.eadandroid.debug.ReportDialog;
import es.eucm.eadandroid.ecore.gui.GUI;

/**
 * This class holds the information for an animation frame
 * 
 * @author Eugenio Marchiori
 * 
 */
public class Frame implements Cloneable, Timed {

    /**
     * The xml tag for the sound of the frame
     */
    public static final String RESOURCE_TYPE_SOUND = "sound";

    /**
     * The frame is a image
     */
    public static final int TYPE_IMAGE = 0;

    /**
     * The frame is a video
     */
    public static final int TYPE_VIDEO = 1;

    /**
     * The default time of a frame
     */
    public static final int DEFAULT_TIME = 100;

    /**
     * The url/resource path
     */
    private String uri;

    /**
     * Time to display the frame
     */
    private long time;

    /**
     * Type of the frame: {@link #TYPE_IMAGE} or {@link #TYPE_VIDEO}
     */
    private int type;

    /**
     * The image of the frame, buffered when possible
     */
    private Bitmap image;

    /**
     * Set of resources for the frame
     */
    private List<Resources> resources;

    private boolean waitforclick;

    private String soundUri;

    private int maxSoundTime;

    private String animationPath;
    
    private ImageLoaderFactory factory;

    /**
     * Creates a new empty frame
     */
    public Frame( ImageLoaderFactory factory ) {
        this.factory = factory;
        uri = "";
        type = TYPE_IMAGE;
        time = DEFAULT_TIME;
        image = null;
        waitforclick = false;
        resources = new ArrayList<Resources>( );
        soundUri = "";
        maxSoundTime = 1000;
    }

    /**
     * Creates a new frame with a image uri
     * 
     * @param uri
     *            the uri for the image
     */
    public Frame( ImageLoaderFactory factory, String uri ) {
        this.uri = uri;
        this.factory = factory;
        type = TYPE_IMAGE;
        time = DEFAULT_TIME;
        image = null;
        waitforclick = false;
        resources = new ArrayList<Resources>( );
        soundUri = "";
        maxSoundTime = 1000;
    }

    /**
     * Creates a new frame with a image uri and a duration time
     * 
     * @param uri
     *            The uri for the image
     * @param time
     *            The time (duration) of the frame
     */
    public Frame( ImageLoaderFactory factory, String uri, long time ) {
        this.uri = uri;
        this.factory = factory;
        type = TYPE_IMAGE;
        this.time = time;
        image = null;
        waitforclick = false;
        resources = new ArrayList<Resources>( );
        soundUri = "";
        maxSoundTime = 1000;
    }

    /**
     * Returns the uri/path of the frame resource
     * 
     * @return the uri/path of the frame resource
     */
    public String getUri( ) {

        return uri;
    }

    /**
     * Set the uri/path of the frame resource
     * 
     * @param uri
     *            the uri/path of the frame resource
     */
    public void setUri( String uri ) {

        this.uri = uri;
        image = null;
    }

    /**
     * Returns the time (duration) of the frame in milliseconds
     * 
     * @return the time (duration) of the frame in milliseconds
     */
    public long getTime( ) {

        return time;
    }

    /**
     * Set the time (duration) of the frame in milliseconds
     * 
     * @param time
     *            the time (duration) of the frame in milliseconds
     */
    public void setTime( long time ) {

        this.time = time;
    }

    /**
     * Returns the type of the frame
     * 
     * @return the type of the frame
     */
    public int getType( ) {

        return type;
    }

    /**
     * Sets the type of the frame
     * 
     * @param type
     *            the type of the frame
     */
    public void setType( int type ) {

        this.type = type;
    }

    public String getSoundUri( ) {

        return soundUri;
    }

    public void setSoundUri( String soundUri ) {

        this.soundUri = soundUri;
    }

    public int getMaxSoundTime( ) {

        return maxSoundTime;
    }

    public void setMaxSoundTime( int maxSoundTime ) {

        this.maxSoundTime = maxSoundTime;
    }

    /**
     * Adds some resources to the list of resources
     * 
     * @param resources
     *            the resources to add
     */
    public void addResources( Resources resources ) {

        this.resources.add( resources );
    }

    /**
     * Returns the list of resources of the frame
     * 
     * @return The list of resources of the frame
     */
    public List<Resources> getResources( ) {

        return resources;
    }

    /**
     * Returns the image for the frame. The image can be vertically inverted or
     * scaled to fullscreen
     * 
     * @param mirror
     *            If true, the image is vertically inverted
     * @param fullscreen
     *            If true, the image is scaled to fullscreen
     * @return The image for the frame, with the necessary modifications made
     */
    public Bitmap getImage( boolean mirror, boolean fullscreen, int where ) {

        if( image != null )
            return image;
        if( uri != null && uri.length( ) > 0 ) {
            if( where == Animation.ENGINE || where == Animation.EDITOR)
                image = factory.getImageFromPath(  uri );
            else if( where == Animation.PREVIEW )
                image = getImageFromAnimationPath( );
        }
        if( image != null && mirror )
            image = getScaledImage( image, -1, 1 );
        if( image != null && fullscreen )
            image = getFullscreenImage( image );
        if( image == null ) {
         
        	image = BitmapFactory.decodeFile("img/icons/noImageFrame.png");
        	
        	if (image==null)
        	image = CreateImage.createImage(200, 200, "No image Frame");
        	
        //PORTCOMMENT	Esta guarro que te cagainsx
       /* 	ImageIcon icon = new ImageIcon( "img/icons/noImageFrame.png" );
            if( icon != null && icon.getImage( ) != null )
                return icon.getImage( );
            else
                return new BufferedImage( 100, 120, BufferedImage.TYPE_3BYTE_BGR );*/
        }

        return image;
    }

    private Bitmap getImageFromAnimationPath( ) {

        Bitmap image = null;

        try {
            InputStream inputStream = null;

            String regexp = java.io.File.separator;
            if( regexp.equals( "\\" ) )
                regexp = "\\\\";
            String temp[] = this.animationPath.split( regexp );
            String filename = "";
            for( int i = 0; i < temp.length - 1; i++ ) {
                filename += temp[i] + java.io.File.separator;
            }

            temp = this.uri.split( "/" );
            filename += temp[temp.length - 1];
            if( new File( filename ).exists( ) )
                inputStream = new FileInputStream( filename );
//PORTCOMMENT
            if( inputStream != null ) {
            	
            	image = BitmapFactory.decodeStream(inputStream);
             //   image = ImageIO.read( inputStream ); // hago cambios en la linea de abajo...
                if( image == null || image.getHeight() == -1 || image.getWidth() == -1 ) {
                    factory.showErrorDialog( TC.get( "Error.Title") , TC.get( "Error.ImageTypeNotSupported" ) );
                }
                inputStream.close( );
            }
        }
        catch( IOException e ) {
            ReportDialog.GenerateErrorReport( e, true, "UNKNOWERROR" );
        }
        catch( Exception e ) {
            ReportDialog.GenerateErrorReport( e, true, "UNKNOWERROR" );
        }

        return image;
    }

    /**
     * Returns a new imaged scaled accordingly.
     * 
     * @param image
     *            The original image
     * @param x
     *            The scale along the x-axis
     * @param y
     *            The scale alogn the y-axis
     * @return A new, scaled image
     */
    private Bitmap getScaledImage( Bitmap image, float x, float y ) {

        Bitmap scaledImage = null;
         
  		  if( image != null ) {	  
  			 scaledImage = Bitmap.createScaledBitmap(image, (int) (image.getWidth() * x),(int) (image.getHeight() * y), false);
  		  }
  		          
		return scaledImage;
    }

    /**
     * Returns a scaled image that fits in the game screen.
     * 
     * @param image
     *            the image to be scaled.
     * @return a scaled image that fits in the game screen.
     */
    private Bitmap getFullscreenImage( Bitmap image ) {

    	Bitmap newImage = null;
    	
    	newImage = Bitmap.createScaledBitmap(image, GUI.WINDOW_WIDTH, GUI.WINDOW_HEIGHT, true);
    	
// PORTCOMMENT
  /*  	
        // set up the transform
        AffineTransform transform = new AffineTransform( );
        transform.scale( GUI.WINDOW_WIDTH / (double) image.getWidth( null ), GUI.WINDOW_HEIGHT / (double) image.getHeight( null ) );

        // create a transparent (not translucent) image
        Image newImage = GUI.getInstance( ).getGraphicsConfiguration( ).createCompatibleImage( GUI.WINDOW_WIDTH, GUI.WINDOW_HEIGHT, Transparency.BITMASK );

        // draw the transformed image
        Graphics2D g = (Graphics2D) newImage.getGraphics( );
        g.drawImage( image, transform, null );
        g.dispose( );
*/
        return newImage;
    }

    /**
     * Returns the value of waitforclick
     * 
     * @return the value of waitforclick
     */
    public boolean isWaitforclick( ) {

        return waitforclick;
    }

    /**
     * Set the value of waitforclick
     * 
     * @param waitforclick
     */
    public void setWaitforclick( boolean waitforclick ) {

        this.waitforclick = waitforclick;
    }

    @Override
    public Object clone( ) throws CloneNotSupportedException {

        Frame f = (Frame) super.clone( );
        f.image = image;
        if( resources != null ) {
            f.resources = new ArrayList<Resources>( );
            for( Resources r : resources )
                f.resources.add( (Resources) r.clone( ) );
        }
        f.time = time;
        f.type = type;
        f.uri = ( uri != null ? new String( uri ) : null );
        f.waitforclick = waitforclick;
        return f;
    }

    public void setAbsolutePath( String animationPath ) {

        this.animationPath = animationPath;
    }

    public String getImageAbsolutePath( ) {

        String regexp = java.io.File.separator;
        if( regexp.equals( "\\" ) )
            regexp = "\\\\";
        String temp[] = this.animationPath.split( regexp );
        String filename = "";
        for( int i = 0; i < temp.length - 1; i++ ) {
            filename += temp[i] + java.io.File.separator;
        }

        temp = this.uri.split( "/" );
        filename += temp[temp.length - 1];

        return filename;
    }

    public String getSoundAbsolutePath( ) {

        if( soundUri == null || soundUri.equals( "" ) )
            return null;

        String regexp = java.io.File.separator;
        if( regexp.equals( "\\" ) )
            regexp = "\\\\";
        String temp[] = this.animationPath.split( regexp );
        String filename = "";
        for( int i = 0; i < temp.length - 1; i++ ) {
            filename += temp[i] + java.io.File.separator;
        }

        temp = this.soundUri.split( "/" );
        filename += temp[temp.length - 1];

        return filename;
    }


}