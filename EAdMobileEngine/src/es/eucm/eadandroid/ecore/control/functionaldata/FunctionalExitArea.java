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
package es.eucm.eadandroid.ecore.control.functionaldata;




import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import es.eucm.eadandroid.common.data.Polygon;
import es.eucm.eadandroid.common.data.chapter.Exit;
import es.eucm.eadandroid.common.data.chapter.InfluenceArea;
import es.eucm.eadandroid.common.data.chapter.elements.Item;
import es.eucm.eadandroid.common.data.chapter.resources.Resources;
import es.eucm.eadandroid.ecore.gui.GUI;

public class FunctionalExitArea extends FunctionalItem {

	

    private Exit exit;

    private Polygon polygon;

    private static Item buildItem( Exit exit ) {

        Item item = new Item( "", "", "", "" );

        item.addResources( new Resources( ) );
        return item;
    }

    private static int calculateX( Exit exit ) {

        return exit.getX( ) + exit.getWidth( ) / 2;
    }

    private static int calculateY( Exit exit ) {

        return exit.getY( ) + exit.getHeight( );
    }

    public FunctionalExitArea( Exit exit, InfluenceArea influenceArea ) {

        super( buildItem( exit ), null, calculateX( exit ), calculateY( exit ) );

        this.exit = exit;
        this.influenceArea = influenceArea;

        // Create transparent image
        Bitmap bImagenTransparente = GUI.getInstance( ).getGraphicsConfiguration( ).createCompatibleImage( exit.getWidth( ), exit.getHeight( ), true );

        //Graphics2D g2d = bImagenTransparente.createGraphics( );
        Canvas c = new Canvas(bImagenTransparente);
        
        // Make all pixels transparent
        Paint p = new Paint();
        p.setColor(Color.TRANSPARENT);      
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        c.drawRect(new Rect(0, 0, exit.getWidth( ), exit.getHeight( )), p);
  
        image = bImagenTransparente;

        if( !exit.isRectangular( ) ) {
            polygon = new Polygon(exit.getPoints( ).size());
            for( Point point : exit.getPoints( ) ) {
                polygon.addPoint( point.x, point.y );
            }
        }
    }

    @Override
    public int getWidth( ) {

        return exit.getWidth( );
    }

    @Override
    public int getHeight( ) {

        return exit.getHeight( );
    }

    public Exit getExit( ) {

        return exit;
    }

    @Override
    public boolean isPointInside( float x, float y ) {

        boolean isInside = false;
        if( exit.isRectangular( ) ) {

            int mousex = (int) ( x - ( this.x - getWidth( ) / 2 ) );
            int mousey = (int) ( y - ( this.y - getHeight( ) ) );

            isInside = ( ( mousex >= 0 ) && ( mousex < getWidth( ) ) && ( mousey >= 0 ) && ( mousey < getHeight( ) ) );

            //System.out.println( "IS ACTIVE AREA INSIDE("+this.activeArea.getId( )+")="+isInside+" "+x+" , "+y );
            //System.out.println( "X="+this.x+" Y="+ this.y+" WIDTH="+this.getWidth( )+" HEIGHT="+this.getHeight( ));
        }
        else {
            return polygon.contains( x, y );
        }
        return isInside;
    }

    /**
     * Triggers the giving action associated with the item
     * 
     * @param npc
     *            The character receiver of the item
     * @return True if the item was given, false otherwise
     */
    @Override
    public boolean giveTo( FunctionalNPC npc ) {

        return false;
    }

    /**
     * Triggers the grabbing action associated with the item
     * 
     * @return True if the item was grabbed, false otherwise
     */
    @Override
    public boolean grab( ) {

        return false;
    }

}
