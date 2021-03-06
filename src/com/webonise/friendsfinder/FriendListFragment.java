package com.webonise.friendsfinder;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.facebook.Response;
import com.webonise.friendsfinder.adapter.FriendListAdapter;
import com.webonise.friendsfinder.model.FriendModel;
import com.webonise.friendsfinder.parser.JsonParser;
import com.webonise.friendsfinder.webservice.FacebookService;

@SuppressLint({ "ValidFragment", "ResourceAsColor" })
public class FriendListFragment extends FragmentActivity implements
		OnClickListener {

	private static final String TAG = GetField.class.getName();
	String stringArrayFriends[];
	List<FriendModel> friendsList;
	ListView listView;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		final Intent intent = new Intent(this, MapFragment.class);
		setContentView(R.layout.friend_list_fragment);
		initializeComponents();
		FacebookService mService = FacebookService.getIntance();
		listView = (ListView) findViewById(R.id.frinds_list);
		mService.getFriendsList(this);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				FriendModel friend = new FriendModel();
				friend = friendsList.get(position);
				Log.v("clicked", friend.getName());
				Log.v("clicked", friend.getLocation());
				Log.v("clicked", friend.getState());
				Log.v("clicked", "" + friend.getLatitude());
				Log.v("clicked", "" + friend.getLongitude());
				intent.putExtra("name", friend.getName());
				intent.putExtra("longitude", friend.getLongitude());
				intent.putExtra("latitude", friend.getLatitude());
				intent.putExtra("location", friend.getLocation());
				intent.putExtra("imageUrl", friend.getImageUrl());
				startActivity(intent);
			}
		});
		Log.v(TAG, "__friends__");
	}

	@SuppressLint("ResourceAsColor")
	private void initializeComponents() {
		Button buttonFriends = (Button) findViewById(R.id.button_friends);
		Button buttonMap = (Button) findViewById(R.id.button_map);
		buttonFriends.setClickable(false);
		buttonMap.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Log.v(null, "friends clicked");
		Intent intent = new Intent(this, MapFragment.class);
		startActivity(intent);
	}

	public void onFriendsListResult(Response response) {
		Response mResponse = response;
		friendsList = new ArrayList<FriendModel>();
		JsonParser jsonParser = new JsonParser();
		Log.v("list", response.toString());
		friendsList = new ArrayList<FriendModel>();
		friendsList = jsonParser.parseJsonObject(mResponse);
		callForAdapter();
	}

	private void callForAdapter() {
		FriendListAdapter adapter = new FriendListAdapter(this, friendsList);
		listView.setAdapter(adapter);
	}
}
