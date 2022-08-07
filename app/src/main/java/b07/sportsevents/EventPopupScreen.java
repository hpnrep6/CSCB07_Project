package b07.sportsevents;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
//IGNORE this was just to make sure the click was working & info could be sent to popup
//- gelila
public class EventPopupScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);
        //displays all event info
        //has a join event button
        Bundle bundle = getIntent().getExtras();
        String v = bundle.get("event_id").toString();
        //View createdView = getLayoutInflater().inflate(R.layout.popup, null);
        ((TextView) this.findViewById(R.id.textView15)).setText(v);
    }

}
