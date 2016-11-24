package com.goeuro;

import com.rudolfschmidt.alkun.Request;
import com.rudolfschmidt.alkun.Response;
import com.rudolfschmidt.alkun.Route;

import javax.json.Json;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The Bus Route
 */
public class BusRoute implements Route {

	/**
	 * The path to the bus file
	 */
	private final Path filePath;

	/**
	 * Creates a Bus Route
	 *
	 * @param filePath the path to the bus file
	 */
	public BusRoute(Path filePath) {
		this.filePath = filePath;
	}

	@Override
	public void process(Request request, Response response) throws Exception {

		// the dep_sid query integer
		String departureId = request.queries(Consts.DEP_SID).orElseThrow(IllegalArgumentException::new);
		// the arr_sid query integer
		String arrivalId = request.queries(Consts.ARR_SID).orElseThrow(IllegalArgumentException::new);

		// if a bus connection exists
		boolean directExists = isDirectionConnection(departureId, arrivalId);

		// the sever response
		String jsonResponse = getServerResponse(departureId, arrivalId, directExists);

		// send a response
		response.send(jsonResponse);
	}

	/**
	 * Create the server response
	 *
	 * @param departureId the departure station id
	 * @param arrivalId the arrival station id
	 * @param directExists if a bus connection exists
	 * @return the json string that the server response
	 */
	private String getServerResponse(String departureId, String arrivalId, boolean directExists) {
		return Json.createObjectBuilder()
				.add(Consts.DEP_SID, departureId)
				.add(Consts.ARR_SID, arrivalId)
				.add(Consts.DIRECT_BUS_ROUTE, directExists)
				.build()
				.toString();
	}

	/**
	 * @param departureId the departure station id
	 * @param arrivalId the arrival station id
	 * @return if a connection exists, true will be returned, otherwise false
	 * @throws IOException if can not read file from system
	 */
	private boolean isDirectionConnection(String departureId, String arrivalId) throws IOException {
		return Files.lines(filePath)
				// skip number of bus routes
				.skip(1)
				.anyMatch(line -> {
					List<String> stationIds = Stream.of(line.split("\\s"))
							// skip route id
							.skip(1)
							.collect(Collectors.toList());
					int dep_sid_index = stationIds.indexOf(departureId);
					int arr_sid_index = stationIds.indexOf(arrivalId);
					return dep_sid_index > -1 && arr_sid_index > -1 && dep_sid_index < arr_sid_index;
				});
	}
}
