package com.abbytech.shoppingapp.account;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.abbytech.shoppingapp.MainActivity;
import com.abbytech.shoppingapp.R;
import com.abbytech.shoppingapp.ShoppingApp;
import com.abbytech.util.eventform.SimpleRequestHandler;
import com.abbytech.util.ui.SupportSingleFragmentActivity;

public class LoginActivity extends SupportSingleFragmentActivity {
    @Override
    protected Fragment getFragment() {
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setAdapter(ShoppingApp.getInstance().getLoginAdapter());
        return loginFragment;
    }

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_login);
    }

    public void navigateToRegisterActivity(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
    }

    public static class LoginFragment extends com.abbytech.login.ui.LoginFragment {
        @Override
        protected SimpleRequestHandler setRequestHandler() {
            return new SimpleRequestHandler(getActivity()) {
                @Override
                protected void onSuccessfulSubmission(String response) {
                    super.onSuccessfulSubmission(response);
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finish();
                }
            };
        }
    }
}
