package com.abbytech.login;

import com.abbytech.util.eventform.SubmissionEvent;

/**
 * Event-bus event that's sent by LoginActivity when the user submits the form.
 */
public class LoginRequest<T> extends SubmissionEvent<T> {

    public LoginRequest(T submissionData) {
        super(submissionData);
    }
}
