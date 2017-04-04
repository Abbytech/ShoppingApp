package com.abbytech.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abbytech.login.LoginRequest;
import com.abbytech.login.R;
import com.abbytech.util.eventform.RequestFragment;
import com.abbytech.util.eventform.SimpleRequestHandler;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public abstract class GoogleSignInFragment extends RequestFragment<LoginRequest> {
    private static final String EXTRA_OAUTH_CLIENT_ID = "BUNDLE EXTRA OAUTH CLIENT ID";
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "GoogleSignIn";
    private GoogleApiClient mGoogleApiClient;

    public GoogleSignInFragment() {
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String oauthClientID = getArguments().getString(EXTRA_OAUTH_CLIENT_ID);
        init(oauthClientID);
        setRetainInstance(true);
    }

    private void init(String oAuthClientID) {
        GoogleSignInOptions gso = createGoogleSignInOptions(oAuthClientID);

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), null)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_google_sign_in, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SignInButton signInButton = (SignInButton) view.findViewById(R.id.google_sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    protected GoogleSignInOptions createGoogleSignInOptions(String oauthClientID) {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestIdToken(oauthClientID)
                .build();
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            onSuccessfulSignIn(acct);
        }
    }

    protected abstract void onSuccessfulSignIn(GoogleSignInAccount account);
}
