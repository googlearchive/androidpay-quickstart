/*
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.gms.samples.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.Scope;

public class LoginFragment extends Fragment implements
        OnClickListener, ConnectionCallbacks, OnConnectionFailedListener {

    private static final String TAG = "LoginFragment";

    public static final int REQUEST_CODE_RESOLVE_ERR = 1005;
    private static final int REQUEST_CODE_SIGN_IN = 1006;
    private static final String WALLET_SCOPE =
            "https://www.googleapis.com/auth/payments.make_payments";

    private GoogleApiClient mGoogleApiClient;
    private int mLoginAction;

    public static LoginFragment newInstance(int loginAction) {
        LoginFragment fragment = new LoginFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(LoginActivity.EXTRA_ACTION, loginAction);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mLoginAction = args.getInt(LoginActivity.EXTRA_ACTION);
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(WALLET_SCOPE))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addConnectionCallbacks(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        SignInButton signInButton = (SignInButton) view.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(this);

        view.findViewById(R.id.button_login_bikestore).setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SIGN_IN:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                onSignInClicked();
                break;
            case R.id.button_login_bikestore:
                Toast.makeText(getActivity(), R.string.login_bikestore_message, Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (mLoginAction == LoginActivity.Action.LOGOUT) {
            logOut();
        } else {
            silentSignIn();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
       Log.e(TAG, "onConnectionFailed:" + result.getErrorMessage());
    }

    private void onSignInClicked() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, REQUEST_CODE_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            Log.d(TAG, "googleSignIn:SUCCESS");
            handleSignInSuccess(result.getSignInAccount());
        } else {
            Log.d(TAG, "googleSignIn:FAILURE:" + result.getStatus());
            Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_LONG).show();
        }
    }

    private void handleSignInSuccess(GoogleSignInAccount account) {
        Toast.makeText(getActivity(), getString(R.string.welcome_user,
                account.getDisplayName()), Toast.LENGTH_LONG).show();

        ((BikestoreApplication) getActivity().getApplication()).login(account.getEmail());
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    private void silentSignIn() {
        OptionalPendingResult<GoogleSignInResult> opr =
                Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);

        if (opr.isDone()) {
            handleSignInResult(opr.get());
        }
    }

    private void logOut() {
        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);

            ((BikestoreApplication) getActivity().getApplication()).logout();
            Toast.makeText(getActivity(), getString(R.string.logged_out), Toast.LENGTH_LONG).show();
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        } else {
            mLoginAction = LoginActivity.Action.LOGOUT;
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // nothing specifically required here, onConnected will be called when connection resumes
    }
}
