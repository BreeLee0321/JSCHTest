package pingshow.com.jschtest;

import android.os.Environment;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;
import com.jcraft.jsch.UserInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bree on 2017/7/19.
 */

public class SFTPGetTest {


    public SFTPChannel getSFTPChannel() {
        return new SFTPChannel();
    }

    public static void downLoadFile(String remotPath, String localPath) {
        ChannelSftp chSftp = null;
        SFTPChannel channel = null;
        try {
            SFTPGetTest test = new SFTPGetTest();
            Map<String, String> sftpDetails = new HashMap<String, String>();
            // 设置主机ip，端口，用户名，密码
            sftpDetails.put(SFTPConstants.SFTP_REQ_HOST, "192.168.1.223");
            sftpDetails.put(SFTPConstants.SFTP_REQ_USERNAME, "root");
            sftpDetails.put(SFTPConstants.SFTP_REQ_PASSWORD, "123456");
            sftpDetails.put(SFTPConstants.SFTP_REQ_PORT, "22");
            channel = test.getSFTPChannel();

            //获得channel
            chSftp = channel.getChannel(sftpDetails, 60000);

            File file = Environment.getExternalStorageDirectory();
            Log.d("remotPath : " + remotPath);
            SftpATTRS attr = chSftp.stat(remotPath);
            long fileSize = attr.getSize();

            String dst = localPath;
            OutputStream out = new FileOutputStream(file + dst);


//            chSftp.get(filename, dst, new SftpMonitorImpl()); // 代码段1

//             chSftp.get(filename, out, new FileProgressMonitor(fileSize)); // 代码段2

            InputStream is = chSftp.get(remotPath, new SftpMonitorImpl());
            byte[] buff = new byte[1024 * 1];
            int read;
            if (is != null) {
                Log.d("Start to read input stream");
                do {
                    read = is.read(buff, 0, buff.length);
                    if (read > 0) {
                        out.write(buff, 0, read);
                    }
                    out.flush();
                } while (read >= 0);
                Log.d("input stream read done.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                chSftp.quit();
                channel.closeChannel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
