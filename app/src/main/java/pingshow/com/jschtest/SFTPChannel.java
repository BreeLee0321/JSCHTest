package pingshow.com.jschtest;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

import java.util.Map;

import java.util.Properties;


import com.jcraft.jsch.JSch;

/**
 * Created by kongtao on 2017/7/19.
 */

public class SFTPChannel {
    private final int   tunnelRemotePort=4444;
    String secondPassword="admin";
    Session session = null;
    Channel channel = null;
    Session secondSession=null;
    String RemoteHost="localhost"; // The host of the second target


    public ChannelSftp getChannel(Map<String, String> sftpDetails, int timeout){
        try {
            String host = sftpDetails.get(SFTPConstants.SFTP_REQ_HOST);
            String port = sftpDetails.get(SFTPConstants.SFTP_REQ_PORT);
            String ftpUserName = sftpDetails.get(SFTPConstants.SFTP_REQ_USERNAME);
            String ftpPassword = sftpDetails.get(SFTPConstants.SFTP_REQ_PASSWORD);
            int ftpPort = SFTPConstants.SFTP_DEFAULT_PORT;
            if (port != null && !port.equals("")) {
                ftpPort = Integer.valueOf(port);
            }

            JSch jsch = new JSch(); // 创建JSch对象
            session = jsch.getSession(ftpUserName, host, ftpPort); // 根据用户名，主机ip，端口获取一个Session对象
            Log.d("Session created.");
            if (ftpPassword != null) {
                session.setPassword(ftpPassword); // 设置密码
            }
            localUserInfo lui=new localUserInfo();
            session.setUserInfo(lui);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config); // 为Session对象设置properties
            session.setTimeout(timeout); // 设置timeout时间
            session.setUserInfo(new localUserInfo());
            session.setPortForwardingL(tunnelRemotePort, "192.168.1.13", 22);

            Log.d("Session start connect.");
            session.connect(); // 通过Session建立链接
            Log.d("Session connected.");

            Log.d("Opening Channel direct-tcpip.");
            session.openChannel("direct-tcpip");

            // create a session connected to port 3333 on the localhost.
            secondSession = jsch.getSession(ftpUserName, RemoteHost, tunnelRemotePort);
            secondSession.setUserInfo(lui);
            secondSession.setPassword(secondPassword);
            secondSession.setConfig("StrictHostKeyChecking", "no");

            secondSession.connect(); // now we're connected to the second system
            Log.d("Opening Channel sftp.");
            channel = secondSession.openChannel("sftp"); // 打开SFTP通道
            Log.d("secondSession start connect.");
            channel.connect(); // 建立SFTP通道的连接
            Log.d("secondSession connected");

        } catch (Exception e) {
            Log.d("Session connect error");
            e.printStackTrace();
        }finally {

        }
        Log.d("return  ChannelSftp :"+channel);
        return (ChannelSftp) channel;
    }


    public void closeChannel() throws Exception {
        try {
            if (channel != null)
                channel.disconnect();
            if (session != null)
                session.disconnect();
            if (secondSession!=null){
                secondSession.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
