Feature: Test IEX EndPoints
    Scenario: Customer calls IEX Historical Prices endpoint with symbol NET and range max
        When the client calls endpoint "/iex/historicalTradedPrices?symbols=NET&range=max"
        Then response status code is 200
        And returned symbol should be "NET"
        And returned IPO date should be "2019-09-13"

    Scenario: Customer calls IEX Historical Prices endpoint with symbols NET,TSLA and range max
        When the client calls endpoint "/iex/historicalTradedPrices?symbols=NET,TSLA&range=max"
        Then response status code is 200
        And returned symbol should be "NET"
        And returned IPO date should be "2019-09-13"
        And second returned symbol should be "TSLA"
        And second returned IPO date should be "2010-06-29"

    Scenario: Customer calls IEX Historical Prices endpoint with symbol NET and date 20220202
        When the client calls endpoint "/iex/historicalTradedPrices?symbols=NET&date=20220202"
        Then response status code is 200
        And returned symbol should be "NET"
        And returned date should be "2022-02-02"

    Scenario: Customer calls IEX Historical Prices endpoint with symbols NET,TSLA and date 20220202
        When the client calls endpoint "/iex/historicalTradedPrices?symbols=NET,TSLA&date=20220202"
        Then response status code is 200
        And returned symbol should be "NET"
        And returned date should be "2022-02-02"
        And second returned symbol should be "TSLA"
        And second returned date should be "2022-02-02"