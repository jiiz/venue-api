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
package net.laivuri.venueapi.service.venues.fs.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author Juhani Laitakari
 *
 * Partial data model of FourSquare Venue {@link https://developer.foursquare.com/docs/responses/venue}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FsVenue {

    private String id;
    private String name;
    private String url;
    private FsVenueContact contact;
    private FsVenueLocation location;
    private FsVenueStats stats;

    public FsVenue() {
        this.contact = new FsVenueContact();
        this.location = new FsVenueLocation();
        this.stats = new FsVenueStats();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FsVenueLocation {

        private String address;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FsVenueContact {

        private String phone;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FsVenueStats {

        private int checkinsCount;
        private int usersCount;
        private int tipCount;
        private int visitsCount;

        public int getCheckinsCount() {
            return checkinsCount;
        }

        public void setCheckinsCount(int checkinsCount) {
            this.checkinsCount = checkinsCount;
        }

        public int getUsersCount() {
            return usersCount;
        }

        public void setUsersCount(int usersCount) {
            this.usersCount = usersCount;
        }

        public int getTipCount() {
            return tipCount;
        }

        public void setTipCount(int tipCount) {
            this.tipCount = tipCount;
        }

        public int getVisitsCount() {
            return visitsCount;
        }

        public void setVisitsCount(int visitsCount) {
            this.visitsCount = visitsCount;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public FsVenueContact getContact() {
        return contact;
    }

    public void setContact(FsVenueContact contact) {
        this.contact = contact;
    }

    public FsVenueLocation getLocation() {
        return location;
    }

    public void setLocation(FsVenueLocation location) {
        this.location = location;
    }

    public FsVenueStats getStats() {
        return stats;
    }

    public void setStats(FsVenueStats stats) {
        this.stats = stats;
    }

}
