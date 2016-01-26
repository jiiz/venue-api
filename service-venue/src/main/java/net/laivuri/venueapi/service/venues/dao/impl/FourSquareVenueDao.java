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
package net.laivuri.venueapi.service.venues.dao.impl;

import java.util.ArrayList;
import java.util.List;
import net.laivuri.venueapi.service.common.exp.EntityNotFoundException;
import net.laivuri.venueapi.service.common.exp.EntityStorageException;
import net.laivuri.venueapi.service.venues.dao.VenueDAO;
import net.laivuri.venueapi.service.venues.dto.Venue;
import net.laivuri.venueapi.service.venues.dto.VenueSummary;
import net.laivuri.venueapi.service.venues.fs.FourSquareClient;
import net.laivuri.venueapi.service.venues.fs.FsResponse;
import net.laivuri.venueapi.service.venues.fs.FsResponseVenueSearch;
import net.laivuri.venueapi.service.venues.fs.FsResponseVenue;
import net.laivuri.venueapi.service.venues.fs.FsResponseVenuePhotos;
import net.laivuri.venueapi.service.venues.fs.FsSearchParams;
import net.laivuri.venueapi.service.venues.fs.data.FsPhoto;
import net.laivuri.venueapi.service.venues.fs.data.FsVenue;
import org.springframework.beans.BeanUtils;

/**
 *
 * @author Juhani Laitakari
 *
 * Data access object that converts FourSquare client response data into venue api format
 */
public class FourSquareVenueDao implements VenueDAO {

    private final FourSquareClient fsClient;

    public FourSquareVenueDao(FourSquareClient fsClient) {
        this.fsClient = fsClient;
    }

    private void checkVenueResponseStatus(FsResponse response, String venueId) throws EntityNotFoundException, EntityStorageException {
        // foursquare api responds with 400 for venue not found
        if ("400".equals(response.getMeta().getCode())) {
            throw new EntityNotFoundException("No FourSquare venue with id '" + venueId + "'");
        } else if (!"200".equals(response.getMeta().getCode())) {
            throw new EntityStorageException("Could not get response from FourSquare API");
        }
    }

    @Override
    public List<Venue> searchVenues(String locName, String locCoords, String query) throws EntityStorageException {
        FsSearchParams params = new FsSearchParams();
        params.setNear(locName);
        params.setLl(locCoords);
        params.setQuery(query);

        FsResponse<FsResponseVenueSearch> response = fsClient.searchVenues(params);

        if (!"200".equals(response.getMeta().getCode())) {
            throw new EntityStorageException("Could not get response from FourSquare API");
        }

        List<Venue> venues = new ArrayList<>();
        for (FsVenue fsVenue : response.getResponse().getVenues()) {
            Venue venue = new Venue();
            BeanUtils.copyProperties(fsVenue, venue);
            // add 'address' and 'phone' manually as they are in nested structures in foursquare data
            // and thus weren't copied automatically
            venue.setPhone(fsVenue.getContact().getPhone());
            venue.setAddress(fsVenue.getLocation().getAddress());
            venues.add(venue);
        }
        return venues;
    }

    @Override
    public List<String> getVenuePhotos(String venueId) throws EntityNotFoundException, EntityStorageException {
        FsResponse<FsResponseVenuePhotos> response = fsClient.getVenuePhotos(venueId);
        checkVenueResponseStatus(response, venueId);

        List<String> photos = new ArrayList<>();
        for (FsPhoto fsPhoto : response.getResponse().getPhotos().getItems()) {
            photos.add(fsPhoto.getAssembledUrl());
        }
        return photos;
    }

    @Override
    public VenueSummary getVenueSummary(String venueId) throws EntityNotFoundException, EntityStorageException {
        VenueSummary summary = new VenueSummary();

        // get photos count
        FsResponse<FsResponseVenuePhotos> photoResp = fsClient.getVenuePhotos(venueId);
        checkVenueResponseStatus(photoResp, venueId);

        summary.setPhotosCount(photoResp.getResponse().getPhotos().getCount());

        // get other stats
        FsResponse<FsResponseVenue> venueResp = fsClient.getVenue(venueId);
        checkVenueResponseStatus(venueResp, venueId);

        BeanUtils.copyProperties(venueResp.getResponse().getVenue().getStats(), summary);

        return summary;
    }
}
