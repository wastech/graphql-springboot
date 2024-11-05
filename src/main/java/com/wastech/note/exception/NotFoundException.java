package com.wastech.note.exception;

import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.List;
import java.util.Map;

public class NotFoundException extends RuntimeException implements GraphQLError {
    private final Long id;

    public NotFoundException(String message, Long id) {
        super(message);
        this.id = id;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + id;
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public Map<String, Object> getExtensions() {
        return Map.of("invalidId", id);
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.DataFetchingException;
    }
}
