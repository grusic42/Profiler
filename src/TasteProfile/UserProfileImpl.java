package TasteProfile;

import java.util.ArrayList;
import java.util.List;


public class UserProfileImpl extends UserProfile implements Comparable<UserProfile> {
		
		public UserProfileImpl() {
			id="";
			long total_play_count=0;
			songs= new Song[5000];
		}

		public String toString() {
			String str = "";
			str += id + " ";
			str += total_play_count + " ";
			StringBuilder sb = new StringBuilder();
			int i=0;
			while(songs[i] != null) {	
				sb.append(songs[i].id + " " + songs[i].play_count + " ");
				i++;
			}
			str += i+1 + " "; 
			str += sb.toString(); 			
			
			return str;
		}

		public int compareTo(UserProfile other) {
			if (total_play_count == other.total_play_count) {
				return 0;// keep as is
			} else if (total_play_count > other.total_play_count) {
				return -1; // this is higher, move up
			} else {
				return 1;// this is lower, move down
			}
		}

}
