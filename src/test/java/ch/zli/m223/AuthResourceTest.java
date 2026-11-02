package ch.zli.m223;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

@QuarkusTest
public class AuthResourceTest {

    @Test
    public void testLoginReturnsToken() {
        given()
          .contentType("application/json")
          .body("""
              {"username":"admin","password":"admin123"}
              """)
          .when().post("/auth/login")
          .then()
             .statusCode(200)
             .body("token", notNullValue());
    }

    @Test
    public void testLoginWithWrongPasswordReturns401() {
        given()
          .contentType("application/json")
          .body("""
              {"username":"admin","password":"wrong"}
              """)
          .when().post("/auth/login")
          .then()
             .statusCode(401);
    }

    @Test
    public void testLoginWithUnknownUserReturns401() {
        given()
          .contentType("application/json")
          .body("""
              {"username":"nobody","password":"admin123"}
              """)
          .when().post("/auth/login")
          .then()
             .statusCode(401);
    }

    @Test
    public void testSecureEndpoint() {
        given()
          .when().get("/secure/data")
          .then()
             .statusCode(401);

        given()
          .header("Authorization", "Bearer " + login("user", "user123"))
          .when().get("/secure/data")
          .then()
             .statusCode(403);

        given()
          .header("Authorization", "Bearer " + login("admin", "admin123"))
          .when().get("/secure/data")
          .then()
             .statusCode(200)
             .body(is("This is protected data for admin."));
    }

    @Test
    public void testInvalidTokenReturns401() {
        given()
          .header("Authorization", "Bearer not.a.token")
          .contentType("application/json")
          .body("""
              {"checkIn":"2026-09-29T08:00:00","checkOut":"2026-09-29T17:00:00"}
              """)
          .when().post("/entries")
          .then()
             .statusCode(401);
    }

    @Test
    public void testEntryIsAssignedToUserFromToken() {
        Long entryId = given()
          .header("Authorization", "Bearer " + login("user", "user123"))
          .contentType("application/json")
          .body("""
              {"checkIn":"2026-09-29T08:00:00","checkOut":"2026-09-29T17:00:00"}
              """)
          .when().post("/entries")
          .then()
             .statusCode(200)
             .body("userId", notNullValue())
             .extract().jsonPath().getLong("id");

        Long anonymousEntryId = given()
          .contentType("application/json")
          .body("""
              {"checkIn":"2026-09-30T08:00:00","checkOut":"2026-09-30T12:00:00"}
              """)
          .when().post("/entries")
          .then()
             .statusCode(200)
             .body("userId", nullValue())
             .extract().jsonPath().getLong("id");

        given().when().delete("/entries/" + entryId).then().statusCode(204);
        given().when().delete("/entries/" + anonymousEntryId).then().statusCode(204);
    }

    private String login(String username, String password) {
        return given()
          .contentType("application/json")
          .body("""
              {"username":"%s","password":"%s"}
              """.formatted(username, password))
          .when().post("/auth/login")
          .then()
             .statusCode(200)
             .extract().jsonPath().getString("token");
    }
}
