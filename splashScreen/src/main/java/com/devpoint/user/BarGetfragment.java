package com.devpoint.user;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.RP.database.Repo;
import com.RPRT.database.model.bargain;
import com.devpoint.adapter.CustomBargetAdapter;
import com.devpoint.model.Bargainglistdetails;
import com.devpoint.rprtgnet.R;
import com.devpoint.rprtgnet.SplashScreen;




import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BarGetfragment extends Fragment {
	private View rootView;
	 ListView liv;
	 CustomBargetAdapter custombargetadpter;
		CopyOnWriteArrayList<Bargainglistdetails> ListItems;
		EditText barmsg;
		String msg ;
		String fromuid;
		String touid;
		String Id;
		Button barsave;
		ProgressDialog progresdialog;
		private String Offer;
		private String ShopName;
		private String AreaName;
		private String PostedDate;
		private String PromoCode;
		private String Category;
		private float Distance;
		private Repo repoObject; 

		private Bundle args;

	 double latitude;
		double longitude;
		private JSONObject SaveOfferObj;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);

		rootView = inflater.inflate(R.layout.bargete, container,
				false);
		
		 args = getArguments();
			//OfferId = args.getString(getString(R.string.Offer_id));
		 Offer = args.getString(getString(R.string.user_offer));
		ShopName = args.getString(getString(R.string.Shop_Name));
		AreaName = args.getString(getString(R.string.Area_Name));
		PostedDate = args.getString(getString(R.string.Posted_Date));
		//EndDate = args.getString(getString(R.string.End_Date));
		PromoCode = args.getString(getString(R.string.promo_code));
		Category = args.getString(getString(R.string.Rprt_Category));
		Distance = args.getFloat(getString(R.string.Place_distance));
		//GetOfferShopList();

		TextView categ = (TextView) rootView.findViewById(R.id.catego_text);
		TextView sname = (TextView) rootView.findViewById(R.id.sho_name);
		TextView podate = (TextView) rootView.findViewById(R.id.bar_posdat);
		TextView dist = (TextView) rootView.findViewById(R.id.Dist_txt);
		TextView promo = (TextView) rootView.findViewById(R.id.pro_code);
		TextView off = (TextView) rootView.findViewById(R.id.offer_name);
		
		dist.setText(Distance+" KM Away"); 
		sname.setText(ShopName+" , "+ AreaName);
		podate.setText(PostedDate);
		off.setText(Offer);
		promo.setText(PromoCode);
		categ.setText(Category);
		
		  liv=(ListView)rootView.findViewById(R.id.barget_list);
		  
		
		 ListItems = new CopyOnWriteArrayList<Bargainglistdetails>(); 
		 
		 barmsg = (EditText) rootView.findViewById(R.id.bargmessage);

		 barsave = (Button) rootView.findViewById(R.id.save_barget); 
		 
		 barsave.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					
					msg = barmsg.getText().toString();
					

						progresdialog = new ProgressDialog(getActivity());
						progresdialog.setMessage(Html
								.fromHtml("<b>Posting Deal</b><br/>Please Wait..."));
						progresdialog.setIndeterminate(false);
						progresdialog.setCancelable(false);
						progresdialog.show(); 
						final JSONObject child = new JSONObject();
						final JSONObject parent = new JSONObject();
						SaveOfferObj = new JSONObject();
						try {

							child.put("fromuid", 8);
							child.put("touid", 18);							
							child.put("OfferID", 5);
							child.put("Id", 12);
							child.put("messagetext", msg);
							
							parent.put("SaveBargainResult",child);

							SaveOfferObj.put("SaveBargainObj", parent);
						
						} catch (JSONException e) {
							Toast.makeText(getActivity(), e.getMessage(),
									Toast.LENGTH_LONG).show();
							e.printStackTrace();
						}

						Thread thread = new Thread(new Runnable() {
							@Override
							public void run() {
								try {

									HttpClient SaveWorkOrderclient = new DefaultHttpClient();
									HttpPost request = new HttpPost(
											"http://192.168.2.96/Firoj/DevpointAndroidPHP/AndroidCreatingRESTAPI/PHPProject/RPRT/v1/SaveBargain");

									StringEntity s = new StringEntity(SaveOfferObj
											.toString());

									request.setEntity(s);
									HttpResponse resp = SaveWorkOrderclient
											.execute(request);
									int responseCode = resp.getStatusLine()
											.getStatusCode(); 
									String message = resp.getStatusLine()
											.getReasonPhrase();

									if (message.equals(getString(R.string.rprt_ok)) && responseCode == 200) {
										((Activity) rootView.getContext())
												.runOnUiThread(new Runnable() {
													@Override
													public void run() {

														
															((Activity) rootView
																	.getContext())
																	.runOnUiThread(new Runnable() {
																		@Override
																		public void run() {
																			progresdialog.dismiss();
																			Toast.makeText(
																					getActivity(),
																					"success",
																					Toast.LENGTH_LONG)
																					.show();
																		}
																	});
														

													}

												});
									} else {
										((Activity) rootView.getContext())
												.runOnUiThread(new Runnable() {
													@Override
													public void run() {
														Toast.makeText(
																getActivity(),
																"Failed",
																Toast.LENGTH_LONG)
																.show();
													}
												});
									}
									
									

								} catch (Exception e) {
									Toast.makeText(getActivity(), e.getMessage(),
											Toast.LENGTH_LONG).show();
									e.printStackTrace();
								}
							}
						});

						thread.start();
						
						bargain bargi = null;

						 repoObject = SplashScreen.getRepo();
						 
						 bargi = new bargain();
						 bargi.setOfferID(5);
						 bargi.setFromuid(8);
						 bargi.setTouid(18);
						 bargi.setMessagetext(msg);
						 bargi.setId(12);
						 bargi.setRowStatus("A");
						repoObject.rbargain.save(bargi);
						
						 GetOfferShopList();
						
						
				}
			});
	      
		 
		 
		return rootView;
	}
	
	private void GetOfferShopList() {
		
		repoObject = SplashScreen.getRepo();

		List<bargain> alloffers = repoObject.rbargain.getAllCommentsbyOfferid(String.valueOf(5));
		
		
		if (!alloffers.isEmpty()) {
			for (bargain s1 : alloffers) {
				
				final Bargainglistdetails ListModelObj = new Bargainglistdetails();
				
	
	ListModelObj.setFromuid(String.valueOf(s1.getFromuid()));	
	ListModelObj.setTouid(String.valueOf(s1.getTouid()));
	ListModelObj.setOfferID(String.valueOf(s1.getOfferID()));
	ListModelObj.setMessagetext(s1.getMessagetext());
	ListModelObj.setId(String.valueOf(s1.getId()));

	ListItems.add(ListModelObj); 
			}
			custombargetadpter = new CustomBargetAdapter(
					getActivity(), ListItems);
			liv.setAdapter(custombargetadpter);

			custombargetadpter.notifyDataSetChanged();
			
			
			
		}
		/*
		try {
			if(SplashScreen.isOnline)
			{
			final Location CurrentLocation = new Location("point A");     
			CurrentLocation.setLatitude(latitude); 
			CurrentLocation.setLongitude(longitude);
			
			// Creating volley request obj
			JsonObjectRequest movieReq = new JsonObjectRequest(url+OfferId, null,
					new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) { 
							// Log.d(TAG, response.toString());

							JSONArray GetMG_AppointmentRequestResult = null;
							try {
								GetMG_AppointmentRequestResult = response
										.getJSONArray("GetAllComResult");
								
							} catch (JSONException e1) {
								Toast.makeText(getActivity(), e1.getMessage(),
										Toast.LENGTH_LONG).show();
								e1.printStackTrace();
							}
							// Parsing json
							for (int i = 0; i < GetMG_AppointmentRequestResult
									.length(); i++) {
								try {

									JSONObject obj = GetMG_AppointmentRequestResult
											.getJSONObject(i);

									final Bargainglistdetails ListModelObj = new Bargainglistdetails();
									
									ListModelObj.setFromuid(obj.getString("fromuid"));
	 								ListModelObj.setTouid(obj.getString("touid"));
	 									 								
	 								ListModelObj.setOfferID(obj.getString("OfferID")); 
	 								ListModelObj.setId(obj.getString("Id"));
	 								ListModelObj.setMessagetext(obj.getString("messagetext"));
									
									

									ListItems.add(ListModelObj); 
									
									bargain barga = null;

									Repo repoObject = LoadActivity.getRepo();
									
									barga = new bargain();
									
									//comts.setCommentID(Integer.parseInt(obj.getString("CommentID")));
									barga.setOfferID(Integer.parseInt(obj.getString("OfferID")));

									barga.setFromuid(Integer.parseInt(obj.getString("Fromuid")));
									barga.setTouid(Integer.parseInt(obj.getString("Touid")));
									barga.setId(Integer.parseInt(obj.getString("Id")));


									barga.setReaddt(obj.getString("Readdt"));
									barga.setMessagetext(obj.getString("Messagetext"));
									
									
									repoObject.rbargain.save(barga);
									
									
								} catch (JSONException e) {
									//PostLogcatErorrs(e);
								}
							}
					

							custombargetadpter = new CustomBargetAdapter(
									getActivity(), ListItems);
							liv.setAdapter(custombargetadpter);

							custombargetadpter.notifyDataSetChanged();

						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
						
						}

					});

			AppController.getInstance().addToRequestQueue(movieReq);
			}
			else {
				Repo repoObject = LoadActivity.getRepo();

				List<bargain> alloffers = repoObject.rbargain.getAllCommentsbyOfferid(OfferId);
				
				
				ListItems = new CopyOnWriteArrayList<Bargainglistdetails>();
				if (alloffers != null) {
					for (bargain s : alloffers) {
						final Bargainglistdetails ListModelObj = new Bargainglistdetails();
			ListModelObj.setMessagetext(s.getMessagetext());
			ListModelObj.setOfferID(String.valueOf(s.getOfferID()));

			ListItems.add(ListModelObj); 
					}
					custombargetadpter = new CustomBargetAdapter(
							getActivity(), ListItems);
					liv.setAdapter(custombargetadpter);

					custombargetadpter.notifyDataSetChanged();
					
					
				}
			}
		} catch (Exception e) {
			
			//PostLogcatErorrs(e);
		}
	*/}
	

}
