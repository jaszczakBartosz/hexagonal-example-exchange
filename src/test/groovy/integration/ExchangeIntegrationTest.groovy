package integration

import com.milionpugs.exchange.inrastructure.api.AccountBalanceDto
import com.milionpugs.exchange.inrastructure.rates.RateResponse
import com.milionpugs.exchange.inrastructure.rates.RateValue

import static java.math.BigDecimal.valueOf
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ExchangeIntegrationTest extends BaseIntegrationTest {

    private static final def USD = "USD"
    private static final def EUR = "EUR"
    private static final def ID = 10

    def "should return proper exchanged value for USD"() {
        given:
        insertAccountBalance(ID, valueOf(30.0))
        stubRates(rateResponse(3), USD)
        when:
        def result = mockMvc.perform(get("/account/$ID/balance").queryParam("currency", USD).accept(APPLICATION_JSON_VALUE))
        then:
        result
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(responseJson("10", USD)))
    }

    def "should return proper exchanged value for EUR"() {
        given:
        insertAccountBalance(ID, valueOf(30.0))
        stubRates(rateResponse(2), EUR)
        when:
        def result = mockMvc.perform(get("/account/$ID/balance").queryParam("currency", EUR).accept(APPLICATION_JSON_VALUE))
        then:
        result
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(responseJson("15", EUR)))
    }

    def "should return proper exchanged value for zero balance"() {
        given:
        insertAccountBalance(ID, valueOf(0))
        stubRates(rateResponse(2), USD)
        when:
        def result = mockMvc.perform(get("/account/$ID/balance").queryParam("currency", USD).accept(APPLICATION_JSON_VALUE))
        then:
        result
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(responseJson("0", USD)))
    }

    def "should return error if there is no account"() {
        when:
        def result = mockMvc.perform(get("/account/$ID/balance").queryParam("currency", USD).accept(APPLICATION_JSON_VALUE))
        then:
        result.andExpect(status().is4xxClientError())
    }

    def "should return 400 error if currency not present"() {
        when:
        def result = mockMvc.perform(get("/account/$ID/balance").accept(APPLICATION_JSON_VALUE))
        then:
        result.andExpect(status().is4xxClientError())
    }

    private RateResponse rateResponse(double rate) {
        new RateResponse([
                new RateValue(valueOf(rate))
        ])
    }

    private String responseJson(String balance, String currency) {
        return mapper.writeValueAsString(new AccountBalanceDto(balance, currency))
    }


}
