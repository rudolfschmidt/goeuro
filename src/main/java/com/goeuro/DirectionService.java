package com.goeuro;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirectionService {

	/**
	 * @param filePath
	 * @param departureId the departure station id
	 * @param arrivalId   the arrival station id
	 * @return if a connection exists, true will be returned, otherwise false
	 * @throws IOException if can not read file from system
	 */
	public boolean isDirectionConnection(Path filePath, String departureId, String arrivalId) throws IOException {
		return Files.lines(filePath)
				// skip number of bus routes
				.skip(1)
				.anyMatch(str -> isConnected(str, Integer.valueOf(departureId), Integer.valueOf(arrivalId)));
	}

	/**
	 * @param str
	 * @param departureId the departure station id
	 * @param arrivalId   the arrival station id
	 * @return if a connection exists, true will be returned, otherwise false
	 */
	boolean isConnected(String str, int departureId, int arrivalId) {
		List<String> stationIds = Stream.of(str.split("\\s"))
				// skip route id
				.skip(1)
				.collect(Collectors.toList());
		int dep_sid_index = stationIds.indexOf(String.valueOf(departureId));
		int arr_sid_index = stationIds.indexOf(String.valueOf(arrivalId));
		return dep_sid_index > -1 && arr_sid_index > -1 && dep_sid_index < arr_sid_index;
	}
}
