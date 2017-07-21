package pingshow.com.jschtest;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new AsyncTask<Integer, String, List<Map<String, String>>>() {
            @Override
            protected List<Map<String, String>> doInBackground(Integer... params) {
//                SFTPGetTest.downLoadFile("/sdcard/AireTalkTV/security/VID_20170718_132116.mp4", "/" + "VID_20170718_132116.mp4");
                RemoteCommand.executeRemoteCommandShell(getSession("root", "123456", "192.168.1.223", 22),"sshpass -p admin ssh -p 4444 localhost","cd /sdcard/AireTalkTV/security/",
                        "ls");
                return executeRemoteCommandShell();
            }

            @Override
            protected void onPostExecute(final List<Map<String, String>> list) {
                ((TextView) findViewById(R.id.tv)).setText(list.toString());
                super.onPostExecute(list);
            }
        }.execute(1);

    }

    private Session getSession(String username,
                               String password,
                               String hostname,
                               int port) {
        Session session = null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(username, hostname, port);
            session.setPassword(password);
            // Avoid asking for key confirmation
            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);
            session.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return session;
    }


    public List<Map<String, String>> executeRemoteCommandShell() {
        // SSH Channel
        Session session = getSession("root", "123456", "192.168.1.223", 22);
        ChannelShell channelssh = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        BufferedReader in = null;
        PrintWriter printWriter = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            channelssh = (ChannelShell)
                    session.openChannel("shell");
            channelssh.connect();
            inputStream = channelssh.getInputStream();//从远程端到达的所有数据都能从这个流中读取到
            outputStream = channelssh.getOutputStream();//写入该流的所有数据都将发送到远程端。
            printWriter = new PrintWriter(outputStream);
            printWriter.println("sshpass -p admin ssh -p 4444 localhost");
            printWriter.flush();
            printWriter.println("cd /sdcard/AireTalkTV/security/");
            printWriter.println("ls");
            printWriter.flush();
            in = new BufferedReader(new InputStreamReader(inputStream));
            String msg = null;
            while ((msg = in.readLine()) != null) {
                stringBuilder.append(msg + "\n");
                if (msg.contains("001.mp4")) break;
            }
//            pingshow.com.jschtest.Log.d("stringBuilder.toString(): " + stringBuilder.toString());
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outputStream.close();
                in.close();
                printWriter.close();
                session.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String result = stringBuilder.toString();
        String s2 = "\\[0;0m";
        String[] split = result.split(s2);
        pingshow.com.jschtest.Log.d("split " + split.length);
        List<Map<String, String>> fileList = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            Map<String, String> map = new HashMap<>();
            String fileName = split[i].trim().replace("[0m", "");
            map.put("filepath", "/sdcard/AireTalkTV/security/" + fileName);
            map.put("filename", fileName);
            fileList.add(map);
            pingshow.com.jschtest.Log.d("filename = " + fileName);
        }
        fileList.remove(0);
        return fileList;
    }
}
