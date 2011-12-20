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
package es.eucm.eadandroid.common.loader.subparsers;

import org.xml.sax.Attributes;

import es.eucm.eadandroid.common.data.chapter.Chapter;
import es.eucm.eadandroid.common.data.chapter.Timer;
import es.eucm.eadandroid.common.data.chapter.conditions.Conditions;
import es.eucm.eadandroid.common.data.chapter.effects.Effects;

/**
 * Class to subparse timers
 */
public class TimerSubParser extends SubParser {

    /* Attributes */
    /**
     * Constant for subparsing nothing
     */
    private static final int SUBPARSING_NONE = 0;

    /**
     * Constant for subparsing condition tag
     */
    private static final int SUBPARSING_CONDITION = 1;

    /**
     * Constant for subparsing effect tag
     */
    private static final int SUBPARSING_EFFECT = 2;

    /**
     * Stores the current element being subparsed
     */
    private int subParsing = SUBPARSING_NONE;

    /**
     * Stores the current timer being parsed
     */
    private Timer timer;

    /**
     * Stores the current conditions being parsed
     */
    private Conditions currentConditions;

    /**
     * Stores the current effects being parsed
     */
    private Effects currentEffects;

    /**
     * The subparser for the condition or effect tags
     */
    private SubParser subParser;

    /* Methods */

    /**
     * Constructor
     * 
     * @param chapter
     *            Chapter data to store the read data
     */
    public TimerSubParser( Chapter chapter ) {

        super( chapter );
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.eucm.eadventure.engine.loader.subparsers.SubParser#startElement(java.lang.String, java.lang.String,
     *      java.lang.String, org.xml.sax.Attributes)
     */
    @Override
    public void startElement( String namespaceURI, String sName, String qName, Attributes attrs ) {

        // If no element is being subparsed
        if( subParsing == SUBPARSING_NONE ) {

            // If it is a timer tag, create a new timer with its time
            if( sName.equals( "timer" ) ) {
                String time = "";
                boolean usesEndCondition = true;
                boolean runsInLoop = true;
                boolean multipleStarts = true;
                boolean countDown = false, showWhenStopped = false, showTime = false;
                String displayName = "timer";

                for( int i = 0; i < attrs.getLength( ); i++ ) {
                    if( attrs.getLocalName( i ).equals( "time" ) )
                        time = attrs.getValue( i );
                    if( attrs.getLocalName( i ).equals( "usesEndCondition" ) )
                        usesEndCondition = attrs.getValue( i ).equals( "yes" );
                    if( attrs.getLocalName( i ).equals( "runsInLoop" ) )
                        runsInLoop = attrs.getValue( i ).equals( "yes" );
                    if( attrs.getLocalName( i ).equals( "multipleStarts" ) )
                        multipleStarts = attrs.getValue( i ).equals( "yes" );
                    if( attrs.getLocalName( i ).equals( "showTime" ) )
                        showTime = attrs.getValue( i ).equals( "yes" );
                    if( attrs.getLocalName( i ).equals( "displayName" ) )
                        displayName = attrs.getValue( i );
                    if( attrs.getLocalName( i ).equals( "countDown" ) )
                        countDown = attrs.getValue( i ).equals( "yes" );
                    if( attrs.getLocalName( i ).equals( "showWhenStopped" ) )
                        showWhenStopped = attrs.getValue( i ).equals( "yes" );
                }

                timer = new Timer( Long.parseLong( time ) );
                timer.setRunsInLoop( runsInLoop );
                timer.setUsesEndCondition( usesEndCondition );
                timer.setMultipleStarts( multipleStarts );
                timer.setShowTime( showTime );
                timer.setDisplayName( displayName );
                timer.setCountDown( countDown );
                timer.setShowWhenStopped( showWhenStopped );
            }

            // If it is a condition tag, create the new condition, the subparser and switch the state
            else if( sName.equals( "init-condition" ) || sName.equals( "end-condition" ) ) {
                currentConditions = new Conditions( );
                subParser = new ConditionSubParser( currentConditions, chapter );
                subParsing = SUBPARSING_CONDITION;
            }

            // If it is a effect tag, create the new effect, the subparser and switch the state
            else if( sName.equals( "effect" ) || sName.equals( "post-effect" ) ) {
                currentEffects = new Effects( );
                subParser = new EffectSubParser( currentEffects, chapter );
                subParsing = SUBPARSING_EFFECT;
            }

        }

        // If it is reading an effect or a condition, spread the call
        if( subParsing != SUBPARSING_NONE ) {
            subParser.startElement( namespaceURI, sName, sName, attrs );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.eucm.eadventure.engine.loader.subparsers.SubParser#endElement(java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    @Override
    public void endElement( String namespaceURI, String sName, String qName ) {

        // If no element is being subparsed
        if( subParsing == SUBPARSING_NONE ) {

            // If it is a timer tag, add it to the game data
            if( sName.equals( "timer" ) ) {
                chapter.addTimer( timer );
            }

            // If it is a documentation tag, hold the documentation in the slidescene
            else if( sName.equals( "documentation" ) ) {
                timer.setDocumentation( currentString.toString( ).trim( ) );
            }

            // Reset the current string
            currentString = new StringBuffer( );
        }

        // If a condition is being subparsed
        else if( subParsing == SUBPARSING_CONDITION ) {
            // Spread the call
            subParser.endElement( namespaceURI, sName, sName );

            // If the condition tag is being closed
            if( sName.equals( "init-condition" ) ) {
                timer.setInitCond( currentConditions );

                // Switch the state
                subParsing = SUBPARSING_NONE;
            }

            // If the condition tag is being closed
            if( sName.equals( "end-condition" ) ) {
                timer.setEndCond( currentConditions );

                // Switch the state
                subParsing = SUBPARSING_NONE;
            }
        }

        // If an effect is being subparsed
        else if( subParsing == SUBPARSING_EFFECT ) {
            // Spread the call
            subParser.endElement( namespaceURI, sName, sName );

            // If the effect tag is being closed, store the effect in the next scene and switch the state
            if( sName.equals( "effect" ) ) {
                timer.setEffects( currentEffects );
                subParsing = SUBPARSING_NONE;
            }

            // If the effect tag is being closed, add the post-effects to the current next scene and switch the state
            if( sName.equals( "post-effect" ) ) {
                timer.setPostEffects( currentEffects );
                subParsing = SUBPARSING_NONE;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.eucm.eadventure.engine.loader.subparsers.SubParser#characters(char[], int, int)
     */
    @Override
    public void characters( char[] buf, int offset, int len ) {

        // If no element is being subparsed
        if( subParsing == SUBPARSING_NONE )
            super.characters( buf, offset, len );

        // If it is reading an effect or a condition
        else
            subParser.characters( buf, offset, len );
    }
}
