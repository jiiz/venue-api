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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.core.MediaType;
import static net.laivuri.venueapi.service.common.itest.AbstractVenueApiTest.testUserName;
import static net.laivuri.venueapi.service.common.itest.AbstractVenueApiTest.testUserPwd;
import net.laivuri.venueapi.service.favorites.TestData;
import net.laivuri.venueapi.service.favorites.dto.FavoriteVenue;
import static net.laivuri.venueapi.service.favorites.itest.AbstractFavoriteVenueApiTest.CSRF_HEADER;
import org.junit.Test;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.unitils.reflectionassert.ReflectionComparatorMode.LENIENT_ORDER;

/**
 *
 * @author Juhani Laitakari
 *
 * Integration tests for favorite venue resource collection
 */
public class FavoriteVenueResourceIT extends AbstractFavoriteVenueApiTest {

    @Test
    public void testGet_Error_NotFound() throws Exception {

        expect().
                statusCode(404).
                when().
                get(UUID.randomUUID().toString());
    }

    @Test
    public void testGet_Success() throws Exception {

        FavoriteVenue expected = TestData.getFreshTestData().get(0);
        FavoriteVenue result = expect().
                statusCode(200).
                get(expected.getId()).
                as(FavoriteVenue.class);
        assertReflectionEquals(expected, result, LENIENT_ORDER);
    }

    @Test
    public void testUpdateKeywords_Error_NoAuth() throws Exception {

        FavoriteVenue expResult = TestData.getFreshTestData().get(0);

        List<String> updateData = new ArrayList<>();
        updateData.add("newkeyword");

        expect().
                statusCode(401).
                when().
                given().
                header(CSRF_HEADER).
                contentType(MediaType.APPLICATION_JSON).
                body(updateData).
                put(expResult.getId() + "/keywords");

        // check that nothing was updated
        FavoriteVenue result
                = get(expResult.getId()).
                as(FavoriteVenue.class);
        assertReflectionEquals(expResult, result, LENIENT_ORDER);
    }

    @Test
    public void testUpdateKeywords_Error_NoCSRFHeader() throws Exception {

        FavoriteVenue expResult = TestData.getFreshTestData().get(0);

        List<String> updateData = new ArrayList<>();
        updateData.add("newkeyword");

        expect().
                statusCode(400).
                when().
                given().
                auth().preemptive().basic(testUserName, testUserPwd).
                contentType(MediaType.APPLICATION_JSON).
                body(updateData).
                put(expResult.getId() + "/keywords");

        // check that nothing was updated
        FavoriteVenue result
                = get(expResult.getId()).
                as(FavoriteVenue.class);
        assertReflectionEquals(expResult, result, LENIENT_ORDER);
    }

    @Test
    public void testUpdateKeywords_Error_InvalidData() throws Exception {

        FavoriteVenue expResult = TestData.getFreshTestData().get(0);

        List<String> invalidData = new ArrayList<>();
        invalidData.add(" ");
        invalidData.add("");

        expect().
                statusCode(400).
                when().
                given().
                header(CSRF_HEADER).
                auth().preemptive().basic(testUserName, testUserPwd).
                contentType(MediaType.APPLICATION_JSON).
                body(invalidData).
                put(expResult.getId() + "/keywords");
    }

    @Test
    public void testUpdateKeywords_Error_NotFound() throws Exception {

        FavoriteVenue expResult = TestData.getFreshTestData().get(0);

        List<String> updateData = new ArrayList<>();
        updateData.add("newkeyword");

        expect().
                statusCode(404).
                when().
                given().
                header(CSRF_HEADER).
                auth().preemptive().basic(testUserName, testUserPwd).
                contentType(MediaType.APPLICATION_JSON).
                body(updateData).
                put(UUID.randomUUID().toString() + "/keywords");

        // check that nothing was updated
        FavoriteVenue result
                = get(expResult.getId()).
                as(FavoriteVenue.class);
        assertReflectionEquals(expResult, result, LENIENT_ORDER);
    }

    @Test
    public void testUpdateKeywords_Success() throws Exception {

        FavoriteVenue expected = TestData.getFreshTestData().get(0);

        List<String> updateData = new ArrayList<>();
        updateData.add("newkeyword");

        expected.setKeywords(updateData);

        expect().
                statusCode(200).
                when().
                given().
                header(CSRF_HEADER).
                auth().preemptive().basic(testUserName, testUserPwd).
                contentType(MediaType.APPLICATION_JSON).
                body(updateData).
                put(expected.getId() + "/keywords");

        // check that data was updated correctly
        FavoriteVenue result
                = get(expected.getId()).
                as(FavoriteVenue.class);
        assertReflectionEquals(expected, result, LENIENT_ORDER);
    }

    @Test
    public void testDelete_Error_NoAuth() throws Exception {

        List<FavoriteVenue> expected = TestData.getFreshTestData();
        FavoriteVenue deleted = expected.get(0);

        expect().
                statusCode(401).
                when().
                given().
                header(CSRF_HEADER).
                delete(deleted.getId());

        // check that nothing was deleted
        FavoriteVenue[] result
                = get("").
                as(FavoriteVenue[].class);
        assertReflectionEquals(expected, result, LENIENT_ORDER);;
    }

    @Test
    public void testDelete_Error_NoCSRFHeader() throws Exception {

        List<FavoriteVenue> expected = TestData.getFreshTestData();
        FavoriteVenue deleted = expected.get(0);

        expect().
                statusCode(400).
                when().
                given().
                auth().preemptive().basic(testUserName, testUserPwd).
                delete(deleted.getId());

        // check that nothing was deleted
        FavoriteVenue[] result
                = get("").
                as(FavoriteVenue[].class);
        assertReflectionEquals(expected, result, LENIENT_ORDER);;
    }

    @Test
    public void testDelete_Error_NotFound() throws Exception {

        expect().
                statusCode(404).
                when().
                given().
                header(CSRF_HEADER).
                auth().preemptive().basic(testUserName, testUserPwd).
                delete(UUID.randomUUID().toString());

        // check that nothing was deleted
        List<FavoriteVenue> expected = TestData.getFreshTestData();
        FavoriteVenue[] result
                = get("").
                as(FavoriteVenue[].class);
        assertReflectionEquals(expected, result, LENIENT_ORDER);
    }

    @Test
    public void testDelete_Success() throws Exception {

        List<FavoriteVenue> expected = TestData.getFreshTestData();
        FavoriteVenue deleted = expected.remove(0);

        expect().
                statusCode(200).
                when().
                given().
                header(CSRF_HEADER).
                auth().preemptive().basic(testUserName, testUserPwd).
                delete(deleted.getId());

        // check that data was deleted
        FavoriteVenue[] result
                = get("").
                as(FavoriteVenue[].class);
        assertReflectionEquals(expected, result, LENIENT_ORDER);
    }

}
