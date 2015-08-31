package com.devpoint.user;

import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.RP.database.Repo;
import com.RPRT.database.model.logcaterror;
import com.devpoint.rprtgnet.SplashScreen;
public class PostLogcatErrors {

	private JSONObject SaveOfferObj;

	public void PostLogcatErorrs(Exception e) {

		try {
			 
			String gcmid = SplashScreen.regId;
			
			
				final JSONObject child = new JSONObject();
				final JSONObject parent = new JSONObject();
				SaveOfferObj = new JSONObject();
				try {

					child.put("MobileGCMID",gcmid);										
					child.put("ClassName",e.getClass());
					child.put("ErrorMSG",e.getMessage() );	
					
					parent.put("SaveComResult", child);

					SaveOfferObj.put("SaveComObj", parent);
					

				} catch (JSONException e1) {
					
				}
				
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						try {

							HttpClient SaveWorkOrderclient = new DefaultHttpClient();
							HttpPost request = new HttpPost(
									"http://192.168.2.96/Firoj/DevpointAndroidPHP/AndroidCreatingRESTAPI/PHPProject/RPRT/v1/SaveLogCatInfo");

							StringEntity s = new StringEntity(SaveOfferObj
									.toString());

							request.setEntity(s);
							
							 
						} catch (Exception e) {
							
						}
					}
				});

				thread.start();
				
				
			e.printStackTrace();
			
			logcaterror logcat = null;

			Repo repoObject = SplashScreen.getRepo();
			
			logcat = new logcaterror();
			
			
			logcat.setDBLogID(1);
			logcat.setMobileGCMID(2);
			logcat.setClassName(e.getClass().toString());
			logcat.setErrorMSG(e.getMessage());
			logcat.setHashCode(e.hashCode());
			logcat.setGetLocalizedMessage(e.getLocalizedMessage());
			logcat.setGetMessage(e.getMessage());
			logcat.setToString(e.toString());
			logcat.setFillInStackTrace(e.fillInStackTrace().toString());
			logcat.setGetCause("");
			logcat.setGetClass(e.getClass().toString());
			logcat.setGetStackTrace(e.getStackTrace().toString());
			
			repoObject.rlogcat.save(logcat);
			
			repoObject = SplashScreen.getRepo();

			List<logcaterror> allViews = repoObject.rlogcat.getAllActions();

			if (allViews != null) {
				for (logcaterror s : allViews) {
					
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}


	
	}

}
