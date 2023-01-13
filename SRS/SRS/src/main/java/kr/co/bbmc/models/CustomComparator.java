package kr.co.bbmc.models;

import java.io.File;
import java.util.Comparator;

import kr.co.bbmc.models.fnd.Site;
import kr.co.bbmc.utils.Util;
import kr.co.bbmc.viewmodels.DropDownListItem;
import kr.co.bbmc.viewmodels.fnd.TreeViewItem;

public class CustomComparator {
	public static Comparator<TreeViewItem> TreeViewItemSiblingSeqComparator = 
			new Comparator<TreeViewItem>() {
    	public int compare(TreeViewItem item1, TreeViewItem item2) {
    		return item1.getSiblingSeq().compareTo(item2.getSiblingSeq());
    	}
    };

	public static Comparator<Site> SiteSiteNameComparator = 
			new Comparator<Site>() {
    	public int compare(Site item1, Site item2) {
    		return item1.getSiteName().toUpperCase()
    				.compareTo(item2.getSiteName().toUpperCase());
    	}
    };

	public static Comparator<DropDownListItem> DropDownListItemTextComparator = 
			new Comparator<DropDownListItem>() {
    	public int compare(DropDownListItem item1, DropDownListItem item2) {
    		return item1.getText().toUpperCase()
    				.compareTo(item2.getText().toUpperCase());
    	}
    };

	public static Comparator<DropDownListItem> DropDownListItemTextVerComparator = 
			new Comparator<DropDownListItem>() {
    	public int compare(DropDownListItem item1, DropDownListItem item2) {
    		int idx1 = item1.getText().indexOf(" <ver");
    		int idx2 = item2.getText().indexOf(" <ver");
    		
    		if (idx1 > -1 && idx2 > -1) {
    			String name1 = item1.getText().substring(0, idx1);
    			String name2 = item2.getText().substring(0, idx2);
    			
				String tmp1 = item1.getText().substring(idx1 + 6);
				int ver1 = Util.parseInt(tmp1.replace(">", ""), 0);
    			
				String tmp2 = item2.getText().substring(idx2 + 6);
				int ver2 = Util.parseInt(tmp2.replace(">", ""), 0);

				if (name1.equals(name2)) {
					return Integer.compare(ver2, ver1);
				} else {
					return name2.toUpperCase().compareTo(name1.toUpperCase());
				}
    		} else {
        		return item2.getText().toUpperCase()
        				.compareTo(item1.getText().toUpperCase());
    		}
    	}
    };
    
    public static Comparator<File> FileAdTrackFileComparator =
    		new Comparator<File>() {
    	public int compare(File item1, File item2) {
    		if (item1.getName().indexOf("_track_") > -1 && item2.getName().indexOf("_track_") > -1 &&
    			item1.getName().endsWith(".xml") && item2.getName().endsWith(".xml") &&
    			item1.getName().length() > 12 && item2.getName().length() > 12) {
    			String dateStr1 = item1.getName().substring(item1.getName().length() - 12, item1.getName().length() - 4);
    			String dateStr2 = item2.getName().substring(item2.getName().length() - 12, item2.getName().length() - 4);
    			
    			if (dateStr1.equals(dateStr2)) {
    				return Long.valueOf(item1.lastModified()).compareTo(Long.valueOf(item2.lastModified()));
    			} else {
    				return dateStr1.compareTo(dateStr2);
    			}
    		}
    		
    		return item1.getName().compareToIgnoreCase(item2.getName());
    	}
    };
    
    public static Comparator<DropDownListItem> DropDownListItemAssetDisplayOrderPolicyComparator =
    		new Comparator<DropDownListItem>() {
    	public int compare(DropDownListItem item1, DropDownListItem item2) {
    		if (Util.isNotValid(item1.getValue()) || Util.isNotValid(item2.getValue()) ||
    				item1.getValue().length() < 3 || item2.getValue().length() < 3) {
        		return item1.getText().toUpperCase()
        				.compareTo(item2.getText().toUpperCase());
    		}
    		
    		String deviceType1 = item1.getValue().substring(0, 1);
    		String deviceType2 = item2.getValue().substring(0, 1);
    		
    		if (deviceType1.equals(deviceType2)) {
    			String deviceCode1 = item1.getValue().substring(2);
    			String deviceCode2 = item2.getValue().substring(2);
    			
    			return deviceCode1.compareTo(deviceCode2);
    		} else {
        		return deviceType2.compareTo(deviceType1);
    		}
    	}
    };
    
	//
	// [CU] ext
	//
    public static Comparator<DropDownListItem> DropDownListItemAssetDisplayOrderCUPolicyComparator =
    		new Comparator<DropDownListItem>() {
    	public int compare(DropDownListItem item1, DropDownListItem item2) {
    		if (Util.isNotValid(item1.getValue()) || Util.isNotValid(item2.getValue()) ||
    				item1.getValue().length() < 3 || item2.getValue().length() < 3) {
        		return item1.getText().toUpperCase()
        				.compareTo(item2.getText().toUpperCase());
    		}
    		
    		String deviceType1 = item1.getValue().substring(0, 1);
    		String deviceType2 = item2.getValue().substring(0, 1);

    		if (deviceType1.equals(deviceType2) && deviceType1.equals("D")) {
    			String deviceCode1 = item1.getValue().substring(2);
    			String deviceCode2 = item2.getValue().substring(2);
    			
    			if (deviceCode1.length() > 3 && deviceCode2.length() > 3) {
    				String size1 = deviceCode1.substring(1, 3);
    				String size2 = deviceCode2.substring(1, 3);
    				if (!size1.equals(size2)) {
    					return size2.compareTo(size1);
    				} else {
    					return deviceCode2.substring(0, 1).compareTo(
    							deviceCode1.substring(0, 1));
    				}
    			}
    			
    			return deviceCode1.compareTo(deviceCode2);
    		} else {
        		return deviceType2.compareTo(deviceType1);
    		}
    	}
    };
    //-
}
