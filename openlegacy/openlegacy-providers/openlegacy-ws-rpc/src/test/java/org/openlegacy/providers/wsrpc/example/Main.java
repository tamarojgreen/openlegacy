package org.openlegacy.providers.wsrpc.example;

import javax.xml.ws.Endpoint;

public class Main {

	private static int servicePort = 8080;
	private static boolean running = false;
	private static Endpoint[] endpoints = new Endpoint[2];
	private static Object[] services = new Object[2];

	public static void main(String args[]) {
		services[0] = new SimpleWebService();
		services[1] = new NewWebService();
		try {
			servicePort = Integer.valueOf(args[0]).intValue();
		} catch (Exception exception) {
		}

		publish();

		if (System.console() == null) {
			return;
		}

		String param = "";
		while (true) {
			System.out.print("command:");
			param = System.console().readLine();

			if (param.equals("publish")) {
				publish();
			}

			if (param.equals("stop")) {
				stop();
			}

			if (param.equals("port")) {
				try {
					System.out.print("port:");
					servicePort = Integer.valueOf(System.console().readLine()).intValue();
					stop();
					publish();
				} catch (Exception e) {
				}
			}

			if (param.equals("exit")) {
				stop();
				break;
			}
		}
	}

	public static void publish() {
		if (running) {
			return;
		}

		for (int i = 0; i < endpoints.length; i++) {
			endpoints[i] = Endpoint.create(services[i]);
			try {
				String path = String.format("http://localhost:%d/%s", servicePort, services[i].getClass().getSimpleName());
				endpoints[i].publish(path);
				System.out.println(String.format("Service was started at: %s", path));
				running = true;
			} catch (Exception e) {
				e.printStackTrace();
				System.console().readLine();
				return;
			}
		}
	}

	public static void stop() {
		if (!running) {
			return;
		}

		for (int i = 0; i < endpoints.length; i++) {
			endpoints[i].stop();
		}

		System.out.println("Service was stopped");
		running = false;
	}

}
