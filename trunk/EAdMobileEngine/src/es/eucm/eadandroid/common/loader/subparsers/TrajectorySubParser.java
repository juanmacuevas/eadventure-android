/*******************************************************************************
 * <e-Adventure> Mobile for Android is a port of the <e-Adventure> research project to 	the Android platform.
 *     
 *      Copyright 2009-2012 <e-UCM> research group.
 *    
 *       <e-UCM> is a research group of the Department of Software Engineering
 *            and Artificial Intelligence at the Complutense University of Madrid
 *            (School of Computer Science).
 *    
 *            C Profesor Jose Garcia Santesmases sn,
 *            28040 Madrid (Madrid), Spain.
 *    
 *            For more info please visit:  <http://e-adventure.e-ucm.es/android> or
 *            <http://www.e-ucm.es>
 *    
 *    ****************************************************************************
 * 	This file is part of <e-Adventure> Mobile, version 1.0.
 * 
 * 	Main contributors - Roberto Tornero
 * 
 * 	Former contributors - Alvaro Villoria 
 * 						    Juan Manuel de las Cuevas
 * 						    Guillermo Mart�n 	
 * 
 *     	You can access a list of all the contributors to <e-Adventure> Mobile at:
 *            	http://e-adventure.e-ucm.es/contributors
 *    
 *    ****************************************************************************
 *         <e-Adventure> Mobile is free software: you can redistribute it and/or modify
 *        it under the terms of the GNU Lesser General Public License as published by
 *        the Free Software Foundation, either version 3 of the License, or
 *        (at your option) any later version.
 *    
 *        <e-Adventure> Mobile is distributed in the hope that it will be useful,
 *        but WITHOUT ANY WARRANTY; without even the implied warranty of
 *        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *        GNU Lesser General Public License for more details.
 *    
 *        See <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
import es.eucm.eadandroid.common.data.chapter.Trajectory;
import es.eucm.eadandroid.common.data.chapter.scenes.Scene;

/**
 * Class to subparse items.
 */
public class TrajectorySubParser extends SubParser {

    /* Attributes */

    /**
     * Constant for subparsing nothing.
     */
    private static final int SUBPARSING_NONE = 0;

    /**
     * Stores the current element being subparsed.
     */
    private int subParsing = SUBPARSING_NONE;

    /**
     * Trajectory being parsed.
     */
    private Trajectory trajectory;

    /**
     * Subparser for effects and conditions.
     */
    private SubParser subParser;

    /**
     * Scene to add the trajectory
     */
    private Scene scene;

    /* Methods */

    /**
     * Constructor.
     * 
     * @param chapter
     *            Chapter data to store the read data
     */
    public TrajectorySubParser( Chapter chapter, Scene scene ) {

        super( chapter );
        this.trajectory = new Trajectory( );
        //scene.setTrajectory(trajectory);
        this.scene = scene;
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.eucm.eadventure.engine.cargador.subparsers.SubParser#startElement(java.lang.String, java.lang.String,
     *      java.lang.String, org.xml.sax.Attributes)
     */
    @Override
    public void startElement( String namespaceURI, String sName, String qName, Attributes attrs ) {

        // If no element is being subparsed
        if( subParsing == SUBPARSING_NONE ) {
            // If it is a object tag, create the new object (with its id)
            if( sName.equals( "node" ) ) {

                int x = 0, y = 0;
                float scale = 1.0f;

                String id = "";

                for( int i = 0; i < attrs.getLength( ); i++ ) {
                    if( attrs.getLocalName( i ).equals( "x" ) )
                        x = Integer.parseInt( attrs.getValue( i ) );
                    if( attrs.getLocalName( i ).equals( "y" ) )
                        y = Integer.parseInt( attrs.getValue( i ) );
                    if( attrs.getLocalName( i ).equals( "id" ) )
                        id = attrs.getValue( i );
                    if( attrs.getLocalName( i ).equals( "scale" ) ) {
                        scale = Float.parseFloat( attrs.getValue( i ) );
                    }
                }

                trajectory.addNode( id, x, y, scale );

            }

            // If it is a condition tag, create new conditions and switch the state
            else if( sName.equals( "side" ) ) {
                String idStart = "";
                String idEnd = "";
                int length = -1;
                
                for( int i = 0; i < attrs.getLength( ); i++ ) {
                    if( attrs.getLocalName( i ).equals( "idStart" ) )
                        idStart = attrs.getValue( i );
                    if( attrs.getLocalName( i ).equals( "idEnd" ) )
                        idEnd = attrs.getValue( i );
                    if ( attrs.getLocalName( i ).equals( "length"))
                        length = Integer.parseInt( attrs.getValue( i ) );
                }

                trajectory.addSide( idStart, idEnd, length );
            }

            else if( sName.equals( "initialnode" ) ) {
                String id = "";

                for( int i = 0; i < attrs.getLength( ); i++ ) {
                    if( attrs.getLocalName( i ).equals( "id" ) )
                        id = attrs.getValue( i );
                }

                trajectory.setInitial( id );
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
     * @see es.eucm.eadventure.engine.cargador.subparsers.SubParser#endElement(java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    @Override
    public void endElement( String namespaceURI, String sName, String qName ) {

        // If no element is being subparsed
        if( subParsing == SUBPARSING_NONE ) {

            // Reset the current string
            currentString = new StringBuffer( );
        }

        if( sName.equals( "trajectory" ) ) {
            if( trajectory.getNodes( ).size( ) != 0 ) {
                trajectory.deleteUnconnectedNodes( );
                scene.setTrajectory( trajectory );

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

        // If it is reading an effect or a condition, spread the call
        else
            subParser.characters( buf, offset, len );
    }
}
