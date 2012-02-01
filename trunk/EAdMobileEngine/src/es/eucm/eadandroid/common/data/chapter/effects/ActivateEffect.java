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
package es.eucm.eadandroid.common.data.chapter.effects;

import es.eucm.eadandroid.common.data.HasTargetId;

/**
 * An effect that activates a flag
 */
public class ActivateEffect extends AbstractEffect implements HasTargetId {

    /**
     * Name of the flag to be activated
     */
    private String idFlag;

    /**
     * Creates a new Activate effect.
     * 
     * @param idFlag
     *            the id of the flag to be activated
     */
    public ActivateEffect( String idFlag ) {

        super( );
        this.idFlag = idFlag;
    }

    @Override
    public int getType( ) {

        return ACTIVATE;
    }

    /**
     * Returns the idFlag
     * 
     * @return String containing the idFlag
     */
    public String getTargetId( ) {

        return idFlag;
    }

    /**
     * Sets the new idFlag
     * 
     * @param idFlag
     *            New idFlag
     */
    public void setTargetId( String idFlag ) {

        this.idFlag = idFlag;
    }

    @Override
    public Object clone( ) throws CloneNotSupportedException {

        ActivateEffect ae = (ActivateEffect) super.clone( );
        ae.idFlag = ( idFlag != null ? new String( idFlag ) : null );
        return ae;
    }
}
