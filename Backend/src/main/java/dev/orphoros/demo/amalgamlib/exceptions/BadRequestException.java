package dev.orphoros.demo.amalgamlib.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class for modeling HTTP exceptions
 * <p>
 * The {@code BadRequestException} may be used in any repository or service.
 *
 * <dl>
 * <dt><span class="strong">HTTP Status code:</span></dt>
 * <dd>
 * 400 – Bad Request
 * </dd>
 * </dl>
 *
 * @author Orphoros
 * @version 1.0
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    /**
     * Create a new {@code BadRequestException} with a custom message.
     * @param message Custom message to describe the reason why the error happened
     */
    public BadRequestException(String message) {
        super(message);
    }
}

