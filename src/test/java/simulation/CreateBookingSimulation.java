package simulation;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class CreateBookingSimulation extends Simulation {
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:3001/");

    ScenarioBuilder scn = scenario("CreateBooking")
            .exec(http("CreateBooking")
                    .post("booking/")
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .body(StringBody("{\"firstname\": \"Jim\"," +
                            "\"lastname\": \"Brown\"," +
                            "\"totalprice\": 111," +
                            "\"depositpaid\": true," +
                            " \"bookingdates\": {" +
                                    "\"checkin\": \"2018-01-01\"," +
                                    "\"checkout\": \"2019-01-01\"}," +
                            "\"additionalneeds\": \"Breakfast\"}"))
                    .transformResponse((response, session) -> {
                                if (response.status().code() == 200) {
                                    System.out.println(response.body().toString());
                                    System.out.println("??????????++++++++++++++++++++===================????????????");
                                    System.out.println(response.request());
                                } return response;
                            }
                    )
            )
            .pause(5);
    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
        ).protocols(httpProtocol);
    }
}