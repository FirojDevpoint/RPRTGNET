package com.devpoint.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.RP.database.Repo;
import com.RPRT.database.model.ReserveTable;
import com.devpoint.adapter.ViewPagerAdapter;
import com.devpoint.common.CustomViewPager;
import com.devpoint.common.FavoriteListFragment;
import com.devpoint.common.GetAllGooglePlaces;
import com.devpoint.model.ListDetails;
import com.devpoint.retailer.Retailers_Login_Page;
import com.devpoint.rprtgnet.LoadActivity;
import com.devpoint.rprtgnet.R;
import com.devpoint.rprtgnet.SplashScreen;
import com.devpoint.sharedpreferences.SessionManager;

import java.util.HashMap;

@SuppressLint("CutPasteId")
public class DetailsViewpagerFragment extends Fragment {

    private static View rootView;
    public static int ClickListID;
    protected SessionManager session;
    public static String catname;
    public static String areaname;
    public static String Vendorid;
    public static int PositionVal;
    public static double to_lat;
    public static double to_lng;
    public static ViewPagerAdapter adapter;
    public static CustomViewPager viewPager;
    
    private Repo repoObject;

    public DetailsViewpagerFragment() {
    }

    public void setClickList(int ClickListID) {
        DetailsViewpagerFragment.ClickListID = ClickListID;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @SuppressWarnings("deprecation")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            try {
                rootView = inflater.inflate(R.layout.details_viewpager_fragment, container, false);
                viewPager = (CustomViewPager) rootView.findViewById(R.id.pager);
                if (User_Places_ListView.pagename.equals("FavouriteList")) {
                    adapter = new ViewPagerAdapter(getActivity(), FavoriteListFragment.favorites);
                } else if (User_Places_ListView.pagename.equals("Userplaceslist")) {
                    adapter = new ViewPagerAdapter(getActivity(), GetAllGooglePlaces.ListItemsData);
                } 
                
                if(Retailers_Login_Page.VendorID==null)
                {
                	Retailers_Login_Page.VendorID="";
                }
                
                
                ListDetails product = GetAllGooglePlaces.ListItemsData.get(ClickListID); 
                session = new SessionManager(getActivity());
                session.createPageID(product.getOfferID());
                session.createToDeviceId(product.getDeviceId());
                catname = product.getCategoryName();
                viewPager.setAdapter(adapter);
                viewPager.setCurrentItem(ClickListID); 
                
                Vendorid = String.valueOf(product.getPosition_id()); 
                session.createVendorId(Vendorid);
                
                
                //viewPager.setOffscreenPageLimit(0);

                viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {  

                    @Override
                    public void onPageSelected(int position) {
                        try {
                            PositionVal = position;
                            ListDetails product = GetAllGooglePlaces.ListItemsData.get(position);
                            to_lat = product.getLat();
                            to_lng = product.getLng();
                            areaname = product.getAreaName();
                        catname = product.getCategoryName();
                            session.createPageID(product.getOfferID());

                            Vendorid = String.valueOf(product.getPosition_id());
                            session.createVendorId(Vendorid);

                            SessionManager session = new SessionManager(getActivity());
                            HashMap<String, String> VendorID = session.getVendorId();
                            String VendorIDVal = VendorID.get(SessionManager.KEY_Vendorid);

                            if (LoadActivity.isOnline) {

                                if (Retailers_Login_Page.VendorID.equals(VendorIDVal)) {
                                    ViewPagerAdapter.edit.setVisibility(View.VISIBLE);
                                    ViewPagerAdapter.delete.setVisibility(View.VISIBLE);
                                } else {
                                    ViewPagerAdapter.edit.setVisibility(View.GONE);
                                    ViewPagerAdapter.delete.setVisibility(View.GONE);
                                }
                            }
                            
                            HashMap<String, String> PageID = session.getPageChangID();
                            String PageIDVal = PageID.get(SessionManager.KEY_PageID);
                            repoObject = SplashScreen.getRepo();
                            ReserveTable Reserved = repoObject.rReserveoffer
                					.getByOpportunityId(Integer.parseInt(PageIDVal)); 
                            
                            if(Reserved!=null)
                            {
                            
                            String Key = Reserved.getKeyValue();
                            
                            if(Key !=null)
                            {
                            	ViewPagerAdapter.btn_reserve.setVisibility(View.GONE);
                            	ViewPagerAdapter.ReserveKeyLayout.setVisibility(View.VISIBLE);
                            	ViewPagerAdapter.reservekey.setText(Key);
                            	
                            }
                            else
                            {
                            	ViewPagerAdapter.btn_reserve.setVisibility(View.VISIBLE);
                            	ViewPagerAdapter.ReserveKeyLayout.setVisibility(View.GONE);
                            } 
                            }
                            else
                            {
                            	ViewPagerAdapter.btn_reserve.setVisibility(View.VISIBLE);
                            	ViewPagerAdapter.ReserveKeyLayout.setVisibility(View.GONE);
                             }
                            
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int arg0) {
                    }
                });
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG)
                    .show();
            e.printStackTrace();
            PostLogcatErrors ple = new PostLogcatErrors();
            ple.PostLogcatErorrs(e);
        }
        return rootView;
    }
    
   /* @Override
	   public void onDestroyView() {
		 super.onDestroyView();
		    MapFragment f = (MapFragment) getFragmentManager()
		                                         .findFragmentById(R.id.map);
		    if (f != null) 
		        getFragmentManager().beginTransaction().remove(f).commit();
	
    super.onDestroyView();
    }*/
}
