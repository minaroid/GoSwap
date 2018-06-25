package swap.go.george.mina.goswap.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import swap.go.george.mina.goswap.R;

public class ConnectionBroadCast extends BroadcastReceiver{
    CommonUtils utils = new CommonUtils();
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
               if(!utils.checkConnection(context)){
                   Toast.makeText(context,R.string.msg_no_connection,Toast.LENGTH_SHORT).show();
               }
            }

    }
}
