package es.eucm.eadandroid.ecore.control.gamestate.eventlisteners.events;

import android.view.MotionEvent;

public class PressedEvent extends UIEvent{

	public MotionEvent event ;

	public PressedEvent(MotionEvent event) {
		super(UIEvent.PRESSED_ACTION);
		this.event = event;
	}
	
}
