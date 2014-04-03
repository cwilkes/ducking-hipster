package com.ladro.ecs;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ElevatorServiceImplTest {
	@Test
	public void testSetup() {
		ElevatorServiceImpl elevatorService = makeElevatorService(1);
		// only one elevator
		assertEquals(1, elevatorService.bankStatus().size());
		// and it is on the first floor
		assertEquals(0, elevatorService.bankStatus().get(0).intValue());
	}
	
	@Test
	public void testMoveOneFloor() {
		ElevatorServiceImpl elevatorService = makeElevatorService(1);
		elevatorService.requestPickup(10, ElevatorDirection.DOWN);		
		Elevator elevator = elevatorService.getElevator(0);
		System.out.println("Stops: " + elevator.getElevatorStops());
		for (int i = 0; i < 110; i++) {
			elevatorService.step();
			System.out.println(String.format("Step %d, elevator floor %d", i, elevatorService.bankStatus().values().iterator().next()));
		}
		// now go to the fifth floor
		elevator.userRequestFloor(5);
		System.out.println("Stops: " + elevator.getElevatorStops());
		for (int i = 0; i < 110; i++) {
			elevatorService.step();
			System.out.println(String.format("Step %d, elevator floor %d", i, elevatorService.bankStatus().values().iterator().next()));
		}
	}

	private ElevatorServiceImpl makeElevatorService(int numberElevators) {
		List<Elevator> elevators = new ArrayList<Elevator>();
		for (int i = 0; i < numberElevators; i++) {
			elevators.add(new Elevator(i));
		}
		return new ElevatorServiceImpl(elevators, 0.1, 100.0);
	}
}
