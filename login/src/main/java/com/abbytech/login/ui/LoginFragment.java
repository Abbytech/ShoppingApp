package com.abbytech.login.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.abbytech.login.data.BasicLoginData;
import com.abbytech.login.LoginRequest;
import com.abbytech.login.R;
import com.abbytech.util.eventform.SimpleRequestHandler;
import com.abbytech.util.form.TextFormErrorChecker;
import com.abbytech.util.form.TextFormFragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LoginFragment extends TextFormFragment<LoginRequest> {
    @NonNull
    @Override
    public LoginRequest<BasicLoginData> getFormData(Collection<TextView> fields) {
        List<TextView> fieldsList = (List<TextView>) fields;
        String username = fieldsList.get(0).getText().toString();
        String password = fieldsList.get(1).getText().toString();
        return new LoginRequest<>(new BasicLoginData(username, password));
    }

    @Override
    protected SimpleRequestHandler setRequestHandler() {
        return new SimpleRequestHandler(getActivity()) {
            @Override
            protected void onSuccessfulSubmission(String response) {
                Toast.makeText(getActivity(), R.string.successful_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String setProgressDialogMessage() {
                return getString(R.string.progress_dialog_login);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setSubmitButton(view.findViewById(R.id.button_login));
    }

    @Override
    public Collection<TextView> findAndSetFields(View view) {
        Collection<TextView> fields = new ArrayList<>();
        fields.add((TextView) view.findViewById(R.id.EditText_login_username));
        fields.add((TextView) view.findViewById(R.id.EditText_login_password));
        return fields;
    }

    @Override
    protected TextFormErrorChecker setupErrorChecker(Collection<TextView> fields) {
        return TextFormErrorChecker.getDefaultErrorChecker(fields);
    }
}
