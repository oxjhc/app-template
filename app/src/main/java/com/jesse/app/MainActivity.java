package com.jesse.app;

import android.animation.Animator;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity {

    private ViewFlipper mFlipper;
    private Animation in_from_left;
    private Animation in_from_right;
    private Animation out_to_left;
    private Animation out_to_right;
    private Menu menu;

    private void enableMenu(Boolean enable) {
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setEnabled(enable);
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            enableMenu(false);

            int curChild = mFlipper.getDisplayedChild();
            int nextId;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    nextId = R.id.homeView;
                    break;
                case R.id.navigation_dashboard:
                    nextId = R.id.dashboardView;
                    break;
                case R.id.navigation_proofs:
                    nextId = R.id.proofsView;
                    break;
                default:
                    nextId = -1;
            }
            int nextChild = mFlipper.indexOfChild(findViewById(nextId));

            if (curChild == nextChild) {
                enableMenu(true);
                return false;
            } else if (curChild < nextChild) {
                mFlipper.setInAnimation(in_from_right);
                mFlipper.setOutAnimation(out_to_left);
                mFlipper.setDisplayedChild(nextChild);
            } else if (curChild > nextChild) {
                mFlipper.setInAnimation(in_from_left);
                mFlipper.setOutAnimation(out_to_right);
                mFlipper.setDisplayedChild(nextChild);
            }

            enableMenu(true);
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        menu = navigation.getMenu();

        mFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        mFlipper.setAutoStart(false);
        mFlipper.setDisplayedChild(0);

        in_from_left = AnimationUtils.loadAnimation(this, R.anim.in_from_left);
        in_from_right = AnimationUtils.loadAnimation(this, R.anim.in_from_right);
        out_to_left = AnimationUtils.loadAnimation(this, R.anim.out_to_left);
        out_to_right  = AnimationUtils.loadAnimation(this, R.anim.out_to_right);

        FloatingActionButton proveLocation = (FloatingActionButton) findViewById(R.id.proveLocation);
        final ActivityOptions options =
                ActivityOptions.makeCustomAnimation(this, R.anim.in_from_right, R.anim.out_to_left);
        final Intent intent = new Intent(this, ProveLocation.class);
        proveLocation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    startActivity(intent, options.toBundle());
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    public static class AboutDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("About")
                   .setMessage("Made by OxJHC.");
            return builder.create();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_settings:;
                return true;
            case R.id.settings_about:
                DialogFragment newFragment = new AboutDialogFragment();
                newFragment.show(getFragmentManager(), "about");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
