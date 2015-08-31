package com.devpoint.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.RP.database.Repo;
import com.RPRT.database.model.OpportunityTable;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.devpoint.PlacesandMaps.ConnectionDetector;
import com.devpoint.PlacesandMaps.PlacesMapActivity;
import com.devpoint.adapter.ProductListAdapter;
import com.devpoint.adapter.SingleChoiceAdapter;
import com.devpoint.adapter.SortListAdapter;
import com.devpoint.common.BackStackingManager.ModuleFragmentBackStackingClass;
import com.devpoint.common.BackStackingManager.ModulesTagsClass.EnumModuleTags;
import com.devpoint.common.GetAllGooglePlaces;
import com.devpoint.model.CategoryListModel;
import com.devpoint.model.ListDetails;
import com.devpoint.retailer.Retailers_Posting_Details;
import com.devpoint.rprtgnet.LoadActivity;
import com.devpoint.rprtgnet.R;
import com.devpoint.rprtgnet.SplashScreen;
import com.devpoint.sharedpreferences.SessionManager;
import com.devpoint.sharedpreferences.SharedPreference;
import com.devpoint.volley.AppController;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.ScrollDirectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressLint("ShowToast")
public class User_Places_ListView extends Fragment implements OnItemClickListener {
	Boolean isInternetPresent = false;
	ConnectionDetector connectdetector;
	public static ListView swipelisview;
	Activity activity;
	public static ProductListAdapter productListAdapter; 
	SharedPreference sharedPreference;
	String LoggedIn;
	boolean fragmentAlreadyLoaded = false;
	// AIzaSyCZaeQvnUnTGQbJaTQWZun6aIiGqdDv2cY
	// AIzaSyCAekTB0o1MuSYvUb-8HTZxhlJHE8yBUfI
	// AIzaSyCRLa4LQZWNQBcjCYcIVYA45i9i8zfClqc
	// AIzaSyAwwlN-oROAweLBCwmtc1HSbexihcnMNwk
	private Repo repoObject;
	public static String pagename = "Userplaceslist";
	public static String multipleCat = "";
	public static String OpportunityID;
	public static LinearLayout footerlayout;
	String[] Sortby = new String[] { "Date", "Time", "Distance" };
	public static ArrayList<String> checkSelectedCat = new ArrayList<>(); 
	public static ArrayList<String> categoryids = new ArrayList<>();
	SessionManager session;
	protected EditText searchedit;
	String SearchText = "";
	public ProgressDialog progresdialog;
	String GooglePlacesString;
    String Radiosname;
	public static AutoCompleteTextView autoCompView;
	public static ProgressDialog pdialog;
	GetAllGooglePlaces GPlaces;
	Dialog searchdialog;
	private Button clearbtn;
	View rootView;
	
	
	
	

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

		try {
			super.onViewCreated(view, savedInstanceState);
			if (savedInstanceState == null && !fragmentAlreadyLoaded) {
				fragmentAlreadyLoaded = true;
				repoObject = SplashScreen.getRepo();
				session = new SessionManager(getActivity());
				HashMap<String, String> Radious = session.getRadiousName();
				Radiosname = Radious.get(SessionManager.KEY_RadiousName);
				GPlaces.GetOpportunityList("", Radiosname, getActivity());
				DisPlayOppList();
            }

			productListAdapter = new ProductListAdapter(getActivity(),
                    GetAllGooglePlaces.ListItemsData);
			swipelisview.setAdapter(productListAdapter);
			productListAdapter.notifyDataSetChanged();


			// Code placed here will be executed even when the fragment comes from
			// backstack
		} catch (Exception e) {
			e.printStackTrace();        	
            PostLogcatErrors ple = new PostLogcatErrors();
            ple.PostLogcatErorrs(e);
		}
	}

	// String APIkey ="AIzaSyCAekTB0o1MuSYvUb-8HTZxhlJHE8yBUfI";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			activity = getActivity();
			sharedPreference = new SharedPreference();
			setRetainInstance(true);
		} catch (Exception e) {
			e.printStackTrace();        	
            PostLogcatErrors ple = new PostLogcatErrors();
            ple.PostLogcatErorrs(e);
		}
	}

	@SuppressWarnings("static-access")
	@SuppressLint({ "CutPasteId", "ClickableViewAccessibility" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		try {
			setRetainInstance(true);
			
			
			 rootView = inflater.inflate(R.layout.user_places_listview, container,
					false);
			session = new SessionManager(getActivity());
			HashMap<String, String> Radious = session.getRadiousName();
			Radiosname = Radious.get(SessionManager.KEY_RadiousName);

			GPlaces = new GetAllGooglePlaces();
			searchedit = (EditText) rootView.findViewById(R.id.searchbox);

			Button searchbtn = (Button) rootView.findViewById(R.id.searchbtn);
			
			
			searchedit = (EditText) rootView.findViewById(R.id.searchbox);

			clearbtn = (Button) rootView.findViewById(R.id.clearbtn);

			searchedit.addTextChangedListener(watch);
			clearbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
			searchedit.setText("");
			GPlaces.GetOpportunityList("", Radiosname, getActivity());

			DisPlayOppList();
			}
			});
			
			
			
			
			searchbtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {

					getActivity().runOnUiThread(new Runnable() {

						public void run() {
							multipleCat = "";
							GetAllGooglePlaces.ListItemsData = new CopyOnWriteArrayList<>();
							SearchText = searchedit.getText()
									.toString();

							LoadActivity.Status = "Search";

							if (LoadActivity.isOnline) {
								GPlaces.GetOpportunityList(SearchText, Radiosname, getActivity());

								DisPlayOppList();
							}

							else {
								List<OpportunityTable> alloffers = repoObject.roffertable
										.getAlloffersbySearchKeyword(SearchText);

								if (alloffers != null) {

									GPlaces.FillArrayListOffline(alloffers);
								}

							}

						}
					});

				}
			});

			

			HashMap<String, String> user = session.getLogin();
			LoggedIn = user.get(SessionManager.KEY_Login);

			if (LoggedIn == null) {
				LoggedIn = "";
			}

			connectdetector = new ConnectionDetector(getActivity());

			

			try {
				// Check if Internet present
				isInternetPresent = connectdetector.isConnectingToInternet();
			
			} catch (Exception e) {
				e.printStackTrace();        	
			    PostLogcatErrors ple = new PostLogcatErrors();
			    ple.PostLogcatErorrs(e);
			}

			

			swipelisview = (ListView) rootView.findViewById(R.id.list);
			
			 /*LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
			 swipelisview.setLayoutManager(mLayoutManager);

			 swipelisview.addOnItemTouchListener(new RecyclerItemClickListener(
			            getActivity(), swipelisview,
			            new RecyclerItemClickListener.OnItemClickListener() {
			                @Override
			                public void onItemClick(View view, final int position) {

			                    try {

			    					InputMethodManager imm = (InputMethodManager) getActivity()
			    							.getSystemService(Context.INPUT_METHOD_SERVICE);
			    					imm.hideSoftInputFromWindow(swipelisview.getWindowToken(), 0);

			    					try {
			    						final DetailsViewpagerFragment mDetailsViewpagerFragment = new DetailsViewpagerFragment();
			    						String url;
			    						if (LoadActivity.isOnline) {

			    							progresdialog = new ProgressDialog(getActivity());
			    							progresdialog.setMessage(Html
			    									.fromHtml("<b>Search</b><br/>Loading Details..."));
			    							progresdialog.setIndeterminate(false);
			    							progresdialog.setCancelable(false);
			    							progresdialog.show();

			    							ListDetails product = GetAllGooglePlaces.ListItemsData
			    									.get(position);
			    							OpportunityID = product.getOfferID();


			    							url = LoadActivity.BaseUri + "SaveUserViewedOpportunities";

			    							JsonObjectRequest jsObjRequest;
			    							jsObjRequest = new JsonObjectRequest(
			    									Request.Method.POST, url,
			    									getSaveViewUserParams(),
			    									new Response.Listener<JSONObject>() {

			    										@Override
			    										public void onResponse(JSONObject response) {
			    											
			    											

			    											pagename = "Userplaceslist";

			    											mDetailsViewpagerFragment
			    													.setClickList(position);
			    											FragmentManager fragment = getFragmentManager();

			    											fragment.beginTransaction()
			    													.setCustomAnimations(
			    															R.anim.slide_in_up,
			    															R.anim.slide_out_up)
			    													.replace(R.id.frame_container,
			    															mDetailsViewpagerFragment)
			    													.commit();

			    											LoadActivity.CURRENTFRAGMENT = EnumModuleTags.SinglePlaceActivity;



			    											ModuleFragmentBackStackingClass
			    													.AddtoStack(
			    															mDetailsViewpagerFragment,
			    															EnumModuleTags.SinglePlaceActivity,
			    															getString(R.string.mainfrgment_rprt));
			    											LoadActivity.updateActionbarMenu();

			    											progresdialog.dismiss();


			    										}

			    									}, new Response.ErrorListener() {

			    										@Override
			    										public void onErrorResponse(
			    												VolleyError error) {
			    											Toast.makeText(getActivity(), "False",
			    													Toast.LENGTH_LONG).show();  
			    											progresdialog.dismiss();
			    										}
			    									});

			    							AppController.getInstance().addToRequestQueue(
			    									jsObjRequest);
			    						} else { 

			    							mDetailsViewpagerFragment.setClickList(position);
			    							FragmentManager fragment = getFragmentManager();

			    							fragment.beginTransaction()
			    									.setCustomAnimations(R.anim.slide_in_up,
			    											R.anim.slide_out_up)
			    									.replace(R.id.frame_container,
			    											mDetailsViewpagerFragment).commit();

			    							LoadActivity.CURRENTFRAGMENT = EnumModuleTags.SinglePlaceActivity;
			    							ModuleFragmentBackStackingClass.AddtoStack(
			    									mDetailsViewpagerFragment,
			    									EnumModuleTags.SinglePlaceActivity,
			    									getString(R.string.mainfrgment_rprt));
			    						}

			    					} catch (Exception e) {
			    						e.printStackTrace();        	
			    			            PostLogcatErrors ple = new PostLogcatErrors();
			    						ple.PostLogcatErorrs(e);
			    					}

			    				

			                    } catch (NotFoundException e) {
			                        e.printStackTrace();
			                    }
			                }

			                private JSONObject getSaveViewUserParams() {

								JSONObject params = new JSONObject();

								HashMap<String, String> UserId = session.getUserID(); 
								String UserIdVal = UserId.get(SessionManager.KEY_UserID);
								
								

								if (!(UserIdVal == null)) {
									UserIdVal = UserId.get(SessionManager.KEY_UserID);
								} else {
									UserIdVal = "0";
								}

								try {
									params.put("UserId", UserIdVal);
									params.put("OpportunityID", OpportunityID);
									params.put("UserViewedID", 0);
									params.put("Shortlisted", true);
									params.put("KeyValue",  null);
								} catch (JSONException e) {
									e.printStackTrace();        	
						            PostLogcatErrors ple = new PostLogcatErrors();
									ple.PostLogcatErorrs(e);
								}

								return params;
							}

							@Override
			                public void onItemLongClick(View view, int position) {
								
								InputMethodManager imm;
								imm = (InputMethodManager) getActivity()
										.getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(swipelisview.getWindowToken(), 0);

								try {
									ImageView button;
									button = (ImageView) view
											.findViewById(R.id.fav_checkbox);

									String tag = button.getTag().toString();
									if (tag.equals(getString(R.string.grey_favcolor))) {
										sharedPreference.addFavorite(activity,
												GetAllGooglePlaces.ListItemsData.get(position));
										button.setTag(getString(R.string.red_favcolor));
										button.setImageResource(R.drawable.checked);
									} else if (tag.equals(getString(R.string.red_favcolor))){
										sharedPreference.removeFavorite(activity,
												GetAllGooglePlaces.ListItemsData.get(position));
										button.setTag(getString(R.string.grey_favcolor));
										button.setImageResource(R.drawable.unchecked);
									}
									
								} catch (NotFoundException e) {
									e.printStackTrace();        	
						            PostLogcatErrors ple = new PostLogcatErrors();
						            ple.PostLogcatErorrs(e);
									Toast.makeText(getActivity(), e.getMessage(),
											Toast.LENGTH_LONG).show();
									e.printStackTrace();
								}
								
							}
			            }));*/

			//swipelisview.setFriction(ViewConfiguration.getScrollFriction());

			//swipelisview.setSmoothScrollbarEnabled(true);
			//swipelisview.setFastScrollEnabled(true);
			
			//getListView().setFastScrollEnabled(true);

			/*
			 * loadmore = new Button(getActivity());
			 * 
			 * loadmore.setText(getString(R.string.userlist_loadmore));
			 * 
			 * swipelisview.addFooterView(loadmore);
			 * 
			 * loadmore.setOnClickListener(new View.OnClickListener() {
			 * 
			 * @Override public void onClick(View arg0) {
			 * 
			 * PageNo++; DisPlayOppList();
			 * 
			 * } }); 
			 */

			footerlayout = (LinearLayout) rootView.findViewById(R.id.footerlayout);

			footerlayout.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					swipelisview.requestDisallowInterceptTouchEvent(true);
					return true;
				}
			});

			ImageView imgfilter = (ImageView) rootView.findViewById(R.id.img_filter);

			imgfilter.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					try {
						
						 final Dialog dialog = new Dialog(getActivity());
						 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			                // Include dialog.xml file
			                dialog.setContentView(R.layout.single_choice_listview);
			                
			                // Set dialog title
			               // dialog.setTitle("Filter Options");
			 
			                final ListView listView = (ListView) dialog
									.findViewById(R.id.listview); 
									
									TextView optiontv = (TextView) dialog.findViewById(R.id.optiontv);

						optiontv.setText("Filter Options");
						

							ImageView sort = (ImageView) dialog.findViewById(R.id.right);

							ImageView cancel = (ImageView) dialog
									.findViewById(R.id.cancel);


							HashMap<String, String> user = session.getSortby();
							String mSelectedVariation = user
									.get(SessionManager.KEY_Sortby);

							if (mSelectedVariation == null) {
								mSelectedVariation = "0";
							}

							String[] FilterbySetting = new String[LoadActivity.SlideMenuListItems.size()];
							int i =0;
							for (CategoryListModel product : LoadActivity.SlideMenuListItems) {
								//multipleCat = product.getCategory() + "," + multipleCat;
								FilterbySetting[i] = product.getCategory();
								i++;
							}

							SingleChoiceAdapter adapter = new SingleChoiceAdapter(
									getActivity(), FilterbySetting, mSelectedVariation);

							listView.setAdapter(adapter);

							Window window = getActivity().getWindow();
							window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
							WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);


							sort.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {

									if (categoryids.size() == 0) {
										List<OpportunityTable> alloffers = repoObject.roffertable
												.getAlloffers(Integer.parseInt(Radiosname));
										getalloffersbyfiltering(alloffers);
									} else {
										List<OpportunityTable> alloffers = repoObject.roffertable
												.getAlloffersByFilter(categoryids,Radiosname);
										getalloffersbyfiltering(alloffers);
									}

									dialog.cancel();

								}

								private void getalloffersbyfiltering(
										List<OpportunityTable> alloffers) {
									GetAllGooglePlaces.ListItemsData = new CopyOnWriteArrayList<>();

									if (alloffers != null) {
										GPlaces.FillArrayListOffline(alloffers);
									}
								}

							});

							cancel.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {
									dialog.cancel();
								} 
							});
			        		
			 
			                dialog.show();

					} catch (Exception e) {
						e.printStackTrace();        	
			            PostLogcatErrors ple = new PostLogcatErrors();
						ple.PostLogcatErorrs(e);
					}
				}
			});

			ImageView imgSort = (ImageView) rootView.findViewById(R.id.img_sort);

			imgSort.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					try {
						
						 final Dialog dialog = new Dialog(getActivity());
						//dialog.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
						 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			                // Include dialog.xml file
			                dialog.setContentView(R.layout.single_choice_listview);
			                // Set dialog title
			               // dialog.setTitle("Sort Options");
			                
			                //dialog.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.sort_custom_title);
			 
			                final ListView listView = (ListView) dialog
									.findViewById(R.id.listview);
			                TextView taxation = (TextView) dialog.findViewById(R.id.optiontv);
							
							taxation.setText("Sort Options");

							ImageView sort = (ImageView) dialog.findViewById(R.id.right);

							ImageView cancel = (ImageView) dialog
									.findViewById(R.id.cancel);


							HashMap<String, String> user = session.getSortby();
							String mSelectedVariation = user
									.get(SessionManager.KEY_Sortby);

							if (mSelectedVariation == null) {
								mSelectedVariation = "0";
							}

							SortListAdapter adapter = new SortListAdapter(
									getActivity(), Sortby, mSelectedVariation);

							listView.setAdapter(adapter);

							Window window = getActivity().getWindow();
							window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
							WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);


							sort.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {
									if (SortListAdapter.mSelectedVariation == 0) {
										List<OpportunityTable> allOfferByDate = repoObject.roffertable
												.getAlloffersByDatesort(categoryids , Radiosname);
										galleries(allOfferByDate);
									} else if (SortListAdapter.mSelectedVariation == 1) {
										List<OpportunityTable> allOffersByTime = repoObject.roffertable
												.getAlloffersByTimesort(categoryids , Radiosname);
										galleries(allOffersByTime);
									} else if (SortListAdapter.mSelectedVariation == 2) {
										List<OpportunityTable> allOffersByDis = repoObject.roffertable
												.getAlloffersBydistancesort(categoryids , Radiosname);
										galleries(allOffersByDis);
									}

									dialog.cancel();

								}

								private void galleries(
										List<OpportunityTable> allOffersBySort) {

									GetAllGooglePlaces.ListItemsData = new CopyOnWriteArrayList<>();
									if (allOffersBySort != null) {
										GPlaces.FillArrayListOffline(allOffersBySort);
									}
								}
							});

							cancel.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {
									dialog.cancel();
								}

							});
							
							dialog.show();
						} catch (Exception e) {
							e.printStackTrace();        	
			                PostLogcatErrors ple = new PostLogcatErrors();
						ple.PostLogcatErorrs(e);
					}
				}
			});

			ImageView imgSearch = (ImageView) rootView.findViewById(R.id.img_search);

			imgSearch.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					try {
						
						
						searchdialog = new Dialog(getActivity());
			                // Include dialog.xml file
						searchdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						searchdialog.setContentView(R.layout.search_dialog);
			                // Set dialog title
			                //dialog.setTitle("Search Keywords");
			 
			              
			                autoCompView = (AutoCompleteTextView) searchdialog.findViewById(R.id.autoCompleteTextView);
			                
			                autoCompView.addTextChangedListener(new TextWatcher() {
					        	 
					        	   public void afterTextChanged(Editable s) {
					        	   }
					        	 
					        	   public void beforeTextChanged(CharSequence s, int start, 
					        	     int count, int after) {
					        	   }
					        	 
					        	   public void onTextChanged(CharSequence s, int start, 
					        	     int before, int count) {
					        	   
					        		   LoadSearchPlaces(s.toString());
					        	   }
					        	  });
			                
			                
			                final Button clearbtn = (Button) searchdialog.findViewById(R.id.clearbtn);
					        
					        clearbtn.setOnClickListener(new View.OnClickListener() {  

								@Override
								public void onClick(View arg0) {
									try {
										
										InputMethodManager imm = (InputMethodManager) getActivity()
												.getSystemService(Context.INPUT_METHOD_SERVICE);
										imm.hideSoftInputFromWindow(clearbtn.getWindowToken(), 0);
										
										    autoCompView.setText("");
									        multipleCat="";
										GPlaces.GetOpportunityList(SearchText, Radiosname, getActivity());


										DisPlayOppList();
										searchdialog.cancel(); 
										
									} catch (Exception e) {
										e.printStackTrace();        	
						                PostLogcatErrors ple = new PostLogcatErrors();
										ple.PostLogcatErorrs(e);
									}
								}
							});

							ImageView cancel = (ImageView) searchdialog
									.findViewById(R.id.cancel); 

							cancel.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {

									searchdialog.cancel();
								}
							});

							searchdialog.show();
			        		
			        		
					

					} catch (Exception e) {
						e.printStackTrace();        	
			            PostLogcatErrors ple = new PostLogcatErrors();
						ple.PostLogcatErorrs(e);
					}
				}
			});

			ImageButton imgmap = (ImageButton) rootView.findViewById(R.id.img_map);

			imgmap.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					try {

						Intent i = new Intent(getActivity(),
								PlacesMapActivity.class);
						i.putExtra(getString(R.string.User_Lat), Double
								.toString(SplashScreen.nwLocation
										.getLatitude()));
						i.putExtra(getString(R.string.User_long), Double
								.toString(SplashScreen.nwLocation
										.getLongitude()));
						i.putExtra(getString(R.string.User_long), Double
								.toString(SplashScreen.nwLocation
										.getLongitude()));
						PlacesMapActivity
								.getNearPlaces(GetAllGooglePlaces.ListItemsData);
						startActivity(i);

					} catch (Exception e) {
						e.printStackTrace();        	
			            PostLogcatErrors ple = new PostLogcatErrors();
						ple.PostLogcatErorrs(e);
					}
				}
			});

			swipelisview.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View v,
						final int position, long id) {
					
					InputMethodManager imm = (InputMethodManager) getActivity()
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(swipelisview.getWindowToken(), 0);

					try {
						final DetailsViewpagerFragment mDetailsViewpagerFragment = new DetailsViewpagerFragment();
						String url;
						if (LoadActivity.isOnline) {

							progresdialog = new ProgressDialog(getActivity());
							progresdialog.setMessage(Html
									.fromHtml("<b>Search</b><br/>Loading Details..."));
							progresdialog.setIndeterminate(false);
							progresdialog.setCancelable(false);
							progresdialog.show();

							ListDetails product = GetAllGooglePlaces.ListItemsData
									.get(position);
							OpportunityID = product.getOfferID();


							url = LoadActivity.BaseUri + "SaveUserViewedOpportunities";

							JsonObjectRequest jsObjRequest;
							jsObjRequest = new JsonObjectRequest(
									Request.Method.POST, url,
									getSaveViewUserParams(),
									new Response.Listener<JSONObject>() {

										@Override
										public void onResponse(JSONObject response) {
											
											pagename = "Userplaceslist";

											mDetailsViewpagerFragment
													.setClickList(position);
											FragmentManager fragment = getFragmentManager();

											fragment.beginTransaction()
													.setCustomAnimations(
															R.anim.slide_in_up,
															R.anim.slide_out_up)
													.replace(R.id.frame_container,
															mDetailsViewpagerFragment)
													.commit();

											LoadActivity.CURRENTFRAGMENT = EnumModuleTags.SinglePlaceActivity;



											ModuleFragmentBackStackingClass
													.AddtoStack(
															mDetailsViewpagerFragment,
															EnumModuleTags.SinglePlaceActivity,
															getString(R.string.mainfrgment_rprt));
											LoadActivity.updateActionbarMenu();

											progresdialog.dismiss();


										}

									}, new Response.ErrorListener() {

										@Override
										public void onErrorResponse(
												VolleyError error) {
											Toast.makeText(getActivity(), "False",
													Toast.LENGTH_LONG).show();  
											progresdialog.dismiss();
										}
									});

							AppController.getInstance().addToRequestQueue(
									jsObjRequest);
						} else { 

							mDetailsViewpagerFragment.setClickList(position);
							FragmentManager fragment = getFragmentManager();

							fragment.beginTransaction()
									.setCustomAnimations(R.anim.slide_in_up,
											R.anim.slide_out_up)
									.replace(R.id.frame_container,
											mDetailsViewpagerFragment).commit();

							LoadActivity.CURRENTFRAGMENT = EnumModuleTags.SinglePlaceActivity;
							ModuleFragmentBackStackingClass.AddtoStack(
									mDetailsViewpagerFragment,
									EnumModuleTags.SinglePlaceActivity,
									getString(R.string.mainfrgment_rprt));
						}

					} catch (Exception e) {
						e.printStackTrace();        	
			            PostLogcatErrors ple = new PostLogcatErrors();
						ple.PostLogcatErorrs(e);
					} 

				}

				private JSONObject getSaveViewUserParams() {

					JSONObject params = new JSONObject();

					HashMap<String, String> UserId = session.getUserID(); 
					String UserIdVal = UserId.get(SessionManager.KEY_UserID);
					
					

					if (!(UserIdVal == null)) {
						UserIdVal = UserId.get(SessionManager.KEY_UserID);
					} else {
						UserIdVal = "0";
					}

					try {
						params.put("UserId", UserIdVal);
						params.put("OpportunityID", OpportunityID);
						params.put("UserViewedID", 0);
						params.put("Shortlisted", true);
						params.put("KeyValue",  null); 
					} catch (JSONException e) {
						e.printStackTrace();        	
			            PostLogcatErrors ple = new PostLogcatErrors();
						ple.PostLogcatErorrs(e);
					}

					return params;
				}

			});

			// Click The FavoritesItem on LongPress
			swipelisview.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view,
						int position, long id) {
					
					InputMethodManager imm;
					imm = (InputMethodManager) getActivity()
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(swipelisview.getWindowToken(), 0);

					try {
						ImageView button;
						button = (ImageView) view
								.findViewById(R.id.fav_checkbox);

						String tag = button.getTag().toString();
						if (tag.equals(getString(R.string.grey_favcolor))) {
							sharedPreference.addFavorite(activity,
									GetAllGooglePlaces.ListItemsData.get(position));
							button.setTag(getString(R.string.red_favcolor));
							button.setImageResource(R.drawable.checked);
						} else if (tag.equals(getString(R.string.red_favcolor))){
							sharedPreference.removeFavorite(activity,
									GetAllGooglePlaces.ListItemsData.get(position));
							button.setTag(getString(R.string.grey_favcolor));
							button.setImageResource(R.drawable.unchecked);
						}

						return true;
					} catch (NotFoundException e) {
						e.printStackTrace();        	
			            PostLogcatErrors ple = new PostLogcatErrors();
			            ple.PostLogcatErorrs(e);
						Toast.makeText(getActivity(), e.getMessage(),
								Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
					return true;
				}

			});

			FloatingActionButton fab = (FloatingActionButton) rootView
					.findViewById(R.id.fab);

			/*
			 * if (LoggedIn.equals(getString(R.string.main_RETAILER))) {
			 * 
			 * fab.setVisibility(View.VISIBLE); } else {
			 * fab.setVisibility(View.GONE); }
			 */

			fab.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					try {

						Retailers_Posting_Details mRetailers_Posting_Page = new Retailers_Posting_Details();
						FragmentManager mfragmentmag = getFragmentManager();
						mfragmentmag
								.beginTransaction()
								.setCustomAnimations(R.anim.slide_in_up,
										R.anim.slide_out_up)
								.replace(R.id.frame_container,
										mRetailers_Posting_Page).commit();
						LoadActivity.CURRENTFRAGMENT = EnumModuleTags.Retailers_Posting_Details;
						ModuleFragmentBackStackingClass.AddtoStack(
								mRetailers_Posting_Page,
								EnumModuleTags.Retailers_Posting_Details,
								getString(R.string.mainfrgment_rprt));

					} catch (Exception e) {
						e.printStackTrace();        	
			            PostLogcatErrors ple = new PostLogcatErrors();
						ple.PostLogcatErorrs(e);
					}
				}
			});

			fab.attachToListView(swipelisview, new ScrollDirectionListener() {
				@Override
				public void onScrollDown() {
					Log.d(getString(R.string.user_listviewfragment),
							getString(R.string.scroll_down));

					footerlayout.setVisibility(View.VISIBLE);

					/*
					 * if (footerlayout.getVisibility() == View.GONE) {
					 * ShowView(footerlayout);
					 * 
					 * }
					 */

				}

				@Override
				public void onScrollUp() {
					try {
						Log.d(getString(R.string.user_listviewfragment),
								getString(R.string.scroll_up));

						footerlayout.setVisibility(View.GONE);

						/*
						 * if (footerlayout.getVisibility() == View.VISIBLE) {
						 * hideView(footerlayout);
						 * 
						 * }
						 */

					} catch (Exception e) {
						e.printStackTrace();        	
			            PostLogcatErrors ple = new PostLogcatErrors();
						ple.PostLogcatErorrs(e);
					}
				}
			});

			return rootView;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rootView;
	}
	
	
	TextWatcher watch = new TextWatcher(){

		@Override
		public void afterTextChanged(Editable arg0) {

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
		int arg3) {

		}

		@Override
		public void onTextChanged(CharSequence s, int a, int b, int c) {

		if(c == 0){
		clearbtn.setVisibility(View.GONE);
		}
		else {
		clearbtn.setVisibility(View.VISIBLE);
		}
		}};
	

	protected void LoadSearchPlaces(String searchparam) {
		try {
			if (LoadActivity.isOnline) {
				
				JsonArrayRequest movieReq = new JsonArrayRequest("http://192.168.2.10/RPRT.WebApi/api/RPRT/" + "SearchPlaces/"+searchparam,
						new Response.Listener<JSONArray>() {
								@Override
								public void onResponse(JSONArray response) { 
									List<HashMap<String, String>> aList = new ArrayList<>();
									for (int i = 0; i < response.length(); i++) {
										JSONObject json_data;
										try {
											json_data = response.getJSONObject(i);
											HashMap<String, String> hm = new HashMap<>();
											hm.put("Address", json_data.getString("Address"));
											hm.put("City", json_data.getString("City"));
											hm.put("Latitude", json_data.getString("Latitude"));
											hm.put("Longitude", json_data.getString("Longitude"));
											aList.add(hm);
										} catch (JSONException e) {
											e.printStackTrace(); 
										}
										
									}
									String[] from = { "Address"} ;
									int[] to = { R.id.tv };
									SimpleAdapter adapterId = new SimpleAdapter(getActivity(), aList,
											R.layout.list_placeitem, from, to);

									//final AutoCompleteTextView autoCompView = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextView);

									autoCompView.setAdapter(adapterId);

									autoCompView.setThreshold(1);

									// Pname.setThreshold(1);

									autoCompView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

										@SuppressWarnings("unchecked")
										@Override
										public void onItemClick(AdapterView<?> parent, View view,
												int position, long id) {
											HashMap<String, String> hm = (HashMap<String, String>) parent
													.getAdapter().getItem(position);
											
											autoCompView.setText(hm.get("Address"));
							 				SplashScreen.nwLocation.setLatitude(Double.parseDouble(hm.get("Latitude")));
											SplashScreen.nwLocation.setLongitude(Double.parseDouble(hm.get("Longitude")));
											
											 HashMap<String, String> Radious = session.getRadiousName();
								                final String Radiosname;
								                Radiosname = Radious.get(SessionManager.KEY_RadiousName);

										        multipleCat="";
											ProgressDialog progresdialog = new ProgressDialog(getActivity());
											progresdialog.setMessage(Html
													.fromHtml("<b>Search</b><br/>Loading Details..."));
											progresdialog.setIndeterminate(false);
											progresdialog.setCancelable(false);
											//progresdialog.show();
											
											GPlaces.GetOpportunityList(SearchText, Radiosname, getActivity());

											DisPlayOppList();
											
											searchdialog.cancel();

  
										}

									}); 
									

							}
						},
						new com.android.volley.Response.ErrorListener() {
							@Override
							public void onErrorResponse(
									VolleyError error) {

							}

						});

				AppController.getInstance().addToRequestQueue(movieReq);
				
				
			} 

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	


	private void DisPlayOppList() {
		try {

			pdialog = new ProgressDialog(getActivity());
			pdialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Details..."));
			pdialog.setIndeterminate(false);
			pdialog.setCancelable(false);


				if(LoadActivity.Status.equals("Slidemenulist"))  
				{
					int CategoryId = 0;
					switch (User_Places_ListView.multipleCat) {
						case "Books":
							CategoryId = 1;
							break;
						case "BeautyandFashion":
							CategoryId = 2; 
							break;
						case "Electronic":
							CategoryId = 3;
							break;
						case "Food":
							CategoryId = 4;
							break;
						case "HomeService":
							CategoryId = 5;
							break;
						case "Jobs":
							CategoryId = 6;
							break;
						case "RealEstate":
							CategoryId = 7;
							break;
						case "Vehicles":
							CategoryId = 8;
							break;
					}

					List<OpportunityTable> alloffers = repoObject.roffertable
							.getAllCatOpp(CategoryId , Radiosname);

					if (alloffers != null) {

						GPlaces.FillArrayListOffline(alloffers);
					}

				} else {
					List<OpportunityTable> alloffers = repoObject.roffertable
							.getAlloffers(Integer.parseInt(Radiosname));

					if (alloffers != null) {

						GPlaces.FillArrayListOffline(alloffers);
					}
				}
		}

		catch (Exception e) {
			e.printStackTrace();        	
            PostLogcatErrors ple = new PostLogcatErrors();
            ple.PostLogcatErorrs(e);
		}
	}


	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

		multipleCat = "";
		DisPlayOppList(); 

	}
	
	/*@Override
	   public void onDestroyView() {
		 
		    MapFragment f = (MapFragment) getFragmentManager()
		                                         .findFragmentById(R.id.map);  
		    if (ProductListAdapter.gmap != null) 
		        getFragmentManager().beginTransaction().remove(f).commit();
	
       super.onDestroyView();
       }*/
	
/*@Override
	public void onDestroyView() { 
	if (ProductListAdapter.gmap != null) {
	        getActivity().getFragmentManager().beginTransaction()
	            .remove(getActivity().getFragmentManager().findFragmentById(R.id.map)).commit();
	        ProductListAdapter.gmap=null;
	    }
	super.onDestroyView();
	}*/
}