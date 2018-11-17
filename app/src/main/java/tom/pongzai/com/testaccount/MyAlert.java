package tom.pongzai.com.testaccount;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;


public class MyAlert {
  private Context context;

    public MyAlert(Context context) {
        this.context = context;
    }

    public void normalDialog(String title, String message){
        AlertDialog.Builder builer = new AlertDialog.Builder(context);
        builer.setCancelable(false);
        builer.setIcon(R.drawable.ic_action_alert);
        builer.setTitle(title);
        builer.setMessage(message);
        builer.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builer.show();

    }

}
