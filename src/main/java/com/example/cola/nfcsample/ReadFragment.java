package com.example.cola.nfcsample;

import android.app.Activity;
import android.app.Fragment;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by cola on 15/5/7.
 */
public class ReadFragment extends Fragment implements OnClickListener {

    private Tag mytag;
    private EditText blockNumEdTxt;
    private Button readTagBtn;
    private TextView readTagTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.read_fragment, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findView();
        setListener();
    }


    private void findView() {
        blockNumEdTxt = (EditText)getActivity().findViewById(R.id.read_block_num_edTxt);
        readTagBtn = (Button)getActivity().findViewById(R.id.read_tag_btn);
        readTagTxt = (TextView)getActivity().findViewById(R.id.read_tagTxt);
    }

    private void setListener() {
        readTagBtn.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void SetTag(Tag nfcTag) {
        mytag = nfcTag;
        Toast.makeText(getActivity(), this.getString(R.string.ok_detection), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        String fullData = "";
        int blockNum = 0;
        switch (v.getId()) {
            case R.id.read_tag_btn:
                if(blockNumEdTxt.getText().toString().trim().length() == 0) {
                    Toast.makeText(getActivity(), this.getString(R.string.not_null), Toast.LENGTH_SHORT).show();
                }else {
                    blockNum = Integer.valueOf(blockNumEdTxt.getText().toString());
                    try {
                        fullData = NfcvFunction.getHexString(NfcvFunction.read(mytag, blockNum)).substring(2, 10);
                        readTagTxt.setText(fullData);
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), this.getString(R.string.error_reading), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

}
