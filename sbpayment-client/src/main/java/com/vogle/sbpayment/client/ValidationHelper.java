package com.vogle.sbpayment.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Validator
 *
 * @author Allan Im
 **/
public class ValidationHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationHelper.class);

    private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();

    private static Validator getValidator() {
        return FACTORY.getValidator();
    }

    /**
     * Check validation with Bean Object
     *
     * @param params Bean Objects
     */
    @SafeVarargs
    public static <T> void beanValidate(final T... params) {
        for (T param : params) {

            if (param == null) {
                throw new IllegalArgumentException("param is null");
            }

            Set<ConstraintViolation<T>> constraintViolations = getValidator().validate(param);
            if (!constraintViolations.isEmpty()) {
                if (LOGGER.isWarnEnabled()) {
                    for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                        String rootBeanName = constraintViolation.getRootBeanClass().getSimpleName();
                        String targetProperty = constraintViolation.getPropertyPath().toString();
                        String message = constraintViolation.getMessage();
                        LOGGER.warn("SPS Validation fail {}.{} : {}", rootBeanName, targetProperty, message);
                    }
                }
                throw new ConstraintViolationException(constraintViolations);
            }
        }
    }

    /**
     * The param Must not be empty
     */
    public static void assertsNotEmpty(String keyName, String param) {
        if (param == null || param.isEmpty()) {
            throw new IllegalArgumentException(keyName + " is empty.");
        }
    }

    /**
     * The param Must not be NULL
     */
    public static void assertsNotNull(String keyName, Object param) {
        if (param == null) {
            throw new IllegalArgumentException(keyName + " is null.");
        }
    }

    /**
     * The amount Must be greater then Zero
     */
    public static void assertsAmount(Integer amount) {
        if (amount == null || amount < 1) {
            throw new IllegalArgumentException("The amount value MUST be grater then zero.");
        }
    }
}
