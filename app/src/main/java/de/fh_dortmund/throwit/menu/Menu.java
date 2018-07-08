package de.fh_dortmund.throwit.menu;

import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import de.fh_dortmund.throwit.R;
import de.fh_dortmund.throwit.menu.dummy.UserScoreContent;

public class Menu extends AppCompatActivity
        implements  ThrowFragment.OnFragmentInteractionListener,
                    AchieveFragment.OnFragmentInteractionListener,
                    HighscoreFragment.OnListFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ViewPager pager = findViewById(R.id.vpa_menu);
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        pager.setCurrentItem(1);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {


    }

    @Override
    public void onListFragmentInteraction(UserScoreContent.UserScoreItem item) {

    }

}
