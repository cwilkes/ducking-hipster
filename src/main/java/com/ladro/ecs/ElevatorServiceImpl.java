package com.ladro.ecs;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class ElevatorServiceImpl implements ElevatorService {

	private final Map<Integer, Elevator> m_elevators;
	private final double m_elevatorSpeed;
	private final double m_elevatorWaitOpen;
	private final Comparator<Pair<Integer, FloorRequestPosition>> m_elevatorTimeCompare;

	public ElevatorServiceImpl(List<Elevator> elevators, double elevatorSpeed, double elevatorWaitOpen) {
		m_elevatorSpeed = elevatorSpeed;
		m_elevatorWaitOpen = elevatorWaitOpen;
		m_elevators = new HashMap<Integer, Elevator>();
		for (Elevator elevator : elevators) {
			m_elevators.put(elevator.getCurrentFloor(), elevator);
		}
		m_elevatorTimeCompare = new Comparator<Pair<Integer, FloorRequestPosition>>() {

			public int compare(Pair<Integer, FloorRequestPosition> o1, Pair<Integer, FloorRequestPosition> o2) {
				double time1 = o1.getSecond().getDistance() * m_elevatorSpeed + o1.getSecond().getNumberStops() * m_elevatorWaitOpen;
				double time2 = o2.getSecond().getDistance() * m_elevatorSpeed + o2.getSecond().getNumberStops() * m_elevatorWaitOpen;
				if (time1 < time2) {
					return -1;
				}
				if (time1 > time2) {
					return 1;
				}
				return 0;
			}
		};
	}

	public void requestPickup(int floor, ElevatorDirection direction) {
		// first pass: what elevator can get there the quickest?
		PriorityQueue<Pair<Integer, FloorRequestPosition>> byDistance = new PriorityQueue<Pair<Integer, FloorRequestPosition>>(m_elevators.size(),
				m_elevatorTimeCompare);
		for (Elevator elevator : m_elevators.values()) {
			byDistance.add(new Pair<Integer, FloorRequestPosition>(elevator.getElevatorId(), elevator.getFloorRequestPosition(floor, direction)));
		}
		Pair<Integer, FloorRequestPosition> closestElevator = byDistance.poll();
		m_elevators.get(closestElevator.getFirst()).userRequestFloor(floor);
	}

	public void step() {
		for (Elevator elevator : m_elevators.values()) {
			elevator.advance(m_elevatorSpeed);
		}
	}

	public Map<Integer, Integer> bankStatus() {
		Map<Integer, Integer> ret = new HashMap<Integer, Integer>();
		for (Elevator elevator : m_elevators.values()) {
			ret.put(elevator.getElevatorId(), elevator.getCurrentFloor());
		}
		return ret;
	}

	public Elevator getElevator(int elevatorId) {
		return m_elevators.get(elevatorId);
	}
}
