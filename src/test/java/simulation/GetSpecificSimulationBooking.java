package simulation;

import io.gatling.http.response.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class GetSpecificSimulationBooking extends Simulation {
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:3001");
    ScenarioBuilder scn = scenario("GetBookingIds")
            .exec(http("GetSpecificRequest")
                    .get("/booking")
                    .transformResponse((response, session) -> {
                                if (response.status().code() == 200) {
                                    System.out.println(response.body().string());
                                } else {
                                    System.out.println(response.body().string());
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