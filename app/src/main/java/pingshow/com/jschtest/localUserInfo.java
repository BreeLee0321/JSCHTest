package pingshow.com.jschtest;

import com.jcraft.jsch.UserInfo;

/**
 * Created by kongtao on 2017/7/21.
 */

public  class localUserInfo implements UserInfo {
    String passwd="admin";
    public String getPassword(){ return passwd; }
    public boolean promptYesNo(String str){return true;}
    public String getPassphrase(){ return null; }
    public boolean promptPassphrase(String message){return true; }
    public boolean promptPassword(String message){return true;}
    public void showMessage(String message){}
}