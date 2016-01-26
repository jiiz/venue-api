package net.laivuri.venueapi.service.common.itest;

import com.jayway.restassured.RestAssured;
import java.util.logging.Logger;
import org.junit.BeforeClass;
import org.springframework.util.StringUtils;

/**
 *
 * @author Juhani Laitakari
 */
public abstract class AbstractVenueApiTest {

    private static final Logger LOG = Logger.getLogger(AbstractVenueApiTest.class.getName());

    private static final String PARAM_TEST_BASE_URI = "test.baseUri";
    private static final String PARAM_TEST_PORT = "test.port";
    private static final String PARAM_TEST_BASE_PATH = "test.basePath";

    @BeforeClass
    public static void setupRestAssured() {

        String baseUri = System.getProperty(PARAM_TEST_BASE_URI);
        String port = System.getProperty(PARAM_TEST_PORT);
        String basePath = System.getProperty(PARAM_TEST_BASE_PATH);

        if (!StringUtils.isEmpty(baseUri)) {
            RestAssured.baseURI = baseUri;
        } else {
            RestAssured.baseURI = "http://localhost";
        }
        if (!StringUtils.isEmpty(port)) {
            RestAssured.port = Integer.parseInt(port);
        } else {
            RestAssured.port = 8080;
        }
        if (!StringUtils.isEmpty(basePath)) {
            RestAssured.basePath = basePath;
        }

        LOG.info(
            "Api integration tests set to target api @ "
            + RestAssured.baseURI + ":"
            + RestAssured.port + "/"
            + RestAssured.basePath);
    }
}
