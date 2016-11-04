package com.example.adityasrivastava.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.amazonmobileanalytics.AnalyticsEvent;
import com.amazonaws.mobileconnectors.amazonmobileanalytics.MobileAnalyticsManager;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.mobileconnectors.cognito.exceptions.DataStorageException;
import com.amazonaws.regions.Regions;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    CognitoCachingCredentialsProvider credentialsProvider;
    CognitoSyncManager syncClient;
    private static MobileAnalyticsManager analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        Button buttonTwo = (Button) findViewById(R.id.xyz);
        buttonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                functionTwo();
            }
        });

    }

    private void init(){
        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:ea1d4533-7e05-45db-80b4-db0d28679a6e", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        analytics = MobileAnalyticsManager.getOrCreateInstance(getApplicationContext(),
                "3024dbf3f83c43ce920848b659ef3e56",
                Regions.US_EAST_1,
                credentialsProvider);

    }

    private void functionTwo() {

        AnalyticsEvent analyticsEvent = analytics.getEventClient().createEvent("Test3");

        analyticsEvent.addAttribute("studentId", "ZYZ-213");
        analyticsEvent.addMetric("questionAttempted", new Double(56));
        analyticsEvent.addMetric("questionTotalMarksObtained", new Double(32));
        analyticsEvent.addMetric("averageDuration", new Double(55));


        analytics.getEventClient().recordEvent(analyticsEvent);

        Toast.makeText(MainActivity.this,"dklfjsdflk", Toast.LENGTH_LONG).show();

    }


    public void functionOne(View v){

        // Create a record in a dataset and synchronize with the server
        Dataset dataset = syncClient.openOrCreateDataset("myDataset");
        dataset.put("myKey", "myValue");
        dataset.synchronize(new DefaultSyncCallback() {
            @Override
            public void onSuccess(Dataset dataset, List newRecords) {
                Toast.makeText(MainActivity.this, "Gekki sdfjsdlkfj", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(DataStorageException dse) {

                Toast.makeText(MainActivity.this, "dfsfdsfj", Toast.LENGTH_SHORT).show();
            }

            /**/
        });



    }

    /**
     * Invoked when the Activity loses user focus
     */
    @Override
    protected void onPause() {
        super.onPause();
        if(analytics != null) {
            analytics.getSessionClient().pauseSession();
            //Attempt to send any events that have been recorded to the Mobile Analytics service.
            analytics.getEventClient().submitEvents();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(analytics != null)  {
            analytics.getSessionClient().resumeSession();
        }
    }

}
