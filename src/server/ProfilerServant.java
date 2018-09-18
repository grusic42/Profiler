package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import TasteProfile.ProfilerPOA;

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

}
