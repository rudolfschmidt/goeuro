package com.goeuro;

import com.rudolfschmidt.alkun.Alkun;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {

	private static final String NO_PATH_MESSAGE = "no path provided";
	private static final String ROUTE = "/api/direct";
	private static final String DEP_SID = "dep_sid";
	private static final String ARR_SID = "arr_sid";
	private static final String DIRECT_BUS_ROUTE = "direct_bus_route";

	public static void main(String[] args) throws IOException {

		if (args.length < 1) {
			throw new IllegalArgumentException(NO_PATH_MESSAGE);
		}

		String pathArgument = args[0];
		Path filePath = Paths.get(pathArgument);

		Alkun app = Alkun.newInstance();
		app.route(ROUTE)
				.get((req, res) -> {

					String dep_sid = req.queries(DEP_SID)
							.orElseThrow(IllegalArgumentException::new);
					String arr_sid = req.queries(ARR_SID)
							.orElseThrow(IllegalArgumentException::new);

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

					JsonObject jsonModel = Json.createObjectBuilder()
							.add(DEP_SID, dep_sid)
							.add(ARR_SID, arr_sid)
							.add(DIRECT_BUS_ROUTE, directExists)
							.build();

					String json = jsonModel.toString();

					res.send(json);
				});
		app.listen(8088);
	}
}
