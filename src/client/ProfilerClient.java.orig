package client;

import java.util.Scanner;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import TasteProfile.Profiler;
import TasteProfile.ProfilerHelper;

public class ProfilerClient {

	static Profiler profilerImpl;

	static final String CMD_TIMES_PLAYED = "getTimesPlayed ";
	static final String CMD_USER_TIMES_PLAYED = "getTimesPlayedByUser ";
	static final String CMD_SONG_TOP_3 = "getTopThreeUsersBySong ";
	static final String CMD_HELP = "help";
	static final String CMD_QUIT = "quit";

	public static class Input implements Runnable {

<<<<<<< HEAD
        public void run() {
            Scanner in = new Scanner(System.in);
            while (true) {
                String s = in.nextLine();
                parse(s);
            }
        }

        void parse(String str) {
            if (str.startsWith(CMD_TIMES_PLAYED)) {
            	String song = str.substring(CMD_TIMES_PLAYED.length());
            	timesPlayed(song);
            } else if (str.startsWith(CMD_USER_TIMES_PLAYED)) {
            	String[] sp = str.split(" ");
            	String user = sp[1];
            	String song = sp[2];
            	timesPlayedByUser(user, song);
            } else if (str.startsWith(CMD_SONG_TOP_3)) {
                String song = str.substring(CMD_SONG_TOP_3.length());
                topThree(song);
            } else if (str.startsWith(CMD_HELP)) {
            	help();
            } else if (str.startsWith(CMD_QUIT)) {
                System.exit(0);
            } else {
                profilerImpl.sendMessage(str);
            }
        }

        void help() {
            String str = "Commands:\n" +
            		CMD_TIMES_PLAYED + "[song]\n" +
            		CMD_USER_TIMES_PLAYED + "[song] [user]\n" +
            		CMD_SONG_TOP_3 + "[song]\n" +
                    CMD_QUIT;
            System.out.println(str);
        }
        
        void loadPrompt() {
        	try {
=======
		public void run() {
			Scanner in = new Scanner(System.in);
			while (true) {
				String s = in.nextLine();
				parse(s);
			}
		}

		void parse(String str) {
			if (str.startsWith(CMD_TIMES_PLAYED)) {
				String song = str.substring(CMD_TIMES_PLAYED.length());
				loadPrompt();
				System.out.println("Song " + song + " played " + profilerImpl.getTimesPlayed(song) + " times.");
			} else if (str.startsWith(CMD_USER_TIMES_PLAYED)) {
				String[] sp = str.split(" ");
				String user = sp[1];
				String song = sp[2];
				loadPrompt();
				System.out.println("Song " + song + " played " + profilerImpl.getTimesPlayedByUser(user, song)
						+ " times by user " + user + ".");
			} else if (str.startsWith(CMD_SONG_TOP_3)) {
				String song = str.substring(CMD_SONG_TOP_3.length());
				topThree(song);
			} else if (str.startsWith(CMD_HELP)) {
				help();
			} else if (str.startsWith(CMD_QUIT)) {
				System.exit(0);
			} else {
				profilerImpl.sendMessage(str);
			}
		}

		void help() {
			String str = "Commands:\n" + CMD_TIMES_PLAYED + "[song]\n" + CMD_USER_TIMES_PLAYED + "[song] [user]\n"
					+ CMD_SONG_TOP_3 + "[song]\n" + CMD_QUIT;
			System.out.println(str);
		}

		void loadPrompt() {
			try {
>>>>>>> aa79db621c5dfee523195385aacc3229bdf46c4a
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
<<<<<<< HEAD
        	System.out.println("...[working hard]...");
        }
        
        void timesPlayed (String song) {
        	long startTime = System.currentTimeMillis();
        	loadPrompt();
        	int timesPlayed = profilerImpl.getTimesPlayed(song);
        	long elapsedTime = System.currentTimeMillis() - startTime;
        	System.out.println("Song " + song + " played " + timesPlayed + " times.(" + elapsedTime  + "ms)");
        	
        	
        }
        
        void timesPlayedByUser (String user, String song) {
        	long startTime = System.currentTimeMillis();
        	loadPrompt();
        	int timesPlayed = profilerImpl.getTimesPlayedByUser(user, song);
        	long elapsedTime = System.currentTimeMillis() - startTime;
        	if (timesPlayed != 0) {
        		System.out.println("Song " + song + " played " + timesPlayed + " times by user " + user + ".("+ elapsedTime  + "ms)");
        	} else System.out.println("Song not played by this user");
        }
        
        void topThree (String song) {
        	long startTime = System.currentTimeMillis();
        	loadPrompt();
        	String[] top3 = (profilerImpl.getTopThreeUsersBySong(song)).split("\n");
        	long elapsedTime = System.currentTimeMillis() - startTime;
        	
        	System.out.println("Song " + song  +" (" + elapsedTime  + "ms)");
        	for (String go : top3) {
        		String[] wow = go.split("\t");
            	System.out.println("User " + wow[0] + " listened " + wow[2] + " times.");
        	}
        }
=======
			System.out.println("...[working hard]...");
		}

		void topThree(String song) {
			String[] top3 = (profilerImpl.getTopThreeUsersBySong(song)).split("\n");
			loadPrompt();
			System.out.println("Song " + song);
			for (String go : top3) {
				String[] wow = go.split("\t");
				System.out.println("User " + wow[0] + " listened " + wow[2] + " times");
			}
		}
>>>>>>> aa79db621c5dfee523195385aacc3229bdf46c4a
	}

	public static void main(String[] args) {

		try {
			ORB orb = ORB.init(args, null);

			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			String name = "Profiler";
			profilerImpl = ProfilerHelper.narrow(ncRef.resolve_str(name));

			System.out.println("Obtained a handle on server object: " + profilerImpl);

			System.out.println("Welcome to the Musical Taste Profiler\nType \"help\" for help or \"quit\" to quit");

			new Thread(new Input()).start();

		} catch (Exception e) {
			System.out.println("ProfilerClient Error: " + e.getMessage());
			e.printStackTrace(System.out);
		}
	}

}
