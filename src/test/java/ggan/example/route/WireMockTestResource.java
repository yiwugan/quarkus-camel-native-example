package ggan.example.route;

import java.util.Map;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class WireMockTestResource implements QuarkusTestResourceLifecycleManager {
	private WireMockServer mockServer;

	@Override
	public Map<String, String> start() {
		mockServer = new WireMockServer(options().dynamicPort());
		mockServer.stubFor(get(urlEqualTo("/")).atPriority(1).willReturn(
				aResponse().withStatus(200).withHeader("Content-Type", "text/html").withBody("mock result")));

		mockServer.start();
		// pass test properties
		return Map.of("badssl-untrusted.endpoint",
				"http://localhost:" + mockServer.port() + "/");
	}

	@Override
	public void stop() {
		if (mockServer != null)
			mockServer.stop();
	}

}
