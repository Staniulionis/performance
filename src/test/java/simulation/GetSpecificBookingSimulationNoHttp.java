package simulation;

import io.gatling.javaapi.core.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class GetSpecificBookingSimulationNoHttp extends Simulation {

    ScenarioBuilder scn = scenario("GetBookingIds")
            .exec(http("Get specific ID")
                    .get("http://localhost:3001/booking")
            )
            .pause(5);
    {
        setUp(
                scn.injectOpen(atOnceUsers(5)));
    }
}
