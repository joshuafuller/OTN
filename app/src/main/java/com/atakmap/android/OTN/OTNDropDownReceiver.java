
package com.atakmap.android.OTN;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

//import androidx.recyclerview.widget.RecyclerView;

import com.atak.plugins.impl.PluginLayoutInflater;
import com.atakmap.android.OTN.plugin.R;
import com.atakmap.android.dropdown.DropDown.OnStateListener;
import com.atakmap.android.dropdown.DropDownReceiver;
import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.maps.MapView;
import com.atakmap.coremap.log.Log;

import java.util.List;

public class OTNDropDownReceiver extends DropDownReceiver implements
        OnStateListener {

    public static final String TAG = OTNDropDownReceiver.class
            .getSimpleName();

    public static final String SHOW_PLUGIN = "com.atakmap.android.OTN.SHOW_PLUGIN";
    public static final String SET_GRAPHS = "com.atakmap.android.OTN.SET_GRAPHS";
    public static final String FIND_GRAPHS = "com.atakmap.android.OTN.FIND_GRAPHS";
    private final View templateView;
    private final Context pluginContext;
    private MapView _view;


    /**************************** CONSTRUCTOR *****************************/

    public OTNDropDownReceiver(final MapView mapView,
                               final Context context) {
        super(mapView);
        this._view = mapView;
        this.pluginContext = context;

        // Remember to use the PluginLayoutInflator if you are actually inflating a custom view
        // In this case, using it is not necessary - but I am putting it here to remind
        // developers to look at this Inflator
        templateView = PluginLayoutInflater.inflate(context,
                R.layout.main_layout, null);
        ImageButton refreshButton =  templateView.findViewById(R.id.refresh_button);
        if (refreshButton == null) {
            Log.w( TAG , "refresh button not working");
        }
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG , "refreshcallback");
                Intent intent = new Intent( OTNDropDownReceiver.FIND_GRAPHS);
                AtakBroadcast.getInstance().sendBroadcast(intent);
            }
        });




    }

    /**************************** PUBLIC METHODS *****************************/

    public void disposeImpl() {
    }

    /**************************** INHERITED METHODS *****************************/

    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();
        if (action == null)
            return;

        switch (action){
            case (SHOW_PLUGIN): {
                Log.d(TAG, "showing plugin drop down");
                showDropDown(templateView, HALF_WIDTH, FULL_HEIGHT, FULL_WIDTH,
                    HALF_HEIGHT, false, this);
                break;
            }
            case (SET_GRAPHS):{
                Bundle graphsBundle = intent.getBundleExtra("GRAPHS");
                if (graphsBundle == null) {
                    Log.w(TAG,"failled importing bundle");
                    return;

                }

                List<OTNGraph> graphs = (List<OTNGraph>) graphsBundle.getSerializable("GRAPHS");
                if ( graphs == null ) {
                    Log.w(TAG,"failled importing graph list");
                    return;
                }

                Bundle graphBundle = intent.getBundleExtra("GRAPH");
                if (graphBundle == null) {
                    Log.w(TAG,"failled importing bundle");
                    return;
                }

                OTNGraph selectedGraph = (OTNGraph) graphBundle.getSerializable( "GRAPH" );
                ListView graphListView = (ListView) templateView.findViewById(R.id.graph_list);
                ArrayAdapter graphListAdapter = new OTNGraphAdapter(pluginContext , R.layout.graph_listitem , graphs , selectedGraph );
                graphListView.setAdapter(graphListAdapter);
                // highlight selceted graph

                //text.setText( graphs.get(0).getGraphPath() +" dir and profile name:"+graphs.get(0).getConfigGH().getProfiles().get(0).getName() );
                break;

            }
        }

    }

    @Override
    public void onDropDownSelectionRemoved() {
    }

    @Override
    public void onDropDownVisible(boolean v) {
    }

    @Override
    public void onDropDownSizeChanged(double width, double height) {
    }

    @Override
    public void onDropDownClose() {
    }



}
