package com.abbytech.shoppingapp.account;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.abbytech.shoppingapp.MainActivity;
import com.abbytech.shoppingapp.ShoppingApp;
import com.abbytech.util.eventform.SimpleRequestHandler;
import com.abbytech.util.ui.SupportSingleFragmentActivity;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.Result;
import retrofit2.http.GET;
import rx.Observable;

public class LoginActivity extends SupportSingleFragmentActivity {
    @Override
    protected Fragment getFragment() {
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setAdapter(ShoppingApp.getInstance().getLoginAdapter());
        return loginFragment;
    }

    public interface LoginAPI {
        @GET("User/Login")
        Observable<Result<ResponseBody>> login();
    }

    public static class LoginFragment extends com.abbytech.login.ui.LoginFragment {
        @Override
        protected SimpleRequestHandler setRequestHandler() {
            return new SimpleRequestHandler(getActivity()) {
                @Override
                protected void onSuccessfulSubmission(String response) {
                    super.onSuccessfulSubmission(response);
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
            };
        }
    }
}
