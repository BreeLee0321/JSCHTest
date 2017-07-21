package pingshow.com.jschtest;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by kongtao on 2017/7/21.
 */

public class RemoteCommand {
    public static List<Map<String, String>> executeRemoteCommandShell(Session session ,String... cmd) {
        // SSH Channel
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
            for (int i=0;i<cmd.length;i++){
                printWriter.println(cmd[i]);
            }
            printWriter.flush();
            in = new BufferedReader(new InputStreamReader(inputStream));
            String msg = null;
            while ((msg = in.readLine()) != null) {
                pingshow.com.jschtest.Log.d("msg  == " + msg);
//                stringBuilder.append(msg + "\n");
//                if (msg.contains("001.mp4")) break;
            }
//            pingshow.com.jschtest.Log.d("stringBuilder.toString(): " + stringBuilder.toString());
        } catch (Exception e) {
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
//        String result = stringBuilder.toString();
//        String s2 = "\\[0;0m";
//        String[] split = result.split(s2);
//        pingshow.com.jschtest.Log.d("split " + split.length);
        List<Map<String, String>> fileList = new ArrayList<>();
//        for (int i = 0; i < split.length; i++) {
//            Map<String, String> map = new HashMap<>();
//            String fileName = split[i].trim().replace("[0m", "");
//            map.put("filepath", "/sdcard/AireTalkTV/security/" + fileName);
//            map.put("filename", fileName);
//            fileList.add(map);
//            pingshow.com.jschtest.Log.d("filename = " + fileName);
//        }
//        fileList.remove(0);
        return fileList;
    }

    public static String executeRemoteCommandExec(Session session ,String... cmd) throws Exception {
        // SSH Channel
        ByteArrayOutputStream baos=null;
        ChannelExec channelssh=null;
        String result="";
        try {
            channelssh  = (ChannelExec)
                    session.openChannel("exec");
            baos = new ByteArrayOutputStream();
            channelssh.setOutputStream(baos);
            // Execute command
            channelssh.setCommand("sshpass -p admin ssh -p 3333 localhost");
            channelssh.connect();
            Thread.sleep(6000);
            channelssh.disconnect();
            byte[] bytes = baos.toByteArray();
            result = new String(bytes, "utf-8");
            pingshow.com.jschtest.Log.d("result=" + result);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            result=e.getMessage();
        }finally {
            if (baos!=null){
                baos.close();
            }
            if (channelssh!=null)
                channelssh.disconnect();
        }
       return result;
    }

}
