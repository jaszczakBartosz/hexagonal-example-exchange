package integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.bartoszjaszczak.exchange.ExchangeApplication
import com.bartoszjaszczak.exchange.common.CacheConfig
import com.bartoszjaszczak.exchange.common.RestConfig
import com.bartoszjaszczak.exchange.common.RetryConfig
import com.bartoszjaszczak.exchange.adapter.out.RateResponse
import com.maciejwalkowiak.wiremock.spring.EnableWireMock
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration

import static org.jooq.impl.DSL.field
import static org.jooq.impl.DSL.table
import static com.github.tomakehurst.wiremock.client.WireMock.*


@SpringBootTest(classes = [ExchangeApplication, CacheConfig, RestConfig, RetryConfig],
        properties = ["spring.profiles.active:integration", "wiremock.server.port=8080"],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@EnableWireMock()
class BaseIntegrationTest extends Specification {

    @Autowired
    private DSLContext dslContext

    @Autowired
    protected MockMvc mockMvc

    protected static WireMockServer wireMockServer

    def setupSpec() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort())
        wireMockServer.start()
        configureFor("localhost", wireMockServer.port())
    }

    def cleanupSpec() {
        wireMockServer.stop()
        cleanDb()
    }

    def setup() {
        wireMockServer.resetAll()
    }

    protected def mapper = new ObjectMapper()

    protected def stubRates(RateResponse rateResponse, String currency) {
        wireMockServer.stubFor(get("/api/exchangerates/rates/a/$currency")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(mapper.writeValueAsString(rateResponse))
                        .withStatus(200)))
    }

    protected def insertAccountBalance(Long id, BigDecimal balance) {
        dslContext.insertInto(table("account"), field("id"), field("balance"), field("currency")).values(id, balance, "PLN").execute()
    }

    protected def cleanDb() {
        dslContext.deleteFrom(table("account")).execute()
    }

}
