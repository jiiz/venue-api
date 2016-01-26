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
package net.laivuri.venueapi.service.favorites.store;

import java.util.List;
import net.laivuri.venueapi.service.common.exp.EntityAlreadyExistsException;
import net.laivuri.venueapi.service.common.exp.EntityNotFoundException;
import net.laivuri.venueapi.service.common.exp.EntityStorageException;
import net.laivuri.venueapi.service.favorites.dto.FavoriteVenue;
import net.laivuri.venueapi.service.favorites.dto.FavoriteVenueData;

/**
 *
 * @author Juhani Laitakari 
 *
 */
public interface FavoriteVenueStore {

    /**
     * Retrieve all favorite venues in the storage
     *
     * @return Result list
     * @throws EntityStorageException error while connecting to the storage
     */
    public List<FavoriteVenue> getAll() throws EntityStorageException;

    /**
     * Retrieve single favorite venue from the storage
     *
     * @param id id of the venue
     * @return corresponding favorite venue
     * @throws EntityNotFoundException no favorite venue found with the given id
     * @throws EntityStorageException error while connecting to the storage
     */
    public FavoriteVenue get(String id) throws EntityNotFoundException, EntityStorageException;

    /**
     * Store new favorite venue
     *
     * @param newVenue favorite venue data to be stored
     * @throws EntityAlreadyExistsException favorite venue with the given venue id already exists
     * @throws EntityStorageException error while connecting to the storage
     */
    public void store(FavoriteVenue newVenue) throws EntityAlreadyExistsException, EntityStorageException;

    /**
     * Update favorite venue data
     *
     * @param id id of the venue
     * @param updateData The new data for the venue
     * @throws EntityNotFoundException no favorite venue found with the given id
     * @throws EntityStorageException error while connecting to the storage
     */
    public void update(String id, FavoriteVenueData updateData) throws EntityNotFoundException, EntityStorageException;

    /**
     * Delete favorite venue
     *
     * @param id id of the venue
     * @throws EntityNotFoundException no favorite venue found with the given id
     * @throws EntityStorageException error while connecting to the storage
     */
    public void delete(String id) throws EntityNotFoundException, EntityStorageException;

    /**
     * Delete all favorite venues
     *
     * @throws EntityStorageException error while connecting to the storage
     */
    public void deleteAll() throws EntityStorageException;
}
