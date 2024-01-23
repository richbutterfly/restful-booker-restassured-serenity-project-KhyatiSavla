package com.restful.booker.bookinginfo;

import com.restful.booker.model.BookingDatesPojo;
import com.restful.booker.testbase.TestBase;
import com.restful.booker.utils.TestUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.annotations.Title;
import net.serenitybdd.junit.runners.SerenityRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.hamcrest.core.IsAnything.anything;

@RunWith(SerenityRunner.class)
public class BookingCRUDTest extends TestBase {

    public static String token;
    public static String firstname  = "Prime" + TestUtils.getRandomValue();
    public static String lastname  = "Test" + TestUtils.getRandomValue();
    public static int totalprice  = Integer.parseInt(TestUtils.getRandomValue());
    public static boolean depositpaid  = true;
    public static String additionalneeds = "Dairy free" ;
    public static int bookingID;
    public static String username = "admin";
    public static String password = "password123";


    @Steps
    BookingSteps bookingSteps;

    @Title("This will create auth token")
    @Test
    public void test001() {
        ValidatableResponse response = bookingSteps.createToken(username, password);

        response.log().all().statusCode(200);

        HashMap<Object, Object> tokenMap= response.log().all().extract().path("");

        Assert.assertThat(tokenMap,hasKey("token"));
        String jsonString = response.extract().asString();
        token = JsonPath.from(jsonString).get("token");

        System.out.println(token);
    }


    @Title("This will create a new booking")
    @Test
    public void test002() {

        BookingDatesPojo bookingdates = new BookingDatesPojo();
        bookingdates.setCheckin("2023-03-20");
        bookingdates.setCheckout("2023-04-30");

        ValidatableResponse response = bookingSteps.test001_createBooking(firstname , lastname , totalprice , depositpaid , additionalneeds, bookingdates);
        response.log().all().statusCode(200);
        bookingID = response.log().all().extract().path("bookingid");

        HashMap<Object,Object>bookingMap= response.log().all().extract().path("");
        Assert.assertThat(bookingMap,anything(firstname));
        System.out.println(token);
    }
    @Title("This will verify Booking is added")
    @Test
    public void test003() {

        ValidatableResponse response = bookingSteps.getBookingInfoByID(bookingID);
        response.log().all().statusCode(200);

    }

    @Title("Update the booking information and verify the updated information")
    @Test
    public void test004() {
        BookingDatesPojo bookingdates = new BookingDatesPojo();
        bookingdates.setCheckin("2023-03-20");
        bookingdates.setCheckout("2023-04-30");


        ValidatableResponse response = bookingSteps.test003_updateBooking(bookingID,firstname, lastname, totalprice , depositpaid , additionalneeds, bookingdates, token);
        response.statusCode(200);
    }

    @Title("This will Deleted with BookingId")
    @Test
    public void test005() {

        ValidatableResponse response = bookingSteps.test004_deleteBooking(token, bookingID);
        response.log().all().statusCode(201);

        ValidatableResponse response1 = bookingSteps.test005_getBookingInfoById(bookingID);
        response1.log().all().statusCode(404);

    }
}