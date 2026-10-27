package ch.zli.m223;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

@QuarkusTest
public class EntryDetailResourceTest {

    @Test
    public void testEntryDetailEndpoints() {
        Long categoryId = create("/categories", "Projekt");
        Long firstTagId = create("/tags", "Meeting");
        Long secondTagId = create("/tags", "Remote");

        Long entryId = createEntry("""
            {"checkIn":"2026-09-29T08:00:00","checkOut":"2026-09-29T17:00:00","categoryId":%d,"tagIds":[%d,%d]}
            """.formatted(categoryId, firstTagId, secondTagId));
        Long plainEntryId = createEntry("""
            {"checkIn":"2026-09-30T08:00:00","checkOut":"2026-09-30T12:00:00"}
            """);

        given()
          .when().get("/entry-details/" + entryId)
          .then()
             .statusCode(200)
             .body("id", is(entryId.intValue()))
             .body("checkIn", is("2026-09-29T08:00:00"))
             .body("checkOut", is("2026-09-29T17:00:00"))
             .body("category.id", is(categoryId.intValue()))
             .body("category.title", is("Projekt"))
             .body("category.size()", is(2))
             .body("tags.size()", is(2))
             .body("tags[0].id", is(firstTagId.intValue()))
             .body("tags[0].title", is("Meeting"))
             .body("tags[0].size()", is(2))
             .body("tags[1].title", is("Remote"));

        given()
          .when().get("/entry-details/" + plainEntryId)
          .then()
             .statusCode(200)
             .body("category", nullValue())
             .body("tags.size()", is(0));

        given()
          .when().get("/entry-details")
          .then()
             .statusCode(200)
             .body("find { it.id == %d }.category.title".formatted(entryId), is("Projekt"))
             .body("find { it.id == %d }.tags.size()".formatted(plainEntryId), is(0));

        given()
          .when().get("/entry-details/999999")
          .then()
             .statusCode(404);

        given().when().delete("/entries/" + entryId).then().statusCode(204);
        given().when().delete("/entries/" + plainEntryId).then().statusCode(204);
        given().when().delete("/categories/" + categoryId).then().statusCode(204);
        given().when().delete("/tags/" + firstTagId).then().statusCode(204);
        given().when().delete("/tags/" + secondTagId).then().statusCode(204);
    }

    private Long create(String path, String title) {
        return given()
          .contentType("application/json")
          .body("""
              {"title":"%s"}
              """.formatted(title))
          .when().post(path)
          .then()
             .statusCode(200)
             .extract().jsonPath().getLong("id");
    }

    private Long createEntry(String body) {
        return given()
          .contentType("application/json")
          .body(body)
          .when().post("/entries")
          .then()
             .statusCode(200)
             .extract().jsonPath().getLong("id");
    }
}
