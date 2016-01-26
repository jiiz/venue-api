package net.laivuri.venueapi.service.venues.itest;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import com.xebialabs.restito.semantics.Action;
import static com.xebialabs.restito.semantics.Action.ok;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static com.xebialabs.restito.semantics.Condition.get;
import com.xebialabs.restito.server.StubServer;
import java.io.IOException;
import javax.ws.rs.core.MediaType;
import net.laivuri.venueapi.service.venues.TestData;
import net.laivuri.venueapi.service.venues.fs.FsResponse;
import net.laivuri.venueapi.service.venues.fs.FsResponseVenue;
import net.laivuri.venueapi.service.venues.fs.FsResponseVenuePhotos;
import net.laivuri.venueapi.service.venues.fs.FsResponseVenueSearch;

/**
 *
 * @author Juhani Laitakari
 *
 * Setups FourSquare server stub for integration tests
 */
public class FourSquareServerStub {

    private static StubServer fourSquareServer;

    public static final String NOT_FOUND_VENUE_ID = "not-found-venue-id";

    private FourSquareServerStub() {
    }

    public static void start(int port) throws IOException {
        if (fourSquareServer == null) {
            fourSquareServer = new StubServer(port).run();

            // venues search
            FsResponse<FsResponseVenueSearch> searchRespData = TestData.getFreshVenueSearchResponse();
            whenHttp(fourSquareServer).
                match(get("/v2/venues/search")).
                then(
                    ok(),
                    Action.contentType(MediaType.APPLICATION_JSON),
                    stringContent(TestData.getAsJsonString(searchRespData)));

            // venue get
            FsResponse<FsResponseVenue> venueRespData = TestData.getFreshVenueResponse();
            final String venueId = venueRespData.getResponse().getVenue().getId();
            whenHttp(fourSquareServer).
                match(get("/v2/venues/" + venueId)).
                then(
                    ok(),
                    Action.contentType(MediaType.APPLICATION_JSON),
                    stringContent(TestData.getAsJsonString(venueRespData)));
            // venue get - not found
            venueRespData.getMeta().setCode("400"); // foursquare api responds with 400 for venue not found
            whenHttp(fourSquareServer).
                match(get("/v2/venues/" + NOT_FOUND_VENUE_ID)).
                then(
                    ok(),
                    Action.contentType(MediaType.APPLICATION_JSON),
                    stringContent(TestData.getAsJsonString(venueRespData)));

            // venue photos
            FsResponse<FsResponseVenuePhotos> venuePhotosRespData = TestData.getFreshVenuePhotosResponse();
            whenHttp(fourSquareServer).
                match(get("/v2/venues/" + venueId + "/photos")).
                then(
                    ok(),
                    Action.contentType(MediaType.APPLICATION_JSON),
                    stringContent(TestData.getAsJsonString(venuePhotosRespData)));
            // venue phots - not found
            venuePhotosRespData.getMeta().setCode("400"); // foursquare api responds with 400 for venue not found
            whenHttp(fourSquareServer).
                match(get("/v2/venues/" + NOT_FOUND_VENUE_ID + "/photos")).
                then(
                    ok(),
                    Action.contentType(MediaType.APPLICATION_JSON),
                    stringContent(TestData.getAsJsonString(venuePhotosRespData)));
        }
    }

    public static void stop() {
        if (fourSquareServer != null) {
            fourSquareServer.stop();
            fourSquareServer = null;
        }
    }
}
