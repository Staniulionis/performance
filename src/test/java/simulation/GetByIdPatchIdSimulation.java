package simulation;

import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class GetByIdPatchIdSimulation extends Simulation {
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:3001/");
    FeederBuilder.Batchable<String> feeder =
            csv("data/search.csv").random();

    ScenarioBuilder scn = scenario("GetUserDetailsFromCSV")
            .feed(feeder)
            .exec(http("Het specific booking")
                    .get("booking/10")
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .basicAuth("admin", "password123")
                    .check(status().is(200))
                    .check(jsonPath("$.totalprice").exists(),
                                        jsonPath("$.totalprice").saveAs("newtotal"),
                                        jsonPath("$.depositpaid").exists(),
                                        jsonPath("$.depositpaid").saveAs("newdeposit"),
                                        jsonPath("$.additionalneeds").exists(),
                                        jsonPath("$.additionalneeds").saveAs("newadditional"))
                    .transformResponse((response, session) -> {
                                if (response.status().code() == 200) {
                                    System.out.println("????????????????????????????????????????????????????GET");
                                    System.out.println(response.body().toString());
                                    System.out.println(response.request());
                                } return response;
                            }
                    )
            )
            .pause(5)
            .exec(http("Het specific booking")
                    .patch("booking/10")
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .basicAuth("admin", "password123")
                    .check(status().is(200))
                    .body(StringBody("{\"totalprice\": \"#{newtotal}\"," +
                                        "\"depositpaid\": \"#{newdeposit}\"," +
                                        "\"additionalneeds\": \"#{newadditional}\"}"))
                    .transformResponse((response, session) -> {
                                if (response.status().code() == 200) {
                                    System.out.println("????????????????????????????????????????????????????PATCH");
                                    System.out.println(response.body().toString());
                                    System.out.println(response.request());
                                } return response;
                            }
                    )
            )
            .pause(5);
    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
                        .protocols(httpProtocol));
    }
}