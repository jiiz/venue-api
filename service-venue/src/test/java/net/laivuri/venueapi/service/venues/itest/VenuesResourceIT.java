package net.laivuri.venueapi.service.venues.itest;

import static com.jayway.restassured.RestAssured.*;
import java.util.List;
import net.laivuri.venueapi.service.venues.TestData;
import net.laivuri.venueapi.service.venues.dto.Venue;
import org.junit.Test;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.unitils.reflectionassert.ReflectionComparatorMode.LENIENT_ORDER;

/**
 *
 * @author Juhani Laitakari
 */
public class VenuesResourceIT extends AbstractVenueSearchApiTest {

    /**
     * Test of searchVenues method, of class VenuesResource.
     */
    @Test
    public void testSearchVenues_Error_InvalidParams() throws Exception {

        expect().
            statusCode(400).
            when().
            given().
            param("query", "test-query").
            get();
    }

    /**
     * Test of searchVenues method, of class VenuesResource.
     */
    @Test
    public void testSearchVenues_Success() throws Exception {

        List<Venue> expected = TestData.getFreshVenues();
        Venue[] result = expect().
            statusCode(200).
            when().
            given().
            param("locName", "test-loc-name").
            get().
            as(Venue[].class);
        assertReflectionEquals(expected, result, LENIENT_ORDER);
    }

}
