package simulation;

import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class GetBookingFromCSV extends Simulation {
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:3001/");
    FeederBuilder.Batchable<String> feeder =
            csv("data/search.csv").random();

    ScenarioBuilder scn = scenario("GetUserDetailsFromCSV")
            .feed(feeder)
            .exec(http("Change user firstname, lastname")
                    .patch("booking/10")
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .basicAuth("admin", "password123")
                    .body(StringBody("{\"firstname\": \"#{firstname}\"," +
                                         "\"lastname\": \"#{lastname}\"}"))
                    .check(status().is(200))
                    .check(jsonPath("$.firstname").is("Jonas"))
                    .transformResponse((response, session) -> {
                                if (response.status().code() == 404) {
                                    System.out.println(response.body().toString());
                                    System.out.println(response.request());
                                } return response;
                            }
                    )
            )
            .pause(5);
    {
        setUp(
                scn.injectOpen(atOnceUsers(1)
        ).protocols(httpProtocol));
    }
}
