package com.gametimegiving.android;

import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class BaseActivityTest extends AppCompatActivity {
    @Before
    public void setUp() {

    }

    @Test
    public void WhenLoggedOutSetUserProfileSetsNameToLoggedOut() {
        //Arrange
        String expectedText = "Logged Out";
        String actualText = "";
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);
        TextView tvUserName = headerLayout.findViewById(R.id.tvusername);
        Assert.assertEquals(expectedText, actualText);

    }
}