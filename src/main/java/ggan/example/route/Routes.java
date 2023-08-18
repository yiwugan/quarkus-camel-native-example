package ggan.example.route;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestParamType;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * 
 * @author ggan 202308
 *
 */
@RegisterForReflection(targets = { java.lang.Exception.class, com.fasterxml.jackson.core.JsonParseException.class })
@ApplicationScoped
public class Routes extends RouteBuilder {

	public Routes() {
	}

	@ConfigProperty(name = "badssl-untrusted.endpoint")
	String badsslUntrustedEndpoint;

	@Override
	public void configure() throws Exception {
		// JsonParseException error
		onException(com.fasterxml.jackson.core.JsonParseException.class).handled(true)
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
				.setHeader("Content-Type", constant("application/json"))
				.setBody(simple("{\"error\":\"invalid_request\",\"error_description\":\"unable to parse json\"}"))
				.log(LoggingLevel.ERROR, "json exception: ${exception}");

		// General Exception error
		onException(Exception.class).handled(true).maximumRedeliveries(0)
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
				.setHeader("Content-Type", constant("application/json"))
				.setBody(simple("{\"error\":500,\"error_description\":\"Exception: ${exception}\"}"))
				.log(LoggingLevel.ERROR, "general exception: ${exception}");

		interceptFrom("rest*").log("rest endpoint received request ${body}");

		// rest config
		restConfiguration().enableCORS(true).apiContextPath("/apis/badssl/v1/api-doc")
				.apiProperty("api.title", "example api to call badssl web endpoint with trust store")
				.apiProperty("api.contact.name", "yiwugan@gmail.com")
				.apiProperty("api.description", "example api to call badssl web endpoint with trust store")
				.apiProperty("api.contact.email", "").apiProperty("api.contact.url", "https://github.com/yiwugan")
				.apiProperty("api.version", "1.0").apiProperty("host", "").apiProperty("port", "")
				.dataFormatProperty("prettyPrint", "true");

		// rest post
		rest("/apis/badssl/v1")
				.id("quarkus-camel-example-rest-route")
				.consumes("application/json")
				.produces("text/html")
				// post method
				.get("/untrustedroot")
					.description("get badssl untrusted web page")
					.param()
						.name("body")
						.type(RestParamType.body)
						.description("JSON Message")
					.endParam()
					// 200 ok
					.responseMessage().code(201).message("Success").endResponseMessage()
					// 400 client error
					.responseMessage().code(400).message("Invalid request").endResponseMessage()
					// 500 Internal server error
					.responseMessage().code(500).message("Internal server error").endResponseMessage()
					// to
					.to("direct:badssl-untrusted");

		from("direct:badssl-untrusted")
				.id("badssl-untrusted-route")
				.removeHeaders("camel*")
				.log(LoggingLevel.INFO,
						"badssl-untrusted-route before calling badssl untrusted " + badsslUntrustedEndpoint)
				.to(badsslUntrustedEndpoint)
				.log(LoggingLevel.INFO, "badssl-untrusted-route after calling badssl untrusted")
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant("200"))
				.setHeader(Exchange.CONTENT_TYPE, constant("text/html"));
	}
}
