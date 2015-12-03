package taxiapp.handlers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

/**
 * Created by hassanjamil on 2015-12-04.
 */
public class GeocodeHandler extends Handler {
    private TextView tvAddress;

    public GeocodeHandler(TextView tvAddress) {
        this.tvAddress = tvAddress;
    }

    @Override
    public void handleMessage(Message message) {
        String locationAddress;
        switch (message.what) {
            case 1:
                Bundle bundle = message.getData();
                locationAddress = bundle.getString("address");
                break;
            default:
                locationAddress = null;
        }
        tvAddress.setText(locationAddress);
    }
}
