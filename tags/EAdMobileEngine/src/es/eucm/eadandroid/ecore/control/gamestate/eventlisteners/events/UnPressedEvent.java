package es.eucm.eadandroid.ecore.control.gamestate.eventlisteners.events;

import android.view.MotionEvent;

public class UnPressedEvent extends UIEvent{

	public MotionEvent event ;

	public UnPressedEvent(MotionEvent event) {
		super(UIEvent.UNPRESSED_ACTION);
		this.event = event;
	}
	
	
	
}
