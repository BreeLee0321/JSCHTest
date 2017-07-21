package pingshow.com.jschtest;

//airecenter
public class Log {
    //	public static boolean enDEBUG = false;
    public static boolean enDEBUG = true;

    static public void d(String Msg) {
//		android.util.Log.d("AMP",Msg);
        if (enDEBUG) {
            android.util.Log.d("AMP", Msg);
        }
    }

    static public void i(String Msg) {
//		android.util.Log.i("AMP",Msg);
        if (enDEBUG) {
            android.util.Log.i("AMP", Msg);
        }
    }

    static public void e(String Msg) {
//		android.util.Log.e("AMP",Msg);
        if (enDEBUG) {
            android.util.Log.e("AMP", Msg);
        }
    }

    static public void w(String Msg) {
//		android.util.Log.w("AMP",Msg);
        if (enDEBUG) android.util.Log.w("AMP", Msg);
    }

    //-------------------------------------------------------可分类的log-------------------------------------------------------------------------------//

    static public void d(String TAG, String Msg) {
        if (enDEBUG) {
            android.util.Log.d("AMP-"+TAG, Msg);
        }
    }

    static public void i(String TAG, String Msg) {
        if (enDEBUG) {
            android.util.Log.i("AMP-"+TAG, Msg);
        }
    }

    static public void e(String TAG, String Msg) {
        if (enDEBUG) {
            android.util.Log.e("AMP-"+TAG, Msg);
        }
    }

    static public void w(String TAG, String Msg) {
        if (enDEBUG) android.util.Log.w("AMP-"+TAG, Msg);
    }
}
