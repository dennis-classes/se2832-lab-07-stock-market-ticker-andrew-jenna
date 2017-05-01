import exceptions.InvalidAnalysisState;
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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class StockQuoteAnalyzerTest {
    @Mock
    private StockQuoteGeneratorInterface generatorMock;
    @Mock
    private StockTickerAudioInterface audioMock;

    private StockQuoteAnalyzer analyzer;

    @DataProvider (name = "QuoteClasses")
    public Object[][] generalMotors(){
        return new Object[][]{  new Object[]{"AA", "Alcoa Corporation", 100.00, 101.00, 1.00}, //+1%
                                new Object[]{"BXC","Bluelinx Holdings Inc", 500.0, 500.0, 0}, //no change
                                new Object[]{"CAJ","Canon Inc", 400.00, 396.00, -4}, //-1%
                                new Object[]{"DIS","Walt Disney Company", 1010.00, 500.0, -510}, //-50.4950495%
                                new Object[]{"ELF","E.L.F. Beauty Inc", 300.0, 500.0, 200}}; //+66.6%
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

    @Test(expectedExceptions = NullPointerException.class)
    public void constructorShouldThrowExceptionWhenGeneratorIsInvalid() throws Exception {
        analyzer = new StockQuoteAnalyzer("A", null, audioMock);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void constructorShouldThrowExceptionWhenPlayerIsInvalid() throws Exception {
        analyzer = new StockQuoteAnalyzer("A", generatorMock, null);
    }

    @Test
    public void getSymbolShouldReturnSymbol() throws Exception{
        analyzer = new StockQuoteAnalyzer("A", generatorMock, audioMock);
        assertEquals("A", analyzer.getSymbol());
    }

    @Test (expectedExceptions = StockTickerConnectionError.class)
    public void refreshShouldThrowStockTickerConnectionErrorWhenUnableToConnectToTickerSource() throws Exception {
        analyzer = new StockQuoteAnalyzer("GM", generatorMock, audioMock);
        when(generatorMock.getCurrentQuote()).thenThrow(new Exception());
        analyzer.refresh();
    }

    @Test (expectedExceptions = InvalidAnalysisState.class)
    public void getPercentChangeSinceCloseShouldThrowInvalidAnalysisStateWhenNoQuoteReceived() throws Exception {
        analyzer = new StockQuoteAnalyzer("GM", generatorMock, audioMock);
        analyzer.getPercentChangeSinceClose();
    }

    @Test
    public void getPercentChangeSinceCloseShouldReturnPercentChangeWhenQuotesProvided() throws Exception {
        analyzer = new StockQuoteAnalyzer("GM", generatorMock, audioMock);
        double change = 0;
        double previousClose = 5.0;
        double lastTrade = 6.0;
        when(generatorMock.getCurrentQuote()).thenReturn(new StockQuote("GM", previousClose, lastTrade, change));
        analyzer.refresh();
        double result = analyzer.getPercentChangeSinceClose();

        assertEquals(0.0, result);
        verify(generatorMock, times(1)).getCurrentQuote();
    }

    @Test (expectedExceptions = InvalidAnalysisState.class)
    public void getPreviousCloseShouldReturnShouldThrowExceptionWhenCurrentQuoteIsNull() throws Exception {
        analyzer = new StockQuoteAnalyzer("GM", generatorMock, audioMock);
        when(generatorMock.getCurrentQuote()).thenReturn(null);
        analyzer.refresh();
        analyzer.getPreviousClose();
    }

    @Test (dataProvider = "QuoteClasses")
    public void getPreviousCloseShouldReturnPreviousCloseWhenThereExistsACurrentQuote(String symbol, String name, double previousClose, double lastTrade,  double change) throws Exception {
        analyzer = new StockQuoteAnalyzer(symbol, generatorMock, audioMock);
        when(generatorMock.getCurrentQuote()).thenReturn(new StockQuote(symbol, previousClose, lastTrade, change));
        analyzer.refresh();
        assertEquals(analyzer.getPreviousClose(), previousClose);
    }
}