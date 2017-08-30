package com.eko.lieferando.assignment.util;

public class NumberEngine {

	public static int getNextNumber(int number) {
		if (number % 3 == 0) {
			System.out.println("Sending number --> (" + number + " + 0) / 3 : " + (number / 3));
			return number / 3;
		} else if (number % 3 == 1) {
			System.out.println("Sending number --> (" + number + " - 1) / 3 : " + ((number - 1) / 3));
			return (number - 1) / 3;
		}
		System.out.println("Sending number --> (" + number + " + 1) / 3 : " + ((number + 1) / 3));
		return (number + 1) / 3;
	}
}
