package com.restful.booker.bookinginfo;

import com.restful.booker.constants.EndPoints;
import com.restful.booker.model.AuthPojo;
import com.restful.booker.model.BookingDatesPojo;
import com.restful.booker.model.BookingPojo;
import com.restful.booker.utils.TestUtils;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

public class BookingSteps {

    public static String token;

    @Step("Create token with userName : {0}, password: {1}")
    public ValidatableResponse createToken(String username, String password) {
        AuthPojo authPojo = new AuthPojo();
        authPojo.setUsername(username);
        authPojo.setPassword(password);

        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .body(authPojo)
                .when()
                .post(EndPoints.CREATE_TOKEN)
                .then();
    }

    @Step("Creating booking with firstname : {0}, lastname : {1}, totalprice : {2}, depositpaid : {3}, additionalNeeds : {4} and bookingdates : {5}")
    public ValidatableResponse test001_createBooking(String firstname, String lastname, int totalprice, boolean depositpaid, String additionalneeds, BookingDatesPojo bookingdates) {

        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setFirstname(firstname);
        bookingPojo.setLastname(lastname);
        bookingPojo.setTotalprice(totalprice);
        bookingPojo.setDepositpaid(true);
        bookingPojo.setBookingdates(bookingdates);
        bookingPojo.setAdditionalneeds(additionalneeds);

        return SerenityRest.given().log().all()
                .header("Accept", "application/json")
                .header("Cookie", "token=abc123")
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .contentType(ContentType.JSON)
                .when()
                .body(bookingPojo)
                .post(EndPoints.GET_BOOKING)
                .then();

    }
    @Step("Verifying Booking is added  with Booking ID : {0}")
    public ValidatableResponse getBookingInfoByID(int bookingID) {

        return SerenityRest.given().log().all()
                .header("Accept", "application/json")
                .header("Cookie", "token=abc123")
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .contentType(ContentType.JSON)
                .when()
                .pathParam("bookingID",bookingID)
                .get(EndPoints.GET_BOOKING_BY_ID)
                .then();
    }
    @Step("Updating booking with firstname : {0}, lastname : {1}, totalprice : {2}, depositPaid : {3}, additionalNeeds : {4}, bookingDates : {5} and token : {6} ")
    public ValidatableResponse test003_updateBooking(int bookingID, String firstname, String lastname, int totalprice, boolean depositpaid, String additionalneeds, BookingDatesPojo bookingdates, String token) {

        firstname = "Sanket01" + TestUtils.getRandomValue();
        lastname = "Savla" + TestUtils.getRandomValue();

        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setFirstname(firstname);
        bookingPojo.setLastname(lastname);
        bookingPojo.setTotalprice(1000);
        bookingPojo.setDepositpaid(false);
        bookingPojo.setAdditionalneeds("Gluten free");
        bookingPojo.setBookingdates(bookingdates);

        return SerenityRest.given()
                .header("Accept", "application/json")
                .header("Cookie", "token=abc123")
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .contentType(ContentType.JSON)
                .pathParam("bookingID", bookingID)
                .auth().preemptive().basic("admin","password123")
                .when()
                .body(bookingPojo)
                .put(EndPoints.UPDATE_BOOKING_BY_ID)
                .then().log().all();
    }
    @Step("Updating Booking partially firstname : {0}, lastname : {1}, totalprice : {2}, depositPaid : {3}, additionalNeeds : {4}, bookingDates : {5} and token : {6} \"")
    public ValidatableResponse test004_updateBookingPartially (String firstname, String lastname, int totalprice, boolean depositpaid, String additionalneeds, BookingDatesPojo bookingdates, String token, int bookingID) {

        firstname = firstname + "_updated";
        lastname = lastname + "_updated";

        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setFirstname(firstname);
        bookingPojo.setLastname(lastname);
        bookingPojo.setBookingdates(bookingdates);

        return SerenityRest.given()
                .header("Accept", "application/json")
                .header("Cookie", "token=abc123")
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .contentType(ContentType.JSON)
                .pathParam("bookingID", bookingID)
                .auth().preemptive().basic("admin","password123")
                .when()
                .body(bookingPojo)
                .patch(EndPoints.UPDATE_BOOKING_BY_ID)
                .then().log().all();
    }
    @Step("Deleting the booking information with token : {0}")
    public ValidatableResponse test004_deleteBooking(String token, int bookingID) {

        return SerenityRest.given().log().all()
                .header("Content-Type", "application/json")
                .header("Cookie", "token=" + token)
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .pathParam("bookingID", bookingID)
                .auth().preemptive().basic("admin","password123")
                .when()
                .delete(EndPoints.DELETE_BOOKING_BY_ID)
                .then().log().all();
    }

    @Step("Getting the student information by studentID : {0}")
    public ValidatableResponse test005_getBookingInfoById(int bookingID) {
        return SerenityRest.given().log().all()
                .pathParam("bookingID",bookingID)
                .auth().preemptive().basic("admin","password123")
                .when()
                .get(EndPoints.GET_BOOKING_BY_ID)
                .then();
    }
}