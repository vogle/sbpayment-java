package com.vogle.sbpayment;

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
class ParamValidator {

    private static final Logger logger = LoggerFactory.getLogger(ParamValidator.class);

    private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();

    private ParamValidator() {
    }

    private static Validator getValidator() {
        return FACTORY.getValidator();
    }

    @SafeVarargs
    static <T> void beanValidate(final T... params) {
        for (T param : params) {

            if (param == null) {
                throw new IllegalArgumentException("param is null");
            }

            Set<ConstraintViolation<T>> constraintViolations = getValidator().validate(param);
            if (constraintViolations.size() > 0) {
                if (logger.isWarnEnabled()) {
                    for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                        String rootBeanName = constraintViolation.getRootBeanClass().getSimpleName();
                        String targetProperty = constraintViolation.getPropertyPath().toString();
                        String message = constraintViolation.getMessage();
                        logger.warn("SPS Validation fail {}.{} : {}", rootBeanName, targetProperty, message);
                    }
                }
                throw new ConstraintViolationException(constraintViolations);
            }
        }
    }

    static void assertNotEmpty(String keyName, String param) {
        if (param == null || param.isEmpty()) {
            throw new IllegalArgumentException(keyName + " is empty");
        }
    }

    static void assertNotNull(String keyName, Object param) {
        if (param == null) {
            throw new IllegalArgumentException(keyName + " is null");
        }
    }

    static void assertAmount(Integer amount) {
        if (amount == null || amount < 1) {
            throw new IllegalArgumentException("Wrong amount value");
        }
    }
}
