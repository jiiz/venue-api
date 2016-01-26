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

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import net.laivuri.venueapi.service.common.exp.EntityInvalidDataException;

/**
 *
 * @author Juhani Laitakari 
 * @param <T> DTO type to be validated
 */
public final class DTOValidator<T> {

    private final Validator validator;

    public DTOValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    /**
     * Check venue api DTO validity
     *
     * @param dto DTO to be checked
     * @throws EntityInvalidDataException if DTO is not valid
     */
    public void checkValidity(T dto) throws EntityInvalidDataException {

        Set<ConstraintViolation<T>> errors = validator.validate(dto);

        if (!errors.isEmpty()) {
            StringBuilder b = new StringBuilder();
            for (ConstraintViolation error : errors) {
                b.append(error.getMessage());
                b.append(", ");
            }

            String error = b.toString();
            error = error.substring(0, error.length() - 2);

            throw new EntityInvalidDataException(error);
        }
    }

}
