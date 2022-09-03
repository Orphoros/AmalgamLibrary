package dev.orphoros.demo.amalgamlib.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class for modeling HTTP exceptions
 * <p>
 * The {@code NotFoundException} may be used in any repository or service.
 *
 * <dl>
 * <dt><span class="strong">HTTP Status code:</span></dt>
 * <dd>
 * 404 – Not Found
 * </dd>
 * </dl>
 *
 * @author Orphoros
 * @version 1.0
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    /**
     * Create a new {@code NotFoundException} with a custom message.
     * @param message Custom message to describe the reason why the error happened
     */
    public NotFoundException(String message) {
        super(message);
    }
}