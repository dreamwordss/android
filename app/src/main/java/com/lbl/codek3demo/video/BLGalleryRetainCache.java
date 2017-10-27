package com.lbl.codek3demo.video;

public class BLGalleryRetainCache {
	private static BLGalleryRetainCache sSingleton;
	public BLGalleryCache mRetainedCache;

	public static BLGalleryRetainCache getOrCreateRetainableCache() {
		if (sSingleton == null) {
			sSingleton = new BLGalleryRetainCache();
		}
		return sSingleton;
	}

}
