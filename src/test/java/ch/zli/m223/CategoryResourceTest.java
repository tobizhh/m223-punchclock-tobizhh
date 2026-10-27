package ch.zli.m223;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

@QuarkusTest
public class CategoryResourceTest {

    @Test
    public void testCategoryEndpoints() {
        Long id = given()
          .contentType("application/json")
          .body("""
              {"title":"Projekt"}
              """)
          .when().post("/categories")
          .then()
             .statusCode(200)
             .body("title", is("Projekt"))
             .extract().jsonPath().getLong("id");

        given()
          .when().get("/categories/" + id)
          .then()
             .statusCode(200)
             .body("id", is(id.intValue()))
             .body("title", is("Projekt"));

        given()
          .contentType("application/json")
          .body("""
              {"title":"Schule"}
              """)
          .when().put("/categories/" + id)
          .then()
             .statusCode(200)
             .body("title", is("Schule"));

        given()
          .when().get("/categories")
          .then()
             .statusCode(200)
             .body("find { it.id == %d }.title".formatted(id), is("Schule"));

        given()
          .contentType("application/json")
          .body("""
              {"title":""}
              """)
          .when().post("/categories")
          .then()
             .statusCode(400);

        given()
          .when().delete("/categories/" + id)
          .then()
             .statusCode(204);

        given()
          .when().get("/categories/" + id)
          .then()
             .statusCode(404);

        given()
          .when().delete("/categories/" + id)
          .then()
             .statusCode(404);
    }

    @Test
    public void testEntryWithCategory() {
        Long categoryId = given()
          .contentType("application/json")
          .body("""
              {"title":"Projekt"}
              """)
          .when().post("/categories")
          .then()
             .statusCode(200)
             .extract().jsonPath().getLong("id");

        Long entryId = given()
          .contentType("application/json")
          .body("""
              {"checkIn":"2026-09-29T08:00:00","checkOut":"2026-09-29T17:00:00","categoryId":%d}
              """.formatted(categoryId))
          .when().post("/entries")
          .then()
             .statusCode(200)
             .body("categoryId", is(categoryId.intValue()))
             .extract().jsonPath().getLong("id");

        given()
          .contentType("application/json")
          .body("""
              {"checkIn":"2026-09-29T08:00:00","checkOut":"2026-09-29T17:00:00","categoryId":999999}
              """)
          .when().post("/entries")
          .then()
             .statusCode(404);

        given()
          .when().delete("/categories/" + categoryId)
          .then()
             .statusCode(204);

        given()
          .when().get("/entries")
          .then()
             .statusCode(200)
             .body("find { it.id == %d }.categoryId".formatted(entryId), nullValue());

        given()
          .when().delete("/entries/" + entryId)
          .then()
             .statusCode(204);
    }
}
