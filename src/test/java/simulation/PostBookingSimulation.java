package simulation;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class PostBookingSimulation extends Simulation {
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:3001/");

    ScenarioBuilder scn = scenario("CreateBooking")
            .exec(http("Post Booking")
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
                    )
            )
            .pause(5);
    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
        ).protocols(httpProtocol);
    }
}
