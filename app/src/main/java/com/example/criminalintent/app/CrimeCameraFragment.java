package com.example.criminalintent.app;

import android.annotation.TargetApi;


import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.*;
import android.widget.Button;

import java.io.IOException;
import java.util.List;

/**
 * Created by Donkey on 4/27/2015.
 */
public class CrimeCameraFragment extends Fragment {

    public static final String TAG = "CrimeCameraFragment";
    private Camera mCamera;
    private SurfaceView mSurfaceView;



    @Override
    @SuppressWarnings("deprecated")
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_crime_camera, parent, false);

        Button takePictureButton = (Button)v.findViewById(R.id.crime_camera_takePictureButton);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mSurfaceView = (SurfaceView)v.findViewById(R.id.crime_camera_surfaceView);
        SurfaceHolder holder = mSurfaceView.getHolder();

        //old stuff that work on pre 3.0 devices
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        holder.addCallback(new SurfaceHolder.Callback() {


            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //Tell the camera to use this surface as its preview area
                try
                {
                    if (mCamera != null)
                    {
                        mCamera.setPreviewDisplay(holder);
                    }
                }
                catch(IOException exception)
                {
                    Log.e(TAG, "Error setting up preview display", exception);
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if(mCamera != null)
                    return;

                //The surfacce has changed size; update the camera preview size
                Camera.Parameters parameters = mCamera.getParameters();
                Camera.Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(),width,height);
                parameters.setPictureSize(s.width, s.height);
                mCamera.setParameters(parameters);
                try
                {
                    mCamera.startPreview();
                }
                catch(Exception e)
                {
                    Log.e(TAG,"Could not start preview", e);
                    mCamera.release();
                    mCamera = null;
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                //We can no longer display on this surface, so stop the preview
                if(mCamera != null)
                {
                    mCamera.stopPreview();;
                }
            }
        });

        return v;
    }

    @TargetApi(9)
    @Override
    public void onResume()
    {
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
        {
            mCamera = Camera.open(0);

        }
        else
        {
            mCamera = Camera.open(1);
        }

    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(mCamera != null)
        {
            mCamera.release();
            mCamera = null;
        }
    }
    //simple algorithm to get the largest size available
    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes, int width, int height)
    {
        Camera.Size bestSize = sizes.get(0);
        int largestArea = bestSize.width * bestSize.height;
        for(Camera.Size s:sizes)
        {
            int area = s.width * s.height;
            if(area > largestArea)
            {
                bestSize = s;
                largestArea = area;
            }
        }
        return bestSize;
    }
}
