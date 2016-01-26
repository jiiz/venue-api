/*
 * The MIT License
 *
 * Copyright 2016 Juhani Laitakari
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
package net.laivuri.venueapi.service.favorites.rest;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import net.laivuri.venueapi.service.common.dto.DTOValidator;
import net.laivuri.venueapi.service.common.exp.EntityAlreadyExistsException;
import net.laivuri.venueapi.service.common.exp.EntityInvalidDataException;
import net.laivuri.venueapi.service.common.exp.EntityStorageException;
import net.laivuri.venueapi.service.common.util.ResourceCreatedUtil;
import net.laivuri.venueapi.service.favorites.dto.FavoriteVenue;
import net.laivuri.venueapi.service.favorites.store.FavoriteVenueStore;
import org.springframework.beans.factory.annotation.Autowired;
import net.laivuri.venueapi.service.common.annotation.SuccessfulStatus;
import net.laivuri.venueapi.service.common.exp.EntityNotFoundException;

/**
 *
 * @author Juhani Laitakari 
 */
@Path("/")
public class FavoriteVenuesResource {

    @Autowired
    private FavoriteVenueStore favoriteStore;

    @Context
    private HttpServletResponse resp;
    @Context
    private UriInfo uriInfo;
    @Context
    private ResourceContext rc;

    @Path("{id}")
    public FavoriteVenueResource getSubResource(@PathParam("id") String id) {
        // instantiate subresource with ResourceContext in order to allow @Context injection in the subresource too
        FavoriteVenueResource res = rc.getResource(FavoriteVenueResource.class);
        res.setId(id);
        return res;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<FavoriteVenue> list() throws EntityStorageException {
        return favoriteStore.getAll();
    }

    @POST
    @SuccessfulStatus(status = Response.Status.CREATED)
    @Consumes(MediaType.APPLICATION_JSON)
    public void create(FavoriteVenue favorite) throws
        EntityInvalidDataException,
        EntityAlreadyExistsException,
        EntityStorageException {

        final DTOValidator<FavoriteVenue> validator = new DTOValidator<>();
        validator.checkValidity(favorite);
        favoriteStore.store(favorite);
        ResourceCreatedUtil.setLocationHeader(resp, uriInfo, favorite.getId());
    }

    @DELETE
    @SuccessfulStatus(status = Response.Status.OK)
    public void deleteAll() throws EntityNotFoundException, EntityStorageException {
        favoriteStore.deleteAll();
    }
}
