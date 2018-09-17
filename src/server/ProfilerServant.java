package server;

import TasteProfile.ProfilerPOA;
import java.io.*;

public class ProfilerServant extends ProfilerPOA {
	
	@Override
	public String sendMessage(String message) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTimesPlayed(String song_id) {
		
		BufferedReader br = null;
		int sum = 0;
		
		try {
		File file = new File("/root/Documents/INF5020/first assignment/train_triplets.txt");
		
		br = new BufferedReader(new FileReader(file));
		
		String st;
		while ((st = br.readLine()) != null) {
			String[] tuple = st.split("\t");
			if (song_id.equals(tuple[1]))
				sum += Integer.parseInt(tuple[2]);
			}
		} catch (IOException e) {
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
		return sum;
	}
	

	@Override
	public int getTimesPlayedByUser(String user_id, String song_id) {
		
		BufferedReader br = null;
		int sum = 0;
		
		try {
		File file = new File("/root/Documents/INF5020/first assignment/train_triplets.txt");
		
		br = new BufferedReader(new FileReader(file));
		
		String st;
		while ((st = br.readLine()) != null) {
			String[] tuple = st.split("\t");
			if (user_id.equals(tuple[0]) && song_id.equals(tuple[1]))
				sum += Integer.parseInt(tuple[2]);
			}
		} catch (IOException e) {
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
		return sum;
	}

	@Override
	public String getTopThreeUsersBySong(String song_id) {
		// TODO Auto-generated method stub
		return null;
	}

}
