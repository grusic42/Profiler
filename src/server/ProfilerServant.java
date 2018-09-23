package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

	public UserProfile(String pid, long ptotal_play_count, String pSongID, int pSongCount) {
		id = pid;
		total_play_count = ptotal_play_count;
		songs.add(new Song(pSongID, pSongCount));
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
		LoadCacheUserProfiles();
	}

	/*
	 * class UserCounter { public String user_id; public long song_play_time; }
	 * 
	 * class TopThree { public UserCounter[] topThreeUsers; }
	 * 
	 * class SongProfile { public long total_play_count; public TopThree
	 * top_three_users; }
	 * 
	 * private void loadCache() { BufferedReader br = null; try { File file = new
	 * File("/root/Documents/INF5020/first assignment/train_triplets.txt");
	 * 
	 * br = new BufferedReader(new FileReader(file)); //Map<String,int> userCounter
	 * = new HashMap<String,int>();
	 * 
	 * String st; while ((st = br.readLine()) != null) { String[] tuple =
	 * st.split("\t"); String user = tuple[0]; String song = tuple[1]; int plays =
	 * Integer.parseInt(tuple[2]); Map<String, Integer> userCounter = new
	 * HashMap<>(); userCounter.put(user, plays);
	 * 
	 * } } catch (IOException e) { e.printStackTrace(); } }
	 */

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

		fakeNetworkLatency();

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

			File file = new File("root/../../train_triplets.txt");

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
			return sb.toString();
		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
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

	void LoadCacheUserProfiles() {
		BufferedReader br = null;
		String st;
		boolean userInCache;
		int tempPlayTime = 0;
		long tempTotalPlayTime = 0;
		String tempUserID = "";
		String previousUserID = "";
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
			tempSongArray = new ArrayList<Song>();
			tempSongArray.add(new Song(tuple[1], tempPlayTime));
			tempTotalPlayTime = tempPlayTime;
			while (st != null) {
				userInCache = false;

				for (int i = cacheUserProfiles.size() - 1; i >= 0; i--) {
					if (cacheUserProfiles.get(i).id.equals(tuple[0])) {
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
						cacheUserProfiles.add(new UserProfile(tuple[0], tempTotalPlayTime, tuple[1], tempPlayTime));
						Collections.sort(cacheUserProfiles);
						userInCache = true;
					} else if (cacheUserProfiles.get(CACHE_SIZE - 1).total_play_count < tempTotalPlayTime) {
						// replace last with the new one
						cacheUserProfiles.remove(CACHE_SIZE - 1);
						cacheUserProfiles.add(new UserProfile(tuple[0], tempTotalPlayTime, tuple[1], tempPlayTime));
						Collections.sort(cacheUserProfiles);
						userInCache = true;
					}

				}

				st = br.readLine(); // read next line of input
				tuple = st.split("\t");
				tempUserID = tuple[0];
				tempPlayTime = Integer.parseInt(tuple[2]);
				while (st != null && tempUserID.equals(previousUserID)) { // quickly add up sequential usersentries with
																			// same userId

					if (tempUserID.equals(previousUserID)) {
						tempSongArray.add(new Song(tuple[1], tempPlayTime));
						tempTotalPlayTime += tempPlayTime;

						previousUserID = tempUserID;
						st = br.readLine();
						if (st == null) {
							break;
						}
						tuple = st.split("\t");
						tempUserID = tuple[0];
						tempPlayTime = Integer.parseInt(tuple[2]);
					} else {
						tempSongArray = new ArrayList<Song>();
						tempSongArray.add(new Song(tuple[1], tempPlayTime));
						tempTotalPlayTime = tempPlayTime;

					}

				}
				if (previousUserID != tempUserID) {
					tempSongArray = new ArrayList<Song>();
					tempSongArray.add(new Song(tuple[1], tempPlayTime));
					tempTotalPlayTime = tempPlayTime;
				}
				previousUserID = tempUserID;

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
