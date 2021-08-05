package com.example.iGlass;

import java.text.DecimalFormat;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iGlass.messages.MeasurementStepMessage;
import com.example.iGlass.messages.MessageHUB;
import com.example.iGlass.messages.MessageListener;

public class ScreenFaceDistance extends AppCompatActivity implements MessageListener {

    public static final String CAM_SIZE_WIDTH = "intent_cam_size_width";
    public static final String CAM_SIZE_HEIGHT = "intent_cam_size_height";
    public static final String AVG_NUM = "intent_avg_num";
    public static final String PROBANT_NAME = "intent_probant_name";
    String testType, indicator = "va";

    private CameraSurfaceView _mySurfaceView;
    Camera _cam;

    private final static DecimalFormat _decimalFormater = new DecimalFormat(
            "0.0");

    private float _currentDevicePosition;

    private int _cameraHeight;
    private int _cameraWidth;
    private int _avgNum;

    TextView _currentDistanceView;
    Button _calibrateButton, _reset, _next;
    Switch _showEyePoints, _showMiddlePoint;

    /**
     * Abusing the media controls to create a remote control
     */
    // ComponentName _headSetButtonReceiver;
    // AudioManager _audioManager;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_face);

        testType = getIntent().getStringExtra("test");
        _mySurfaceView = (CameraSurfaceView) findViewById(R.id.surface_camera);

        RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(
                (int) (0.95 * this.getResources().getDisplayMetrics().widthPixels),
                (int) (0.6 * this.getResources().getDisplayMetrics().heightPixels));

        layout.setMargins(0, (int) (0.05 * this.getResources()
                .getDisplayMetrics().heightPixels), 0, 0);

        _mySurfaceView.setLayoutParams(layout);
        _currentDistanceView = (TextView) findViewById(R.id.currentDistance);
        _calibrateButton = (Button) findViewById(R.id.calibrateButton);
        _showMiddlePoint = findViewById(R.id.switch1);
        _showEyePoints = findViewById(R.id.switch2);
        _reset = findViewById(R.id.resetButton);
        _next = findViewById(R.id.nextButton);

        _calibrateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _next.setEnabled(true);
                pressedCalibrate();
            }
        });

        _reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_mySurfaceView.isCalibrated()) {

                    _calibrateButton.setBackgroundResource(R.drawable.red_button);
                    _calibrateButton.setText("CALIBRATE");
                    _mySurfaceView.reset();
                }
            }
        });

        _next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVisualAcuity();
            }
        });

        _showEyePoints.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                _mySurfaceView.showEyePoints(_showEyePoints.isChecked());
            }
        });

        _showMiddlePoint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                _mySurfaceView.showMiddleEye(_showMiddlePoint.isChecked());
            }
        });

        // _audioManager = (AudioManager) this
        // .getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        MessageHUB.get().registerListener(this);
        // _audioManager.registerMediaButtonEventReceiver(_headSetButtonReceiver);

        // 1 for front cam. No front cam ? Not my fault!
        _cam = Camera.open(1);
        Camera.Parameters param = _cam.getParameters();
        //Camera.Size previewSize = pre

        // Find the best suitable camera picture size for your device. Competent
        // research has shown that a smaller size gets better results up to a
        // certain point.
        // http://ieeexplore.ieee.org/xpl/login.jsp?tp=&arnumber=6825217&url=http%3A%2F%2Fieeexplore.ieee.org%2Fiel7%2F6816619%2F6825201%2F06825217.pdf%3Farnumber%3D6825217
        List<Size> pSize = param.getSupportedPictureSizes();
        double deviceRatio = (double) this.getResources().getDisplayMetrics().widthPixels
                / (double) this.getResources().getDisplayMetrics().heightPixels;

        Size bestSize = pSize.get(0);
        double bestRation = (double) bestSize.width / (double) bestSize.height;

        for (Size size : pSize) {
            double sizeRatio = (double) size.width / (double) size.height;

            if (Math.abs(deviceRatio - bestRation) > Math.abs(deviceRatio
                    - sizeRatio)) {
                bestSize = size;
                bestRation = sizeRatio;
            }
        }
        _cameraHeight = bestSize.height;
        _cameraWidth = bestSize.width;

        Log.d("PInfo", _cameraWidth + " x " + _cameraHeight);

        //param.setPreviewSize(_cameraWidth, _cameraHeight);
        param.setPictureSize(_cameraWidth, _cameraHeight);
        _cam.setParameters(param);

        _mySurfaceView.setCamera(_cam);
    }

    @Override
    protected void onPause() {
        super.onPause();

        MessageHUB.get().unregisterListener(this);

        // _audioManager
        // .unregisterMediaButtonEventReceiver(_headSetButtonReceiver);

        resetCam();
    }

    /**
     * Sets the current eye distance to the calibration point.
     *
     * @param v
     */
    public void pressedCalibrate() {

        if (!_mySurfaceView.isCalibrated()) {

            _calibrateButton.setBackgroundResource(R.drawable.yellow_button);
            _calibrateButton.setText("WAIT");
            _mySurfaceView.calibrate();
        }
    }

    /**
     * Update the UI values.
     *
     * @param eyeDist
     * @param frameTime
     */
    public void updateUI(final MeasurementStepMessage message) {

        _currentDistanceView.setText(_decimalFormater.format(message
                .getDistToFace()) + " cm");

        float fontRatio = message.getDistToFace() / 29.7f;

        _currentDistanceView.setTextSize(fontRatio * 20);

    }

    private void resetCam() {
        _mySurfaceView.reset();

        _cam.stopPreview();
        _cam.setPreviewCallback(null);
        _cam.release();
    }

    @Override
    public void onMessage(final int messageID, final Object message) {

        switch (messageID) {

            case MessageHUB.MEASUREMENT_STEP:
                updateUI((MeasurementStepMessage) message);
                break;

            case MessageHUB.DONE_CALIBRATION:

                _calibrateButton.setBackgroundResource(R.drawable.green_button);
                _calibrateButton.setText("DONE");

                break;
            default:
                break;
        }

    }

    public void openVisualAcuity(){
        Intent intent;
        if (testType.equals(indicator)){
            intent = new Intent(this, visualAcuity.class);
        }
        else {
            intent = new Intent(this, ishihara.class);
        }
        finish();
        startActivity(intent);
    }
}