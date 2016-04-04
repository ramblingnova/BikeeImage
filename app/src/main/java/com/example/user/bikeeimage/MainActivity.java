package com.example.user.bikeeimage;

import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ncloud.filestorage.FSRestClient;
import com.ncloud.filestorage.model.FSClientException;
import com.ncloud.filestorage.model.FSResourceID;
import com.ncloud.filestorage.model.FSServiceException;
import com.ncloud.filestorage.model.FSUploadFileResult;
import com.ncloud.filestorage.model.FSUploadSourceInfo;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ncloud.Ncloud;


public class MainActivity extends AppCompatActivity {
    private List<File> list;
    private final int MAX_BUF_BYTE = 1024000;
    private static final String TAG = "FINALLY_R_B_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        String orderBy = MediaStore.Images.Media._ID;
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
        int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        int count = cursor.getCount();
        int temp = 1;

        list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            String path = cursor.getString(dataColumnIndex);

            if (path.split("/")[4].equals("bikee") && (temp++ <= 5)) {
                if (BuildConfig.DEBUG)
                    Log.d(TAG, "path : " + path);

                list.add(new File(path));
            }
        }
        Ncloud ncloud = new Ncloud();
        ncloud.fileUpload(list);

  /*      FSRestClient.initialize();
        FSRestClient client = new FSRestClient(
                "restapi.fs.ncloud.com",
                80,
                "EQGtmtLqgwPNONDwuODO",
                "YbJwoJw9q5fO7qj4EQMzT4LpCl97CQ4Bmp3vwWqA"
        );
        InputStream ins = null;
        try {
            for (File file : list) {
                String fileName = Long.toString(new Date().getTime())
                        + "_" + file.getName();
                byte fileArray[] = fileWrite(file);
                String mimeType = "image/png";

                FSResourceID rid = new FSResourceID("bikee-image/" + fileName);
                ins = new ByteArrayInputStream(fileArray);
                FSUploadSourceInfo info = new FSUploadSourceInfo(
                        ins,
                        mimeType,
                        fileArray.length,
                        null
                );

                FSUploadFileResult result = client.uploadFile(rid, info);
                if (BuildConfig.DEBUG)
                    Log.d(TAG, "fileUrl : " + "http://restapi.fs.ncloud.com/bikee-image/" + fileName
                                    + "\nresult.getETag() : " + result.getETag()
                                    + "\nresult.toString() : " + result.toString()
                    );

                ins.close();
            }
        } catch (FSClientException e) {
            if (BuildConfig.DEBUG)
                Log.d(TAG, "FSClientException...", e);
        } catch (FSServiceException e) {
            if (BuildConfig.DEBUG)
                Log.d(TAG, "FSServiceException...", e);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.d(TAG, "Exception...", e);
        } finally {
            try {
                if (ins != null)
                    ins.close();
            } catch (IOException e) {
                if (BuildConfig.DEBUG)
                    Log.d(TAG, "IOException...", e);
            } finally {
                FSRestClient.destroy();
            }
        }*/
    }

    public byte[] fileWrite(File file) {
        InputStream in = null;
        BufferedInputStream bis = null;
        ByteArrayOutputStream arrayBuff = new ByteArrayOutputStream();
        try {

            byte[] buffer = new byte[MAX_BUF_BYTE];

            in = new FileInputStream(file);
            bis = new BufferedInputStream(in);
            int len = 0;
            while ((len = bis.read(buffer)) >= 0) {
                arrayBuff.write(buffer, 0, len);
            }

        } catch (Exception e) {

        } finally {

            try {

                in.close();
                bis.close();

            } catch (Exception e) {
            }
        }
        return arrayBuff.toByteArray();
    }
}
