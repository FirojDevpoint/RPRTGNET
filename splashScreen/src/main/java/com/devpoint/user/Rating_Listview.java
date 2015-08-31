package com.devpoint.user;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.RP.database.Repo;
import com.RPRT.database.model.OpportunityFeedBack;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.devpoint.adapter.CustomAdapter;
import com.devpoint.model.RatingLIstDetails;
import com.devpoint.rprtgnet.LoadActivity;
import com.devpoint.rprtgnet.R;
import com.devpoint.rprtgnet.SplashScreen;
import com.devpoint.volley.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Rating_Listview extends Fragment {
	ListView lv;

	TextView rev1;
	TextView rev2;
	TextView rev3;
	TextView rev4;
	TextView rev5;
	private String OfferIDVal;

	double latitude;
	double longitude;
	CopyOnWriteArrayList<RatingLIstDetails> ListItems;
	CustomAdapter CustomAdapter;
	View rootView;

	private ProgressDialog progresdialog;
	// ProgressDialog progresdialog;
	//private Bundle args;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		try {
			setRetainInstance(true);

			 rootView = inflater.inflate(R.layout.rating_list, container, false);

			Bundle args = getArguments();
			// OfferId = args.getString(getString(R.string.Offer_id));
			String OfferID = args.getString(getString(R.string.user_offer));

			OfferIDVal = args.getString(getString(R.string.Offer_id));

			ListItems = new CopyOnWriteArrayList<>();

			lv = (ListView) rootView.findViewById(R.id.rating_listView);
			View emptyView = rootView.findViewById(R.id.emptyView);
			lv.setEmptyView(emptyView);
			RatingBar rat_list1 = (RatingBar) rootView.findViewById(R.id.user_rat1);
			RatingBar rat_list2 = (RatingBar) rootView.findViewById(R.id.user_rat2);
			RatingBar rat_list3 = (RatingBar) rootView.findViewById(R.id.user_rat3);
			RatingBar rat_list4 = (RatingBar) rootView.findViewById(R.id.user_rat4);
			RatingBar rat_list5 = (RatingBar) rootView.findViewById(R.id.user_rat5);

			// offerid.setText(OfferID);
			rat_list1.setRating(1f);
			rat_list2.setRating(2f);
			rat_list3.setRating(3f);
			rat_list4.setRating(4f);
			rat_list5.setRating(5f);

			TextView offerid = (TextView) rootView.findViewById(R.id.offer_id);

			rev1 = (TextView) rootView.findViewById(R.id.rev_rat1);
			rev2 = (TextView) rootView.findViewById(R.id.rev_rat2);
			rev3 = (TextView) rootView.findViewById(R.id.rev_rat3);
			rev4 = (TextView) rootView.findViewById(R.id.rev_rat4);
			rev5 = (TextView) rootView.findViewById(R.id.rev_rat5);

			offerid.setText(OfferID);

			GetOfferShopList();

			return rootView;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rootView;
	}

	private void GetOfferShopList() {
		try {
			if (LoadActivity.isOnline) {
				
				progresdialog = new ProgressDialog(getActivity());
				progresdialog.setMessage(Html
						.fromHtml("<b>Search</b><br/>Loading Comments..."));
				progresdialog.setIndeterminate(false);
				progresdialog.setCancelable(false);
				progresdialog.show();
				
				final Location CurrentLocation = new Location("point A");
				CurrentLocation.setLatitude(latitude);
				CurrentLocation.setLongitude(longitude); 

				String url = LoadActivity.BaseUri + "GetFeedBackByOpportunityID/"+ OfferIDVal;
				
				JsonObjectRequest jsObjRequest;
				jsObjRequest = new JsonObjectRequest(Request.Method.GET, url,
						null, new Response.Listener<JSONObject>() { 

							@Override
							public void onResponse(JSONObject response) {

								try {
									JSONArray Ratings = response
											.getJSONArray("FeedbackItems");

									JSONArray RatingValues = response
											.getJSONArray("RatingsCount");

									for (int i = 0; i < RatingValues.length(); i++) {
										JSONObject obj = RatingValues
												.getJSONObject(i);

										String RatingNum = obj
												.getString("Rating");


										switch (String.valueOf(RatingNum)) {
											case "1":
												rev1.setText(obj
														.getString("Ratings")
														+ "  Ratings");
												break;
											case "2":
												rev2.setText(obj
														.getString("Ratings")
														+ "  Ratings");

												break;
											case "3":
												rev3.setText(obj
														.getString("Ratings")
														+ "  Ratings");

												break;
											case "4":
												rev4.setText(obj
														.getString("Ratings")
														+ "  Ratings");

												break;
											case "5":
												rev5.setText(obj
														.getString("Ratings")
														+ "  Ratings");
												break;
										}
									}

									if (rev1.getText().toString().equals("")) {
										rev1.setText(" 0 Ratings");
									}
									if (rev2.getText().toString().equals("")) {
										rev2.setText(" 0 Ratings");
									}
									if (rev3.getText().toString().equals("")) {
										rev3.setText(" 0 Ratings");
									}
									if (rev4.getText().toString().equals("")) {
										rev4.setText(" 0 Ratings");
									}
									if (rev5.getText().toString().equals("")) {
										rev5.setText(" 0 Ratings");
									}

									for (int i = 0; i < Ratings.length(); i++) {

										JSONObject obj = Ratings
												.getJSONObject(i);

										final RatingLIstDetails ListModelObj = new RatingLIstDetails();

										ListModelObj.setRating(obj
												.getString(getString(R.string.ratingbar_value)));
										ListModelObj.setReview(obj
												.getString(getString(R.string.review_Comments)));
										ListModelObj.setUserName(obj
												.getString(getString(R.string.Chat_UserName)));
										ListModelObj.setCreatedDate(obj
												.getString(getString(R.string.Created_Date)));

										ListItems.add(ListModelObj);

										OpportunityFeedBack feedbacks;

										Repo repoObject = SplashScreen.getRepo();

										feedbacks = new OpportunityFeedBack();

										feedbacks.setFeedbackId(Integer.parseInt(obj
												.getString("FeedbackId")));
										feedbacks.setUserId(Integer
												.parseInt(obj
														.getString("UserId")));
										feedbacks.setOpportunityID(Integer.parseInt(obj
												.getString("OpportunityID")));
										feedbacks.setRating(obj
												.getString("Rating"));
										feedbacks.setComments(obj
												.getString("Comments"));
										feedbacks.setFeedbackDate(obj
												.getString("FeedbackDate"));
										feedbacks.setFeedBackTitle(obj
												.getString("FeedbackTitle"));
										feedbacks.setRowStatus("A");

										repoObject.rOpportunityfeedback
												.save(feedbacks);

									}

									CustomAdapter = new CustomAdapter(
											getActivity(), ListItems);
									lv.setAdapter(CustomAdapter);

									CustomAdapter.notifyDataSetChanged();
									progresdialog.dismiss();
								} catch (JSONException e) {
									e.printStackTrace();        	
					                PostLogcatErrors ple = new PostLogcatErrors();
					                ple.PostLogcatErorrs(e);
								}

								Toast.makeText(getActivity(), "Success",
										Toast.LENGTH_LONG).show();

							}

						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								Toast.makeText(getActivity(), "False",
										Toast.LENGTH_LONG).show();
								progresdialog.dismiss();
							}
						});

				AppController.getInstance().addToRequestQueue(jsObjRequest);

				
			} else {

				Repo repoObject = SplashScreen.getRepo();

				List<OpportunityFeedBack> allfeedbacks = repoObject.rOpportunityfeedback
						.getAllfeedbacksbyOppid(OfferIDVal);

				ListItems = new CopyOnWriteArrayList<>();
				if (allfeedbacks != null) {
					for (OpportunityFeedBack s : allfeedbacks) {
						final RatingLIstDetails ListModelObj = new RatingLIstDetails();
						ListModelObj.setReview(s.getComments());
						ListModelObj.setUserName(String.valueOf(s.getUserId()));
						ListModelObj.setCreatedDate(s.getFeedbackDate());
						ListModelObj.setRating(String.valueOf(s.getRating()));

						ListItems.add(ListModelObj);
					}
					CustomAdapter = new CustomAdapter(getActivity(), ListItems);
					lv.setAdapter(CustomAdapter);

					CustomAdapter.notifyDataSetChanged();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();        	
            PostLogcatErrors ple = new PostLogcatErrors();
            ple.PostLogcatErorrs(e);
		}
	}

}
