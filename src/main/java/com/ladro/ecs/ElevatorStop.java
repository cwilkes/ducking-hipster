package com.ladro.ecs;

public class ElevatorStop {

	private final int m_floor;
	private final long m_timeSinceRequest;
	private final int m_stopCount;
	private final int m_totalDistance;

	public ElevatorStop(int floor, long timeSinceRequest, int stopCount, int totalDistance) {
		m_floor = floor;
		m_timeSinceRequest = timeSinceRequest;
		m_stopCount = stopCount;
		m_totalDistance = totalDistance;
	}

	public int getFloor() {
		return m_floor;
	}

	public long getTimeSinceRequest() {
		return m_timeSinceRequest;
	}

	public int getStopCount() {
		return m_stopCount;
	}

	public int getTotalDistance() {
		return m_totalDistance;
	}
	@Override
	public String toString() {
		return String.format("Floor: %d, Time: %d, Stop Count: %d, Distance: %d", m_floor, m_timeSinceRequest, m_stopCount, m_totalDistance);
	}
}
