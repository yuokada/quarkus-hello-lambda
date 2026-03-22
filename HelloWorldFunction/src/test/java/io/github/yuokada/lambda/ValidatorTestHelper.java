package io.github.yuokada.lambda;

import java.lang.reflect.Proxy;
import java.util.Collections;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

class ValidatorTestHelper {

    private static final Validator REAL_VALIDATOR =
            Validation.buildDefaultValidatorFactory().getValidator();

    static Validator realValidator() {
        return REAL_VALIDATOR;
    }

    static GreetingService serviceWith(Validator validator) {
        return new GreetingService(validator);
    }

    static Validator permissiveValidator() {
        return (Validator)
                Proxy.newProxyInstance(
                        Validator.class.getClassLoader(),
                        new Class<?>[] {Validator.class},
                        (proxy, method, args) -> {
                            if ("validate".equals(method.getName())) {
                                return Collections.<ConstraintViolation<Object>>emptySet();
                            }
                            if ("forExecutables".equals(method.getName())
                                    || "unwrap".equals(method.getName())) {
                                throw new UnsupportedOperationException();
                            }
                            return defaultValue(method.getReturnType());
                        });
    }

    private static Object defaultValue(Class<?> type) {
        if (!type.isPrimitive()) {
            return null;
        }
        if (boolean.class.equals(type)) {
            return false;
        }
        if (char.class.equals(type)) {
            return '\0';
        }
        if (byte.class.equals(type)
                || short.class.equals(type)
                || int.class.equals(type)
                || long.class.equals(type)) {
            return 0;
        }
        if (float.class.equals(type) || double.class.equals(type)) {
            return 0.0;
        }
        return null;
    }
}
