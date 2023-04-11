package org.galatea.starter.Cucumber;


import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
 import wiremock.com.jayway.jsonpath.JsonPath;

 import java.io.UnsupportedEncodingException;

@Slf4j
@RequiredArgsConstructor
// this gives us the MockMvc variable
@AutoConfigureMockMvc
public class CucumberStepDefinitions {

    @Autowired
    private MockMvc mvc;
    MvcResult result;

    @When("the client calls endpoint {string}")
    public void the_client_calls_endpoint(String url) throws Exception {
        result = this.mvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .get(url)
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
    }

    @Then("response status code is {int}")
    public void response_status_code_is(int expected) {
        Assert.assertEquals(expected, result.getResponse().getStatus());
    }

    @Then("returned symbol should be {string}")
    public void thenStringIs(String expected) throws UnsupportedEncodingException {
        Assert.assertEquals(expected, JsonPath.parse(result.getResponse().getContentAsString()).read("$[0][0].symbol"));
    }
}
