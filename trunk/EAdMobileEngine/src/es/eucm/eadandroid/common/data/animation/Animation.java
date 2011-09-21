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

//TODO
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import es.eucm.eadandroid.common.auxiliar.CreateImage;
import es.eucm.eadandroid.common.data.Documented;
import es.eucm.eadandroid.common.data.HasId;
import es.eucm.eadandroid.common.data.chapter.resources.Resources;

/**
 * This class holds an "animation" data. An animation must have at least a start
 * transition, a end transition and a frame.
 * 
 * 
 * @author Eugenio Marchiori
 */
public class Animation implements Cloneable, Documented, HasId {

    /**
     * The xml tag for the background music of the animation
     */
    public static final String RESOURCE_TYPE_MUSIC = "music";

    public static final int ENGINE = 0;

    public static final int EDITOR = 1;

    public static final int PREVIEW = 2;

    /**
     * Set of frames for the animation
     */
    private List<Frame> frames;

    /**
     * Set of transitions for the animation
     */
    private List<Transition> transitions;

    /**
     * Documentation of the animation
     */
    private String documentation;

    /**
     * Set of resources for the animation
     */
    private List<Resources> resources;

    /**
     * Id of the animation
     */
    private String id;

    /**
     * Boolean that indicates that the animation will be used as slides
     */
    private boolean slides;

    /**
     * Boolean that indicates that the animation will use transitions. If true,
     * transitions will be ignored
     */
    private boolean useTransitions;

    private int skippedFrames;

    private boolean mirror;

    private boolean fullscreen;

    private String newSound = null;

    private int soundMaxTime = 1000;

    private int lastSoundFrame = -1;

    private String animationPath;
    
    private ImageLoaderFactory factory;

    /**
     * Creates a new Animation. It can be created without any frames (empty =
     * true) or with the minimum number of frames and transitions (empty =
     * false)
     * 
     * @param id
     *            the id of the animation
     * @param empty
     *            boolean indicating where the animation should be empty or not
     */
    public Animation( String id, ImageLoaderFactory factory ) {
        this.factory = factory;

        this.id = id;
        resources = new ArrayList<Resources>( );
        frames = new ArrayList<Frame>( );
        transitions = new ArrayList<Transition>( );
        frames.add( new Frame( factory ) );
        transitions.add( new Transition( ) );
        transitions.add( new Transition( ) );
        skippedFrames = 0;
        useTransitions = true;
        slides = false;
    }

    /**
     * Creates a new Animation with a default Frame
     * 
     * @param id
     *            the id of the animation
     * @param frame
     *            the default frame of the animation
     */
    public Animation( String id, Frame frame, ImageLoaderFactory factory ) {

        this( id , factory);
        frames.clear( );
        frames.add( frame );
    }

    /**
     * Returns the frame at a given position (null if it doesn't exist)
     * 
     * @param i
     *            index of the frame
     * @return frame at given position (null if it doesn't exist)
     */
    public Frame getFrame( int i ) {

        if( frames.size( ) <= i || i < 0 )
            return null;
        return frames.get( i );
    }

    /**
     * Returns the transition for a given frame
     * 
     * @param i
     *            index of the frame
     * @return transition for tha frame (null if it doesn't exist)
     */
    public Transition getTranstionForFrame( int i ) {

        if( frames.size( ) <= i - 1 || i < 0 )
            return null;
        return transitions.get( i + 1 );
    }

    /**
     * Returns the start transition
     * 
     * @return the start transition
     */
    public Transition getStartTransition( ) {

        return transitions.get( 0 );
    }

    /**
     * Returns the end transition
     * 
     * @return the end transition
     */
    public Transition getEndTransition( ) {

        return transitions.get( transitions.size( ) - 1 );
    }

    /**
     * Adds a new frame after the one at the given index. If the index is
     * invalid it adds it at the end. If the frame is null it creates a new one.
     * 
     * @param after
     *            index of the previous frame
     * @param frame
     *            the frame to add (a new one is created if null)
     * @return the added frame
     */
    public Frame addFrame( int after, Frame frame ) {

        if( after >= frames.size( ) || after < 0 )
            after = frames.size( ) - 1;
        if( frame == null )
            frame = new Frame( factory );

        if( frames.size( ) == 1 && frames.get( 0 ).getUri( ).equals( "" ) ) {
            frames.remove( 0 );
            frames.add( frame );
        }
        else {
            frames.add( after + 1, frame );
            transitions.add( after + 2, new Transition( ) );
        }
        return frame;
    }

    /**
     * Removes the frame at a given index from the animation.
     * 
     * @param index
     *            the index of the frame to remove
     */
    public void removeFrame( int index ) {

        if( frames.size( ) <= index && index < 0 )
            return;
        frames.remove( index );
        transitions.remove( index + 1 );
    }

    /**
     * Returns the documentation of the animation
     * 
     * @return the documentation of the animation
     */
    public String getDocumentation( ) {

        return documentation;
    }

    /**
     * Sets the documentation of the animation
     * 
     * @param documentation
     *            The new documentation
     */
    public void setDocumentation( String documentation ) {

        this.documentation = documentation;
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
     * Returns the animation's id
     * 
     * @return the animation's id
     */
    public String getId( ) {

        return id;
    }

    /**
     * Returns the list of animation frames
     * 
     * @return the list of animation frames
     */
    public List<Frame> getFrames( ) {

        return frames;
    }

    /**
     * Returns the list of animation transitions
     * 
     * @return the list of animation transitions
     */
    public List<Transition> getTransitions( ) {

        return transitions;
    }

    public List<Resources> getResources( ) {

        return resources;
    }

    /**
     * @return the slides
     */
    public boolean isSlides( ) {

        return slides;
    }

    /**
     * @param slides
     *            the slides to set
     */
    public void setSlides( boolean slides ) {

        this.slides = slides;
    }

    /**
     * @return the useTransitions
     */
    public boolean isUseTransitions( ) {

        return useTransitions;
    }

    /**
     * @param useTransitions
     *            the useTransitions to set
     */
    public void setUseTransitions( boolean useTransitions ) {

        this.useTransitions = useTransitions;
    }

    /**
     * Returns the image in a given moment, or null if the animation has
     * finished.
     * 
     * @param elapsedTime
     *            Time elapsed since the animation began
     * @return The image to draw, in a loop
     */
    public Bitmap getImage( long elapsedTime, int where ) {

        int temp = skippedFrames;

        // check to see if the all the waiting frames have been
        // skipped
        int temp2 = 0;
        for( int i = 0; i < frames.size( ); i++ ) {
            if( frames.get( i ).isWaitforclick( ) )
                temp2++;
        }
        if( !slides || temp2 <= skippedFrames )
            elapsedTime = elapsedTime % getTotalTime( );

        for( int i = 0; i < frames.size( ); i++ ) {
            if( frames.get( i ).isWaitforclick( ) )
                temp--;
            if( frames.get( i ).getTime( ) > elapsedTime || ( frames.get( i ).isWaitforclick( ) && temp < 0 && slides ) ) {
                if( lastSoundFrame != i ) {
                    newSound = frames.get( i ).getSoundUri( );
                    soundMaxTime = frames.get( i ).getMaxSoundTime( );
                    lastSoundFrame = i;
                }
                return frames.get( i ).getImage( mirror, fullscreen, where );
            }
            if( i == frames.size( ) - 1 )
                return noImage( );
            elapsedTime -= frames.get( i ).getTime( );
            if( transitions.get( i + 1 ).getTime( ) > elapsedTime && useTransitions ) {
                return combinedFrames( i, elapsedTime, where );
            }
            if( useTransitions )
                elapsedTime -= transitions.get( i + 1 ).getTime( );
        }
        return noImage( );
    }

    public String getNewSound( ) {

        String temp = newSound;
        newSound = null;
        return temp;
    }

    public int getSoundMaxTime( ) {

        return soundMaxTime;
    }

    /**
     * Method to generate an image with no content.
     * 
     * @return A null o empty image.
     */
    private Bitmap noImage( ) {
/*PORTCOMMENT
        ImageIcon icon = new ImageIcon( "img/icons/noImageFrame.png" );
        if( icon != null && icon.getImage( ) != null )
            return icon.getImage( );
        else
            return new BufferedImage( 100, 120, BufferedImage.TYPE_3BYTE_BGR );*/
    	Bitmap image = BitmapFactory.decodeFile("img/icons/noImageFrame.png");
    	
    	if (image==null)
    	image = CreateImage.createImage(200, 200, "No image Frame");
    	
    	return image;
    }

    /**
     * Returns true if the animation has already played once.
     * 
     * @param elapsedTime
     *            The time passed for the animation
     * @return True if the animation has already played once.
     */
    public boolean finishedFirstTime( long elapsedTime ) {

        int temp = skippedFrames;
        for( int i = 0; i < frames.size( ); i++ ) {
            if( frames.get( i ).isWaitforclick( ) )
                temp--;
            if( frames.get( i ).getTime( ) > elapsedTime || ( frames.get( i ).isWaitforclick( ) && temp < 0 && slides ) ) {
                return false;
            }
            if( i == frames.size( ) - 1 )
                return true;
            elapsedTime -= frames.get( i ).getTime( );
            if( transitions.get( i + 1 ).getTime( ) > elapsedTime && useTransitions ) {
                return false;
            }
            if( useTransitions )
                elapsedTime -= transitions.get( i + 1 ).getTime( );
        }
        return true;

    }

    /**
     * Returns the total time of the animation (the "waitforclick" property of
     * the frames is ignored)
     * 
     * @return Total time of the animation
     */
    public long getTotalTime( ) {

        long temp = 0;
        for( int i = 0; i < frames.size( ); i++ ) {
            temp += frames.get( i ).getTime( );
            if( i < frames.size( ) - 1 && useTransitions )
                temp += transitions.get( i + 1 ).getTime( );
        }
        return temp;
    }

    /**
     * Returns the combinations of frames i and i+1 that represents the
     * transition after the elapsed time
     * 
     * @param i
     *            The index of the first frame
     * @param elapsedTime
     *            The time elapsed in the transition
     * @return An image with the combination of the two frames
     */
    private Bitmap combinedFrames( int i, long elapsedTime, int where ) {

        Bitmap start = frames.get( i ).getImage( mirror, fullscreen, where );
        Bitmap end = frames.get( i + 1 ).getImage( mirror, fullscreen, where );
        
        Log.e("Animation", "Uso de animaciones combinedFrames no implementadas");
        
      
//        long time = transitions.get( i + 1 ).getTime( );
//        Bitmap temp;
//        Canvas c;
//
//        switch( transitions.get( i + 1 ).getType( ) ) {
//            case Transition.TYPE_NONE:
//                return start;
//            case Transition.TYPE_FADEIN:
//            	
//                temp = Bitmap.createBitmap(end.getWidth(), end.getHeight(),Bitmap.Config.ARGB_4444)
//
//                c = new Canvas(temp);
//                AlphaComposite alphaComposite = AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 1 - (float) elapsedTime / (float) time );
//                g.setComposite( alphaComposite );
//                g.drawImage( start, 0, 0, null );
//
//                alphaComposite = AlphaComposite.getInstance( AlphaComposite.SRC_OVER, (float) elapsedTime / (float) time );
//                g.setComposite( alphaComposite );
//                g.drawImage( end, 0, 0, null );
//
//                return temp;
//            case Transition.TYPE_VERTICAL:
//                temp = new BufferedImage( end.getWidth( null ), end.getHeight( null ), BufferedImage.TYPE_4BYTE_ABGR );
//
//                g = (Graphics2D) temp.getGraphics( );
//                g.drawImage( start, (int) ( end.getWidth( null ) * (float) elapsedTime / time ), 0, null );
//
//                g.drawImage( end, (int) ( end.getWidth( null ) * (float) elapsedTime / time ) - end.getWidth( null ), 0, null );
//
//                return temp;
//            case Transition.TYPE_HORIZONTAL:
//                temp = new BufferedImage( end.getWidth( null ), end.getHeight( null ), BufferedImage.TYPE_4BYTE_ABGR );
//
//                g = (Graphics2D) temp.getGraphics( );
//                g.drawImage( start, 0, (int) ( end.getHeight( null ) * (float) elapsedTime / time ), null );
//
//                g.drawImage( end, 0, (int) ( end.getHeight( null ) * (float) elapsedTime / time ) - end.getHeight( null ), null );
//
//                return temp;
//            default:
//                return start;
//        }
        
        return start ;
    }

    public void setMirror( boolean mirror ) {

        this.mirror = mirror;
    }

    /**
     * Returns the time that must pass for the animation to get into the next
     * frame of transition.
     * 
     * @param accumulatedTime
     *            Time that the animation has been playing
     * 
     * @return Time till the next frame or transition
     */
    public long skipFrame( long elapsedTime ) {

        //elapsedTime = elapsedTime % getTotalTime();
        if( !slides )
            return elapsedTime;

        long tempTime = 0;
        int temp = ++skippedFrames;
        for( int i = 0; i < frames.size( ); i++ ) {
            if( frames.get( i ).isWaitforclick( ) )
                temp--;
            if( frames.get( i ).getTime( ) > elapsedTime || ( frames.get( i ).isWaitforclick( ) && temp < 1 ) ) {
                return tempTime + frames.get( i ).getTime( );
            }
            tempTime += frames.get( i ).getTime( );
            if( i == frames.size( ) - 1 )
                return 0;
            elapsedTime -= frames.get( i ).getTime( );
            if( transitions.get( i + 1 ).getTime( ) > elapsedTime ) {
                skippedFrames--;
                return tempTime += transitions.get( i + 1 ).getTime( );
            }
            tempTime += transitions.get( i + 1 ).getTime( );
            elapsedTime -= transitions.get( i + 1 ).getTime( );
        }
        skippedFrames = 0;
        return 0;
    }

    public void setFullscreen( boolean b ) {

        this.fullscreen = b;
    }

    public void restart( ) {

        skippedFrames = 0;
    }

    @Override
    public Object clone( ) throws CloneNotSupportedException {

        Animation a = (Animation) super.clone( );
        a.documentation = ( documentation != null ? new String( documentation ) : null );
        if( frames != null ) {
            a.frames = new ArrayList<Frame>( );
            for( Frame f : frames )
                a.frames.add( (Frame) f.clone( ) );
        }
        a.fullscreen = fullscreen;
        a.id = ( id != null ? new String( id ) : null );
        a.mirror = mirror;
        if( resources != null ) {
            a.resources = new ArrayList<Resources>( );
            for( Resources r : resources )
                a.resources.add( (Resources) r.clone( ) );
        }
        a.skippedFrames = skippedFrames;
        a.slides = slides;
        if( transitions != null ) {
            a.transitions = new ArrayList<Transition>( );
            for( Transition t : transitions )
                a.transitions.add( (Transition) t.clone( ) );
        }
        a.useTransitions = useTransitions;
        return a;
    }

    public void setId( String id ) {

        this.id = id;
    }

    public void setAbsolutePath( String animationPath ) {

        this.animationPath = animationPath;
        for( Frame frame : frames )
            frame.setAbsolutePath( animationPath );
    }

    public String getAboslutePath( ) {

        return animationPath;
    }

    public ImageLoaderFactory getImageLoaderFactory( ) {
        return factory;
    }

}