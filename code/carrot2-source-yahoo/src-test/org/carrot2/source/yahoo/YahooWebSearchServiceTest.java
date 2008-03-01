package org.carrot2.source.yahoo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.carrot2.core.Document;
import org.carrot2.core.test.ExternalApiTestBase;
import org.carrot2.source.SearchEngineResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junitext.Prerequisite;
import org.junitext.runners.AnnotationRunner;

/**
 * Tests plain service accessor (no queries longer than a single page etc.).
 */
@RunWith(AnnotationRunner.class)
public class YahooWebSearchServiceTest extends ExternalApiTestBase
{
    private YahooSearchService service;

    @Before
    public void init()
    {
        service = new YahooWebSearchService();
    }

    @Test
    @Prerequisite(requires = "externalApiTestsEnabled")
    public void testNoResultsQuery() throws Exception
    {
        final SearchEngineResponse response = service.query("duiogig oiudgisugviw siug iugw iusviuwg", 0, 100);
        assertEquals(0, response.results.size());
    }

    @Test
    @Prerequisite(requires = "externalApiTestsEnabled")
    public void testPolishDiacritics() throws Exception
    {
        final SearchEngineResponse response = service.query("Łódź", 0, 100);
        assertEquals(service.resultsPerPage, response.results.size());
    }

    @Test
    @Prerequisite(requires = "externalApiTestsEnabled")
    public void testLargerQuery() throws Exception
    {
        final int needed = service.resultsPerPage / 2;
        final SearchEngineResponse response = service.query("apache", 0, needed);
        assertEquals(needed, response.results.size());
    }

    @Test
    @Prerequisite(requires = "externalApiTestsEnabled")
    public void testEntities() throws Exception
    {
        final SearchEngineResponse response = service.query("Ala ma kota", 0, 100);

        for (final Document d : response.results)
        {
            final String title = d.getField(Document.TITLE);
            final String summary = d.getField(Document.SUMMARY);

            final String merged = (title + " " + summary);

            assertTrue(merged.indexOf("&gt;") < 0);
            assertTrue(merged.indexOf("&lt;") < 0);
            assertTrue(merged.indexOf("&amp;") < 0);
        }
    }

    @Test(expected=IOException.class)
    @Prerequisite(requires = "externalApiTestsEnabled")
    public void testErrorResult() throws Exception
    {
        service.resultsPerPage = 400;
        service.query("apache", 0, 400);
    }

    @Test
    @Prerequisite(requires = "externalApiTestsEnabled")
    public void testCompressedStreamsUsed() throws Exception
    {
        final SearchEngineResponse response = service.query("apache", 0, 50);
        assertEquals("gzip",
            response.metadata.get(YahooSearchService.COMPRESSION_USED_KEY));
    }
}
