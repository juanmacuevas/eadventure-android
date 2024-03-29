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
package es.eucm.eadandroid.common.loader.parsers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import es.eucm.eadandroid.common.data.assessment.AssessmentProfile;
import es.eucm.eadandroid.common.data.assessment.AssessmentProperty;
import es.eucm.eadandroid.common.data.assessment.AssessmentRule;
import es.eucm.eadandroid.common.data.assessment.TimedAssessmentRule;
import es.eucm.eadandroid.common.data.chapter.conditions.Conditions;
import es.eucm.eadandroid.common.data.chapter.conditions.FlagCondition;
import es.eucm.eadandroid.common.data.chapter.conditions.GlobalStateCondition;
import es.eucm.eadandroid.common.data.chapter.conditions.VarCondition;
import es.eucm.eadandroid.common.loader.InputStreamCreator;

/**
 * This class is the handler to parse the assesment rules file of the adventure.
 * This class is only used for specific xml assessment files, that is a past
 * characteristic, to preserve past game version. In new versions, the
 * assessment info is in chapter.xml file. For this reason the parsing of
 * assessment is now in chapter parsing (ChapterHandler)
 */
public class AssessmentHandler extends DefaultHandler {

    /* Attributes */

    /**
     * String to store the current string in the XML file
     */
    private StringBuffer currentString;

    /**
     * Constant for reading nothing
     */
    private static final int READING_NONE = 0;

    /**
     * Constant for reading either tag
     */
    private static final int READING_EITHER = 1;

    /**
     * Stores the current element being read
     */
    private int reading = READING_NONE;

    /**
     * Array of assessment rules
     */
    private List<AssessmentRule> assessmentRules;

    /**
     * List of flags involved in this assessment script
     */
    private List<String> flags;

    /**
     * List of vars involved in this assessment script
     */
    private List<String> vars;

    /**
     * Assessment rule currently being read
     */
    private AssessmentRule currentAssessmentRule;

    /**
     * Set of conditions being read
     */
    private Conditions currentConditions;

    /**
     * Set of either conditions being read
     */
    private Conditions currentEitherCondition;

    /**
     * InputStreamCreator used in resolveEntity to find dtds (only required in
     * Applet mode)
     */
    private InputStreamCreator isCreator;

    /**
     * The assessment profile
     */
    private AssessmentProfile profile;

    /* Methods */

    /**
     * Default constructor
     */
    public AssessmentHandler( InputStreamCreator isCreator, AssessmentProfile profile ) {

        this.profile = profile;
        if( profile.getRules( ) == null )
            assessmentRules = new ArrayList<AssessmentRule>( );
        else
            assessmentRules = profile.getRules( );
        currentAssessmentRule = null;
        currentString = new StringBuffer( );
        vars = new ArrayList<String>( );
        flags = new ArrayList<String>( );
        profile.setFlags( flags );
        profile.setVars( vars );
        this.isCreator = isCreator;
    }

    private void addFlag( String flag ) {

        if( !flags.contains( flag ) ) {
            flags.add( flag );
        }
    }

    private void addVar( String var ) {

        if( !vars.contains( var ) ) {
            vars.add( var );
        }
    }

    /*
     *  (non-Javadoc)
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    @Override
    public void startElement( String namespaceURI, String sName, String qName, Attributes attrs ) throws SAXException {

        if( sName.equals( "assessment-rules" ) ) {

            for( int i = 0; i < attrs.getLength( ); i++ ) {
                if( attrs.getLocalName( i ).equals( "show-report-at-end" ) ) {
                    profile.setShowReportAtEnd( attrs.getValue( i ).equals( "yes" ) );
                }
                if( attrs.getLocalName( i ).equals( "send-to-email" ) ) {
                    if( attrs.getValue( i ) == null || attrs.getValue( i ).length( ) < 1 ) {
                        profile.setEmail( "" );
                        profile.setSendByEmail( false );
                    }
                    else {
                        profile.setEmail( attrs.getValue( i ) );
                        profile.setSendByEmail( true );
                    }
                }
                if( attrs.getLocalName( i ).equals( "scorm12" ) ) {
                    profile.setScorm12( attrs.getValue( i ).equals( "yes" ) );
                }
                if( attrs.getLocalName( i ).equals( "scorm2004" ) ) {
                    profile.setScorm2004( attrs.getValue( i ).equals( "yes" ) );
                }
            }

        }
        else if( sName.equals( "smtp-config" ) ) {
            for( int i = 0; i < attrs.getLength( ); i++ ) {
                if( attrs.getLocalName( i ).equals( "smtp-ssl" ) )
                    profile.setSmtpSSL( attrs.getValue( i ).equals( "yes" ) );
                if( attrs.getLocalName( i ).equals( "smtp-server" ) )
                    profile.setSmtpServer( attrs.getValue( i ) );
                if( attrs.getLocalName( i ).equals( "smtp-port" ) )
                    profile.setSmtpPort( attrs.getValue( i ) );
                if( attrs.getLocalName( i ).equals( "smtp-user" ) )
                    profile.setSmtpUser( attrs.getValue( i ) );
                if( attrs.getLocalName( i ).equals( "smtp-pwd" ) )
                    profile.setSmtpPwd( attrs.getValue( i ) );
            }
        }

        else if( sName.equals( "assessment-rule" ) ) {

        	String id = null;
            int importance = 0;
            boolean repeatRule = false;

            for( int i = 0; i < attrs.getLength( ); i++ ) {
                if( attrs.getLocalName( i ).equals( "id" ) )
                    id = attrs.getValue( i );
                if( attrs.getLocalName( i ).equals( "importance" ) ) {
                    for( int j = 0; j < AssessmentRule.N_IMPORTANCE_VALUES; j++ )
                        if( attrs.getValue( i ).equals( AssessmentRule.IMPORTANCE_VALUES[j] ) )
                            importance = j;
                }
                if( attrs.getQName( i ).equals( "repeatRule" ) )
                    repeatRule = attrs.getValue( i ).equals( "yes" );
            }

            currentAssessmentRule = new AssessmentRule( id, importance, repeatRule );
        }

        else if( sName.equals( "timed-assessment-rule" ) ) {

        	String id = null;
            int importance = 0;
            boolean repeatRule = false;
            boolean usesEndConditions = false;
            boolean has = false;

            for( int i = 0; i < attrs.getLength( ); i++ ) {
                if( attrs.getLocalName( i ).equals( "id" ) )
                    id = attrs.getValue( i );
                if( attrs.getLocalName( i ).equals( "importance" ) ) {
                    for( int j = 0; j < AssessmentRule.N_IMPORTANCE_VALUES; j++ )
                        if( attrs.getValue( i ).equals( AssessmentRule.IMPORTANCE_VALUES[j] ) )
                            importance = j;
                }
                if( attrs.getQName( i ).equals( "repeatRule" ) )
                    repeatRule = attrs.getValue( i ).equals( "yes" );
                if( attrs.getLocalName( i ).equals( "usesEndConditions" ) ) {
                    has = true;
                    usesEndConditions = attrs.getValue( i ).equals( "yes" );
                }
            }

            currentAssessmentRule = new TimedAssessmentRule( id, importance, repeatRule );
            if( has )
                ( (TimedAssessmentRule) currentAssessmentRule ).setUsesEndConditions( usesEndConditions );
        }

        else if( sName.equals( "condition" ) || sName.equals( "init-condition" ) || sName.equals( "end-condition" ) ) {
            currentConditions = new Conditions( );
        }

        // If it is an either tag, create a new either conditions and switch the state
        else if( sName.equals( "either" ) ) {
            currentEitherCondition = new Conditions( );
            reading = READING_EITHER;
        }

        // If it is an active tag
        else if( sName.equals( "active" ) ) {
            for( int i = 0; i < attrs.getLength( ); i++ ) {
                if( attrs.getLocalName( i ).equals( "flag" ) ) {

                    // Store the active flag in the conditions or either conditions
                    if( reading == READING_NONE )
                        currentConditions.add( new FlagCondition( attrs.getValue( i ) ) );
                    if( reading == READING_EITHER )
                        currentEitherCondition.add( new FlagCondition( attrs.getValue( i ) ) );
                    addFlag( attrs.getValue( i ) );
                }
            }
        }

        // If it is an inactive tag
        else if( sName.equals( "inactive" ) ) {
            for( int i = 0; i < attrs.getLength( ); i++ ) {
                if( attrs.getLocalName( i ).equals( "flag" ) ) {

                    // Store the inactive flag in the conditions or either conditions
                    if( reading == READING_NONE )
                        currentConditions.add( new FlagCondition( attrs.getValue( i ), FlagCondition.FLAG_INACTIVE ) );
                    if( reading == READING_EITHER )
                        currentEitherCondition.add( new FlagCondition( attrs.getValue( i ), FlagCondition.FLAG_INACTIVE ) );
                    addFlag( attrs.getValue( i ) );
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
                currentConditions.add( new VarCondition( var, VarCondition.VAR_GREATER_THAN, value ) );
            if( reading == READING_EITHER )
                currentEitherCondition.add( new VarCondition( var, VarCondition.VAR_GREATER_THAN, value ) );
            addVar( var );
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
                currentConditions.add( new VarCondition( var, VarCondition.VAR_GREATER_EQUALS_THAN, value ) );
            if( reading == READING_EITHER )
                currentEitherCondition.add( new VarCondition( var, VarCondition.VAR_GREATER_EQUALS_THAN, value ) );
            addVar( var );
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
                currentConditions.add( new VarCondition( var, VarCondition.VAR_LESS_THAN, value ) );
            if( reading == READING_EITHER )
                currentEitherCondition.add( new VarCondition( var, VarCondition.VAR_LESS_THAN, value ) );
            addVar( var );
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
                currentConditions.add( new VarCondition( var, VarCondition.VAR_LESS_EQUALS_THAN, value ) );
            if( reading == READING_EITHER )
                currentEitherCondition.add( new VarCondition( var, VarCondition.VAR_LESS_EQUALS_THAN, value ) );
            addVar( var );
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
                currentConditions.add( new VarCondition( var, VarCondition.VAR_EQUALS, value ) );
            if( reading == READING_EITHER )
                currentEitherCondition.add( new VarCondition( var, VarCondition.VAR_EQUALS, value ) );
            addVar( var );
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
                currentConditions.add( new GlobalStateCondition( id ) );
            if( reading == READING_EITHER )
                currentEitherCondition.add( new GlobalStateCondition( id ) );
        }

        else if( sName.equals( "set-property" ) ) {
            String id = null;
            String value = null;

            for( int i = 0; i < attrs.getLength( ); i++ ) {
                if( attrs.getLocalName( i ).equals( "id" ) )
                    id = attrs.getValue( i );
                if( attrs.getLocalName( i ).equals( "value" ) )
                    value = attrs.getValue( i );

            }

            currentAssessmentRule.addProperty( new AssessmentProperty( id, value ) );
        }

        else if( sName.equals( "effect" ) ) {
            if( currentAssessmentRule instanceof TimedAssessmentRule ) {
                int timeMin = Integer.MIN_VALUE;
                int timeMax = Integer.MIN_VALUE;
                for( int i = 0; i < attrs.getLength( ); i++ ) {

                    if( attrs.getLocalName( i ).equals( "time-min" ) )
                        timeMin = Integer.parseInt( attrs.getValue( i ) );
                    if( attrs.getLocalName( i ).equals( "time-max" ) )
                        timeMax = Integer.parseInt( attrs.getValue( i ) );
                }

                TimedAssessmentRule tRule = (TimedAssessmentRule) currentAssessmentRule;
                if( timeMin != Integer.MIN_VALUE && timeMax != Integer.MAX_VALUE ) {
                    tRule.addEffect( timeMin, timeMax );
                }
                else {
                    tRule.addEffect( );
                }
            }
        }
    }

    /*  
     *  (non-Javadoc)
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void endElement( String namespaceURI, String sName, String qName ) throws SAXException {

        if( sName.equals( "assessment-rule" ) || sName.equals( "timed-assessment-rule" ) ) {
            assessmentRules.add( currentAssessmentRule );
        }

        else if( sName.equals( "concept" ) ) {
            currentAssessmentRule.setConcept( currentString.toString( ).trim( ) );
        }

        else if( sName.equals( "condition" ) ) {
            currentAssessmentRule.setConditions( currentConditions );
        }

        else if( sName.equals( "init-condition" ) ) {
            ( (TimedAssessmentRule) currentAssessmentRule ).setInitConditions( currentConditions );
        }

        else if( sName.equals( "end-condition" ) ) {
            ( (TimedAssessmentRule) currentAssessmentRule ).setEndConditions( currentConditions );
        }

        // If it is an either tag
        else if( sName.equals( "either" ) ) {
            // Store the either condition in the condition, and switch the state back to normal
            currentConditions.add( currentEitherCondition );
            reading = READING_NONE;
        }

        else if( sName.equals( "set-text" ) ) {
            currentAssessmentRule.setText( currentString.toString( ).trim( ) );
        }

        // Reset the current string
        currentString = new StringBuffer( );
    }

    /*
     *  (non-Javadoc)
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    @Override
    public void characters( char[] buf, int offset, int len ) throws SAXException {

        // Append the new characters
        currentString.append( new String( buf, offset, len ) );
    }

    /*
     *  (non-Javadoc)
     * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
     */
    @Override
    public void error( SAXParseException exception ) throws SAXParseException {

        // On validation, propagate exception
        exception.printStackTrace( );
        throw exception;
    }

    /*
     *  (non-Javadoc)
     * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String, java.lang.String)
     */
    @Override
    public InputSource resolveEntity( String publicId, String systemId ) {

        // Take the name of the file SAX is looking for
        int startFilename = systemId.lastIndexOf( "/" ) + 1;
        String filename = systemId.substring( startFilename, systemId.length( ) );

        // Build and return a input stream with the file (usually the DTD): 
        // 1) First try looking at main folder
        InputStream inputStream = AdaptationHandler.class.getResourceAsStream( filename );
        if( inputStream == null ) {
            try {
                inputStream = new FileInputStream( filename );
            }
            catch( FileNotFoundException e ) {
                inputStream = null;
            }
        }

        // 2) Secondly use the inputStreamCreator
        if( inputStream == null )
            inputStream = isCreator.buildInputStream( filename );

        return new InputSource( inputStream );
    }
}
