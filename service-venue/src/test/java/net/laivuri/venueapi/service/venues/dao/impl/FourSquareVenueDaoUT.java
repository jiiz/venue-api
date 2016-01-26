package net.laivuri.venueapi.service.venues.dao.impl;

import java.util.List;
import net.laivuri.venueapi.service.common.exp.EntityNotFoundException;
import net.laivuri.venueapi.service.common.exp.EntityStorageException;
import net.laivuri.venueapi.service.venues.TestData;
import net.laivuri.venueapi.service.venues.dto.Venue;
import net.laivuri.venueapi.service.venues.dto.VenueSummary;
import net.laivuri.venueapi.service.venues.fs.FourSquareClient;
import net.laivuri.venueapi.service.venues.fs.FourSquareClientImpl;
import net.laivuri.venueapi.service.venues.fs.FsResponse;
import net.laivuri.venueapi.service.venues.fs.FsResponseVenue;
import net.laivuri.venueapi.service.venues.fs.FsResponseVenuePhotos;
import net.laivuri.venueapi.service.venues.fs.FsResponseVenueSearch;
import net.laivuri.venueapi.service.venues.fs.FsSearchParams;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.mockito.Mockito.*;
import static org.unitils.reflectionassert.ReflectionComparatorMode.LENIENT_ORDER;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

/**
 *
 * @author Juhani Laitakari
 *
 * Unit tests to ensure that {@link net.laivuri.venueapi.service.venues.dao.impl.FourSquareVenueDao} converts responses
 * from {@link net.laivuri.venueapi.service.venues.fs.FourSquareClientImpl} correctly to venue api responses
 */
public class FourSquareVenueDaoUT {

    @Rule
    public ExpectedException expectedExp = ExpectedException.none();

    /**
     * Test of searchVenues method, of class FourSquareVenueDao.
     */
    @Test
    public void testSearchVenues_Error_NotOkResponse() throws Exception {
        FourSquareClient mockClient = mock(FourSquareClientImpl.class);
        FsResponse<FsResponseVenueSearch> failedResp = TestData.getFreshVenueSearchResponse();
        failedResp.getMeta().setCode("not-200");
        when(mockClient.searchVenues(any(FsSearchParams.class))).thenReturn(failedResp);

        FourSquareVenueDao sut = new FourSquareVenueDao(mockClient);

        expectedExp.expect(EntityStorageException.class);

        sut.searchVenues("", "", "");
    }

    /**
     * Test of searchVenues method, of class FourSquareVenueDao.
     */
    @Test
    public void testSearchVenues_Success() throws Exception {
        FourSquareClient mockClient = mock(FourSquareClientImpl.class);
        FsResponse<FsResponseVenueSearch> okResp = TestData.getFreshVenueSearchResponse();
        when(mockClient.searchVenues(any(FsSearchParams.class))).thenReturn(okResp);

        FourSquareVenueDao sut = new FourSquareVenueDao(mockClient);

        List<Venue> result = sut.searchVenues("", "", "");
        assertReflectionEquals(TestData.getFreshVenues(), result, LENIENT_ORDER);
    }

    /**
     * Test of getVenuePhotos method, of class FourSquareVenueDao.
     */
    @Test
    public void testGetVenuePhotos_Error_NotOkResponse() throws Exception {
        FourSquareClient mockClient = mock(FourSquareClientImpl.class);
        FsResponse<FsResponseVenuePhotos> failedResp = TestData.getFreshVenuePhotosResponse();
        failedResp.getMeta().setCode("not-200");
        when(mockClient.getVenuePhotos(any(String.class))).thenReturn(failedResp);

        FourSquareVenueDao sut = new FourSquareVenueDao(mockClient);

        expectedExp.expect(EntityStorageException.class);

        sut.getVenuePhotos("some-id");
    }

    /**
     * Test of getVenuePhotos method, of class FourSquareVenueDao.
     */
    @Test
    public void testGetVenuePhotos_Error_NotFound() throws Exception {
        FourSquareClient mockClient = mock(FourSquareClientImpl.class);
        FsResponse<FsResponseVenuePhotos> failedResp = TestData.getFreshVenuePhotosResponse();
        failedResp.getMeta().setCode("400");
        when(mockClient.getVenuePhotos(any(String.class))).thenReturn(failedResp);

        FourSquareVenueDao sut = new FourSquareVenueDao(mockClient);

        expectedExp.expect(EntityNotFoundException.class);

        sut.getVenuePhotos("some-id");
    }

    /**
     * Test of getVenuePhotos method, of class FourSquareVenueDao.
     */
    @Test
    public void testGetVenuePhotos_Success() throws Exception {
        FourSquareClient mockClient = mock(FourSquareClientImpl.class);
        FsResponse<FsResponseVenuePhotos> okResp = TestData.getFreshVenuePhotosResponse();
        when(mockClient.getVenuePhotos(any(String.class))).thenReturn(okResp);

        FourSquareVenueDao sut = new FourSquareVenueDao(mockClient);

        List<String> result = sut.getVenuePhotos("some-id");

        assertReflectionEquals(TestData.getFreshVenuePhotos(), result, LENIENT_ORDER);
    }

    /**
     * Test of getVenueSummary method, of class FourSquareVenueDao.
     */
    @Test
    public void testGetVenueSummary_Error_NotOkResponse() throws Exception {
        FourSquareClient mockClient = mock(FourSquareClientImpl.class);
        FsResponse<FsResponseVenue> failedResp = TestData.getFreshVenueResponse();
        failedResp.getMeta().setCode("not-200");
        when(mockClient.getVenue(any(String.class))).thenReturn(failedResp);
        when(mockClient.getVenuePhotos(any(String.class))).thenReturn(TestData.getFreshVenuePhotosResponse());

        FourSquareVenueDao sut = new FourSquareVenueDao(mockClient);

        expectedExp.expect(EntityStorageException.class);

        sut.getVenueSummary("some-id");
    }

    /**
     * Test of getVenueSummary method, of class FourSquareVenueDao.
     */
    @Test
    public void testGetVenueSummary_Error_NotFound() throws Exception {
        FourSquareClient mockClient = mock(FourSquareClientImpl.class);
        FsResponse<FsResponseVenue> failedResp = TestData.getFreshVenueResponse();
        failedResp.getMeta().setCode("400");
        when(mockClient.getVenue(any(String.class))).thenReturn(failedResp);
        when(mockClient.getVenuePhotos(any(String.class))).thenReturn(TestData.getFreshVenuePhotosResponse());

        FourSquareVenueDao sut = new FourSquareVenueDao(mockClient);

        expectedExp.expect(EntityNotFoundException.class);

        sut.getVenueSummary("some-id");
    }

    /**
     * Test of getVenueSummary method, of class FourSquareVenueDao.
     */
    @Test
    public void testGetVenueSummary_Success() throws Exception {
        FourSquareClient mockClient = mock(FourSquareClientImpl.class);
        when(mockClient.getVenuePhotos(any(String.class))).thenReturn(TestData.getFreshVenuePhotosResponse());
        when(mockClient.getVenue(any(String.class))).thenReturn(TestData.getFreshVenueResponse());

        FourSquareVenueDao sut = new FourSquareVenueDao(mockClient);

        VenueSummary result = sut.getVenueSummary("some-id");

        assertReflectionEquals(TestData.getFreshVenueSummary(), result, LENIENT_ORDER);
    }

}
