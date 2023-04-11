Feature: Test IEX EndPoints
    Scenario: Customer calls IEX Historical Prices endpoint
        When the client calls endpoint "/iex/historicalTradedPrices?symbols=NET&ranges=5y"
        Then response status code is 200
        And returned symbol should be "NET"