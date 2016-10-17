package com.hitachi_tstv.samsen.tunyaporn.testsignature;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Created by tunyaporns on 10/14/2016.
 */

public class UploadImageUtils {
    public static String uploadFile(String fileNameInServer, String urlServer, Bitmap bitmap) {
        try {

            // configurable parameters
            // 1. upload url
            // 2. file name
            // 3. uploaded file path
            // 4. compress
            // 5. result

            HttpURLConnection connection = null;
            OutputStream outputStream = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";

            URL url = new URL(urlServer);
            connection = (HttpURLConnection) url.openConnection();

            Log.d("TAG", connection.toString() + " 1");

            // Allow Inputs & Outputs
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            // Enable POST method
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            Log.d("TAG", connection.toString() + " 2");
            outputStream = connection.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);


            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
            dataOutputStream
                    .writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
                            + fileNameInServer + "\"" + lineEnd);
            dataOutputStream.writeBytes(lineEnd);


            Log.d("TAG", connection.toString() + " 3");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] data = baos.toByteArray();

            Log.d("Bitmap", "Bitmap ==> " + bitmap);

            dataOutputStream.write(data);

            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens
                    + lineEnd);

            // Convert response message in inputstream to string.
            StringBuilder sb = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }

            Log.d("TAG", connection.toString() + " 4");

            dataOutputStream.flush();
            dataOutputStream.close();

            Log.d("Tag", "Return ==> " + sb.toString());

            return sb.toString();

        } catch (Exception e) {
            return null;
        }
    }

    public static String getRandomFileName() {
        String _df = android.text.format.DateFormat.format("MMddyyyyhhmmss",
                new java.util.Date()).toString();
        Random r = new Random();
        int random = Math.abs(r.nextInt() % 100);
        return String.format("%d%s.jpg", random, _df);
    }
}
