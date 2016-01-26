/*
 * The MIT License
 *
 * Copyright 2016 Juhani Laitakari.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.laivuri.venueapi.service.venues;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import net.laivuri.venueapi.service.venues.dto.Venue;
import net.laivuri.venueapi.service.venues.dto.VenueSummary;
import net.laivuri.venueapi.service.venues.fs.FsResponse;
import net.laivuri.venueapi.service.venues.fs.FsResponseVenue;
import net.laivuri.venueapi.service.venues.fs.FsResponseVenuePhotos;
import net.laivuri.venueapi.service.venues.fs.FsResponseVenueSearch;
import org.springframework.beans.BeanUtils;

/**
 *
 * @author Juhani Laitakari
 */
public class TestData {

    private static final String TEST_DATA_FILE_FOURSQUARE = "/testdata-foursquare.json";
    private static final String TEST_DATA_FILE_VENUES = "/testdata-venues.json";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private TestData() {
    }

    private static FourSquareTestData readFourSquareTestData() throws IOException {
        try (InputStream is = TestData.class.getResourceAsStream(TEST_DATA_FILE_FOURSQUARE)) {
            return MAPPER.readValue(is, FourSquareTestData.class);
        }
    }

    private static VenuesTestData readVenuesTestData() throws IOException {
        try (InputStream is = TestData.class.getResourceAsStream(TEST_DATA_FILE_VENUES)) {
            return MAPPER.readValue(is, VenuesTestData.class);
        }
    }

    public static String getAsJsonString(Object data) throws JsonProcessingException {
        return MAPPER.writeValueAsString(data);
    }

    public static FsResponse<FsResponseVenue> getFreshVenueResponse() throws IOException {
        FsResponse<FsResponseVenue> copy = new FsResponse<>();
        BeanUtils.copyProperties(readFourSquareTestData().getVenueResponse(), copy);
        return copy;
    }

    public static FsResponse<FsResponseVenueSearch> getFreshVenueSearchResponse() throws IOException {
        FsResponse<FsResponseVenueSearch> copy = new FsResponse<>();
        BeanUtils.copyProperties(readFourSquareTestData().getVenueSearchResponse(), copy);
        return copy;
    }

    public static FsResponse<FsResponseVenuePhotos> getFreshVenuePhotosResponse() throws IOException {
        FsResponse<FsResponseVenuePhotos> copy = new FsResponse<>();
        BeanUtils.copyProperties(readFourSquareTestData().getPhotosResponse(), copy);
        return copy;
    }

    public static List<Venue> getFreshVenues() throws IOException {
        List<Venue> copiedData = new ArrayList<>();
        for (Venue source : readVenuesTestData().getVenues()) {
            Venue copy = new Venue();
            BeanUtils.copyProperties(source, copy);
            copiedData.add(copy);
        }
        return copiedData;
    }

    public static List<String> getFreshVenuePhotos() throws IOException {
        List<String> copiedData = new ArrayList<>();
        for (String source : readVenuesTestData().getVenuePhotos()) {
            copiedData.add(source); // strings are immutable thus only the list needs to be cloned
        }
        return copiedData;
    }

    public static VenueSummary getFreshVenueSummary() throws IOException {
        VenueSummary copy = new VenueSummary();
        BeanUtils.copyProperties(readVenuesTestData().getVenueSummary(), copy);
        return copy;
    }

    private static class FourSquareTestData {

        private FsResponse<FsResponseVenue> venueResponse;
        private FsResponse<FsResponseVenueSearch> venueSearchResponse;
        private FsResponse<FsResponseVenuePhotos> photosResponse;

        public FsResponse<FsResponseVenueSearch> getVenueSearchResponse() {
            return venueSearchResponse;
        }

        public void setVenueSearchResponse(FsResponse<FsResponseVenueSearch> venueSearchResponse) {
            this.venueSearchResponse = venueSearchResponse;
        }

        public FsResponse<FsResponseVenuePhotos> getPhotosResponse() {
            return photosResponse;
        }

        public void setPhotosResponse(FsResponse<FsResponseVenuePhotos> photosResponse) {
            this.photosResponse = photosResponse;
        }

        public FsResponse<FsResponseVenue> getVenueResponse() {
            return venueResponse;
        }

        public void setVenueResponse(FsResponse<FsResponseVenue> venueResponse) {
            this.venueResponse = venueResponse;
        }
    }

    private static class VenuesTestData {

        private List<Venue> venues;
        private List<String> venuePhotos;
        private VenueSummary venueSummary;

        public List<Venue> getVenues() {
            return venues;
        }

        public void setVenues(List<Venue> venues) {
            this.venues = venues;
        }

        public List<String> getVenuePhotos() {
            return venuePhotos;
        }

        public void setVenuePhotos(List<String> venuePhotos) {
            this.venuePhotos = venuePhotos;
        }

        public VenueSummary getVenueSummary() {
            return venueSummary;
        }

        public void setVenueSummary(VenueSummary venueSummary) {
            this.venueSummary = venueSummary;
        }
    }
}
