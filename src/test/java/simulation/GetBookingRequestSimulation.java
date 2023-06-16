package simulation;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class GetBookingRequestSimulation extends Simulation {
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:3001");
    ScenarioBuilder scn = scenario("GetBookingIds")
            .exec(http("GetBookingIdsRequest")
                    .get("/booking"))
            .pause(5);
    {
        setUp( // 11
                scn.injectOpen(atOnceUsers(5))
        ).protocols(httpProtocol);
    }
}