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
package net.laivuri.venueapi.service.common.dto;

import net.laivuri.venueapi.service.common.exp.EntityInvalidDataException;
import org.hibernate.validator.constraints.NotBlank;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author Juhani Laitakari
 */
public class DTOValidatorUT {

    public DTOValidatorUT() {
    }

    @Rule
    public ExpectedException expectedExp = ExpectedException.none();

    /**
     * Test of checkValidity method, of class DTOValidator.
     */
    @Test
    public void testCheckValidity() throws EntityInvalidDataException {

        DTOValidator<TestDTO> instance = new DTOValidator<>();

        TestDTO invalid = new TestDTO();
        invalid.setTestParam1(" ");
        invalid.setTestParam2(" ");

        expectedExp.expect(EntityInvalidDataException.class);
        expectedExp.expectMessage(containsString(TestDTO.ERROR_MESSAGE_1));
        expectedExp.expectMessage(containsString(", "));
        expectedExp.expectMessage(containsString(TestDTO.ERROR_MESSAGE_2));

        instance.checkValidity(invalid);
    }

    private static class TestDTO {

        private static final String ERROR_MESSAGE_1 = "error message 1";
        private static final String ERROR_MESSAGE_2 = "error message 2";

        @NotBlank(message = ERROR_MESSAGE_1)
        private String testParam1;
        @NotBlank(message = ERROR_MESSAGE_2)
        private String testParam2;

        /**
         * @return the testParam1
         */
        public String getTestParam1() {
            return testParam1;
        }

        /**
         * @param testParam1 the testParam1 to set
         */
        public void setTestParam1(String testParam1) {
            this.testParam1 = testParam1;
        }

        /**
         * @return the testParam2
         */
        public String getTestParam2() {
            return testParam2;
        }

        /**
         * @param testParam2 the testParam2 to set
         */
        public void setTestParam2(String testParam2) {
            this.testParam2 = testParam2;
        }

    }

}
