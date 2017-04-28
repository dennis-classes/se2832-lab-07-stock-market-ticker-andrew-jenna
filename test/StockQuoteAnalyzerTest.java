import exceptions.InvalidStockSymbolException;
import exceptions.StockTickerConnectionError;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.Mock;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;


public class StockQuoteAnalyzerTest {
    @Mock
    private StockQuoteGeneratorInterface generatorMock;
    @Mock
    private StockTickerAudioInterface audioMock;

    private StockQuoteAnalyzer analyzer;

    @DataProvider (name = "quoteGroup1")
    public Object[][] generalMotors(){
        return new Object[][]{  new Object[]{"AA", "Alcoa Corporation", 100.01, 101.10, 1.09}, //
                                new Object[]{"BXC","Bluelinx Holdings Inc", 500.0, 500.0, 0}, //no change
                                new Object[]{"CAJ","Canon Inc", 400.1, 489.6, -11.5}};
    }

    @BeforeMethod
    public void setUp() throws Exception {
        generatorMock = mock(StockQuoteGeneratorInterface.class);
        audioMock = mock(StockTickerAudioInterface.class);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        generatorMock = null;
        audioMock = null;
    }

    @Test(expectedExceptions = InvalidStockSymbolException.class)
    public void constructorShouldThrowExceptionWhenSymbolIsInvalid() throws Exception {
        analyzer = new StockQuoteAnalyzer("ZZZZZZZZZ", generatorMock, audioMock);
    }

    @Test (expectedExceptions = StockTickerConnectionError.class)
    public void refreshShouldThrowStockTickerConnectionErrorWhenUnableToConnectToTickerSource() throws Exception {
        analyzer = new StockQuoteAnalyzer("GM", generatorMock, audioMock);
        when(generatorMock.getCurrentQuote()).thenReturn(null);
        //todo
    }
}