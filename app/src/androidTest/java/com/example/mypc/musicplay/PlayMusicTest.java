package com.example.mypc.musicplay;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore.Audio.Media;
import android.test.ActivityInstrumentationTestCase2;

public class PlayMusicTest extends ActivityInstrumentationTestCase2<PlayMusic> {
	PlayMusic activity;

	public PlayMusicTest() {
		super("MainActivity", PlayMusic.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
	}

	public void test() {
		String art = activity.musicManager.getMusicAlbumArt(getTestMusicId());
		assertNotNull(art);
		Bitmap bitmap = BitmapFactory.decodeFile(art);
		assertEquals(bitmap, "test");
	}

	private String getTestMusicId() {
		Cursor cur = activity.managedQuery(Media.EXTERNAL_CONTENT_URI, null, null, null, null);
		if (cur.moveToFirst()) {
			return cur.getString(cur.getColumnIndex(Media._ID));
		}
		return null;
	}
}