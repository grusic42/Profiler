package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import TasteProfile.ProfilerPOA;

class Song {
	public String id;
	public long play_count;

	public Song(String pID, int pCount) {
		id = pID;
		play_count = pCount;
	}
}

class UserProfile implements Comparable<UserProfile> {
	public String id;
	public long total_play_count = 0;
	public List<Song> songs = new ArrayList<Song>();

	public UserProfile(String pid) {
		id = pid;
	}

	public UserProfile(String pid, long ptotal_play_count) {
		id = pid;
		total_play_count = ptotal_play_count;
		songs = new ArrayList<Song>();
	}

	public String toString() {
		String str = "";
		str += id + " ";
		str += total_play_count + " ";
		str += songs.size() + " ";
		for (int i = 0; i < songs.size(); i++) {
			str += songs.get(i).id + " " + songs.get(i).play_count + " ";
		}

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

public class ProfilerServant extends ProfilerPOA {

	List<UserProfile> cacheUserProfiles;

	public ProfilerServant() {
		cacheUserProfiles = new ArrayList<UserProfile>();
		if (!readCacheUserProfiles()) {
			LoadCacheUserProfiles();
			writeCacheUserProfiles();
		}
	}

	class SongProfile {
		public int totalPlayCount;
		public TopThree topThreeUsers;

		public SongProfile(int totalPlayCount, TopThree topThreeUsers) {
			this.totalPlayCount = totalPlayCount;
			this.topThreeUsers = topThreeUsers;
		}

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
				UserCounter user = new UserCounter(userid, timesPlayed);

				// tests if song already exists in cache
				if ((songp = cache.get(songid)) != null) {
					top3 = songp.topThreeUsers;
					userList = top3.topThreeList;
					// if the list of users is 1 or 2, it will fill up
					if (userList.size() < 3) {
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
					} else {
						// if 3 users already exists, the one with the least plays, will be replaced
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
					songp = new SongProfile(timesPlayed, top3);
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
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String checkSongCache(String songid) {
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

	private int checkPlayedCache(String songid) {
		SongProfile song = cache.get(songid);
		return song.totalPlayCount;
	}

	@Override
	public int getTimesPlayed(String song_id) {

		fakeNetworkLatency();

		BufferedReader br = null;
		int sum = 0;

		try {

			if ((sum = checkPlayedCache(song_id)) != 0) {
				return sum;
			} else {

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
		// try to find answer in cache
		for (int userIterator = 0; userIterator < cacheUserProfiles.size(); userIterator++) {
			if (cacheUserProfiles.get(userIterator).id.equals(user_id)) {
				for (int songIterator = 0; songIterator < cacheUserProfiles.get(userIterator).songs.size(); songIterator++) {
					if (cacheUserProfiles.get(userIterator).songs.get(songIterator).id.equals(song_id)) {
						System.out.println("...[cacheHIT, success]...");
						return ((int) (cacheUserProfiles.get(userIterator).songs.get(songIterator).play_count));
					}
				}
				// user found in cache but not song || user input error
			}

		}
		// user not found in cache. try database
		BufferedReader br = null;

		try {
			File file = new File("root/../../train_triplets.txt");
			br = new BufferedReader(new FileReader(file));

			String st;
			while ((st = br.readLine()) != null) {
				String[] tuple = st.split("\t");
				if (user_id.equals(tuple[0]) && song_id.equals(tuple[1])) {
					br.close();
					System.out.println("...[CacheMISS, Failure]...");
					return Integer.parseInt(tuple[2]);
				}
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
		return 0; // error user not found in database
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
			} else {
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

				// Sends back the list's last three users, i.e. the ones who has the most song
				// plays
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
	
	UserProfile getUserProfile(String user_id){
		for (UserProfile u : cacheUserProfiles){
			if (u.id == user_id){
				return u;
			}
		}
		return null;
		
	}

	boolean readCacheUserProfiles() {
		String str = null;
		String[] tuple;
		BufferedReader reader;
		UserProfile tempUserProfile;
		try {
			reader = new BufferedReader(new FileReader("root/../../UserProfileCache.txt"));
			str = reader.readLine();
			if (str != null) {
				if (str.startsWith("Cached UserProfiles")) {
					System.out.println("...[File read, success]...");
					cacheUserProfiles = new ArrayList<UserProfile>();
				} else {
					reader.close();
					return false;
				}
			} else {
				reader.close();
				return false;
			}
			while ((str = reader.readLine()) != null) {
				int songCount = 0;
				tuple = str.split(" ");
				tempUserProfile = new UserProfile(tuple[0]);
				tempUserProfile.total_play_count = Integer.parseInt(tuple[1]);
				songCount = Integer.parseInt(tuple[2]);
				for (int i = 3; i < songCount + 3; i += 2) {
					tempUserProfile.songs.add(new Song(tuple[i], Integer.parseInt(tuple[i + 1])));
				}
				cacheUserProfiles.add(tempUserProfile);

			}

			reader.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	void writeCacheUserProfiles() {

		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("root/../../UserProfileCache.txt"));

			writer.write("Cached UserProfiles " + cacheUserProfiles.size() + " entries.");
			writer.newLine();
			for (int i = 0; i < cacheUserProfiles.size(); i++) {
				writer.write((cacheUserProfiles.get(i).toString()));
				writer.newLine();
			}

			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void LoadCacheUserProfiles() {

		BufferedReader br = null;
		String st;
		boolean userInCache;
		int tempPlayTime = 0;
		long tempTotalPlayTime = 0;
		String tempUserID = "";
		// String previousUserID = "";
		ArrayList<Song> tempSongArray;
		final int CACHE_SIZE = 1000;

		String[] tuple;

		try {
			System.out.println("...[Caching User Profiles]...");

			File file = new File("root/../../train_triplets.txt");

			br = new BufferedReader(new FileReader(file));
			st = br.readLine();
			tuple = st.split("\t");
			tempUserID = tuple[0];

			tempPlayTime = Integer.parseInt(tuple[2]);
			tempTotalPlayTime = 0;
			tempSongArray = new ArrayList<Song>();
			cacheUserProfiles.add(new UserProfile(tempUserID, tempPlayTime));
			cacheUserProfiles.get(0).songs.add(new Song(tuple[1], tempPlayTime));
			while (st != null) {

				st = br.readLine(); // read next line of input
				if (st != null) {
					tuple = st.split("\t");
					if (tuple[0].equals(tempUserID)) {
						tempUserID = tuple[0];
					}
				}
				while (st != null && tempUserID.equals(tuple[0])) { // quickly add up sequential usersentries with
																	// same userId

					tempSongArray.add(new Song(tuple[1], Integer.parseInt(tuple[2])));
					tempTotalPlayTime += Integer.parseInt(tuple[2]);

					st = br.readLine();
					if (st != null) {
						tuple = st.split("\t");
						if (tuple[0].equals(tempUserID)) {
							tempUserID = tuple[0];
						}

					}

				}
				if (tempUserID != tuple[0] || st == null) {

					userInCache = false;
					for (int i = cacheUserProfiles.size() - 1; i >= 0; i--) {
						if (cacheUserProfiles.get(i).id.equals(tempUserID)) {
							cacheUserProfiles.get(i).songs.addAll(tempSongArray);
							cacheUserProfiles.get(i).total_play_count += tempTotalPlayTime;
							Collections.sort(cacheUserProfiles);
							userInCache = true;
							break;
						}

					}
					if (!userInCache) {
						if (cacheUserProfiles.size() < CACHE_SIZE) {
							// add it, we have room to spare
							cacheUserProfiles.add(new UserProfile(tempUserID, tempTotalPlayTime));
							cacheUserProfiles.get(cacheUserProfiles.size() - 1).songs.addAll(tempSongArray);
							cacheUserProfiles.get(cacheUserProfiles.size() - 1).total_play_count = tempTotalPlayTime;
							Collections.sort(cacheUserProfiles);
							userInCache = true;
						} else if (cacheUserProfiles.get(CACHE_SIZE - 1).total_play_count < tempTotalPlayTime) {
							// replace last with the new one
							cacheUserProfiles.remove(CACHE_SIZE - 1);
							cacheUserProfiles.add(new UserProfile(tempUserID, tempTotalPlayTime));
							cacheUserProfiles.get(CACHE_SIZE - 1).songs.addAll(tempSongArray);
							cacheUserProfiles.get(CACHE_SIZE - 1).total_play_count = tempTotalPlayTime;
							Collections.sort(cacheUserProfiles);
							userInCache = true;
						}

					}
					if (st != null) {
						tempSongArray = new ArrayList<Song>();
						tempSongArray.add(new Song(tuple[1], Integer.parseInt(tuple[2])));
						tempTotalPlayTime = Integer.parseInt(tuple[2]);
					}
				}
				tempUserID = tuple[0];

			}
			System.out.println("...[Done Caching User Profiles]...");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
