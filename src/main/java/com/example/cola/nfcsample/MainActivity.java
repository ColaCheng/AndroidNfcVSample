package com.example.cola.nfcsample;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;


public class MainActivity extends ActionBarActivity implements OnClickListener{

    private WriteFragment myWriteFragment = new WriteFragment();
    private ReadFragment myReadFragment = new ReadFragment();
    private FrameLayout writePart, writePartBaseline, readPart, readPartBaseline;
    private int Mode = 0; //0:WriteFragment, 1:ReadFragment
    private PendingIntent pendingIntent;
    private IntentFilter writeTagFilters[];
    private Tag mytag;
    private NfcAdapter adapter;
    private boolean writeMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writeTagFilters = new IntentFilter[] { tagDetected };

        findView();
        setListener();

        getFragmentManager().beginTransaction().replace(R.id.fragment_part, myWriteFragment).commit();
    }

    private void findView() {
        writePart = (FrameLayout)findViewById(R.id.write_part);
        writePartBaseline = (FrameLayout)findViewById(R.id.write_part_baseline);

        readPart = (FrameLayout)findViewById(R.id.read_part);
        readPartBaseline = (FrameLayout)findViewById(R.id.read_part_baseline);
        readPartBaseline.setVisibility(View.GONE);
    }

    private void setListener() {
        writePart.setOnClickListener(this);
        readPart.setOnClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent){
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
            mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if(Mode == 0)
                myWriteFragment.SetTag(mytag);
            else if(Mode == 1)
                myReadFragment.SetTag(mytag);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        WriteModeOff();
    }

    @Override
    public void onResume(){
        super.onResume();
        WriteModeOn();
    }

    private void WriteModeOn(){
        writeMode = true;
        adapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
    }

    private void WriteModeOff(){
        writeMode = false;
        adapter.disableForegroundDispatch(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuList;
        menuList = menu.findItem(R.id.action_settings);
        menuList.setVisible(false);
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
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId()){
            case R.id.write_part:
                writePartBaseline.setVisibility(View.VISIBLE);
                readPartBaseline.setVisibility(View.GONE);
                Mode = 0;
                getFragmentManager().beginTransaction().replace(R.id.fragment_part, myWriteFragment).commit();
                break;
            case R.id.read_part:
                writePartBaseline.setVisibility(View.GONE);
                readPartBaseline.setVisibility(View.VISIBLE);
                Mode = 1;
                getFragmentManager().beginTransaction().replace(R.id.fragment_part, myReadFragment).commit();
                break;
        }
    }
}
