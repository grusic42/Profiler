package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import TasteProfile.ProfilerPOA;

public class ProfilerServant extends ProfilerPOA {

	class UserProfile {
		public String id;
		public int total_play_count;
		public List<Song> songs;
	}
	
	class SongProfile {
		public SongProfile(int totalPlayCount, TopThree topThreeUsers) {
			this.totalPlayCount = totalPlayCount;
			this.topThreeUsers = topThreeUsers;
		}

		public int totalPlayCount;
		public TopThree topThreeUsers;
		
		public TopThree getTopThreeUsers() {
			return topThreeUsers;
		}

		public void setTopThreeUsers(TopThree topThreeUsers) {
			this.topThreeUsers = topThreeUsers;
		}

		public void setTotalPlayCount(int totalPlayCount) {
			this.totalPlayCount = totalPlayCount;
		}
	}
	
	class Song {
		public String id;
		public int play_count;
	}
	
	class TopThree {
		public TopThree(List<UserCounter> topThreeList) {
			super();
			this.topThreeList = topThreeList;
		}

		public List<UserCounter> topThreeList;

		public void setTopThreeUsers(List<UserCounter> topThreeList) {
			this.topThreeList = topThreeList;
			
		}
	}
	
	private Map<String, SongProfile> cache;
	
	class UserCounter {
		public UserCounter(String user_id, int songid_play_time) {
			this.user_id = user_id;
			this.songid_play_time = songid_play_time;
		}
		public String user_id;
		public int songid_play_time;
	}
	
	public void loadCache() {
		BufferedReader br = null; 
		try {
			File file = new File("src/../../train_triplets.txt");
			br = new BufferedReader(new FileReader(file));
			
			cache = new HashMap<>();
			
			TopThree top3;
			SongProfile songp;
			
			String st;
			while ((st = br.readLine()) != null) {
				String[] tuple = st.split("\t");			
				String userid = tuple[0];
				String songid = tuple[1];
				int timesPlayed = Integer.parseInt(tuple[2]);
				
				List<UserCounter> userList = new ArrayList<UserCounter>();
				UserCounter user = new UserCounter(userid,timesPlayed);
				
				//tests if song already exists in cache
				if((songp = cache.get(songid)) != null) {
					top3 = songp.topThreeUsers;
					userList = top3.topThreeList;
					//if the list of users is 1 or 2, it will fill up
					if (userList.size() < 3){
						userList.add(user); 
						Collections.sort(userList, new Comparator<UserCounter>() {
							@Override
							public int compare(UserCounter o1, UserCounter o2) {
								Integer a1 = o1.songid_play_time;
								Integer a2 = o2.songid_play_time;
								return Integer.valueOf(a1).compareTo(Integer.valueOf(a2));
							}
						}); 
						top3.setTopThreeUsers(userList);
						songp.setTotalPlayCount(songp.totalPlayCount + timesPlayed);  
						cache.replace(songid, songp);
					} 
					else {
						//if 3 users already exists, the one with the least plays, will be replaced
						if (user.songid_play_time > userList.get(0).songid_play_time) {
							userList.set(0, user);
							Collections.sort(userList, new Comparator<UserCounter>() {
								@Override
								public int compare(UserCounter o1, UserCounter o2) {
									Integer a1 = o1.songid_play_time;
									Integer a2 = o2.songid_play_time;
									return Integer.valueOf(a1).compareTo(Integer.valueOf(a2));
								}
							});	
						}
						top3.setTopThreeUsers(userList);
						songp.setTotalPlayCount(songp.totalPlayCount + timesPlayed); 
						cache.replace(songid, songp);
					}
				}
				// If the song is not yet in the cache
				else { 
					userList.add(user);
					top3 = new TopThree(userList);
					songp = new SongProfile(timesPlayed,top3);
					cache.put(songid, songp);
				}
			}
			songp = cache.get("SODDNQT12A6D4F5F7E");
			top3 = songp.topThreeUsers;
			System.out.println(top3.topThreeList.size());
			System.out.println(songp.totalPlayCount);
			for (UserCounter u : top3.topThreeList) {
				System.out.println(u.songid_play_time + " " + u.user_id);
			}
		}
		catch 
		(IOException e) { 
			e.printStackTrace();
		}
		
	}
	
	private String checkSongCache (String songid) {
		SongProfile song = cache.get(songid);
		TopThree tp = song.topThreeUsers;
		StringBuilder sb = new StringBuilder();
		for (UserCounter u : tp.topThreeList) {
			sb.append(u.user_id + "\t" + songid + "\t" + u.songid_play_time + "\n");
			System.out.println(u.songid_play_time + " " + u.user_id);
		}
		System.out.println(sb.toString());
		return sb.toString();
	}
	
	private int checkPlayedCache (String songid) {
		SongProfile song = cache.get(songid);
		return song.totalPlayCount;
	}
	
	@Override
	public String sendMessage(String message) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTimesPlayed(String song_id) {

		fakeNetworkLatency();

		BufferedReader br = null;
		int sum = 0;
		
		try {
			
			if ((sum = checkPlayedCache(song_id)) != 0) {
				return sum;
			}
			else {
			
				File file = new File("src/../../train_triplets.txt");
	
				br = new BufferedReader(new FileReader(file));
	
				String st;
				while ((st = br.readLine()) != null) {
					String[] tuple = st.split("\t");
					if (song_id.equals(tuple[1]))
						sum += Integer.parseInt(tuple[2]);
				}
				br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return sum;
	}

	@Override
	public int getTimesPlayedByUser(String user_id, String song_id) {

		fakeNetworkLatency();

		BufferedReader br = null;
		int sum = 0;

		try {
			File file = new File("src/../../train_triplets.txt");

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
		
		String cache;

		try {
			fakeNetworkLatency();
			
			if ((cache = checkSongCache(song_id)) != null) {
				return cache;
			}
			else {
				File file = new File("src/../../train_triplets.txt");
	
				br = new BufferedReader(new FileReader(file));
	
				String st;
				while ((st = br.readLine()) != null) {
					String[] tuple = st.split("\t");
					if (song_id.equals(tuple[1]))
						matches.add(st);
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
				for (int i = matches.size() - 3; i < matches.size(); i++) {
					{
						sb.append(matches.get(i));
						sb.append("\n");
					}
				}
				br.close();
				return sb.toString();
			}
		} catch (IOException e) {

			e.printStackTrace();
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
