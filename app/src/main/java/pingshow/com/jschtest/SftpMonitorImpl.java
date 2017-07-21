package pingshow.com.jschtest;

import com.jcraft.jsch.SftpProgressMonitor;

/**
 * Created by bree on 2017/7/19.
 */

public class SftpMonitorImpl implements SftpProgressMonitor {
    private long transfered;
    @Override
    public void init(int i, String s, String s1, long l) {
        Log.d("Transferring begin.");
    }

    @Override
    public boolean count(long count) {
        transfered = transfered + count;
        if(transfered<1024)
        {
            Log.d("Currently transferred total size: " + transfered + " bytes");
        }
        if ((transfered> 1024) && (transfered<1048576))
        {
            Log.d("Currently transferred total size: " + (transfered/1024) + "K bytes");
        }
        else
        {
            Log.d("Currently transferred total size: " +( transfered/1024/1024) + "M bytes");
        }
        return true;
    }

    @Override
    public void end() {
        Log.d("Transferring done.");
    }
}
