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
package es.eucm.eadandroid.ecore.control.gamestate;




import java.util.ArrayList;
import java.util.Queue;

import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import es.eucm.eadandroid.common.data.chapter.conversation.line.ConversationLine;
import es.eucm.eadandroid.common.data.chapter.conversation.node.ConversationNode;
import es.eucm.eadandroid.common.data.chapter.conversation.node.ConversationNodeView;
import es.eucm.eadandroid.common.data.chapter.conversation.node.OptionConversationNode;
import es.eucm.eadandroid.ecore.GameThread;
import es.eucm.eadandroid.ecore.ECoreActivity.ActivityHandlerMessages;
import es.eucm.eadandroid.ecore.control.functionaldata.FunctionalConditions;
import es.eucm.eadandroid.ecore.control.functionaldata.FunctionalPlayer;
import es.eucm.eadandroid.ecore.control.functionaldata.TalkingElement;
import es.eucm.eadandroid.ecore.control.functionaldata.functionaleffects.FunctionalEffects;
import es.eucm.eadandroid.ecore.control.gamestate.eventlisteners.events.UIEvent;
import es.eucm.eadandroid.ecore.gui.GUI;

/**
 * A game main loop during a conversation
 */
public class GameStateConversation extends GameState {


    /**
     * Height of the text of the responses
     */
    private final int RESPONSE_TEXT_HEIGHT;

    /**
     * Ascent of the text of the response
     */
    private final int RESPONSE_TEXT_ASCENT;

    /**
     * Current conversational node being played
     */
    private ConversationNode currentNode;

    /**
     * Index of the line being played
     */
    private int currentLine;

    /**
     * Variable to control the access to doRandom()
     */
    private boolean firstTime;

    /**
     * Store the option selected to use it when it come back to the running
     * effects Game State
     */
    private int optionSelected;

    /**
     * Indicates if an option was selected
     */
    private boolean isOptionSelected;

    /**
     * Number of options that has been displayed in the screen
     */
    private int numberDisplayedOptions;

    /**
     * An array list that match the number "i" option with its real position in
     * option node.
     */
    // Remember that only will be show the options lines which achieves its conditions
    private ArrayList<Integer> correspondingIndex;

    /**
     * Store only the option which has all conditions OK
     */
    private ArrayList<ConversationLine> optionsToShow;

    /**
     * The name of conversation
     */
    private String convID;

	private static boolean skip;

    /**
     * Creates a new GameStateConversation
     */
    
    public GameStateConversation( ) {
        
        RESPONSE_TEXT_ASCENT = GUI.getInstance( ).getAscent( );
        RESPONSE_TEXT_HEIGHT = RESPONSE_TEXT_ASCENT * 3 ;

        currentNode = game.getConversation( ).getRootNode( );
        currentLine = 0;
        optionsToShow = new ArrayList<ConversationLine>( );
        
        skip = false;
        convID = new String( );

    }

    @Override
    public synchronized void mainLoop( long elapsedTime, int fps ) {
    	handleUIEvents();

        Canvas c = setUpGUI( elapsedTime );

        if( currentNode.getType( ) == ConversationNodeView.DIALOGUE )
            processDialogNode( );
        else if( currentNode.getType( ) == ConversationNodeView.OPTION )
            processOptionNode( c );

        GUI.getInstance( ).endDraw( );

    }

    /**
     * Set up the basic gui of the scene.
     * 
     * @param elapsedTime
     *            The time elapsed since the last update
     * @return The graphics element for the scene
     */
    private Canvas setUpGUI( long elapsedTime ) {


        game.getFunctionalScene( ).update( elapsedTime );
        GUI.getInstance( ).update( elapsedTime );

        Canvas g = GUI.getInstance( ).getGraphics( );
        
        game.getFunctionalScene( ).draw( );
        GUI.getInstance( ).drawScene( g, elapsedTime );

        return g;
    }

    /**
     * Processed mouse clicks when in a dialog node. If no button was pressed,
     * the conversation goes on normally (goes to the next line only if no
     * character is talking or the one talking has finished). If the left button
     * (BUTTON1) was pressed, the current line is skipped. If the right button
     * (BUTTON3) was pressed, all the lines are skipped.
     */
    private void processDialogNode( ) {
  	
    	if( game.getCharacterCurrentlyTalking( ) == null || ( game.getCharacterCurrentlyTalking( ) != null && !game.getCharacterCurrentlyTalking( ).isTalking( ) ) ) {
            playNextLine( );
        }
    	
    	if (skip){
    		playNextLine( );
    		skip = false;
    		
    	}

        firstTime = true;
    }

    /**
     * Process all the information available when in an option node. Two cases
     * are distinguished, when there is a selected option and where there is no
     * selected options.
     * 
     * @param g
     *            The graphics in which to draw the options
     */
    private void processOptionNode( Canvas c ) {
    	
    	 if( !isOptionSelected )
             optionNodeNoOptionSelected( c );
         else
             selectOption();
    }


	private void optionNodeNoOptionSelected(Canvas  c ){
        if( firstTime ) {
        	
            ( (OptionConversationNode) currentNode ).doRandom( );
            storeOKConditionsConversationLines( );
            
            //send message to show options on a list activity 
            Handler handler = GameThread.getInstance().getHandler();

    		Message msg = handler.obtainMessage();

    		Bundle b = new Bundle();
    		int textColor = game.getCharacterCurrentlyTalking().getTextFrontColor();
    		for( int i = 0; i < optionsToShow.size( ); i++ ) {
    			b.putString(Integer.toString(i), optionsToShow.get( i ).getText( ));
    			b.putInt("color",textColor );
              //  drawLine( c, optionsToShow.get( i ).getText( ), i, i );
                
            }
    		numberDisplayedOptions = optionsToShow.size( );
    		b.putInt("size", numberDisplayedOptions);
    		msg.what = ActivityHandlerMessages.CONVERSATION;
    		msg.setData(b);

    		msg.sendToTarget();
    		
            firstTime = false;
        }
        // if there are not options to draw, finalize the conversation
        if( numberDisplayedOptions == 0 )
            endConversation( );
    
    }

   

    /**
     * Returns the number of conversation lines in current option node which has
     * all conditions OK
     * 
     * @return number of lines with achieve its conditions.
     */
    private void storeOKConditionsConversationLines( ) {

        optionsToShow = new ArrayList<ConversationLine>( );
        correspondingIndex = new ArrayList<Integer>( );
        for( int i = 0; i < currentNode.getLineCount( ); i++ ) {
            if( ( new FunctionalConditions( currentNode.getLine( i ).getConditions( ) ).allConditionsOk( ) ) ) {
                optionsToShow.add( currentNode.getLine( i ) );
                // Store the real position in node of recent inserted conversation line
                correspondingIndex.add( i );
            }
        }

    }



    /**
     * Finalize the conversation
     */
    private void endConversation( ) {

        for( ConversationNode node : game.getConversation( ).getAllNodes( ) ) {
            node.resetEffect( );
        }
     //HUD   GUI.getInstance( ).toggleHud( true );
        game.endConversation( );
    }

    /**
     * If the user chooses a valid option, it is selected and its text show.
     */
    public void selectOption() {

        if( game.getCharacterCurrentlyTalking( ) != null && game.getCharacterCurrentlyTalking( ).isTalking( ) ) {
            if (skip){

                game.getCharacterCurrentlyTalking( ).stopTalking( );
                skip = false;
            	
            }
                return;
        }

        if( currentNode.hasValidEffect( ) && !currentNode.isEffectConsumed( ) ) {
            currentNode.consumeEffect( );
            game.pushCurrentState( this );
            FunctionalEffects.storeAllEffects( currentNode.getEffects( ), true );
          //HUD   GUI.getInstance( ).toggleHud( true );
        }
        else if( ( !currentNode.hasValidEffect( ) || currentNode.isEffectConsumed( ) ) && currentNode.isTerminal( ) ) {
        	endConversation( );
        }
        else if( !currentNode.isTerminal( ) ) {
        	if( optionSelected >= 0 && optionSelected < currentNode.getChildCount( ) ) {
        		currentNode = currentNode.getChild( correspondingIndex.get( optionSelected ) );
        		isOptionSelected = false;
        	}
        }
    }	
    ///////////////




        public void selectDisplayedOption(int optionChoosed ) {

    	this.optionSelected = optionChoosed;

    	if( optionSelected >= 0 && optionSelected < optionsToShow.size( ) ) {
    		if( game.getCharacterCurrentlyTalking( ) != null && game.getCharacterCurrentlyTalking( ).isTalking( ) )
    			game.getCharacterCurrentlyTalking( ).stopTalking( );

    		FunctionalPlayer player = game.getFunctionalPlayer( );
    		ConversationLine line = currentNode.getLine( correspondingIndex.get( optionSelected ) );

    		if( line.isValidAudio( ) ) {
    			player.speak( line.getText( ), line.getAudioPath( ) );
    		}
    		else if( line.getSynthesizerVoice( ) || player.isAlwaysSynthesizer( ) ) {
    			player.speakWithFreeTTS( line.getText( ), player.getPlayerVoice( ) );
    		}
    		else {
    			player.speak( line.getText( ) );
    		}

    		game.setCharacterCurrentlyTalking( player );
    		isOptionSelected = true;

    	}
    }

 

    public synchronized void mouseClicked( UIEvent e ) {
    	
        if( currentNode.getType( ) == ConversationNodeView.OPTION ) {
            skip = false;
        }
        else if( currentNode.getType( ) == ConversationNodeView.DIALOGUE) {
        	skip = true;
        }
    }

    

    /**
     * Jumps to the next conversation line. If the current line was the last,
     * end the conversation and trigger the effects or jump to the next node
     */
    private void playNextLine( ) {

        if( game.getCharacterCurrentlyTalking( ) != null && game.getCharacterCurrentlyTalking( ).isTalking( ) )
            game.getCharacterCurrentlyTalking( ).stopTalking( );

        if( currentLine < currentNode.getLineCount( ) )
            playNextLineInNode( );
        else
            skipToNextNode( );
    }

    /**
     * Play the next line in the current conversation Nod
     */
    private void playNextLineInNode( ) {

        ConversationLine line = currentNode.getLine( currentLine );
        TalkingElement talking = null;

        // Only talk if all conditions in current line are OK
        if( ( new FunctionalConditions( currentNode.getLine( currentLine ).getConditions( ) ).allConditionsOk( ) ) ) {

            if( line.isPlayerLine( ) )
                talking = game.getFunctionalPlayer( );
            else {
                if( line.getName( ).equals( "NPC" ) )
                    talking = game.getCurrentNPC( );
                else
                    talking = game.getFunctionalScene( ).getNPC( line.getName( ) );
            }

            if( talking != null ) {
                if( line.isValidAudio( ) )
                    talking.speak( line.getText( ), line.getAudioPath( ) );
                else if( line.getSynthesizerVoice( ) || talking.isAlwaysSynthesizer( ) )
                    talking.speakWithFreeTTS( line.getText( ), talking.getPlayerVoice( ) );
                else
                    talking.speak( line.getText( ) );
            }
            game.setCharacterCurrentlyTalking( talking );
        }
        currentLine++;
    }

    /**
     * Skip to the next node in the conversation of finish the conversation.
     * Consume the effects of the current node.
     */
    private void skipToNextNode( ) {

        if( currentNode.hasValidEffect( ) && !currentNode.isEffectConsumed( ) ) {
            currentNode.consumeEffect( );
            game.pushCurrentState( this );
            FunctionalEffects.storeAllEffects( currentNode.getEffects( ), true );}
       
        else if( ( !currentNode.hasValidEffect( ) || currentNode.isEffectConsumed( ) ) && currentNode.isTerminal( ) ) {
            endConversation( );
        }
        else if( !currentNode.isTerminal( ) ) {
            currentNode = currentNode.getChild( 0 );
            currentLine = 0;
        }
        
    }
    

    /**
     * @param convName
     *            the convName to set
     */
    public void setConvID( String convName ) {

        this.convID = convName;
    }

    /**
     * @return the convID
     */
    public String getConvID( ) {

        return convID;
    }
    
    private void handleUIEvents() {
		UIEvent e;
		Queue<UIEvent> vEvents = touchListener.getEventQueue();
		while ((e = vEvents.poll()) != null) {
			switch (e.getAction()) {
			case UIEvent.UNPRESSED_ACTION:				
				mouseClicked(e);
				break;			
			case UIEvent.TAP_ACTION: 
				mouseClicked(e);
			}

		}
	}
}
