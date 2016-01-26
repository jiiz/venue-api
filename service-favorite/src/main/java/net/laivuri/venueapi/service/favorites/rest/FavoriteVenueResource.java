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

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.laivuri.venueapi.service.common.dto.DTOValidator;
import net.laivuri.venueapi.service.common.exp.EntityInvalidDataException;
import net.laivuri.venueapi.service.common.exp.EntityNotFoundException;
import net.laivuri.venueapi.service.common.exp.EntityStorageException;
import net.laivuri.venueapi.service.favorites.dto.FavoriteVenue;
import net.laivuri.venueapi.service.favorites.dto.FavoriteVenueData;
import net.laivuri.venueapi.service.favorites.store.FavoriteVenueStore;
import org.springframework.beans.factory.annotation.Autowired;
import net.laivuri.venueapi.service.common.annotation.SuccessfulStatus;

/**
 *
 * @author Juhani Laitakari 
 *
 * Favorite venue subresource
 */
public class FavoriteVenueResource {

    private String id;

    @Autowired
    private FavoriteVenueStore favoriteStore;

    public void setId(String id) {
        this.id = id;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public FavoriteVenue get() throws EntityNotFoundException, EntityStorageException {
        return favoriteStore.get(id);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @SuccessfulStatus(status = Response.Status.OK)
    public void update(FavoriteVenueData newVenueData) throws
            EntityInvalidDataException,
            EntityNotFoundException,
            EntityStorageException {

        DTOValidator<FavoriteVenueData> validator = new DTOValidator<>();
        validator.checkValidity(newVenueData);
        favoriteStore.update(id, newVenueData);
    }

    @DELETE
    @SuccessfulStatus(status = Response.Status.OK)
    public void delete() throws EntityNotFoundException, EntityStorageException {
        favoriteStore.delete(id);
    }
}
