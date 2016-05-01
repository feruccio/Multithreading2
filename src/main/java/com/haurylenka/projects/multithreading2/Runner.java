package com.haurylenka.projects.multithreading2;

import java.util.Random;

import org.apache.log4j.Logger;

import com.haurylenka.projects.multithreading2.utils.ParallelSorter;

public class Runner {
	
	private static final Logger LOGGER = Logger.getLogger(Runner.class);
	private static final int LENGTH = 10_000_000;
	private static final long SEED = 1111L;

	public static void main(String[] args) {
		// creating an array of random ints
		int[] arr = new int[LENGTH];
		Random r = new Random(SEED);
		for (int i = 0; i < LENGTH; i++) {
			arr[i] = r.nextInt();
		}
		long begin = System.currentTimeMillis();
		// parallel sorting
		ParallelSorter.sortInts(arr);
		long end = System.currentTimeMillis();
		LOGGER.info(end - begin);
	}

}
