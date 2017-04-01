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

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import static com.google.android.gms.samples.wallet.BikestoreFragmentActivity.REQUEST_USER_LOGIN;

public class BuyFlowTemplateActivity extends AppCompatActivity {

    private static final String TAG = "BuyFlowTemplateActivity";

    private DrawerLayout mDrawerLayout;
    private BuyFlowTemplateActivity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator
                    = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(),R.color.white,getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        Intent intent = null;
                        if(menuItem.getItemId() == R.id.menu_option_one_step_buyflow) {
                            intent = new Intent(context, OneStepCheckoutActivity.class);
                        } else if(menuItem.getItemId() == R.id.menu_option_two_step_buyflow){
                            intent = new Intent(context, ItemListActivity.class);
                        } else if(menuItem.getItemId() == R.id.menu_option_product_detail_buyflow){
                            intent = new Intent(context, ProductDetailCheckoutActivity.class);
                        }
                        if(intent != null){
                            startActivity(intent);
                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_USER_LOGIN:
                if (resultCode == RESULT_OK) {
                    ActivityCompat.invalidateOptionsMenu(this);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        if (((BikestoreApplication) getApplication()).isLoggedIn()) {
            inflater.inflate(R.menu.menu_logout, menu);
        } else {
            inflater.inflate(R.menu.menu_login, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return super.onOptionsItemSelected(item);
            case R.id.action_settings:
                return true;
            case R.id.login:
                Intent loginIntent = new Intent(this, LoginActivity.class);
                loginIntent.putExtra(LoginActivity.EXTRA_ACTION, LoginActivity.Action.LOGIN);
                startActivityForResult(loginIntent, REQUEST_USER_LOGIN);
                return true;
            case R.id.logout:
                Intent logoutIntent = new Intent(this, LoginActivity.class);
                logoutIntent.putExtra(LoginActivity.EXTRA_ACTION, LoginActivity.Action.LOGOUT);
                startActivityForResult(logoutIntent, REQUEST_USER_LOGIN);
                return true;
            default:
                return false;
        }

    }
}
