package integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.milionpugs.exchange.ExchangeApplication
import com.milionpugs.exchange.config.CacheConfig
import com.milionpugs.exchange.config.RestConfig
import com.milionpugs.exchange.config.RetryConfig
import com.milionpugs.exchange.inrastructure.rates.RateResponse
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.*
import static org.eclipse.jetty.http.HttpHeader.CONTENT_TYPE
import static org.jooq.impl.DSL.field
import static org.jooq.impl.DSL.table
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@SpringBootTest(classes = [ExchangeApplication, CacheConfig, RestConfig, RetryConfig],
        properties = "spring.profiles.active:integration",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
class BaseIntegrationTest extends Specification {

    protected WireMockServer wireMockServer = new WireMockServer(18090)

    @Autowired
    private DSLContext dslContext

    @Autowired
    protected MockMvc mockMvc

    ObjectMapper mapper = new ObjectMapper()

    void setup() {
        wireMockServer.start()
    }

    void cleanup() {
        wireMockServer.stop()
        wireMockServer.resetAll()
        cleanDb()
    }

    def stubRates(RateResponse rateResponse, String currency) {
        wireMockServer.stubFor(get(urlEqualTo("/api/exchangerates/rates/a/$currency"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE.toString(), APPLICATION_JSON_VALUE)
                        .withBody(mapper.writeValueAsString(rateResponse))
                        .withStatus(200)))
    }

    def insertAccountBalance(Long id, BigDecimal balance) {
        dslContext.insertInto(table("account"), field("id"), field("balance")).values(id, balance).execute()
    }

    def cleanDb() {
        dslContext.deleteFrom(table("account")).execute()
    }

}
