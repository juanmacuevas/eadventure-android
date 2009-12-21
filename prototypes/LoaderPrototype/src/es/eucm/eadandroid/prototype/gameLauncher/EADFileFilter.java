package es.eucm.eadandroid.prototype.gameLauncher;

import java.io.File;
import java.io.FilenameFilter;


/**
 * A filter for ead files
 */
public class EADFileFilter implements FilenameFilter {

	public boolean accept(File f, String name) {
		
	  return ( f.isDirectory( ) && ( name.endsWith( ".ead" ) == true )) ;
		
	}
}