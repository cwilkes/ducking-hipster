package com.ladro.ecs;

public class Pair<T, E> {

	private final T m_first;
	private final E m_second;

	public Pair(T first, E second) {
		m_first = first;
		m_second = second;
	}

	public T getFirst() {
		return m_first;
	}

	public E getSecond() {
		return m_second;
	}
}
