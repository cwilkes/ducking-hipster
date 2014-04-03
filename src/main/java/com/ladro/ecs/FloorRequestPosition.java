package com.ladro.ecs;

public class FloorRequestPosition {

	private final int m_numberStops;
	private final int m_distance;
	private final boolean m_alreadyScheduledToGo;

	public FloorRequestPosition(int numberStops, int distance, boolean alreadyScheduledToGo) {
		m_numberStops = numberStops;
		m_distance = distance;
		m_alreadyScheduledToGo = alreadyScheduledToGo;
	}

	public int getDistance() {
		return m_distance;
	}

	public int getNumberStops() {
		return m_numberStops;
	}

	public boolean isAlreadyScheduledToGo() {
		return m_alreadyScheduledToGo;
	}

}
