package com.example.cola.nfcsample;

import android.app.Activity;
import android.app.Fragment;
import android.nfc.FormatException;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by cola on 15/5/7.
 */
public class WriteFragment extends Fragment implements OnClickListener {

    private Tag mytag;
    private EditText byte1EdTxt, byte2EdTxt, byte3EdTxt, byte4EdTxt, blockNumEdTxt;
    private Button writeTagBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.write_fragment, container, false);
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
        byte1EdTxt = (EditText)getActivity().findViewById(R.id.byte1_edTxt);
        byte2EdTxt = (EditText)getActivity().findViewById(R.id.byte2_edTxt);
        byte3EdTxt = (EditText)getActivity().findViewById(R.id.byte3_edTxt);
        byte4EdTxt = (EditText)getActivity().findViewById(R.id.byte4_edTxt);
        blockNumEdTxt = (EditText)getActivity().findViewById(R.id.write_block_num_edTxt);

        writeTagBtn = (Button)getActivity().findViewById(R.id.write_tag_btn);
    }

    private void setListener() {
        writeTagBtn.setOnClickListener(this);
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
            case R.id.write_tag_btn:
                if(byte1EdTxt.getText().toString().trim().length() == 0 &&
                        byte2EdTxt.getText().toString().trim().length() == 0 &&
                        byte3EdTxt.getText().toString().trim().length() == 0 &&
                        byte4EdTxt.getText().toString().trim().length() == 0 ||
                        blockNumEdTxt.getText().toString().trim().length() == 0 ) {
                    Toast.makeText(getActivity(), this.getString(R.string.not_null), Toast.LENGTH_SHORT).show();
                }else if(byte1EdTxt.getText().toString().trim().length() == 1 ||
                        byte2EdTxt.getText().toString().trim().length() == 1 ||
                        byte3EdTxt.getText().toString().trim().length() == 1 ||
                        byte4EdTxt.getText().toString().trim().length() == 1) {
                    Toast.makeText(getActivity(), this.getString(R.string.check_data), Toast.LENGTH_SHORT).show();
                }else {
                    fullData = byte1EdTxt.getText().toString() + byte2EdTxt.getText().toString() +
                            byte3EdTxt.getText().toString() + byte4EdTxt.getText().toString();
                    blockNum = Integer.valueOf(blockNumEdTxt.getText().toString());
                    try {
                        if (mytag == null) {
                            Toast.makeText(getActivity(),
                                    this.getString(R.string.error_detected),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                NfcvFunction.write(mytag,NfcvFunction.hexStringToByteArray(fullData), blockNum);
                                Toast.makeText(getActivity(), this.getString(R.string.ok_writing), Toast.LENGTH_SHORT).show();
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                Toast.makeText(getActivity(), this.getString(R.string.error_writing), Toast.LENGTH_SHORT).show();
                            }

                        }
                    } catch (IOException e) {
                        Toast.makeText(getActivity(), this.getString(R.string.error_writing), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (FormatException e) {
                        Toast.makeText(getActivity(), this.getString(R.string.error_writing), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

}
