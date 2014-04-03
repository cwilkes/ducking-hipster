package com.ladro.ecs;

import java.util.Map;

public interface ElevatorService {

	/**
	 * Returns the current floor that an elevator is on.  For more detailed status call {@link #elevatorStatus(int)}
	 * 
	 * @return
	 */
	Map<Integer, Integer> bankStatus();

	void requestPickup(int floor, ElevatorDirection direction);

	/**
	 * Advances floor steps
	 */
	void step();
	
	/**
	 * Would prefer to return a status instead of the actual elevator that can be manipulated
	 * @param elevatorId
	 * @return
	 */
	Elevator getElevator(int elevatorId);
}
