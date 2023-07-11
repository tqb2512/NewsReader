package com.tqb.newsreader.backend.broadcast;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tqb.newsreader.MainActivity;
import com.tqb.newsreader.NewsFeed;
import com.tqb.newsreader.R;

public class ConnectivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            if (intent.getBooleanExtra("noConnectivity", false)) {
                MainActivity.noInternetDialog.setMessage(context.getResources().getString(R.string.no_internet));
                MainActivity.noInternetDialog.show();
            } else {
                if (MainActivity.noInternetDialog.isShowing()) {
                    MainActivity.noInternetDialog.setMessage(context.getResources().getString(R.string.connected));
                    android.os.Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.noInternetDialog.dismiss();
                        }
                    }, 2000);
                }


            }
        }
    }
}
