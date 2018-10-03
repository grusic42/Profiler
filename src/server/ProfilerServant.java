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
import TasteProfile.SongImpl;
import TasteProfile.SongProfileImpl;
import TasteProfile.TopThreeImpl;
import TasteProfile.UserCounterImpl;
import TasteProfile.UserProfileImpl;



public class ProfilerServant extends ProfilerPOA {

	List<UserProfileImpl> cacheUserProfiles;

	public ProfilerServant() {
		loadSongCache();
		cacheUserProfiles = new ArrayList<UserProfileImpl>();
		if (!readCacheUserProfiles()) {
			LoadCacheUserProfiles();
			writeCacheUserProfiles();
		}
	}


	private Map<String, SongProfileImpl> cache;

	public void loadSongCache() {
		BufferedReader br = null;
		try {
			File file = new File("src/../../train_triplets.txt");
			br = new BufferedReader(new FileReader(file));

			cache = new HashMap<>();

			TopThreeImpl top3;
			SongProfileImpl songp;

			String st;
			while ((st = br.readLine()) != null) {
				String[] tuple = st.split("\t");
				String userid = tuple[0];
				//int songid = Integer.parseInt(tuple[1]);
				String songid = tuple[1];
				int timesPlayed = Integer.parseInt(tuple[2]);

				List<UserCounterImpl> userList = new ArrayList<UserCounterImpl>();
				UserCounterImpl user = new UserCounterImpl();
				user.user_id = userid;
				user.songid_play_time = timesPlayed;

				// tests if song already exists in cache
				if ((songp = cache.get(songid)) != null) {
					top3 = songp.topThreeUsers;
					userList = top3.topThreeList;
					// if the list of users is 1 or 2, it will fill up
					if (userList.size() < 3) {
						userList.add(user);
						Collections.sort(userList, new Comparator<UserCounterImpl>() {
							@Override
							public int compare(UserCounterImpl o1, UserCounterImpl o2) {
								Integer a1 = o1.songid_play_time;
								Integer a2 = o2.songid_play_time;
								return Integer.valueOf(a1).compareTo(Integer.valueOf(a2));
							}
						});
						top3.topThreeList = userList;
						songp.totalPlayCount = (songp.totalPlayCount + timesPlayed);
						cache.replace(songid, songp);
					} else {
						// if 3 users already exists, the one with the least plays, will be replaced
						if (user.songid_play_time > userList.get(0).songid_play_time) {
							userList.set(0, user);
							Collections.sort(userList, new Comparator<UserCounterImpl>() {
								@Override
								public int compare(UserCounterImpl o1, UserCounterImpl o2) {
									Integer a1 = o1.songid_play_time;
									Integer a2 = o2.songid_play_time;
									return Integer.valueOf(a1).compareTo(Integer.valueOf(a2));
								}
							});
						}
						top3.topThreeList = userList;
						songp.totalPlayCount = (songp.totalPlayCount + timesPlayed);
						cache.replace(songid, songp);
					}
				}
				// If the song is not yet in the cache
				else {
					userList.add(user);
					top3 = new TopThreeImpl();
					top3.topThreeList = userList;
					songp = new SongProfileImpl();
					songp.totalPlayCount = timesPlayed;
					songp.topThreeUsers = top3;
					cache.put(songid, songp);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private TopThreeImpl checkSongCache(String songid) {
		SongProfileImpl song = cache.get(songid);
		TopThreeImpl tp = song.topThreeUsers;
//		StringBuilder sb = new StringBuilder();
//		for (UserCounterImpl u : tp.topThreeList) {
//			sb.append(u.user_id + "\t" + songid + "\t" + u.songid_play_time + "\n");
//		}
		return tp;
	}

	private int checkPlayedCache(String songid) {
		SongProfileImpl song = cache.get(songid);
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
		/*for (int userIterator = 0; userIterator < cacheUserProfiles.size(); userIterator++) {
			if (cacheUserProfiles.get(userIterator).id.equals(user_id)) {
				for (int songIterator = 0; songIterator < 5000; songIterator++) {
					//1000 = cacheUserProfiles.get(userIterator).songs.size();
					if (cacheUserProfiles.get(userIterator).songs[songIterator].id.equals(song_id)) {
						return ((int) (cacheUserProfiles.get(userIterator).songs[songIterator].play_count));
					}
				}
				// user found in cache but not song || user input error
			}

		}*/
		
		for (UserProfileImpl u : cacheUserProfiles) {
			if (u.id.equals(user_id)) {
				for (int i = 0; i < 5000; i++) {
					if(u.songs[i].id.equals(song_id))
						return u.songs[i].play_count;
				}
			}
		}
		
		// user not found in cache. try database
		BufferedReader br = null;

		try {
			File file = new File("src/../../train_triplets.txt");
			br = new BufferedReader(new FileReader(file));

			String st;
			while ((st = br.readLine()) != null) {
				String[] tuple = st.split("\t");
				if (user_id.equals(tuple[0]) && song_id.equals(tuple[1])) {
					br.close();
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
	public TopThreeImpl getTopThreeUsersBySong(String song_id) {

		BufferedReader br = null;
		ArrayList<String> matches = new ArrayList<String>();
		TopThreeImpl cache;
		
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
				List<UserCounterImpl> topUserCounters = new ArrayList<UserCounterImpl>();
				for (int i = matches.size() - 3; i < matches.size(); i++) {
					{
						String line = matches.get(i);
						String[] splitLine = line.split("\t");
						UserCounterImpl userC = new UserCounterImpl();
						userC.user_id = splitLine[0];
						userC.songid_play_time = Integer.parseInt(splitLine[2]);
						topUserCounters.add(userC);
					}
				}
				br.close();
				TopThreeImpl topThreeUsers = new TopThreeImpl();
				topThreeUsers.topThreeList = topUserCounters;
				return topThreeUsers;
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
	
	public UserProfileImpl getUserProfile(String user_id) {
		
			fakeNetworkLatency();
			
			 //Checks if the UserProfile is already in cache.
			for (UserProfileImpl u : cacheUserProfiles) {
				if (u.id.equals(user_id)) {
					return u;
				}
			}
			// Has to create UserProfile from the data file.
			BufferedReader br;
			File file = new File("src/../../train_triplets.txt");
	
			UserProfileImpl userProfile =null;
			String st;
			
			try {
				int index=0;	
				br = new BufferedReader(new FileReader(file));
				
				while ((st = br.readLine()) != null) {
					
					String[] tuple = st.split("\t");
					String userid = tuple[0];
					String songid = tuple[1];
					int timesPlayed = Integer.parseInt(tuple[2]);
					SongImpl song = new SongImpl();
					song.id=songid;
					song.play_count=timesPlayed;
					if (userid.equals(user_id)) {
						if (userProfile == null) {
							userProfile = new UserProfileImpl();
							userProfile.id=userid;
						}
						if(userProfile.songs[index]== null) {
							userProfile.songs[index]=song;
							userProfile.total_play_count += timesPlayed;
							index++;
						}
					} else {
						if (userProfile != null && userProfile.songs[0] != null) {
							return userProfile;
						}
					}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	boolean readCacheUserProfiles() {
		String str = null;
		String[] tuple;
		BufferedReader reader;
		UserProfileImpl tempUserProfile;
		try {
			reader = new BufferedReader(new FileReader("src/UserProfileCache.txt"));
			str = reader.readLine();
			if (str != null) {
				if (str.startsWith("Cached UserProfiles")) {
					cacheUserProfiles = new ArrayList<UserProfileImpl>();
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
				tempUserProfile = new UserProfileImpl();
				tempUserProfile.id=tuple[0];
				tempUserProfile.total_play_count = Integer.parseInt(tuple[1]);
				songCount = Integer.parseInt(tuple[2]);
				int j = 0;
				for (int i = 3; i < songCount + 3; i += 2) {
					SongImpl song = new SongImpl();
					song.id= tuple[i];
					song.play_count=Integer.parseInt(tuple[i + 1]);
					/*for (int j = 0; j < 5000; j++) {
						if(tempUserProfile.songs[j]== null)
						tempUserProfile.songs[j]=song;;
					}*/
					if(tempUserProfile.songs[j]== null) {
						tempUserProfile.songs[j]=song;
						
					}
					j++;

					
				}
				for(;j< 5000 ; j++) {
					tempUserProfile.songs[j]= new SongImpl();
					tempUserProfile.songs[j].id="null";
					tempUserProfile.songs[j].play_count=0;
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
			writer = new BufferedWriter(new FileWriter("src/UserProfileCache.txt"));

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
		int tempTotalPlayTime = 0;
		String tempUserID = "";
		ArrayList<SongImpl> tempSongArray;
		final int CACHE_SIZE = 1000;

		String[] tuple;

		try {

			File file = new File("src/../../train_triplets.txt");

			br = new BufferedReader(new FileReader(file));
			st = br.readLine();
			tuple = st.split("\t");
			tempUserID = tuple[0];

			tempPlayTime = Integer.parseInt(tuple[2]);
			tempTotalPlayTime = 0;
			tempSongArray = new ArrayList<SongImpl>();
			UserProfileImpl userP = new UserProfileImpl();
			userP.id=tempUserID;
			userP.total_play_count=tempPlayTime;
			cacheUserProfiles.add(userP);
			SongImpl song = new SongImpl();
			song.id=tuple[1];
			song.play_count= tempPlayTime;
			for (int i = 0; i < 5000; i++) {
				if(cacheUserProfiles.get(0).songs[i]== null)
				cacheUserProfiles.get(0).songs[i]=song;;
			}
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
					SongImpl songImpl = new SongImpl();
					songImpl.id=tuple[1];
					songImpl.play_count=Integer.parseInt(tuple[2]);
					tempSongArray.add(songImpl);
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
							
							for(int k=0; k<tempSongArray.size(); k++) {
								cacheUserProfiles.get(i).songs[k]=tempSongArray.get(k);
							}
						
							cacheUserProfiles.get(i).total_play_count += tempTotalPlayTime;
							Collections.sort(cacheUserProfiles);
							userInCache = true;
							break;
						}

					}
					if (!userInCache) {
						if (cacheUserProfiles.size() < CACHE_SIZE) {
							// add it, we have room to spare
							UserProfileImpl userPro = new UserProfileImpl();
							userPro.id=tempUserID;
							userPro.total_play_count=tempTotalPlayTime;
							
							for (int i = 0; i < tempSongArray.size(); i++) {
								userPro.songs[i] = tempSongArray.get(i);
							}
							cacheUserProfiles.add(userPro);
							cacheUserProfiles.get(cacheUserProfiles.size() - 1).total_play_count = tempTotalPlayTime;
							Collections.sort(cacheUserProfiles);
							userInCache = true;
						} else if (cacheUserProfiles.get(CACHE_SIZE - 1).total_play_count < tempTotalPlayTime) {
							// replace last with the new one
							cacheUserProfiles.remove(CACHE_SIZE - 1);
							UserProfileImpl u = new UserProfileImpl();
							u.id=tempUserID;
							u.total_play_count=tempTotalPlayTime;
							for (int i =0; i < tempSongArray.size(); i++) {
								u.songs[i] = tempSongArray.get(i);
							}
							cacheUserProfiles.add(u);
							cacheUserProfiles.get(CACHE_SIZE - 1).total_play_count = tempTotalPlayTime;
							Collections.sort(cacheUserProfiles);
							userInCache = true;
						}

					}
					if (st != null) {
						tempSongArray = new ArrayList<SongImpl>();
						SongImpl songI = new SongImpl();
						songI.id=tuple[1];
						songI.play_count= Integer.parseInt(tuple[2]);
						tempSongArray.add(songI);
						tempTotalPlayTime = Integer.parseInt(tuple[2]);
					}
				}
				tempUserID = tuple[0];

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

	}

}
