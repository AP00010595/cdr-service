package com.dcs.cdr.cdrservice.model;

import java.util.List;

public class ChargeDetailRecordResponse {
    private final boolean success;
    private final List<String> validationErrors;

    public ChargeDetailRecordResponse(boolean success, List<String> validationErrors) {
        this.success = success;
        this.validationErrors = validationErrors;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }
}

