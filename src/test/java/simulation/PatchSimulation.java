package simulation;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class PatchSimulation extends Simulation {

    ScenarioBuilder scn = scenario("PatchSimulation")
            .exec(http("Change data details")
                    .patch("http://localhost:3001/booking/10")
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Cookie", "token=<token_value>")
                    .basicAuth("admin", "password123")
                    .body(StringBody("{\"firstname\": \"Jim\"," +
                            "\"lastname\": \"Brown\"," +
                            "\"additionalneeds\": \"Dinner\"}"))
                    )
            )
            .pause(5);
    {
        setUp(scn.injectOpen(atOnceUsers(100)))
                    .assertions(
                            global().responseTime().max().lte(250),
                            global().successfulRequests().percent().gt(95.0),
                            global().requestsPerSec().between(1.0, 100.0));
    }
}
