package ch.zli.m223;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class TagResourceTest {

    @Test
    public void testTagEndpoints() {
        Long id = given()
          .contentType("application/json")
          .body("""
              {"title":"wichtig"}
              """)
          .when().post("/tags")
          .then()
             .statusCode(200)
             .body("title", is("wichtig"))
             .extract().jsonPath().getLong("id");

        given()
          .when().get("/tags/" + id)
          .then()
             .statusCode(200)
             .body("id", is(id.intValue()))
             .body("title", is("wichtig"));

        given()
          .contentType("application/json")
          .body("""
              {"title":"dringend"}
              """)
          .when().put("/tags/" + id)
          .then()
             .statusCode(200)
             .body("title", is("dringend"));

        given()
          .when().get("/tags")
          .then()
             .statusCode(200)
             .body("find { it.id == %d }.title".formatted(id), is("dringend"));

        given()
          .contentType("application/json")
          .body("""
              {"title":""}
              """)
          .when().post("/tags")
          .then()
             .statusCode(400);

        given()
          .contentType("application/json")
          .body("{\"title\":\"%s\"}".formatted("x".repeat(256)))
          .when().post("/tags")
          .then()
             .statusCode(400);

        given()
          .when().delete("/tags/" + id)
          .then()
             .statusCode(204);

        given()
          .when().get("/tags/" + id)
          .then()
             .statusCode(404);

        given()
          .when().delete("/tags/" + id)
          .then()
             .statusCode(404);
    }

    @Test
    public void testEntryWithTags() {
        Long firstTagId = createTag("Meeting");
        Long secondTagId = createTag("Remote");

        Long entryId = given()
          .contentType("application/json")
          .body("""
              {"checkIn":"2026-09-29T08:00:00","checkOut":"2026-09-29T17:00:00","tagIds":[%d,%d]}
              """.formatted(firstTagId, secondTagId))
          .when().post("/entries")
          .then()
             .statusCode(200)
             .body("tagIds.size()", is(2))
             .body("tagIds[0]", is(firstTagId.intValue()))
             .body("tagIds[1]", is(secondTagId.intValue()))
             .extract().jsonPath().getLong("id");

        given()
          .contentType("application/json")
          .body("""
              {"checkIn":"2026-09-29T08:00:00","checkOut":"2026-09-29T17:00:00","tagIds":[999999]}
              """)
          .when().post("/entries")
          .then()
             .statusCode(404);

        given()
          .contentType("application/json")
          .body("""
              {"checkIn":"2026-09-29T08:00:00","checkOut":"2026-09-29T17:00:00","tagIds":[null]}
              """)
          .when().post("/entries")
          .then()
             .statusCode(400);

        given()
          .contentType("application/json")
          .body("""
              {"checkIn":"2026-09-29T08:00:00","checkOut":"2026-09-29T17:00:00","tagIds":[%d]}
              """.formatted(secondTagId))
          .when().put("/entries/" + entryId)
          .then()
             .statusCode(200)
             .body("tagIds.size()", is(1))
             .body("tagIds[0]", is(secondTagId.intValue()));

        given()
          .when().delete("/tags/" + secondTagId)
          .then()
             .statusCode(204);

        given()
          .when().get("/entries")
          .then()
             .statusCode(200)
             .body("find { it.id == %d }.tagIds.size()".formatted(entryId), is(0));

        given()
          .when().delete("/entries/" + entryId)
          .then()
             .statusCode(204);

        given()
          .when().delete("/tags/" + firstTagId)
          .then()
             .statusCode(204);
    }

    private Long createTag(String title) {
        return given()
          .contentType("application/json")
          .body("""
              {"title":"%s"}
              """.formatted(title))
          .when().post("/tags")
          .then()
             .statusCode(200)
             .extract().jsonPath().getLong("id");
    }
}
