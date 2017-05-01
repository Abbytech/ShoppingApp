package com.abbytech.shoppingapp.account;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                        else if (!isValidPassword(field.getText().toString())) {
                            new AlertDialog.Builder(getContext())
                                    .setTitle("weak password")
                                    .setMessage("The password should contain at least 1 capital letter,1 symbol and digits and letters "
                                            )
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                            return "";
                        }
//

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
                        else if (!isValidEmaillId(field.getText().toString()))
                            return "invalid Email";
//
                    }
                    return null;
                }
            };
        }

        public boolean isValidPassword(final String password) {

            Pattern pattern;
            Matcher matcher;

            final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

            pattern = Pattern.compile(PASSWORD_PATTERN);
            matcher = pattern.matcher(password);

            return matcher.matches();

        }
        private boolean isValidEmaillId(String email){

            return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
        }

    }
}
