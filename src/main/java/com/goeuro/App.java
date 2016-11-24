package com.goeuro;

import com.rudolfschmidt.alkun.Alkun;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Contains entry point method of application
 */
public class App {

	/**
	 * Entry point method of application
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {

		if (args.length < 1) {
			throw new IllegalArgumentException(Consts.NO_PATH_MESSAGE);
		}

		String pathArgument = args[0];
		Path filePath = Paths.get(pathArgument);

		// create http server
		Alkun app = Alkun.newInstance();

		app
				// define route http server will handle
				.route(Consts.ROUTE)

				// define the handler and the route method
				.get(new BusRoute(filePath));

		// start and let http server listen to port
		app.listen(Consts.PORT);


	}
}
