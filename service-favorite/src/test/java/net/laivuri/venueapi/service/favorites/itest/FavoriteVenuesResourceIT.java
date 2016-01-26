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
package net.laivuri.venueapi.service.favorites.itest;

import static com.jayway.restassured.RestAssured.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.core.MediaType;
import net.laivuri.venueapi.service.favorites.TestData;
import net.laivuri.venueapi.service.favorites.dto.FavoriteVenue;
import org.junit.Test;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.unitils.reflectionassert.ReflectionComparatorMode.LENIENT_ORDER;

/**
 *
 * @author Juhani Laitakari
 *
 * Integration tests for favorite resource
 */
public class FavoriteVenuesResourceIT extends AbstractFavoriteVenueApiTest {

    private void testCreateWithInvalidData(FavoriteVenue invalidData) throws IOException {
        expect().
            statusCode(400).
            when().
            given().
            header(CSRF_HEADER).
            contentType(MediaType.APPLICATION_JSON).
            body(invalidData).
            post("");

        // check that nothing was added
        List<FavoriteVenue> expected = TestData.getFreshTestData();
        FavoriteVenue[] result
            = get("").
            as(FavoriteVenue[].class);
        assertReflectionEquals(expected, result, LENIENT_ORDER);
    }

    @Test
    public void testList_Success() throws Exception {

        List<FavoriteVenue> expected = TestData.getFreshTestData();
        FavoriteVenue[] result = expect().
            statusCode(200).
            when().
            get("").
            as(FavoriteVenue[].class);
        assertReflectionEquals(expected, result, LENIENT_ORDER);
    }

    @Test
    public void testCreate_Error_NoCSRFHeader() throws Exception {

        FavoriteVenue storedItem = TestData.getFreshTestData().get(0);
        storedItem.setId(UUID.randomUUID().toString());
        expect().
            statusCode(400).
            when().
            given().
            contentType(MediaType.APPLICATION_JSON).
            body(storedItem).
            post("");

        // check that nothing was added
        List<FavoriteVenue> expected = TestData.getFreshTestData();
        FavoriteVenue[] result
            = get("").
            as(FavoriteVenue[].class);
        assertReflectionEquals(expected, result, LENIENT_ORDER);
    }

    @Test
    public void testCreate_Error_AlreadyExists() throws Exception {

        List<FavoriteVenue> expected = TestData.getFreshTestData();
        expect().
            statusCode(409).
            when().
            given().
            header(CSRF_HEADER).
            contentType(MediaType.APPLICATION_JSON).
            body(expected.get(0)).
            post("");

        // check that nothing was added
        FavoriteVenue[] result
            = get("").
            as(FavoriteVenue[].class);
        assertReflectionEquals(expected, result, LENIENT_ORDER);
    }

    @Test
    public void testCreate_Error_InvalidData_Id() throws Exception {

        // invalid id
        FavoriteVenue storedItem = TestData.getFreshTestData().get(0);
        storedItem.setId(" ");
        testCreateWithInvalidData(storedItem);

        // invalid name
        storedItem = TestData.getFreshTestData().get(0);
        storedItem.setName(" ");
        testCreateWithInvalidData(storedItem);

        // malformed URL
        storedItem = TestData.getFreshTestData().get(0);
        storedItem.setUrl("not-a-url");
        testCreateWithInvalidData(storedItem);
    }

    @Test
    public void testCreate_Success() throws Exception {

        FavoriteVenue storedItem = TestData.getFreshTestData().get(0);
        storedItem.setId(UUID.randomUUID().toString());
        expect().
            statusCode(201).
            when().
            given().
            header(CSRF_HEADER).
            contentType(MediaType.APPLICATION_JSON).
            body(storedItem).
            post("");

        // check that stored data is found
        List<FavoriteVenue> expected = TestData.getFreshTestData();
        expected.add(storedItem);
        FavoriteVenue[] result = expect().
            statusCode(200).
            when().
            get("").
            as(FavoriteVenue[].class);
        assertReflectionEquals(expected, result, LENIENT_ORDER);
    }

    @Test
    public void testDeleteAll_Error_NoCSRFHeader() throws Exception {

        expect().
            statusCode(400).
            when().
            delete("");

        List<FavoriteVenue> expected = TestData.getFreshTestData();
        // check that nothing was deleted
        FavoriteVenue[] result = expect().
            when().
            get("").
            as(FavoriteVenue[].class);
        assertReflectionEquals(expected, result, LENIENT_ORDER);
    }

    @Test
    public void testDeleteAll_Success() throws Exception {

        expect().
            statusCode(200).
            when().
            given().
            header(CSRF_HEADER).
            delete("");

        List<FavoriteVenue> expected = new ArrayList<>();
        // check that all data was deleted
        FavoriteVenue[] result = expect().
            when().
            get("").
            as(FavoriteVenue[].class);
        assertReflectionEquals(expected, result, LENIENT_ORDER);
    }

}
