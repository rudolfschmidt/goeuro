package com.goeuro;

import com.rudolfschmidt.alkun.Request;
import com.rudolfschmidt.alkun.Response;
import com.rudolfschmidt.alkun.Route;

import javax.json.Json;
import javax.json.JsonObject;
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

	public BusRoute(Path filePath) {
		this.filePath = filePath;
	}

	@Override
	public void process(Request request, Response response) throws Exception {

		// the dep_sid query integer
		String dep_sid = request.queries(Consts.DEP_SID).orElseThrow(IllegalArgumentException::new);
		// the arr_sid query integer
		String arr_sid = request.queries(Consts.ARR_SID).orElseThrow(IllegalArgumentException::new);

		// if a bus connection exists
		boolean directExists = Files.lines(filePath)
				// skip number of bus routes
				.skip(1)
				.anyMatch(line -> {
					List<String> stationIds = Stream.of(line.split("\\s"))
							// skip route id
							.skip(1)
							.collect(Collectors.toList());
					int dep_sid_index = stationIds.indexOf(dep_sid);
					int arr_sid_index = stationIds.indexOf(arr_sid);
					return dep_sid_index > -1 && arr_sid_index > -1 && dep_sid_index < arr_sid_index;
				});

		// the sever response
		JsonObject jsonModel = Json.createObjectBuilder()
				.add(Consts.DEP_SID, dep_sid)
				.add(Consts.ARR_SID, arr_sid)
				.add(Consts.DIRECT_BUS_ROUTE, directExists)
				.build();

		// convert to json string
		String jsonResponse = jsonModel.toString();

		// send a response
		response.send(jsonResponse);
	}
}
