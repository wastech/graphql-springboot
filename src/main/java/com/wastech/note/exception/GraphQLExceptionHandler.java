package com.wastech.note.exception;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.slf4j.Logger;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;


@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {
    private static final Logger log = LoggerFactory.getLogger(GraphQLExceptionHandler.class);

    @Override
    protected GraphQLError resolveToSingleError(Throwable e, DataFetchingEnvironment env) {
        if (e instanceof NotFoundException) {
            return toGraphQLError(e);
        } else if (e instanceof ConstraintViolationException) {
            return handleConstraintViolationException((ConstraintViolationException) e);
        } else if (e.getMessage() != null && e.getMessage().contains("GraphQlArgumentBinder")) {
            return handleArgumentBindingError(e, env);
        } else if (e instanceof Exception) {
            return toGraphQLError(e);
        }
        return super.resolveToSingleError(e, env);
    }

    private GraphQLError handleArgumentBindingError(Throwable e, DataFetchingEnvironment env) {
        log.warn("Argument binding error: {}", e.getMessage());

        return GraphqlErrorBuilder.newError()
            .message("Invalid input: Please provide a valid number for the ID")
            .errorType(ErrorType.ValidationError)
            .path(env.getExecutionStepInfo().getPath())
            .location(env.getField().getSourceLocation())
            .build();
    }

    private GraphQLError toGraphQLError(Throwable e) {
        log.warn("Exception while handling request: {}", e.getMessage(), e);
        return GraphqlErrorBuilder.newError()
            .message(e.getMessage())
            .errorType(ErrorType.DataFetchingException)
            .build();
    }

    private GraphQLError handleConstraintViolationException(ConstraintViolationException e) {
        Set<String> errorMessages = new HashSet<>();
        e.getConstraintViolations().forEach(violation ->
            errorMessages.add(String.format("Field '%s' %s, but value was [%s]",
                violation.getPropertyPath(),
                violation.getMessage(),
                violation.getInvalidValue())
            )
        );

        String message = String.join("\n", errorMessages);
        log.warn("Exception while handling request: {}", message, e);

        return GraphqlErrorBuilder.newError()
            .message(message)
            .errorType(ErrorType.ValidationError)
            .build();
    }
}