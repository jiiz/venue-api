package net.laivuri.venueapi.service.venues.itest;

import static com.jayway.restassured.RestAssured.expect;
import java.util.List;
import net.laivuri.venueapi.service.venues.TestData;
import net.laivuri.venueapi.service.venues.dto.VenueSummary;
import org.junit.Test;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.unitils.reflectionassert.ReflectionComparatorMode.LENIENT_ORDER;

/**
 *
 * @author Juhani Laitakari
 */
public class VenueResourceIT extends AbstractVenueSearchApiTest {

    /**
     * Test of getVenuePhotos method, of class VenueResource.
     */
    @Test
    public void testGetVenuePhotos_Error_NotFound() throws Exception {
        expect().
            statusCode(404).
            when().
            get(FourSquareServerStub.NOT_FOUND_VENUE_ID + "/photos");
    }

    /**
     * Test of getVenuePhotos method, of class VenueResource.
     */
    @Test
    public void testGetVenuePhotos_Success() throws Exception {
        final String venueId = TestData.getFreshVenues().get(0).getId();
        List<String> expected = TestData.getFreshVenuePhotos();
        String[] result = expect().
            statusCode(200).
            when().
            get(venueId + "/photos").
            as(String[].class);
        assertReflectionEquals(expected, result, LENIENT_ORDER);
    }

    /**
     * Test of getVenueSummary method, of class VenueResource.
     */
    @Test
    public void testGetVenueSummary_Error_NotFound() throws Exception {
        expect().
            statusCode(404).
            when().
            get(FourSquareServerStub.NOT_FOUND_VENUE_ID + "/summary");
    }

    /**
     * Test of getVenueSummary method, of class VenueResource.
     */
    @Test
    public void testGetVenueSummary_Success() throws Exception {
        final String venueId = TestData.getFreshVenues().get(0).getId();
        VenueSummary expected = TestData.getFreshVenueSummary();
        VenueSummary result = expect().
            statusCode(200).
            when().
            get(venueId + "/summary").
            as(VenueSummary.class);
        assertReflectionEquals(expected, result, LENIENT_ORDER);
    }

}
