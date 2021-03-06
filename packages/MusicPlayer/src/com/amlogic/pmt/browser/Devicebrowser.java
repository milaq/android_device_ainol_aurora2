package com.amlogic.pmt.browser;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.geometerplus.zlibrary.ui.android.R;

import com.amlogic.pmt.GLGridLayout;
import com.amlogic.pmt.MiscUtil;
import com.amlogic.pmt.Resolution;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
//import android.os.SystemProperties;//gyx comment
import android.os.Build;

//import android.os.storage.StorageManager;//gyx comment
import android.util.Log;

public class Devicebrowser implements GridBrowser, OnUnFolderBrowserListener {
	private String Filetype = "Picture";// Audio Text Video Picture
	private List<FilebrowserItemData> DeviceName 		  = new ArrayList<FilebrowserItemData>();
	// private boolean IfHaveFolder = true;
	Bitmap titleBitmap 									  = null;
	Bitmap infoTitleBitmap								  = null;
	Bitmap selectedBitmap								  = null;
	Bitmap preiconBitmap                                  = null;
	Bitmap prefolderBitmap                             	  = null;
	Bitmap itmSataIconBitmap                              = null;
	Bitmap itmUsbIconBitmap                               = null;
	Bitmap itmSdcardIconBitmap                            = null;
	private Context mContext							  = null;
	private GLGridLayout gridlayout 					  = null;
	
	private List<FilebrowserItemData> listUnfolderPreInfo = null;
	private int idxUnfolderPreInfo 						  = -1;
	public Devicebrowser(Context cnt, GLGridLayout gridl) {
		mContext = cnt;
		gridlayout = gridl;
	}

	public Bitmap getTitleBitmap() {
		return titleBitmap;
	}

	public Bitmap getInfoTitleBitmap() {
		return infoTitleBitmap;
	}

	public Bitmap getSelectedBitmap() {
		return selectedBitmap;
	}

	public void SetFileType(String filetype) {
		Filetype = filetype;
		if (Filetype.equals("Picture")) {
			titleBitmap = BitmapFactory.decodeResource(mContext.getResources(),
					R.drawable.title_picture);
			infoTitleBitmap = BitmapFactory.decodeResource(mContext
					.getResources(), R.drawable.preview_title_picture);
			preiconBitmap = BitmapFactory.decodeResource(mContext.getResources(), 
					R.drawable.icon_pre_picture);
		} else if (Filetype.equals("Audio")) {
			titleBitmap = BitmapFactory.decodeResource(mContext.getResources(),
					R.drawable.title_music);
			infoTitleBitmap = BitmapFactory.decodeResource(mContext
					.getResources(), R.drawable.preview_title_music);
			preiconBitmap = BitmapFactory.decodeResource(mContext.getResources(),
					R.drawable.icon_pre_music);
		} else if (Filetype.equals("Text")) {
			titleBitmap = BitmapFactory.decodeResource(mContext.getResources(),
					R.drawable.title_txt);
			infoTitleBitmap = BitmapFactory.decodeResource(mContext
					.getResources(), R.drawable.preview_title_txt);
			preiconBitmap = BitmapFactory.decodeResource(mContext.getResources(), 
					R.drawable.icon_pre_txt);
		}
		selectedBitmap = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.list_disk_sel);
		prefolderBitmap = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.icon_pre_folder);
		itmSataIconBitmap = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.list_disk_unsel);
		itmUsbIconBitmap = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.list_usb_unsel);
		itmSdcardIconBitmap = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.list_sd_unsel);

	}
/*
	private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		// @Override
		public void onReceive(Context context, Intent intent) {
			Log.v("Device", intent.getAction());

			String ReceiveName = intent.getData().toString().substring(7);

			if (intent.getAction()
					.equals("android.intent.action.MEDIA_MOUNTED")) {
				if (DeviceName.size() != 0) {
					for (int i = 0; i < DeviceName.size(); i++) {
						if (DeviceName.get(i).getAbsoluteFilePath().indexOf(
								ReceiveName) != -1)
							return;
					}
				}
				File file = new File(ReceiveName);
				if (file != null)
					for (File myF : file.listFiles())
						DeviceName.add(new FilebrowserItemData(myF.getPath(),
								0, 0));

			} else if (intent.getAction().equals(
					"android.intent.action.MEDIA_REMOVED")
					|| intent.getAction().equals(
							"android.intent.action.MEDIA_UNMOUNTED")
					|| intent.getAction().equals(
							"android.intent.action.MEDIA_BAD_REMOVAL")) {
				if (DeviceName.size() != 0) {
					for (int i = 0; i < DeviceName.size(); i++) {
						if (DeviceName.get(i).getAbsoluteFilePath().indexOf(
								ReceiveName) != -1) {
							DeviceName.remove(i);

						}

					}
				}
			}
		}
	};
*/
	public void getDevice() {
		DeviceName.clear();
		File[] files = new File("/mnt").listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.getPath().equals("/mnt/sdcard")) {
					DeviceName.add(new FilebrowserItemData(file
							.getPath(), 0, 0));
				} else if (file.getPath().equals("/mnt/sata")) {
					DeviceName.add(new FilebrowserItemData(file
							.getPath(), 0, 0));
				} else if (file.getPath().startsWith("/mnt/sd")) {
					Log.d("DeviceBrowser", "***********"+file.getPath());
					String version = Build.VERSION.RELEASE;
					if(!version.startsWith("4.0."))
					{
					File[] myfiles = new File(file.getPath()).listFiles();
					if (myfiles != null) {
						for (File myfile : myfiles)
						{
							Log.d("DeviceBrowser", "***********"+myfile.getPath());
							if(myfile.canRead())
							{
								Log.d("DeviceBrowser", "***********"+myfile.getPath()+" can read");
								if(getDeviceName(myfile.getName() )!= null)
									DeviceName.add(new FilebrowserItemData(myfile
											.getPath(), 0, 0));
							}
							
							}		
						}
					}
					else
					{
						DeviceName.add(new FilebrowserItemData(file
							.getPath(), 0, 0));
					}
				}
			}
		}
		gridlayout.browseFinish();
	}

	public int getCount() {
		int count = DeviceName.size();
		Log.d("Devicebrowser", "getCount return "+count);
		return count;
	}
	
/*
	public Bitmap getItemBitmap(int idx) {
		if (idx >= DeviceName.size())
			return null;

		FilebrowserItemData itmd = DeviceName.get(idx);
		String devname = itmd.getFileName();

		Bitmap devBmp = Bitmap.createBitmap(300, 390, Bitmap.Config.ARGB_8888);
		Bitmap resBmp = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.list_disk_unsel);

		MiscUtil.drawBitmapIcon(devBmp, resBmp, 0, 20);
		MiscUtil.drawBitmapText(devBmp, devname, 150, 350, 40, Color.WHITE, Align.CENTER, 
				new Rect(30, 310, 270, 390));
		return devBmp;
	}
*/
	public String getItemAbsoluteName(int idx) {
		if (idx >= DeviceName.size())
			return null;

		FilebrowserItemData itmd = DeviceName.get(idx);
		String aname = new String(itmd.getAbsoluteFilePath());

		return aname;

	}

	// @Override
	public void EnterDir(int position) {
		// TODO Auto-generated method stub

	}

	// @Override
	public boolean IFIsFolder(int position) {
		// TODO Auto-generated method stub
		return false;
	}

	// @Override
	public boolean upOneLevel() {
		// TODO Auto-generated method stub
		return false;
	}

	public List<FilebrowserItemData> GetFileInfo(int idx) {
		List<FilebrowserItemData> lstInfo = null;
		String filename = getItemAbsoluteName(idx);
				
//		if (IFIsFolder(idx)) {
			if (Filebrowser.IfFolder == true) {
				FolderBrowser fdb = new FolderBrowser(mContext, Filetype, filename);
				fdb.SetFileType(Filetype);
				lstInfo = fdb.GetPreFileList(filename);
				if (lstInfo != null) {
				} else {
					lstInfo = new ArrayList<FilebrowserItemData>();
					lstInfo.add(new FilebrowserItemData( mContext.getResources().getString(R.string.tips), 0, 0));// name
					lstInfo.add(new FilebrowserItemData( mContext.getResources().getString(R.string.folder_empty), 0, 0));// date
				}
			} else {
				if(idxUnfolderPreInfo == idx && listUnfolderPreInfo != null){
					return listUnfolderPreInfo;
				}else{
					idxUnfolderPreInfo = idx;
					listUnfolderPreInfo = new ArrayList<FilebrowserItemData>();
					UnFolderBrowser browser = new UnFolderBrowser(mContext, filename); //
					browser.SetFileType(Filetype);
					browser.SetOnUnFolderBrowserListener(this);
					browser.StartLoadData(UnFolderBrowser.skywprebrowser);
				}
			}
/*		} else {
			File file = new File(filename);
			String LastModifiedDate = "";
			Date dt = new Date(file.lastModified());
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss aa");
			LastModifiedDate = sdf.format(dt);
			
			lstInfo = new ArrayList<FilebrowserItemData>();
			lstInfo.add(new FilebrowserItemData(file.getName(), 0, 0));// name
			lstInfo.add(new FilebrowserItemData(LastModifiedDate, 0, 0));// date

			String size = MiscUtil.GetFileSize(file);
			lstInfo.add(new FilebrowserItemData(size, 0, 0));
		}*/
		
		return lstInfo;
	}

	public void setInfoBitmap(int idx) {
		List<FilebrowserItemData> lstInfo = GetFileInfo(idx);
		if(lstInfo != null && lstInfo.size() > 0)
		{
			Bitmap devBmp = Bitmap.createBitmap(350, 440, Bitmap.Config.ARGB_8888);
			int fontsize = 26;
			Canvas canvas = new Canvas(devBmp);
			Paint paint = new Paint();
			paint.setColor(Color.WHITE);
			paint.setTextSize(fontsize);
			paint.setTextAlign(Align.LEFT);
			paint.setAntiAlias(true);	
			Paint paint1 = new Paint();    
			paint1.setARGB(0xff, 0xa0, 0xb2, 0xda);
			paint1.setTextSize(fontsize);
			paint1.setTextAlign(Align.LEFT);
			for(int i=0; i<lstInfo.size() && i<11; i++){
				String text = lstInfo.get(i).getFileName();
				if(text == null)
					break;
				
				int ResID = lstInfo.get(i).GetfocusID();
				if(ResID != 0)
				{
					text = MiscUtil.breakText(text, fontsize, 280);
					if(ResID == R.drawable.icon_browse_folder)
						canvas.drawBitmap(prefolderBitmap, 0, (1+i)*38 -23, null);
					else
						canvas.drawBitmap(preiconBitmap, 0, (1+i)*38 -23, null);
					canvas.drawText(text, 55, (1+i)*38, paint);
				}
				else
				{
					if(lstInfo.size() == 3)
					{
						String name = null ;
							if(i == 0)
								name = mContext.getString(R.string.filename);
							if(i == 1)
								name = mContext.getString(R.string.filetime);
							if(i == 2)
								name = mContext.getString(R.string.filesize);
							canvas.drawText(name, 0, (1+i)*40, paint1);
						text = MiscUtil.breakText(text, fontsize, 220);
						canvas.drawText(text, 120, (1+i)*40, paint);
					}
					else
						canvas.drawText(text, 10, (1+i)*40, paint);
					
				}
					
			}
			gridlayout.setInfoBitmap(devBmp);
		}else{
			gridlayout.setInfoBitmap(null);
		}
	}

	//@Override
	public ArrayList<String> getPlayList() {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public boolean MountDevice(String deviceName) {
		getDevice();
		//DeviceName.add(new FilebrowserItemData(deviceName, 0, 0));
		return true;
	}

	//@Override
	public boolean UnmountDevice(String deviceName) {
		boolean ret = false;
		Log.d("DeviceBrowser", ">>>>>>UnmountDevice "+deviceName);
		for(int i=DeviceName.size()-1; i>=0; i--){
			FilebrowserItemData itmd = DeviceName.get(i);
			if(itmd.getAbsoluteFilePath().startsWith(deviceName)){
				//Donot remove sdcard when deviceName=/mnt/sdc
				if (deviceName.startsWith("/mnt/sdc") && 
					!deviceName.equals("/mnt/sdcard") && 
					itmd.getAbsoluteFilePath().startsWith("/mnt/sdcard"))
					continue;
				Log.d("DeviceBrowser", ">>>>>>remove "+itmd.getAbsoluteFilePath());
				DeviceName.remove(i);
				ret = true;
			}
		}
		return ret;
	}

	//@Override
	public int findItem(String AbsoluteName) {
		for(int i=0; i<DeviceName.size(); i++){
			FilebrowserItemData itmd = DeviceName.get(i);
//			if(AbsoluteName.equals(itmd.getAbsoluteFilePath())){
			if(AbsoluteName.startsWith(itmd.getAbsoluteFilePath())){
				return i;
			}
		}
		return -1;
	}

	// @Override
	public void OnUnFolderBrowser(FilebrowserItemData... FID) {
		if (idxUnfolderPreInfo >= 0 && listUnfolderPreInfo != null) {
			if (FID != null) {
				for (FilebrowserItemData image : FID) {
					listUnfolderPreInfo.add(image);
				}
			} else {
				listUnfolderPreInfo.add(new FilebrowserItemData(mContext.getResources()
						.getString(R.string.tips), 0, 0));// name
				listUnfolderPreInfo.add(new FilebrowserItemData(mContext.getResources()
						.getString(R.string.folder_empty), 0, 0));// date
			}
		}
	}

	//@Override
	public void OnUnFolderBrowserFinish() {
		setInfoBitmap(idxUnfolderPreInfo);
	}

	//@Override
	public String getCurDirPath() {
		// TODO Auto-generated method stub
		return null;
	}

	public Bitmap getItemBkBitmap() {
		return null;
	}

	public Bitmap getItemIconBitmap(int idx) {
		String absName = getItemAbsoluteName(idx);
		if (absName == null)
			return null;
		
		if (absName.startsWith("/mnt/sdcard")) {
			return itmSdcardIconBitmap;
		} else if (absName.startsWith("/mnt/sata")) {
			return itmSataIconBitmap;
		}
		return itmUsbIconBitmap;
	}

	public Bitmap getItemNameBitmap(int idx) {
		if (idx >= DeviceName.size())
			return null;

		String devname = DeviceName.get(idx).getFileName();
		devname = getDeviceName(devname);
		char data = (char) ('A' + (char) idx) ;
		devname =  devname +'(' +data + ":)" ;
		
		Bitmap devBmp = Bitmap.createBitmap(240, 60, Bitmap.Config.ARGB_8888);
		String fname = MiscUtil.breakText(devname, 40, 240);
		MiscUtil.drawBitmapText(devBmp, fname, 120, 50, 40, Color.WHITE, Align.CENTER, null);
		
		return devBmp;
		
		
	}

	
	
	public Bitmap getItemScrollNameBitmap(int idx) {
		if (idx >= DeviceName.size())
			return null;
		String devname = DeviceName.get(idx).getFileName();
		devname = getDeviceName(devname);
	
		char data = (char) ('A' + (char) idx) ;
		devname =  devname +'(' +data + ":)" ;
	
		Bitmap devBmp = null;
		int widthPixel = (int)MiscUtil.getTextWidth(devname, 40);
		if(widthPixel > 1180)
			widthPixel = 1180;
		if(widthPixel <= 240){
			devBmp = Bitmap.createBitmap(240, 60, Bitmap.Config.ARGB_8888);
			MiscUtil.drawBitmapText(devBmp, devname, 120, 50, 40, Color.WHITE, Align.CENTER, null);
		}else{
			devBmp = Bitmap.createBitmap(widthPixel + 60, 60, Bitmap.Config.ARGB_8888);
			MiscUtil.drawBitmapText(devBmp, devname, 0, 50, 40, Color.WHITE, Align.LEFT, null);
		}
		return devBmp;
	}

	public int getNameWidthPixel() {
		return 240;
	}

//	@Override
	public void requestThumbImage(int pos, int total) {
		// TODO Auto-generated method stub
		
	}

//	@Override
	public void cancelThumbImage() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	private String getDeviceName(String devname)
	{
		if(devname == null  || devname.length() == 0) {
			return mContext.getResources().getString(R.string.volume); 
		} else {
			Log.d("DeviceBrowser", "devname "+devname);
			String  protectDevice=mContext.getResources().getString(R.string.protect_device); 
			if(!protectDevice.equals(devname)) {
				if (devname.equals("sdcard"))
					return mContext.getResources().getString(R.string.sdcard); 
				else if (devname.equals("sata"))
					return mContext.getResources().getString(R.string.sata); 
				else
					return mContext.getResources().getString(R.string.volume); 
			} else {
				return null;
			}
		}
	}
	
	class RequestInfoThread extends Thread {
		int index = -1;
		boolean mStop = false;

		RequestInfoThread(int idx) {
			index = idx;
		}

		public void run() {
			synchronized (this) {
				if (!mStop && index >= 0 && index < getCount()) {
					if(Filebrowser.IfFolder)
						setInfoBitmap(index);
				}
			}
		}

		public void reqStop() {
			FolderBrowser.reqStopPreList = true;
			mStop = true;
		}
		public void reqStart(){
			FolderBrowser.reqStopPreList = false;
			start();
		}
	}

	RequestInfoThread requestInfo = null;

	public void requestInfoImage(int idx) {
		if(Filebrowser.IfFolder){
			cancelInfoImage();
			requestInfo = new RequestInfoThread(idx);
			requestInfo.reqStart();
		}else{
			setInfoBitmap(idx);
		}
	}

	public void cancelInfoImage() {
		if(requestInfo != null){
			requestInfo.reqStop();
			synchronized (requestInfo) {
				requestInfo = null;
			}
		}
	}
	
	public Bitmap  getWarningBitmap()
	{
		Bitmap devBmp = Bitmap.createBitmap(702, 480, Bitmap.Config.ARGB_8888);
		int fontsize = 36;
		Canvas canvas = new Canvas(devBmp);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(fontsize);
		paint.setTextAlign(Align.LEFT);
		paint.setAntiAlias(true);	
		canvas.drawBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bg_info_content), 0, 0, null);
		canvas.drawText(mContext.getResources().getString(R.string.not_device), 280, 200, paint);
		
		return devBmp;
	}
	
	
}
