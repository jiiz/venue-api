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
package net.laivuri.venueapi.service.favorites;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import net.laivuri.venueapi.service.favorites.dto.FavoriteVenue;
import net.laivuri.venueapi.service.favorites.store.AbstractFavoriteVenueStoreTest;
import org.springframework.beans.BeanUtils;

/**
 *
 * @author Juhani Laitakari
 */
public final class TestData {

    private static final String TEST_DATA_FILE = "/testdata-favorites.json";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static List<FavoriteVenue> testData;

    private TestData() {
    }

    private static void readTestData() throws IOException {
        try (InputStream is = AbstractFavoriteVenueStoreTest.class.getResourceAsStream(TEST_DATA_FILE)) {
            testData = MAPPER.readValue(is, new TypeReference<List<FavoriteVenue>>() {
            });
        }
    }

    public static List<FavoriteVenue> getFreshTestData() throws IOException {
        if (testData == null) {
            readTestData();
        }
        List<FavoriteVenue> copiedData = new ArrayList<>();
        for (FavoriteVenue source : testData) {
            FavoriteVenue copy = new FavoriteVenue();
            BeanUtils.copyProperties(source, copy);
            copiedData.add(copy);
        }
        return copiedData;
    }
}
