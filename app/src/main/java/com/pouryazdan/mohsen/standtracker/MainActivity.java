package com.pouryazdan.mohsen.standtracker;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;

import net.sqlcipher.database.SQLiteDatabase;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient mApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SQLiteDatabase.loadLibs(this);
        DbHandler db = new DbHandler(this);
        db.getWritableDatabase("P@ssw0rd");

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mApiClient.connect();

        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Chart.class);
                startActivity(intent);
                /*
                DbHandler db = new DbHandler(MainActivity.this);
                try {

                    StandData data = db.findByDate("2017-01-08");
                    TextView tv1 = (TextView) findViewById(R.id.tv1);

                    if (data != null) {
                        tv1.setText("" + data.get_count() + " " + db.getStandDataCount());

                    } else {
                        db.addStandDate(new StandData("2017-01-08", 1));
                        tv1.setText("" + db.getStandDataCount() + " " + db.getStandDate(1).get_date());
                    }
                } catch (Exception e) {
                    Log.e("main", e.getMessage());
                }
                */
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        TextView tv = (TextView) findViewById(R.id.tv1);
        tv.setText("Connected");
        Intent intent = new Intent(this, ActivityRecognizedService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mApiClient, 100, pendingIntent);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
