package com.smartgarden;

import org.junit.jupiter.api.Test;

import io.quarkus.test.hibernate.reactive.panache.TransactionalUniAsserter;
import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@QuarkusTest
public class THPReadingResourceTest {
	
	@Test
	public void testGetLatestReadings(TransactionalUniAsserter asserter) {
	   //There are initial readings in the table
		given()
                .when()
                .get("/thp")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body(containsString("\"temperature\":"));
      //Create a reading:
		LocalDateTime date = LocalDateTime.now();
		  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		  String dateStr = date.format(formatter);
		  String postReadingBody = String.format(
				  "{\"temperature\": \"19.4\", \"pressure\": \"959.78\", \"humidity\": \"73.5\", \"created\": \"%s\"}", dateStr);
		  String postReadingResponseBody = String.format(
				  "{\"id\":1,\"created\":\"%s\",\"temperature\":19.4,\"humidity\":73.5,\"pressure\":959.78}", dateStr);
				  ;
		  String getLatestReadingResponseBody = String.format(
				  "[{\"id\":1,\"created\":\"" + dateStr + "\",\"temperature\":19.4,\"humidity\":73.5,\"pressure\":959.78}]", dateStr);
        given()
                .when()
                .body(postReadingBody)
                .contentType("application/json")
                .post("/thp")
                .then()
                .statusCode(201)
                .body(containsString(postReadingResponseBody));
      //Retrieve one, the latest reading with the usage of custom getLatestReadings() method:
        given()
                .when()
                .get("/thp/data/1")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body(containsString(getLatestReadingResponseBody));
	}
}
