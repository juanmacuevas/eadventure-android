/*******************************************************************************
 * <e-Adventure> Mobile for Android(TM) is a port of the <e-Adventure> research project to 	the Android(TM) platform.
 *        
 *          Copyright 2009-2012 <e-UCM> research group.
 *        
 *          <e-UCM> is a research group of the Department of Software Engineering
 *           and Artificial Intelligence at the Complutense University of Madrid
 *           (School of Computer Science).
 *        
 *           C Profesor Jose Garcia Santesmases sn,
 *           28040 Madrid (Madrid), Spain.
 *       
 *           For more info please visit:  <http://e-adventure.e-ucm.es/android> or
 *           <http://www.e-ucm.es>
 *        
 *        	 *Android is a trademark of Google Inc.
 *       	
 *        ****************************************************************************
 *     	 This file is part of <e-Adventure> Mobile, version 1.0.
 *     
 *    	 Main contributors - Roberto Tornero
 *     
 *     	 Former contributors - Alvaro Villoria 
 *     						    Juan Manuel de las Cuevas
 *     						    Guillermo Martin 	
 *    
 *     	 Directors - Baltasar Fernandez Manjon
 *     				Eugenio Marchiori
 *     
 *         	 You can access a list of all the contributors to <e-Adventure> Mobile at:
 *                	http://e-adventure.e-ucm.es/contributors
 *        
 *        ****************************************************************************
 *             <e-Adventure> Mobile is free software: you can redistribute it and/or modify
 *            it under the terms of the GNU Lesser General Public License as published by
 *            the Free Software Foundation, either version 3 of the License, or
 *            (at your option) any later version.
 *        
 *            <e-Adventure> Mobile is distributed in the hope that it will be useful,
 *            but WITHOUT ANY WARRANTY; without even the implied warranty of
 *            MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *            GNU Lesser General Public License for more details.
 *        
 *            See <http://www.gnu.org/licenses/>
 ******************************************************************************/
package es.eucm.eadandroid.common.data.chapter.conditions;

/**
 * A condition based on a reference to a global state
 * 
 * @author Javier
 * 
 */
public class GlobalStateCondition extends Condition {

    public static final int GS_SATISFIED = 7;

    public static final int GS_NOT_SATISFIED = 8;

    /**
     * Constructor
     * 
     * @param flagVar
     * @param state
     */
    public GlobalStateCondition( String id ) {

        super( Condition.GLOBAL_STATE_CONDITION, id, Condition.NO_STATE );
    }

    /**
     * Constructor
     * 
     * @param flagVar
     * @param state
     */
    public GlobalStateCondition( String id, int state ) {

        //OLD
    	/*super( Condition.GLOBAL_STATE_CONDITION, id, state );
        if( state != GS_SATISFIED && state != GS_NOT_SATISFIED )
            state = GS_SATISFIED;*/
    	super( Condition.GLOBAL_STATE_CONDITION, id, ( state != GS_SATISFIED && state != GS_NOT_SATISFIED ? GS_SATISFIED : state) );

    	
    }

    @Override
    public Object clone( ) throws CloneNotSupportedException {

        GlobalStateCondition gsr = (GlobalStateCondition) super.clone( );
        return gsr;
    }

}
