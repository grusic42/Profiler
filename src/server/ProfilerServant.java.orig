package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import TasteProfile.ProfilerPOA;

public class ProfilerServant extends ProfilerPOA {
<<<<<<< HEAD
	
	/*
		class UserCounter {
			public String user_id;
			public long song_play_time;
		}
		
		class TopThree {
			public UserCounter[] topThreeUsers;
		}
		
		class SongProfile {
			public long total_play_count;
			public TopThree top_three_users;
		}
		 
	private void loadCache() {
		BufferedReader br = null;
		try {
			File file = new File("/root/Documents/INF5020/first assignment/train_triplets.txt");
			
			br = new BufferedReader(new FileReader(file));
			//Map<String,int> userCounter = new HashMap<String,int>();
						
			String st;
			while ((st = br.readLine()) != null) {
				String[] tuple = st.split("\t");
				String user = tuple[0];
				String song = tuple[1];
				int plays = Integer.parseInt(tuple[2]);
				Map<String, Integer> userCounter = new HashMap<>();
				userCounter.put(user, plays);
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	*/
	
=======

>>>>>>> aa79db621c5dfee523195385aacc3229bdf46c4a
	@Override
	public String sendMessage(String message) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTimesPlayed(String song_id) {
<<<<<<< HEAD
		fakeNetworkLatency();
		
=======

>>>>>>> aa79db621c5dfee523195385aacc3229bdf46c4a
		BufferedReader br = null;
		int sum = 0;

		try {
			File file = new File("root/../../train_triplets.txt");

			br = new BufferedReader(new FileReader(file));

			String st;
			while ((st = br.readLine()) != null) {
				String[] tuple = st.split("\t");
				if (song_id.equals(tuple[1]))
					sum += Integer.parseInt(tuple[2]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sum;
	}

	@Override
	public int getTimesPlayedByUser(String user_id, String song_id) {
<<<<<<< HEAD
		fakeNetworkLatency();
		
=======

>>>>>>> aa79db621c5dfee523195385aacc3229bdf46c4a
		BufferedReader br = null;
		int sum = 0;

		try {
			File file = new File("root/../../train_triplets.txt");

			br = new BufferedReader(new FileReader(file));

			String st;
			while ((st = br.readLine()) != null) {
				String[] tuple = st.split("\t");
				if (user_id.equals(tuple[0]) && song_id.equals(tuple[1]))
					sum += Integer.parseInt(tuple[2]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sum;
	}

	@Override
	public String getTopThreeUsersBySong(String song_id) {
		
		BufferedReader br = null;
		ArrayList<String> matches = new ArrayList<String>();

		try {
<<<<<<< HEAD
			fakeNetworkLatency();
			File file = new File("/root/Documents/INF5020/first assignment/train_triplets.txt");
			
			br = new BufferedReader(new FileReader(file));
			
=======
			File file = new File("root/../../train_triplets.txt");

			br = new BufferedReader(new FileReader(file));

>>>>>>> aa79db621c5dfee523195385aacc3229bdf46c4a
			String st;
			while ((st = br.readLine()) != null) {
				String[] tuple = st.split("\t");
				if (song_id.equals(tuple[1]))
					matches.add(st);
<<<<<<< HEAD
				}
			
			// Sorts song array asc order
			Collections.sort(matches, new Comparator<String>() {
		        @Override
		        public int compare(String o1, String o2) {
		        	String a1 = o1.substring(60);
		        	String a2 = o2.substring(60);
		            return Integer.valueOf(a1).compareTo(Integer.valueOf(a2));
		        }
		    });
			
			// Sends back the list's last three users, i.e. the ones who has the most song plays
			StringBuilder sb = new StringBuilder();
			for (int i = matches.size()-3; i < matches.size(); i++) {
			{
			    sb.append(matches.get(i));
			    sb.append("\n");
			}
			}
			return sb.toString();
		} catch (IOException e) {
=======
			}

			// Sorts song array asc order
			Collections.sort(matches, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					String a1 = o1.substring(60);
					String a2 = o2.substring(60);
					return Integer.valueOf(a1).compareTo(Integer.valueOf(a2));
				}
			});

			StringBuilder sb = new StringBuilder();
			for (int i = matches.size() - 3; i < matches.size(); i++) {
				{
					sb.append(matches.get(i));
					sb.append("\n");
				}
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
>>>>>>> aa79db621c5dfee523195385aacc3229bdf46c4a
				e.printStackTrace();
		} 
		finally {
				try {
					br.close();
				} catch 
				(IOException e) {
					e.printStackTrace();
				}
		}
		return null;
	}
	
	
	
	private void fakeNetworkLatency() {
		try {
			Thread.sleep(80);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	

}
