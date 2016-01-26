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
package net.laivuri.venueapi.service.venues.fs;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.text.StrSubstitutor;

/**
 *
 * @author Juhani Laitakari
 *
 * Partial FourSquare API client implementation
 */
public class FourSquareClientImpl implements FourSquareClient {

    private final String API_URL;

    private final Client client;

    public FourSquareClientImpl(String hostUrl, String clientId, String clientSecret) {

        final String urlTemplate
            = ""
            + "${host}/v2/venues/"
            + "${resource}?"
            + "${params}&"
            + "v=20160126&"
            + "client_id=${client_id}&"
            + "client_secret=${client_secret}";

        Map paramMap = new HashMap();
        paramMap.put("host", hostUrl);
        paramMap.put("client_id", clientId);
        paramMap.put("client_secret", clientSecret);

        API_URL = new StrSubstitutor(paramMap).replace(urlTemplate);

        this.client = ClientBuilder
            .newClient()
            .register(JacksonJsonProvider.class);
    }

    private Response doGet(String resource, String params) {
        Map paramMap = new HashMap();
        paramMap.put("resource", resource);
        paramMap.put("params", params);

        String target = new StrSubstitutor(paramMap).replace(API_URL);

        return client
            .target(target)
            .request()
            .get();
    }

    @Override
    public FsResponse<FsResponseVenueSearch> searchVenues(FsSearchParams params) {
        Response resp = doGet("search", params.getAsQueryParam());
        return resp.readEntity(new GenericType<FsResponse<FsResponseVenueSearch>>() {
        });
    }

    @Override
    public FsResponse<FsResponseVenuePhotos> getVenuePhotos(String venueId) {
        Response resp = doGet(venueId + "/photos", "");
        return resp.readEntity(new GenericType<FsResponse<FsResponseVenuePhotos>>() {
        });
    }

    @Override
    public FsResponse<FsResponseVenue> getVenue(String venueId) {
        Response resp = doGet(venueId, "");
        return resp.readEntity(new GenericType<FsResponse<FsResponseVenue>>() {
        });
    }
}
