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
package net.laivuri.venueapi.service.favorites.store;

import java.util.List;
import java.util.UUID;
import net.laivuri.venueapi.service.common.exp.EntityAlreadyExistsException;
import net.laivuri.venueapi.service.common.exp.EntityNotFoundException;
import net.laivuri.venueapi.service.favorites.TestData;
import net.laivuri.venueapi.service.favorites.dto.FavoriteVenue;
import net.laivuri.venueapi.service.favorites.dto.FavoriteVenueData;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.springframework.beans.BeanUtils;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.unitils.reflectionassert.ReflectionComparatorMode.LENIENT_ORDER;

/**
 *
 * @author Juhani Laitakari
 *
 * Abstract base class that executes tests against
 * {@link net.laivuri.venueapi.service.favorites.store.FavoriteVenueStore} and can be extended to implement unit or
 * integration tests for different implementations of the store.
 *
 */
public abstract class AbstractFavoriteVenueStoreTest {

    protected abstract FavoriteVenueStore getStoreInstance();

    @Before
    public void setUp() throws Exception {
        FavoriteVenueStore instance = getStoreInstance();
        instance.deleteAll();
        for (FavoriteVenue fav : TestData.getFreshTestData()) {
            instance.store(fav);
        }
    }

    @Rule
    public ExpectedException expectedExp = ExpectedException.none();

    /**
     * Test of getAll method, of class JsonFavoriteVenueStore.
     */
    @org.junit.Test
    public void testGetAll() throws Exception {
        FavoriteVenueStore instance = getStoreInstance();

        List<FavoriteVenue> expResult = TestData.getFreshTestData();

        List<FavoriteVenue> result = instance.getAll();
        assertReflectionEquals(expResult, result, LENIENT_ORDER);
    }

    /**
     * Test of get method, of class JsonFavoriteVenueStore.
     */
    @org.junit.Test
    public void testGet_Error_NotFound() throws Exception {
        FavoriteVenueStore instance = getStoreInstance();

        FavoriteVenue expResult = TestData.getFreshTestData().get(0);
        expResult.setId(UUID.randomUUID().toString());

        expectedExp.expect(EntityNotFoundException.class);
        instance.get(expResult.getId());
    }

    /**
     * Test of get method, of class JsonFavoriteVenueStore.
     */
    @org.junit.Test
    public void testGet_Success() throws Exception {
        FavoriteVenueStore instance = getStoreInstance();

        FavoriteVenue expResult = TestData.getFreshTestData().get(0);

        FavoriteVenue result = instance.get(expResult.getId());
        assertReflectionEquals(expResult, result, LENIENT_ORDER);
    }

    /**
     * Test of store method, of class JsonFavoriteVenueStore.
     */
    @org.junit.Test
    public void testStore_Error_AlreadyExists() throws Exception {
        FavoriteVenueStore instance = getStoreInstance();

        FavoriteVenue storedItem = TestData.getFreshTestData().get(0);

        // try to store item
        expectedExp.expect(EntityAlreadyExistsException.class);
        instance.store(storedItem);

        // check that it is not stored
        List<FavoriteVenue> expResult = TestData.getFreshTestData();
        expResult.add(storedItem);
        List<FavoriteVenue> result = instance.getAll();
        assertReflectionEquals(expResult, result, LENIENT_ORDER);
    }

    /**
     * Test of store method, of class JsonFavoriteVenueStore.
     */
    @org.junit.Test
    public void testStore_Success() throws Exception {
        FavoriteVenueStore instance = getStoreInstance();

        FavoriteVenue storedItem = TestData.getFreshTestData().get(0);

        // store item
        storedItem.setId(UUID.randomUUID().toString());
        instance.store(storedItem);

        // check that it is stored
        List<FavoriteVenue> expResult = TestData.getFreshTestData();
        expResult.add(storedItem);
        List<FavoriteVenue> result = instance.getAll();
        assertReflectionEquals(expResult, result, LENIENT_ORDER);
    }

    /**
     * Test of update method, of class JsonFavoriteVenueStore.
     */
    @org.junit.Test
    public void testUpdate_Error_NotFound() throws Exception {
        FavoriteVenueStore instance = getStoreInstance();

        FavoriteVenue expResult = TestData.getFreshTestData().get(0);
        expResult.setId(UUID.randomUUID().toString());
        FavoriteVenueData updateData = new FavoriteVenueData();
        BeanUtils.copyProperties(expResult, updateData);

        // try to update
        expectedExp.expect(EntityNotFoundException.class);
        instance.update(expResult.getId(), updateData);
    }

    /**
     * Test of update method, of class JsonFavoriteVenueStore.
     */
    @org.junit.Test
    public void testUpdate_Success() throws Exception {
        FavoriteVenueStore instance = getStoreInstance();

        List<FavoriteVenue> expResult = TestData.getFreshTestData();
        FavoriteVenue updated = expResult.get(0);
        updated.setName(UUID.randomUUID().toString());
        updated.getKeywords().remove(0);
        updated.getKeywords().add("newkeyword");
        FavoriteVenueData updateData = new FavoriteVenueData();
        BeanUtils.copyProperties(updated, updateData);

        // update
        instance.update(updated.getId(), updateData);

        // check that data was updated
        List<FavoriteVenue> result = instance.getAll();
        assertReflectionEquals(expResult, result, LENIENT_ORDER);
    }

    /**
     * Test of delete method, of class JsonFavoriteVenueStore.
     */
    @org.junit.Test
    public void testDelete_Error_NotFound() throws Exception {
        FavoriteVenueStore instance = getStoreInstance();

        // try to delete
        expectedExp.expect(EntityNotFoundException.class);
        instance.delete(UUID.randomUUID().toString());
    }

    /**
     * Test of delete method, of class JsonFavoriteVenueStore.
     */
    @org.junit.Test
    public void testDelete_Error_Success() throws Exception {
        FavoriteVenueStore instance = getStoreInstance();

        List<FavoriteVenue> expResult = TestData.getFreshTestData();
        String id = expResult.remove(0).getId();

        // delete
        instance.delete(id);

        // check that data was deleted
        List<FavoriteVenue> result = instance.getAll();
        assertReflectionEquals(expResult, result, LENIENT_ORDER);
    }
}
