package me.nydev.wifituner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;

import java.util.ArrayList;

import me.nydev.wifituner.model.LocationBuilder;
import me.nydev.wifituner.support.BaseActivity;

public class ScanConfActivity extends BaseActivity implements AdapterView.OnItemSelectedListener
{
    private static final int SPINLAYOUT = R.layout.support_simple_spinner_dropdown_item;
    private static final int[] SPINIDS = {R.id.scan_conf_building, R.id.scan_conf_floor, R.id.scan_conf_room};
    private static final String PROMPT = "[select]";

    private Spinner[] spinners;
    private NumberPicker hourPicker, minutePicker;

    private LocationBuilder location;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState, R.layout.activity_scan_conf);
        location = new LocationBuilder();
        initSpinners();
        initNumberPickers();
    }

    private void initSpinners()
    {
        String[] buildings = dba.buildings();
        spinners = new Spinner[SPINIDS.length];
        for(int x = 0; x < spinners.length; x++)
        {
            spinners[x] = (Spinner) findViewById(SPINIDS[x]);
            spinners[x].setOnItemSelectedListener(this);
        }
        fillSpinner(0, buildings);
        showSpinner(0);
    }

    private void fillSpinner(int x, Object[] objects)
    {
        ArrayList<String> al = new ArrayList<>();
        al.add(PROMPT);
        for(Object o: objects)
            al.add(o.toString());
        ArrayAdapter<String> aa = new ArrayAdapter<>(context, SPINLAYOUT, al);
        spinners[x].setAdapter(aa);
    }

    private void hideSpinner(int x)
    {
        spinners[x].setVisibility(View.INVISIBLE);
    }

    private void showSpinner(int x)
    {
        spinners[x].setVisibility(View.VISIBLE);
    }

    private void initNumberPickers()
    {
        hourPicker = (NumberPicker) findViewById(R.id.scan_conf_hour);
        minutePicker = (NumberPicker) findViewById(R.id.scan_conf_minute);
        hourPicker.setMinValue(0); hourPicker.setMaxValue(3);
        minutePicker.setMinValue(0); minutePicker.setMaxValue(59);
        hourPicker.setWrapSelectorWheel(true);
        minutePicker.setWrapSelectorWheel(true);
    }

    private int duration()
    {
        return 60 * (hourPicker.getValue() * 60 + minutePicker.getValue());
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
    {
        if (pos == 0) return;
        Object x = parent.getSelectedItem();
        switch(parent.getId())
        {
            case R.id.scan_conf_building:
                location.setBuilding(x.toString());
                Integer[] floors = dba.floors(location.getBuilding());
                fillSpinner(1, floors);
                showSpinner(1);
                hideSpinner(2);
                break;
            case R.id.scan_conf_floor:
                location.setFloor((Integer.parseInt(x.toString())));
                String[] rooms = dba.rooms(location.getBuilding(), location.getFloor());
                fillSpinner(2, rooms);
                showSpinner(2);
                break;
            case R.id.scan_conf_room:
                location.setRoom(x.toString());
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent)
    {
        toaster.toast("nothing selected");
    }

    public void start_scan(View view)
    {
        if(location.isValid() || true) {
            //toaster.toast(location);
            Intent intent = new Intent(this, WifiScanService.class);
            //intent.putExtra(Constants.DATA.DURATION, duration());
            intent.putExtra(Constants.DATA.DURATION, 15);
            location.setBuilding("ANY").setFloor(0).setRoom("ANY");
            intent.putExtra(Constants.DATA.BUILDING, location.getBuilding());
            intent.putExtra(Constants.DATA.FLOOR, location.getFloor());
            intent.putExtra(Constants.DATA.ROOM, location.getRoom());
            startService(intent);
            handleNewIntent(HomeActivity.class);
        } else {
            toaster.toast("invalid settings");
        }
    }
}