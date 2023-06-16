package simulation;

import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class PostUpdateSimulation extends Simulation {
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:3001/");
    
    FeederBuilder.Batchable<String> feeder =
            csv("data/search.csv").random();

    ScenarioBuilder scn = scenario("PostUpdateSimulation")
            .feed(feeder)
            .exec(http("Post booking")
                    .post("booking/")
                    .basicAuth("admin", "password123")
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .body(StringBody("{\"firstname\": \"#{firstname}\"," +
                                        "\"lastname\": \"#{lastname}\"," +
                                        "\"totalprice\": #{totalprice}," +
                                        "\"depositpaid\": #{depositpaid}," +
                                        " \"bookingdates\": {" +
                                        "\"checkin\": \"#{checkin}\"," +
                                        "\"checkout\": \"#{checkout}\"}," +
                                        "\"additionalneeds\": \"#{additionalneeds}\"}"))
                    .check(status().is(200))
                    .check(
                            jsonPath("$.bookingid").exists(),
                            jsonPath("$.bookingid").saveAs("newid"))
                    )
            )
            .pause(5)
            .exec(http("Update booking data")
                    .put("booking/#{newid}")
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .basicAuth("admin", "password123")
                    .check(status().is(200))
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
        setUp(scn.injectOpen(
                        nothingFor(2),
                        atOnceUsers(15),
                        rampUsersPerSec(15).to(20).during(10).randomized(),
                        rampUsers(8).during(20),
                        stressPeakUsers(1500).during(20),
                        constantUsersPerSec(20).during(15),
                        rampUsersPerSec(10).to(20).during(10).randomized(),
                        rampUsers(20).during(30),
                        stressPeakUsers(800).during(20),
                        nothingFor(4),
                        rampUsersPerSec(20).to(15).during(5),
                        rampUsersPerSec(15).to(20).during(10).randomized(),
                        stressPeakUsers(350).during(20))
                .protocols(httpProtocol))
                .assertions(
                        global().responseTime().max().lte(800),
                        global().successfulRequests().percent().gt(95.0),
                        global().requestsPerSec().between(0.0, 100.0));
    }
}
