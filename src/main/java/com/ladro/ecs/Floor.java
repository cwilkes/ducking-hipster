package com.ladro.ecs;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Floor {

	private static final Long NO_PENDING_REQUEST_TIME = -1l;

	private final Logger m_logger = LogManager.getLogManager().getLogger(getClass().getName());
	private final int m_floorNumber;
	private final Map<ElevatorDirection, Long> m_directionRequestTimes;

	public Floor(int floorNumber) {
		m_floorNumber = floorNumber;
		m_directionRequestTimes = new HashMap<ElevatorDirection, Long>();
		m_directionRequestTimes.put(ElevatorDirection.UP, NO_PENDING_REQUEST_TIME);
		m_directionRequestTimes.put(ElevatorDirection.DOWN, NO_PENDING_REQUEST_TIME);
	}

	public void registerRequest(ElevatorDirection directionRequest) {
		if (m_directionRequestTimes.get(directionRequest) == NO_PENDING_REQUEST_TIME) {
			// we've already requested an elevator and it hasn't come yet
		} else {
			m_directionRequestTimes.put(directionRequest, System.currentTimeMillis());
		}
	}

	public void registerElevatorArrived(int elevatorId, ElevatorDirection elevatorDirection) {
		long callTime = m_directionRequestTimes.put(elevatorDirection, NO_PENDING_REQUEST_TIME);
		if (callTime != NO_PENDING_REQUEST_TIME) {
			long timeForElevatorMilliseconds = System.currentTimeMillis() - callTime;
			m_logger.info(String.format("Took %d seconds for elevator %d to arrive on floor %d after request to go %s",
					timeForElevatorMilliseconds / 1000, elevatorId, m_floorNumber, elevatorDirection));
		}
	}

	public Map<ElevatorDirection, Long> getPendingRequests() {
		Map<ElevatorDirection, Long> ret = new HashMap<ElevatorDirection, Long>();
		for (Entry<ElevatorDirection, Long> me : m_directionRequestTimes.entrySet()) {
			if (me.getValue() != NO_PENDING_REQUEST_TIME) {
				ret.put(me.getKey(), System.currentTimeMillis() - me.getValue());
			}
		}
		return ret;
	}
}
