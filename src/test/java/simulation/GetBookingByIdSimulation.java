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
            .exec(http("GetBookingById")
                    .get("booking/10")
                    .transformResponse((response, session) -> {
                                if (response.status().code() == 200) {
                                    System.out.println(response.body().string());
                                    System.out.println(response.request());
                                } return response;
                            }
                    )
            )
            .pause(5)
            .exec(http("GetBookingById")
                    .get("booking/")
                    .transformResponse((response, session) -> {
                                if (response.status().code() == 200) {
                                    System.out.println(response.body().string());
                                    System.out.println("??????????????????????");
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
