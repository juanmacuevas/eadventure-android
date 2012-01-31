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
 * <e-Adventure> is an <e-UCM> research project.
 * <e-UCM>, Department of Software Engineering and Artificial Intelligence.
 * Faculty of Informatics, Complutense University of Madrid (Spain).
 * @author Del Blanco, A., Marchiori, E., Torrente, F.J.
 * @author Moreno-Ger, P. & Fern�ndez-Manj�n, B. (directors)
 * @year 2009
 * Web-site: http://e-adventure.e-ucm.es
 */

/*
    Copyright (C) 2004-2009 <e-UCM> research group

    This file is part of <e-Adventure> project, an educational game & game-like 
    simulation authoring tool, availabe at http://e-adventure.e-ucm.es. 

    <e-Adventure> is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    <e-Adventure> is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with <e-Adventure>; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

*/
package es.eucm.eadandroid.common.data.chapter.effects;

import es.eucm.eadandroid.common.data.HasTargetId;


public class HighlightItemEffect extends AbstractEffect implements HasTargetId {

    public static final int NO_HIGHLIGHT = 0;
    
    public static final int HIGHLIGHT_BLUE = 1;
    
    public static final int HIGHLIGHT_RED = 2;
    
    public static final int HIGHLIGHT_GREEN = 3;
    
    public static final int HIGHLIGHT_BORDER = 4;
    
    private String idTarget;
    
    private int highlightType;
    
    private boolean highlightAnimated;
    
    public HighlightItemEffect(String idTarget, int type, boolean animated) {
        super();
        this.idTarget = idTarget;
        highlightType = type;
        highlightAnimated = animated;
    }
    
    @Override
    public int getType( ) {
        return HIGHLIGHT_ITEM;
    }

    public int getHighlightType() {
        return highlightType;
    }
    
    public void setHighlightType(int highlightType) {
        this.highlightType = highlightType;
    }
    
    public boolean isHighlightAnimated() {
        return highlightAnimated;
    }
    
    public void setHighlightAnimated(boolean highlightAnimated) {
        this.highlightAnimated = highlightAnimated;
    }
    
    public String getTargetId( ) {
        return idTarget;
    }

    public void setTargetId( String id ) {
        idTarget = id;
    }

    @Override
    public Object clone( ) throws CloneNotSupportedException {
        HighlightItemEffect coe = (HighlightItemEffect) super.clone( );
        coe.idTarget = ( idTarget != null ? new String( idTarget ) : null );
        coe.highlightType = highlightType;
        coe.highlightAnimated = highlightAnimated;
        return coe;
    }

}
