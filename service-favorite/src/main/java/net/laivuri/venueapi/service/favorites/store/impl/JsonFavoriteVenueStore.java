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
package net.laivuri.venueapi.service.favorites.store.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.laivuri.venueapi.service.common.exp.EntityAlreadyExistsException;
import net.laivuri.venueapi.service.common.exp.EntityNotFoundException;
import net.laivuri.venueapi.service.common.exp.EntityStorageException;
import net.laivuri.venueapi.service.favorites.dto.FavoriteVenue;
import net.laivuri.venueapi.service.favorites.dto.FavoriteVenueData;
import net.laivuri.venueapi.service.favorites.store.FavoriteVenueStore;
import org.springframework.beans.BeanUtils;

/**
 *
 * @author Juhani Laitakari 
 *
 * JSON based implementation of {@link net.laivuri.venueapi.service.favorites.store.FavoriteVenueStore} providing file
 * based persistence.
 *
 * Read and write operations are synchronized with a static instance of {@link java.util.concurrent.locks.ReadWriteLock}
 * which allows single write and multiple reads to the storage file.
 */
public class JsonFavoriteVenueStore implements FavoriteVenueStore {

    private static final Logger LOG = Logger.getLogger(JsonFavoriteVenueStore.class.getName());
    private static final ReadWriteLock LOCK = new ReentrantReadWriteLock();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private File storageFile;

    public JsonFavoriteVenueStore(String storageFile) {
        this.storageFile = new File(storageFile);
        if (!this.storageFile.exists()) {
            try {
                storeData(new ArrayList<FavoriteVenue>());
            } catch (EntityStorageException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    private List<FavoriteVenue> loadData() throws EntityStorageException {
        LOCK.readLock().lock();
        try {
            return MAPPER.readValue(storageFile, new TypeReference<List<FavoriteVenue>>() {
            });
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            throw new EntityStorageException("Cannot connect to favorite venue storage");
        } finally {
            LOCK.readLock().unlock();
        }
    }

    private void storeData(List<FavoriteVenue> data) throws EntityStorageException {
        LOCK.writeLock().lock();
        try {
            MAPPER.writeValue(storageFile, data);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            throw new EntityStorageException("Cannot connect to favorite venue storage");
        } finally {
            LOCK.writeLock().unlock();
        }
    }

    private FavoriteVenue findOneById(List<FavoriteVenue> favorites, String id) {
        FavoriteVenue result = null;
        for (FavoriteVenue fav : favorites) {
            if (fav.getId().equals(id)) {
                result = fav;
                break;
            }
        }
        return result;
    }

    @Override
    public List<FavoriteVenue> getAll() throws EntityStorageException {
        return loadData();
    }

    @Override
    public FavoriteVenue get(String id) throws EntityNotFoundException, EntityStorageException {
        FavoriteVenue result = findOneById(loadData(), id);
        if (result == null) {
            throw new EntityNotFoundException("Favorite venue with id '" + id + "' not found.");
        }
        return result;
    }

    @Override
    public void store(FavoriteVenue newVenue) throws EntityAlreadyExistsException, EntityStorageException {
        String id = newVenue.getId();

        List<FavoriteVenue> favorites = loadData();
        if (findOneById(favorites, id) != null) {
            throw new EntityAlreadyExistsException("Favorite venue with id '" + id + "' already exists.");
        }
        favorites.add(newVenue);

        storeData(favorites);
    }

    @Override
    public void update(String id, FavoriteVenueData updateData) throws EntityNotFoundException, EntityStorageException {
        List<FavoriteVenue> favorites = loadData();

        FavoriteVenue result = findOneById(favorites, id);
        if (result == null) {
            throw new EntityNotFoundException("Favorite venue with id '" + id + "' not found.");
        }
        BeanUtils.copyProperties(updateData, result); // updates result with the new data

        storeData(favorites);
    }

    @Override
    public void delete(String id) throws EntityNotFoundException, EntityStorageException {
        List<FavoriteVenue> favorites = loadData();

        FavoriteVenue result = findOneById(favorites, id);
        if (result == null) {
            throw new EntityNotFoundException("Favorite venue with id '" + id + "' not found.");
        }
        favorites.remove(result);

        storeData(favorites);
    }

    @Override
    public void deleteAll() throws EntityStorageException {
        storeData(new ArrayList<FavoriteVenue>());
    }

}
