package net.laivuri.venueapi.service.venues.itest;

import com.jayway.restassured.RestAssured;
import java.io.IOException;
import net.laivuri.venueapi.service.common.itest.AbstractVenueApiTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.util.StringUtils;

/**
 *
 * @author Juhani Laitakari
 */
public abstract class AbstractVenueSearchApiTest extends AbstractVenueApiTest {

    private static final String PARAM_TEST_FS_HOST = "foursquare.host";
    private static final int DEFAULT_FS_TEST_PORT = 8081;

    @BeforeClass
    public static void ensureRestAssuredBasePath() {
        if (StringUtils.isEmpty(RestAssured.basePath)) {
            RestAssured.basePath = "venues";
        }
    }

    @BeforeClass
    public static void setupClass() throws IOException {
        int port = DEFAULT_FS_TEST_PORT;
        String host = System.getProperty(PARAM_TEST_FS_HOST);
        if (!StringUtils.isEmpty(host)) {
            port = Integer.parseInt(host.substring(host.indexOf(":")));
        }
        FourSquareServerStub.start(port);
    }

    @AfterClass
    public static void teardownClass() {
        FourSquareServerStub.stop();
    }
}
