package ggan.example.route;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
@QuarkusTestResource(WireMockTestResource.class)
class RoutesTest {
	

	@Test
	void testGetSuccess200() throws Exception {
		System.setProperty("token.validation.unit-test.validate", "pass");
		Response response = given()
				.header("Content-type", "application/json")
				.when()
				.get("/apis/badssl/v1/untrustedroot")
				.then()
				.extract()
				.response();
		Assertions.assertEquals(200, response.statusCode());
	}

	
}
