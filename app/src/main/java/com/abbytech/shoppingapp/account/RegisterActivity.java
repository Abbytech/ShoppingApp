package com.abbytech.shoppingapp.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.abbytech.shoppingapp.MainActivity;
import com.abbytech.shoppingapp.R;
import com.abbytech.shoppingapp.ShoppingApp;
import com.abbytech.shoppingapp.databinding.FragmentRegisterBinding;
import com.abbytech.util.eventform.SimpleRequestHandler;
import com.abbytech.util.form.TextFormErrorChecker;
import com.abbytech.util.form.TextFormFragment;
import com.abbytech.util.ui.SupportSingleFragmentActivity;

import java.util.ArrayList;
import java.util.Collection;

public class RegisterActivity extends SupportSingleFragmentActivity {

    @Override
    protected Fragment getFragment() {
        RegisterFragment registerFragment = new RegisterFragment();
        ShoppingApp context = ShoppingApp.getInstance();
        registerFragment.setAdapter(context.getRegisterAdapter());
        return registerFragment;
    }

    public static class RegisterFragment extends
            TextFormFragment<RegisterData> {

        private FragmentRegisterBinding binding;

        @Override
        protected SimpleRequestHandler setRequestHandler() {
            return new SimpleRequestHandler(getActivity()) {
                @Override
                protected void onSuccessfulSubmission(String response) {
                    Toast.makeText(getActivity(),
                            R.string.message_registered_successfully, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), MainActivity.class));
                    getActivity().finish();
                }

                @Override
                protected String setProgressDialogMessage() {
                    return getString(R.string.progress_dialog_register);
                }
            };
        }

        @NonNull
        @Override
        public RegisterData getFormData(Collection<TextView> fields) {
            RegisterData registerData = new RegisterData();
            registerData.email = binding.EditTextEmail.getText().toString().trim();
            registerData.username = binding.EditTextUsername.getText().toString().trim();
            registerData.password = binding.EditTextPassword.getText().toString().trim();
            return registerData;
        }

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            binding = FragmentRegisterBinding.inflate(inflater, container, false);
            setSubmitButton(binding.buttonRegister);
            return binding.getRoot();
        }

        @Override
        public Collection<TextView> findAndSetFields(View view) {
            ArrayList<TextView> fields = new ArrayList<>(4);
            fields.add(binding.EditTextEmail);
            fields.add(binding.EditTextUsername);
            fields.add(binding.EditTextPassword);
            fields.add(binding.EditTextConfirmPassword);
            return fields;
        }

        @Override
        protected TextFormErrorChecker setupErrorChecker(Collection<TextView> fields) {
            return new TextFormErrorChecker(fields) {
                @Override
                protected String checkFieldForError(TextView field) {
                    if (field == binding.EditTextPassword) {
                        if (field.getText().length() < 6)
                            return "Password is too short.";
                    } else if (field == binding.EditTextConfirmPassword) {
                        if (!field.getText().toString()
                                .equals(binding.EditTextPassword.getText().toString()))
                            return "Password mismatch";
                    } else if (field == binding.EditTextUsername) {
                        if (field.getText().length() < 4)
                            return "Username too short";
                    } else if (field == binding.EditTextEmail) {
                        if (field.getText().length() == 0)
                            return "Enter email address";
                    }
                    return null;
                }
            };
        }
    }
}
