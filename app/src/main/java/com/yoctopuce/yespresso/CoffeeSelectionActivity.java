package com.yoctopuce.yespresso;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.yoctopuce.yespresso.coffee.Coffee;
import com.yoctopuce.yespresso.coffee.CoffeeInventory;

import java.util.ArrayList;


public class CoffeeSelectionActivity extends ActionBarActivity implements CoffeeFragment.OnFragmentInteractionListener {

    private YoctopuceInterface _yoctopuceInterface;
    private ViewPager _viewPager;
    private ArrayList<Coffee> _coffeesAvailable;
    private CoffeeFragmentAdapter _coffeeFragmentAdapter;
    private CoffeeInventory _inventory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _inventory = CoffeeInventory.get();
        _coffeesAvailable = _inventory.getAvailableCoffee();

        setContentView(R.layout.activity_coffee_selection);
        FragmentManager fm = getSupportFragmentManager();
        _coffeeFragmentAdapter = new CoffeeFragmentAdapter(fm);

        _viewPager = (ViewPager) findViewById(R.id.pager);
        _viewPager.setAdapter(_coffeeFragmentAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_coffee_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        _yoctopuceInterface = YoctopuceInterface.Get(this);
        _yoctopuceInterface.startUsage();
    }

    @Override
    protected void onStop() {
        super.onStop();
        _yoctopuceInterface.stopUsage();
    }

    @Override
    public void ditribute(String name) {
        int tubeno = _inventory.distribute(name);
        if (tubeno >= 0) {
            _yoctopuceInterface.distributeCapsule(tubeno);
        }
    }

    public class CoffeeFragmentAdapter extends FragmentStatePagerAdapter {
        public CoffeeFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Coffee c = _coffeesAvailable.get(i);
            return CoffeeFragment.newInstance(c.getName());
        }

        @Override
        public int getCount() {
            return _coffeesAvailable.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Coffee c = _coffeesAvailable.get(position);
            return c.getName();
        }
    }


}
