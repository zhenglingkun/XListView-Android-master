package me.maxwin;

import java.util.ArrayList;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class XListViewActivity extends Activity {
	
	private XListView mListView = null;
	private ArrayAdapter<String> mAdapter = null;
	private ArrayList<String> items = new ArrayList<String>();
	private Handler mHandler = null;
	private int start = 0;
	private static int refreshCnt = 0;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		geneItems(0);
		mListView = (XListView) findViewById(R.id.xListView);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				int position = arg2;
				Toast.makeText(getApplicationContext(), "position = " + position, Toast.LENGTH_SHORT).show();
			}
		});
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				final int position = arg2;
				new AlertDialog.Builder(XListViewActivity.this).setTitle("列表框").setItems(
					     new String[] { "删除" }, new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								items.remove(position - 1);
								mAdapter.notifyDataSetChanged();
							}
						}).show();
				Toast.makeText(getApplicationContext(), "position = " + position, Toast.LENGTH_SHORT).show();
				return false;
			}
		});
		mListView.setPullLoadEnable(true);
		mAdapter = new ArrayAdapter<String>(this, R.layout.list_item, items);
		mListView.setAdapter(mAdapter);
//		mListView.setPullLoadEnable(false);
//		mListView.setPullRefreshEnable(false);
		mListView.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						start = ++refreshCnt;
						items.clear();
						geneItems(5);
						// mAdapter.notifyDataSetChanged();
						mAdapter = new ArrayAdapter<String>(XListViewActivity.this, R.layout.list_item, items);
						mListView.setAdapter(mAdapter);
						onLoad();
					}
				}, 2000);
			}
			@Override
			public void onLoadMore() {
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						geneItems(5);
						mAdapter.notifyDataSetChanged();
						onLoad();
					}
				}, 2000);
			}
		});
		mHandler = new Handler();
	}

	private void geneItems(int count) {
		for (int i = 0; i != count; ++i) {
			items.add("refresh cnt " + (++start));
		}
	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
	}
	
}