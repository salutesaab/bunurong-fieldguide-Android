/**
 * 
 */
package au.com.museumvictoria.fieldguide.bunurong.ui.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import au.com.museumvictoria.fieldguide.bunurong.R;
import au.com.museumvictoria.fieldguide.bunurong.util.Utilities;

import com.actionbarsherlock.app.SherlockFragment;

public class WebFragment extends SherlockFragment {
	
	private String htmlPage = "information"; 
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		Bundle bundle = getArguments();
		if (bundle.containsKey("pageurl")) {
			htmlPage = bundle.getString("pageurl"); 
		}
		
		String filename = "html/"+htmlPage+".html";
		
		Log.d("Loading webview", "filename: " + filename); 
		
		AssetManager mgr = getActivity().getAssets();
        try {
            //display this html in the browser
            WebView w = (WebView) getActivity().findViewById(R.id.webView);
            w.setBackgroundColor(Color.BLACK);
            w.getSettings().setSupportZoom(false); 
            w.getSettings().setJavaScriptEnabled(true); 
            
            if (htmlPage.equalsIgnoreCase("opensourcelicences")) {
            	w.loadDataWithBaseURL("", getResources().getString(R.string.opensourcelicences), "text/html", "utf-8", null);
            } else if (htmlPage.equalsIgnoreCase("aboutthreatenedstatus")) {
            	w.loadDataWithBaseURL("", getResources().getString(R.string.aboutthreatenedstatus), "text/html", "utf-8", null);
            } else {
                w.loadUrl("file://" + Utilities.getFullExternalDataPath(getActivity(), filename)); 
            }

        } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        }		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_web, container, false);
		return view;
	}
	
	public static String streamToString(InputStream in) throws IOException {
        if(in == null) {
            return "";
        }

        Writer writer = new StringWriter();
        char[] buffer = new char[1024];

        try {
            Reader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }

        } finally {

        }

        return writer.toString();
    }
}
