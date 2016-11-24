package com.goeuro;

import com.rudolfschmidt.alkun.Request;
import com.rudolfschmidt.alkun.Response;
import com.rudolfschmidt.alkun.Route;

import javax.json.Json;
import java.nio.file.Path;

/**
 * The Bus Route
 */
public class BusRoute implements Route {

	/**
	 * The path to the bus file
	 */
	private final Path filePath;
	/**
	 * Direction Service
	 */
	private final DirectionService directionService;

	/**
	 * Creates a Bus Route
	 *
	 * @param filePath         the path to the bus file
	 * @param directionService
	 */
	public BusRoute(Path filePath, DirectionService directionService) {
		this.filePath = filePath;
		this.directionService = directionService;
	}

	@Override
	public void process(Request request, Response response) throws Exception {

		// the dep_sid query integer
		String departureId = request.queries(Consts.DEP_SID).orElseThrow(IllegalArgumentException::new);
		// the arr_sid query integer
		String arrivalId = request.queries(Consts.ARR_SID).orElseThrow(IllegalArgumentException::new);

		// if a bus connection exists
		boolean directExists = directionService.isDirectionConnection(filePath, departureId, arrivalId);

		// the sever response
		String jsonResponse = getServerResponse(departureId, arrivalId, directExists);

		// send a response
		response.send(jsonResponse);
	}

	/**
	 * Create the server response
	 *
	 * @param departureId  the departure station id
	 * @param arrivalId    the arrival station id
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

}
