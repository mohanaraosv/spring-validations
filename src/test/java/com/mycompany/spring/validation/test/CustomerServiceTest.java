package com.mycompany.spring.validation.test;

import static org.junit.Assert.assertSame;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mycompany.spring.validation.CustomerService;
import com.mycompany.spring.validation.ValidationConfig;

@ContextConfiguration(classes = { ValidationConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class CustomerServiceTest {

    @Autowired
    private CustomerService service;

    @Test
    public void throwsViolationExceptionWhenAllArgumentsInvalid() {
        try {
            service.createUser(null, null, null);
        } catch (ConstraintViolationException ce) {
            assertSame(ce.getConstraintViolations().size() == 3, true);
        }
    }

    @Test
    public void throwsViolationExceptionWhen2ArgumentsInvalid() {
        try {
            service.createUser(null, null, "valid");
        } catch (ConstraintViolationException ce) {
            assertSame(ce.getConstraintViolations().size() == 2, true);
        }
    }

    @Test
    public void throwsViolationExceptionWhenEmailInvalidArgumentsInvalid() {
        try {
            service.createUser("invalid_email", "valid", "valid");
        } catch (ConstraintViolationException ce) {
            assertSame(ce.getConstraintViolations().size() == 1, true);
            Set<ConstraintViolation<?>> violations = ce.getConstraintViolations();
            assertSame("not a well-formed email address", violations.iterator().next().getMessage());
        }
    }

    @Test
    public void throwsViolationExceptionWhenReturnValueTooLong() {
        try {
            service.createUser("user@domain.com", "too_long_username", "valid");
        } catch (ConstraintViolationException ce) {
            assertSame(ce.getConstraintViolations().size() == 1, true);
            Set<ConstraintViolation<?>> violations = ce.getConstraintViolations();
            assertSame("may not be empty", violations.iterator().next().getMessage());
        }
    }

    @Test
    public void throwsViolationExceptionWhenCustomerRead() {
        try {
            service.readUser(null);
        } catch (ConstraintViolationException ce) {
            assertSame(ce.getConstraintViolations().size() == 1, true);
            Set<ConstraintViolation<?>> violations = ce.getConstraintViolations();
            assertSame("length must be between 3 and 5", violations.iterator().next().getMessage());
        }
    }

    @Test
    public void createsUser() {
        service.createUser("user@domain.com", "valid", "valid");
    }
}