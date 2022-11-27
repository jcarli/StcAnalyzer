package api;

import output.JsonPrinter;
import web.DataFetcher;

public class AnalyzerFactory {

    public static StcAnalyzer createAnalyzer() {
        return new StcAnalyzerImpl(
                () -> DataFetcher.createWithHttp("https://stc.brpsystems.com/brponline/api/ver3/businessunits").getClubData(),
                new JsonPrinter()
        );
    }
}
