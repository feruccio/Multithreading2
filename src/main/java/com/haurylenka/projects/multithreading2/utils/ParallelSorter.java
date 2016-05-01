package com.haurylenka.projects.multithreading2.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.haurylenka.projects.multithreading2.exceptions.ParallelSortingException;

public class ParallelSorter {

	private static final int THREADS = 10;
	
	private ParallelSorter() {}
	
	public static void sortInts(int[] arr) {
		try {
			List<Sorter> sorters = init(arr);
			List<Thread> threads = sortParts(sorters);
			joinThreads(threads);
			mergeParts(sorters, arr);
		} catch (InterruptedException e) {
			throw new ParallelSortingException(
					"An unexpected interruption occurred");
		}
	}
	
	/*
	 * Collects all Sorters sorted pieces of the 
	 * initial array and updates the initial array
	 */
	private static void mergeParts(List<Sorter> sorters, int[] arr) {
		/*
		 * map of indexes of sorted arrays cells 
		 * last value taken from, for each sorted array
		 */
		Map<Integer, Integer> statuses = new HashMap<>();
		// multidimensional array of sorted arrays
		int[][] parts = new int[sorters.size()][];
		for (int i = 0; i < sorters.size(); i++) {
			statuses.put(i, 0);
			Sorter sorter = sorters.get(i);
			parts[i] = sorter.getSortedArr();
		}
		int i = 0;
		while (i < arr.length) {
			int min = Integer.MAX_VALUE;
			int minIdx = 0;
			for (int j = 0; j < parts.length; j++) {
				if (statuses.get(j) != parts[j].length) {
					/*
					 * searching for the least value 
					 * among values of sorted arrays
					 */
					int v = parts[j][statuses.get(j)];
					if (v < min) {
						min = v;
						minIdx = j;
					}
				}
			}
			statuses.put(minIdx, statuses.get(minIdx) + 1);
			arr[i] = min;
			i++;
		}
	}

	/*
	 * Joins all threads with the main one
	 */
	private static void joinThreads(List<Thread> threads) 
			throws InterruptedException {
		for (Thread thread : threads) {
			thread.join();
		}
	}

	/*
	 * Starts all Sorter threads
	 */
	private static List<Thread> sortParts(List<Sorter> sorters) {
		List<Thread> threads = new ArrayList<>();
		for (Sorter sorter : sorters) {
			Thread thread = new Thread(sorter);
			threads.add(thread);
			thread.start();
		}
		return threads;
	}

	/*
	 * Creates a list of Sorter instances, 
	 * breaking the initial array into parts 
	 * and distributing them between Sorters
	 */
	private static List<Sorter> init(int[] arr) {
		final int LENGTH = arr.length;
		final int STEP = LENGTH / THREADS;
		List<Sorter> sorters = new ArrayList<>();
		for (int i = 0; i < LENGTH; ) {
			int to = i + STEP;
			if (LENGTH - to < STEP) {
				to = LENGTH;
			}
			sorters.add(new Sorter(i, to, arr));
			i = to;
		}
		return sorters;
	}

	/*
	 * Takes a part of the specified array, 
	 * sorts it and stores the result
	 */
	private static class Sorter implements Runnable {

		private int from;
		private int to;
		private int[] arr;
		private int[] sortedArr;
		
		public Sorter(int from, int to, int[] arr) {
			this.from = from;
			this.to = to;
			this.arr = arr;
		}

		@Override
		public void run() {
			sortedArr = new int[to - from];
			System.arraycopy(arr, from, sortedArr, 0, to - from);
			Arrays.sort(sortedArr);
		}

		public int[] getSortedArr() {
			return sortedArr;
		}
		
	}
	
	
}
