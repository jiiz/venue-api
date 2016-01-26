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
package net.laivuri.venueapi.service.venues.rest;

import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import net.laivuri.venueapi.service.common.exp.EntityStorageException;
import net.laivuri.venueapi.service.venues.dao.VenueDAO;
import net.laivuri.venueapi.service.venues.dto.Venue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 *
 * @author Juhani Laitakari
 */
@Path("/")
public class VenuesResource {

    @Autowired
    private VenueDAO dao;

    @Context
    ResourceContext rc;

    @Path("{id}")
    public VenueResource getVenueSubResource(@PathParam("id") String venueId) {
        // instantiate subresource with ResourceContext in order to allow @Autowired injection in the subresource too
        VenueResource vr = rc.getResource(VenueResource.class);
        vr.setId(venueId);
        return vr;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Venue> searchVenues(
        @QueryParam("locName") String locName,
        @QueryParam("locCoords") String locCoords,
        @QueryParam("query") String query
    ) throws EntityStorageException {
        // Either location name or coordinates is mandatory
        if (StringUtils.isEmpty(locName) && StringUtils.isEmpty(locCoords)) {
            throw new BadRequestException("Either 'locName' or 'locCoords' parameter must be given");
        }

        return dao.searchVenues(locName, locCoords, query);
    }
}
