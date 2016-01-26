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
package net.laivuri.venueapi.service.favorites.store.impl;

import net.laivuri.venueapi.service.favorites.store.AbstractFavoriteVenueStoreTest;
import net.laivuri.venueapi.service.favorites.store.FavoriteVenueStore;

/**
 *
 * @author Juhani Laitakari
 *
 * Unit tests {@link net.laivuri.venueapi.service.favorites.store.impl.JsonFavoriteVenueStore} store implementation.
 *
 * As implementation is file based, implementing tests as unit tests instead of integration tests, can be seen feasible.
 */
public class JsonFavoriteVenueStoreUT extends AbstractFavoriteVenueStoreTest {

    private final JsonFavoriteVenueStore instance;

    public JsonFavoriteVenueStoreUT() {
        instance = new JsonFavoriteVenueStore(".storage-favorites_test.json");
    }

    @Override
    protected FavoriteVenueStore getStoreInstance() {
        return instance;
    }
}
