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
package es.eucm.eadandroid.ecore.control.functionaldata.functionalhighlights;



import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import es.eucm.eadandroid.ecore.gui.GUI;


public class FunctionalHighlightGreen extends FunctionalHighlight {
    
    public FunctionalHighlightGreen(boolean animated) {
       this.animated = animated;
       this.time = System.currentTimeMillis( );
    }
    
    @Override
    public Bitmap getHighlightedImage( Bitmap image ) {
        
        if (animated)
            calculateDisplacements(image.getWidth(  ), image.getHeight(  ));

        if (oldImage == null || oldImage != image) {            
            Bitmap temp = GUI.getInstance( ).getGraphicsConfiguration( ).createCompatibleImage(image.getWidth(), image.getHeight(), true );
            
            Canvas c = new Canvas(temp);
            c.drawBitmap(image, 0, 0, null);
            //GRAPHICS 
            
            //temp.getGraphics( ).drawImage( image, 0, 0, null );
            //OPTIMIZE improve performance using android API?
            for (int i = 0 ; i < image.getWidth(  ); i++) {
                for (int j = 0; j < image.getHeight(  ); j++) {
                    temp.setPixel( i, j, temp.getPixel(i, j) | 0x0000ff00 );
                }
            }
            oldImage = image;
            newImage = temp;
        }
        //OPTIMIZE Change to create scaled bitmap
       
        Bitmap temp = GUI.getInstance( ).getGraphicsConfiguration( ).createCompatibleImage( Math.round( image.getWidth(  ) * scale ),  Math.round( image.getHeight(  ) * scale ), true );
        Canvas c = new Canvas(temp);
        
        Matrix m = new Matrix();
        m.setScale(scale, scale);
        
        c.drawBitmap(temp, m, null);    
        return temp;

//        return newImage.getScaledInstance( (int)(image.getWidth(null) * scale), (int)(image.getHeight( null ) * scale), Image.SCALE_SMOOTH );
    }

}
