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
import es.eucm.eadandroid.common.data.chapter.conditions.Conditions;
import es.eucm.eadandroid.common.data.chapter.conditions.FlagCondition;
import es.eucm.eadandroid.common.data.chapter.conditions.GlobalStateCondition;
import es.eucm.eadandroid.common.data.chapter.conditions.VarCondition;

/**
 * Class to subparse conditions
 */
public class ConditionSubParser extends SubParser {

    /* Attributes */

    /**
     * Constant for reading nothing
     */
    private static final int READING_NONE = 0;

    /**
     * Constant for reading either tag
     */
    private static final int READING_EITHER = 1;

    /**
     * Stores the current element being parsed
     */
    private int reading = READING_NONE;

    /**
     * Stores the conditions
     */
    private Conditions conditions;

    /**
     * Stores the current either conditions
     */
    private Conditions currentEitherCondition;

    /* Methods */

    /**
     * Constructor
     * 
     * @param conditions
     *            Structure in which the conditions will be placed
     * @param chapter
     *            Chapter data to store the read data
     */
    public ConditionSubParser( Conditions conditions, Chapter chapter ) {

        super( chapter );
        this.conditions = conditions;
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.eucm.eadventure.engine.loader.subparsers.SubParser#startElement(java.lang.String, java.lang.String,
     *      java.lang.String, org.xml.sax.Attributes)
     */
    @Override
    public void startElement( String namespaceURI, String sName, String qName, Attributes attrs ) {

        // If it is an either tag, create a new either conditions and switch the state
        if( sName.equals( "either" ) ) {
            currentEitherCondition = new Conditions( );
            reading = READING_EITHER;
        }

        // If it is an active tag
        else if( sName.equals( "active" ) ) {
            for( int i = 0; i < attrs.getLength( ); i++ ) {
                if( attrs.getLocalName( i ).equals( "flag" ) ) {

                    // Store the active flag in the conditions or either conditions
                    if( reading == READING_NONE )
                        conditions.add( new FlagCondition( attrs.getValue( i ), FlagCondition.FLAG_ACTIVE ) );
                    if( reading == READING_EITHER )
                        currentEitherCondition.add( new FlagCondition( attrs.getValue( i ), FlagCondition.FLAG_ACTIVE ) );

                    chapter.addFlag( attrs.getValue( i ) );
                }
            }
        }

        // If it is an inactive tag
        else if( sName.equals( "inactive" ) ) {
            for( int i = 0; i < attrs.getLength( ); i++ ) {
                if( attrs.getLocalName( i ).equals( "flag" ) ) {

                    // Store the inactive flag in the conditions or either conditions
                    if( reading == READING_NONE )
                        conditions.add( new FlagCondition( attrs.getValue( i ), FlagCondition.FLAG_INACTIVE ) );
                    if( reading == READING_EITHER )
                        currentEitherCondition.add( new FlagCondition( attrs.getValue( i ), FlagCondition.FLAG_INACTIVE ) );

                    chapter.addFlag( attrs.getValue( i ) );
                }
            }
        }

        // If it is a greater-than tag
        else if( sName.equals( "greater-than" ) ) {
            // The var
            String var = null;
            // The value
            int value = 0;

            for( int i = 0; i < attrs.getLength( ); i++ ) {
                if( attrs.getLocalName( i ).equals( "var" ) ) {
                    var = attrs.getValue( i );
                }
                else if( attrs.getLocalName( i ).equals( "value" ) ) {
                    value = Integer.parseInt( attrs.getValue( i ) );
                }
            }
            // Store the inactive flag in the conditions or either conditions
            if( reading == READING_NONE )
                conditions.add( new VarCondition( var, VarCondition.VAR_GREATER_THAN, value ) );
            if( reading == READING_EITHER )
                currentEitherCondition.add( new VarCondition( var, VarCondition.VAR_GREATER_THAN, value ) );
            chapter.addVar( var );
        }

        // If it is a greater-equals-than tag
        else if( sName.equals( "greater-equals-than" ) ) {
            // The var
            String var = null;
            // The value
            int value = 0;

            for( int i = 0; i < attrs.getLength( ); i++ ) {
                if( attrs.getLocalName( i ).equals( "var" ) ) {
                    var = attrs.getValue( i );
                }
                else if( attrs.getLocalName( i ).equals( "value" ) ) {
                    value = Integer.parseInt( attrs.getValue( i ) );
                }
            }
            // Store the inactive flag in the conditions or either conditions
            if( reading == READING_NONE )
                conditions.add( new VarCondition( var, VarCondition.VAR_GREATER_EQUALS_THAN, value ) );
            if( reading == READING_EITHER )
                currentEitherCondition.add( new VarCondition( var, VarCondition.VAR_GREATER_EQUALS_THAN, value ) );
            chapter.addVar( var );
        }

        // If it is a less-than tag
        else if( sName.equals( "less-than" ) ) {
            // The var
            String var = null;
            // The value
            int value = 0;

            for( int i = 0; i < attrs.getLength( ); i++ ) {
                if( attrs.getLocalName( i ).equals( "var" ) ) {
                    var = attrs.getValue( i );
                }
                else if( attrs.getLocalName( i ).equals( "value" ) ) {
                    value = Integer.parseInt( attrs.getValue( i ) );
                }
            }
            // Store the inactive flag in the conditions or either conditions
            if( reading == READING_NONE )
                conditions.add( new VarCondition( var, VarCondition.VAR_LESS_THAN, value ) );
            if( reading == READING_EITHER )
                currentEitherCondition.add( new VarCondition( var, VarCondition.VAR_LESS_THAN, value ) );
            chapter.addVar( var );
        }

        // If it is a less-equals-than tag
        else if( sName.equals( "less-equals-than" ) ) {
            // The var
            String var = null;
            // The value
            int value = 0;

            for( int i = 0; i < attrs.getLength( ); i++ ) {
                if( attrs.getLocalName( i ).equals( "var" ) ) {
                    var = attrs.getValue( i );
                }
                else if( attrs.getLocalName( i ).equals( "value" ) ) {
                    value = Integer.parseInt( attrs.getValue( i ) );
                }
            }
            // Store the inactive flag in the conditions or either conditions
            if( reading == READING_NONE )
                conditions.add( new VarCondition( var, VarCondition.VAR_LESS_EQUALS_THAN, value ) );
            if( reading == READING_EITHER )
                currentEitherCondition.add( new VarCondition( var, VarCondition.VAR_LESS_EQUALS_THAN, value ) );
            chapter.addVar( var );
        }

        // If it is a equals-than tag
        else if( sName.equals( "equals" ) ) {
            // The var
            String var = null;
            // The value
            int value = 0;

            for( int i = 0; i < attrs.getLength( ); i++ ) {
                if( attrs.getLocalName( i ).equals( "var" ) ) {
                    var = attrs.getValue( i );
                }
                else if( attrs.getLocalName( i ).equals( "value" ) ) {
                    value = Integer.parseInt( attrs.getValue( i ) );
                }
            }
            // Store the inactive flag in the conditions or either conditions
            if( reading == READING_NONE )
                conditions.add( new VarCondition( var, VarCondition.VAR_EQUALS, value ) );
            if( reading == READING_EITHER )
                currentEitherCondition.add( new VarCondition( var, VarCondition.VAR_EQUALS, value ) );
            chapter.addVar( var );
        }

        // If it is a global-state-reference tag
        else if( sName.equals( "global-state-ref" ) ) {
            // Id
            String id = null;
            for( int i = 0; i < attrs.getLength( ); i++ ) {
                if( attrs.getLocalName( i ).equals( "id" ) ) {
                    id = attrs.getValue( i );
                }
            }
            // Store the inactive flag in the conditions or either conditions
            if( reading == READING_NONE )
                conditions.add( new GlobalStateCondition( id ) );
            if( reading == READING_EITHER )
                currentEitherCondition.add( new GlobalStateCondition( id ) );
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

        // If it is an either tag
        if( sName.equals( "either" ) ) {
            // Store the either condition in the condition, and switch the state back to normal
            conditions.add( currentEitherCondition );
            reading = READING_NONE;
        }
    }

}
