package simulation;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;

public class GetBookingByIdSimulation extends Simulation {
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:3001/")
            .acceptHeader("application/json");

    ScenarioBuilder scn = scenario("GetBookingByIdSimulation")
                .exec(http("Get booking ID")
                    .get("booking/")
                    )
            )
            .pause(5)
            .exec(http("Get booking by ID")
                    .get("booking/10")
                    )
            )
            .pause(5);
    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
        ).protocols(httpProtocol);
    }
}
