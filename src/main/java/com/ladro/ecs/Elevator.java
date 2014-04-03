package com.ladro.ecs;

import java.util.ArrayList;
import java.util.List;

public class Elevator {

	private final Integer m_elevatorId;
	private double m_currentFloor;
	private ElevatorDirection m_elevatorMotion;
	private final List<FloorRequestTime> m_pendingFloors;
	private boolean m_doorOpen = false;

	public Elevator(Integer elevatorId) {
		this(elevatorId, 0, null);
	}

	public Elevator(Integer elevatorId, int currentFloor, ElevatorDirection motion) {
		m_elevatorId = elevatorId;
		m_currentFloor = currentFloor;
		m_elevatorMotion = motion;
		m_pendingFloors = new ArrayList<FloorRequestTime>();
	}

	public Integer getElevatorId() {
		return m_elevatorId;
	}

	public boolean isDoorOpen() {
		return m_doorOpen;
	}

	public int getCurrentFloor() {
		return (int) m_currentFloor;
	}

	public ElevatorDirection getElevatorMotion() {
		return m_elevatorMotion;
	}

	/**
	 * Adds a request to go to a certain floor
	 * 
	 * @param floor
	 */
	public boolean userRequestFloor(int floor) {
		if (m_currentFloor == floor && m_elevatorMotion == null) {
			// we are already on the floor and the elevator is still
			return false;
		}
		FloorRequestPosition floorRequestPosition = getFloorRequestPosition(floor, floor > m_currentFloor ? ElevatorDirection.UP
				: ElevatorDirection.DOWN);
		m_pendingFloors.add(floorRequestPosition.getNumberStops(), new FloorRequestTime(floor, System.currentTimeMillis()));
		return true;
	}

	/**
	 * Returns the number of stops it will take before getting to that floor and
	 * the amount of floors it will travel to get there
	 * 
	 * @param floor
	 * @return
	 */
	public FloorRequestPosition getFloorRequestPosition(int floor, ElevatorDirection direction) {
		// no requests == this is the next stop
		if (m_pendingFloors.isEmpty()) {
			return new FloorRequestPosition(0, (int) Math.abs(m_currentFloor - floor), false);
		}
		// if floor is already requested return that amount
		int totalDistance = 0;
		int lastFloor = (int) m_currentFloor;
		for (int i = 0; i < m_pendingFloors.size(); i++) {
			if (m_pendingFloors.get(i).m_floor == floor) {
				return new FloorRequestPosition(i, totalDistance, true);
			}
			totalDistance += Math.abs(m_pendingFloors.get(i).m_floor - lastFloor);
			lastFloor = m_pendingFloors.get(i).m_floor;
		}

		// if one floor either add before or after it
		if (m_pendingFloors.size() == 1) {
			int nextFloor = m_pendingFloors.get(0).m_floor;
			// are we going up and I'm ahead of the next floor?
			if (isFloorBetween((int) m_currentFloor, nextFloor, floor)) {
				return new FloorRequestPosition(0, (int) Math.abs(m_currentFloor - floor), false);
			} else {
				// either we're past the next floor or in the opposite direction
				return new FloorRequestPosition(1, (int) Math.abs(m_currentFloor - nextFloor) + Math.abs(nextFloor - floor), false);
			}
		}
		// to avoid the case of the elevator going up and a person requesting a
		// down floor preempting a request to
		// go to a higher floor their request is put at the end of the queue
		totalDistance = 0;
		int prevFloor = (int) m_currentFloor;
		for (int i = 0; i < m_pendingFloors.size(); i++) {
			int nextFloor = m_pendingFloors.get(i).m_floor;
			if (isFloorBetween(prevFloor, nextFloor, floor)) {
				return new FloorRequestPosition(i, totalDistance, false);
			}
			totalDistance += Math.abs(nextFloor - prevFloor);
			prevFloor = nextFloor;
		}
		return new FloorRequestPosition(m_pendingFloors.size(), totalDistance, false);
	}

	private boolean isFloorBetween(int floorPrev, int floorNext, int goalFloor) {
		if (floorPrev < goalFloor && goalFloor < floorNext) {
			return true;
		}
		if (floorPrev > goalFloor && goalFloor > floorNext) {
			return true;
		}
		return false;
	}

	public List<ElevatorStop> getElevatorStops() {
		List<ElevatorStop> ret = new ArrayList<ElevatorStop>();
		int stopCount = 0;
		int totalDistance = 0;
		int lastFloor = (int) m_currentFloor;
		for (FloorRequestTime floorTime : m_pendingFloors) {
			ret.add(new ElevatorStop(floorTime.m_floor, System.currentTimeMillis() - floorTime.m_requestTime, stopCount++, totalDistance));
			totalDistance += Math.abs(floorTime.m_floor - lastFloor);
			lastFloor = floorTime.m_floor;
		}
		return ret;
	}

	public void advance(double numberFloors) {
		// did we cross over from one floor to another?
		if (ElevatorDirection.DOWN == m_elevatorMotion) {
			numberFloors = -numberFloors;
		}
		int currentFloorInt = (int) m_currentFloor;
		int nextFloorInt = (int) (m_currentFloor + numberFloors);
		if (currentFloorInt != nextFloorInt) {
			// check if we need to stop here
			if (!m_pendingFloors.isEmpty() && m_pendingFloors.get(0).m_floor == nextFloorInt) {
				m_pendingFloors.remove(0);
				m_doorOpen = true;
				m_currentFloor = nextFloorInt;
				return;
			}
		}
		if (!m_pendingFloors.isEmpty()) {
			m_currentFloor += numberFloors;
		}
	}

	private static final class FloorRequestTime {

		private final int m_floor;
		private final long m_requestTime;

		private FloorRequestTime(int floor, long requestTime) {
			m_floor = floor;
			m_requestTime = requestTime;
		}

	}
}
