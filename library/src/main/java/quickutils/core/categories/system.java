package quickutils.core.categories;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.security.auth.x500.X500Principal;

import quickutils.core.QuickUtils;

public class system {

    /**
     * private constructor
     */
    protected system() {
    }

    /**
     * Common Buffer Size
     */
    static final int BUFFER = 2048;

    /**
     * Navigate to an activity programmatically by providing the package +
     * activity name
     *
     * @param context   Context where I am coming from
     * @param className Full path to the desired Activity (e.g.
     *                  "com.sample.MainActivity")
     */
    public static void navigateToActivityByClassName(Context context, String className) throws ClassNotFoundException {
        Class<?> c = null;
        if (className != null) {
            try {
                c = Class.forName(className);
            } catch (ClassNotFoundException e) {
                QuickUtils.log.d("ClassNotFound", e);
            }
        }

        navigateToActivity(context, c);
    }

    /**
     * Navigate to an activity programmatically by providing the package +
     * activity name
     *
     * @param context   Activity where I am coming from
     * @param className Full path to the desired Activity (e.g.
     *                  "com.sample.MainActivity")
     * @param flags     flags to be set upon the launch of the B Activity
     */
    public static void navigateToActivityByClassName(Context context, String className, int flags) throws ClassNotFoundException {
        Class<?> c = null;
        if (className != null) {
            try {
                c = Class.forName(className);
            } catch (ClassNotFoundException e) {
                QuickUtils.log.d("ClassNotFound", e);
            }
        }

        navigateToActivity(context, c, flags);
    }

    /**
     * Navigate to an activity.<br>
     * e.g. navigateToActivity(context, SecondActivity.class)
     *
     * @param A From Activity
     * @param B Destination Activity
     */
    public static void navigateToActivity(Context A, Class<?> B) {
        Intent myIntent = new Intent(A, B);
        A.startActivity(myIntent);
    }

    /**
     * Navigate to an activity.<br>
     * e.g. navigateToActivity(this,SecondActivity.class,
     * Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
     *
     * @param A     From Activity
     * @param B     Destination Activity
     * @param flags flags to be set upon the launch of the B Activity
     */
    public static void navigateToActivity(Context A, Class<?> B, int flags) {
        Intent myIntent = new Intent(A, B);
        myIntent.setFlags(flags);
        A.startActivity(myIntent);
    }

    /**
     * Navigate to an activity.<br>
     * e.g. navigateToActivity(this,SecondActivity.class, extras)
     *
     * @param A      From Activity
     * @param B      Destination Activity
     * @param extras Extras to be included in the new Activity
     */
    public static void navigateToActivity(Context A, Class<?> B, Bundle extras) {
        Intent myIntent = new Intent(A, B);
        myIntent.putExtras(extras);
        A.startActivity(myIntent);
    }

    /**
     * Navigate to an activity.<br>
     * e.g. navigateToActivity(this,SecondActivity.class, extras,
     * Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK))
     *
     * @param A      From Activity
     * @param B      Destination Activity
     * @param extras Extras to be included in the new Activity
     * @param flags  flags to be set upon the launch of the B Activity
     */
    public static void navigateToActivity(Context A, Class<?> B, Bundle extras, int flags) {
        Intent myIntent = new Intent(A, B);
        myIntent.putExtras(extras);
        myIntent.setFlags(flags);
        A.startActivity(myIntent);
    }

    /**
     * Your app key hash is required for example, for Facebook Login in order to
     * perform security check before authorizing your app.
     *
     * @param packageName name of the package (e.g. "com.example.app")
     * @return the application hash key
     */
    public static String getApplicationHashKey(String packageName) {

        String hash = "";
        try {
            PackageInfo info = QuickUtils.getContext().getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                hash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (NameNotFoundException e) {
            QuickUtils.log.e("NameNotFoundException");
        } catch (NoSuchAlgorithmException e) {
            QuickUtils.log.e("NoSuchAlgorithmException");

        }
        return hash;
    }

    /**
     * Make the smartphone vibrate for a giving time. You need to put the
     * vibration permission in the manifest as follows: <uses-permission
     * android:name="android.permission.VIBRATE"/>
     *
     * @param duration duration of the vibration in miliseconds
     */
    public static void vibrate(int duration) {
        Vibrator v = (Vibrator) QuickUtils.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(duration);
    }

    /**
     * Quick toast method with short duration
     *
     * @param message toast content
     */
    public static void toast(String message) {

        Toast toast = Toast.makeText(QuickUtils.getContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Quick toast method
     *
     * @param context  context in which will be displayed
     * @param message  The text to show. Can be formatted text.
     * @param duration How long to display the message. Either Toast.LENGTH_SHORT or
     *                 Toast.LENGTH_LONG
     */
    public static void toast(Context context, String message, int duration) {

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    /**
     * Sleep
     *
     * @param milliseconds seconds that the app will sleep
     */
    public static void sleep(int milliseconds) {

        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            QuickUtils.log.e("Interrupted exception", e);
        }
    }


    /**
     * Get device unique ID
     *
     * @param context application context
     * @return
     */
    public static String getDeviceID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    /**
     * Toggles the SoftKeyboard Input be careful where you call this from as if you want to
     * hide the keyboard and its already hidden it will be shown
     */
    public static void toggleKeyboard() {
        InputMethodManager imm = ((InputMethodManager) QuickUtils.getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
        imm.toggleSoftInput(0, 0);
    }

    /**
     * Hides the SoftKeyboard input careful as if you pass a view that didn't open the
     * soft-keyboard it will ignore this call and not close
     *
     * @param context the context / usually the activity that the view is being shown within
     * @param v       the view that opened the soft-keyboard
     */
    public static void requestHideKeyboard(View v) {
        InputMethodManager imm = ((InputMethodManager) QuickUtils.getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * Converts the number in pixels to the number in dips
     */
    public static int convertToDip(DisplayMetrics displayMetrics, int sizeInPixels) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sizeInPixels, displayMetrics);
    }

    /**
     * Converts the number in dips to the number in pixels
     */
    public static int convertToPix(float density, int sizeInDips) {
        float size = sizeInDips * density;
        return (int) size;
    }

    /**
     * Returns the version name of the current app
     *
     * @return the version name or "unknown" if the package name (of this current app) is not found
     */
    public static String getVersionName() {
        String versionName = "unknown";
        try {
            versionName = QuickUtils.getContext().getPackageManager().getPackageInfo(QuickUtils.getContext().getPackageName(), PackageManager.GET_ACTIVITIES).versionName;
        } catch (PackageManager.NameNotFoundException ignore) {
            QuickUtils.log.d(ignore.getMessage());
        }
        return versionName;
    }

    /**
     * Returns the version code of the current app
     *
     * @return the version name or "-1" if the package name (of this current app) is not found
     */
    public static int getVersionCode() {
        int versionName = -1;
        try {
            versionName = QuickUtils.getContext().getPackageManager().getPackageInfo(QuickUtils.getContext().getPackageName(), PackageManager.GET_ACTIVITIES).versionCode;
        } catch (PackageManager.NameNotFoundException ignore) {
            QuickUtils.log.d(ignore.getMessage());
        }
        return versionName;
    }

    /**
     * Current device DPI
     *
     * @return amount of DPIs
     */
    public static int deviceDPI() {
        return QuickUtils.getContext().getResources().getDisplayMetrics().densityDpi;
    }

    /**
     * Current device resolution
     *
     * @param c context
     * @return the resolution
     */
    public static String deviceResolution() {
        DisplayMetrics metrics = QuickUtils.getContext().getResources().getDisplayMetrics();
        return String.valueOf(metrics.widthPixels) + "x" + metrics.heightPixels;
    }

    /**
     * Is this service running?
     *
     * @param service service to check
     * @return true if the service is running
     */
    public static boolean isServiceRunning(Class<? extends Service> service) {
        ActivityManager manager = (ActivityManager) QuickUtils.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (service.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String getApplicationNameByContext() {
        final PackageManager pm = QuickUtils.getContext().getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(QuickUtils.getContext().getPackageName(), 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }
        return (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
    }

    /**
     * Writes the specified byte[] to the specified File path.
     *
     * @param theFile File Object representing the path to write to.
     * @param bytes   The byte[] of data to write to the File.
     * @throws java.io.IOException Thrown if there is problem creating or writing the
     *                             File.
     */
    public static void writeBytesToFile(File theFile, byte[] bytes) throws IOException {
        BufferedOutputStream bos = null;

        try {
            FileOutputStream fos = new FileOutputStream(theFile);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } finally {
            if (bos != null) {
                try {
                    //flush and close the BufferedOutputStream
                    bos.flush();
                    bos.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * Read bytes from a File into a byte[].
     *
     * @param file The File to read.
     * @return A byte[] containing the contents of the File.
     * @throws IOException Thrown if the File is too long to read or couldn't be
     *                     read fully.
     */
    public static byte[] readBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            throw new IOException("Could not completely read file " + file.getName() + " as it is too long (" + length + " bytes, max supported " + Integer.MAX_VALUE + ")");
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    /**
     * Compresses a single file (source) and prepares a zip file (target)
     *
     * @param source
     * @param target
     * @throws IOException
     */
    public static void compress(File source, File target) throws IOException {

        ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(target)));
        ZipEntry zipEntry = new ZipEntry(source.getName());
        zipOut.putNextEntry(zipEntry);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(source), BUFFER);
        byte data[] = new byte[BUFFER];

        int count = 0;
        while ((count = bis.read(data, 0, BUFFER)) != -1) {
            zipOut.write(data, 0, count);
        }
        bis.close();
        zipOut.close();

    }

    /**
     * Decompresses a zip file (source) that has a single zip entry.
     *
     * @param source
     * @param target
     * @throws IOException
     */
    public static void decompress(File source, File target) throws IOException {
        ZipInputStream zipIn = new ZipInputStream(new BufferedInputStream(new FileInputStream(source), BUFFER));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(target));
        zipIn.getNextEntry();
        byte data[] = new byte[BUFFER];

        int count = 0;
        while ((count = zipIn.read(data, 0, BUFFER)) != -1) {
            bos.write(data, 0, count);
        }
        bos.close();
        zipIn.close();
    }

    private static final X500Principal DEBUG_DN = new X500Principal("CN=Android Debug,O=Android,C=US");

    /**
     * Is this APK signed or is it a Debug build?
     *
     * @return true if it is not signed
     */
    public static boolean isDebuggable() {
        boolean debuggable = false;
        Context ctx = QuickUtils.getContext();

        try {
            PackageInfo pinfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature signatures[] = pinfo.signatures;

            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            for (int i = 0; i < signatures.length; i++) {
                ByteArrayInputStream stream = new ByteArrayInputStream(signatures[i].toByteArray());
                X509Certificate cert = (X509Certificate) cf.generateCertificate(stream);
                debuggable = cert.getSubjectX500Principal().equals(DEBUG_DN);
                if (debuggable)
                    break;
            }
        } catch (NameNotFoundException e) {
            //debuggable variable will remain false
        } catch (CertificateException e) {
            //debuggable variable will remain false
        }
        return debuggable;
    }

}
