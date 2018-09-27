package client;

import java.util.Arrays;
import java.util.Scanner;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import TasteProfile.Profiler;
import TasteProfile.ProfilerHelper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ProfilerClient {

	static Profiler profilerImpl;
    static final String CMD_TIMES_PLAYED = "getTimesPlayed ";
    static final String CMD_USER_TIMES_PLAYED   = "getTimesPlayedByUser ";
    static final String CMD_SONG_TOP_3   = "getTopThreeUsersBySong ";
    static final String CMD_HELP = "help";
    static final String CMD_QUIT = "quit";
    static final String CMD_READ_INPUT = "inputfile ";

	public static class Input implements Runnable {

        public void run() {
        	File file1 = new File("root/../../output.txt");
        	File file2 = new File("root/../../topthreeoutput.txt");
        	try {
				BufferedWriter writer1 = new BufferedWriter(new FileWriter(file1));
				writer1.write("");
				writer1.close();
				BufferedWriter writer2 = new BufferedWriter(new FileWriter(file2));
				writer2.write("");
				writer2.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        Scanner in = new Scanner(System.in);
        while (true) {
            String s = in.nextLine();
            if(s.startsWith(CMD_READ_INPUT)){
                String filename = s.substring(CMD_READ_INPUT.length());
                System.out.println("Reading input from file " + filename);

                readInputFile(filename);
          }else {
              try {
        parse(s);
      } catch (IOException e) {
        e.printStackTrace();
      }

            }
        }
    }

        void parse(String str) throws IOException {			
        	File file1 = new File("root/../../output.txt");
        	File file2 = new File("root/../../topthreeoutput.txt");
        	BufferedWriter writer1 = new BufferedWriter(new FileWriter(file1, true));
        	BufferedWriter writer2 = new BufferedWriter(new FileWriter(file2, true));
            if (str.startsWith(CMD_TIMES_PLAYED)) {
            	String song = str.substring(CMD_TIMES_PLAYED.length());
            	writer1.append(timesPlayed(song) + "\n\n");
            } else if (str.startsWith(CMD_USER_TIMES_PLAYED)) {
            	String[] sp = str.split(" ");
            	String user = sp[1];
            	String song = sp[2];
            	writer1.append(timesPlayedByUser(user, song) + "\n\n");
            } else if (str.startsWith(CMD_SONG_TOP_3)) {
                String song = str.substring(CMD_SONG_TOP_3.length());
                writer2.append(topThree(song) + "\n");
            } else if (str.startsWith(CMD_HELP)) {
            	help();
            } else if (str.startsWith(CMD_QUIT)) {
            System.exit(0);
        	}
            writer1.close();
            writer2.close();
        }
        
        //file needs to be in src folder
        void readInputFile(String filename){
        	BufferedReader br = null;
        	try {
        	String filePath = new String("./src/" + filename);
        	File file = new File(filePath);	
    		//File file = new File("./src/inputtest.txt");
    		
    		br = new BufferedReader(new FileReader(file));
    		
    		String lineWithTabs;
    		while ((lineWithTabs = br.readLine()) != null){
    			String[] tuple = lineWithTabs.split("\t");
    			String lineWithSpaces = String.join(" ", tuple);
    			System.out.println(lineWithSpaces);
    			parse(lineWithSpaces);
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
    }

        void help() {
            String str = "Commands:\n" +
            		CMD_TIMES_PLAYED + "[song]\n" +
            		CMD_USER_TIMES_PLAYED + "[user] [song]\n" +
            		CMD_SONG_TOP_3 + "[song]\n" +
            		CMD_READ_INPUT + "[filename]\n" +
                    CMD_QUIT;
            System.out.println(str);
        }
        
        String timesPlayed (String song) {
        	long startTime = System.currentTimeMillis();
        	int timesPlayed = profilerImpl.getTimesPlayed(song);
        	long elapsedTime = System.currentTimeMillis() - startTime;
        	String result = ("Song " + song + " played " + timesPlayed + " times.(" + elapsedTime  + "ms)");
        	System.out.println(result);
        	return result;
        	
        	
        }
        
        String timesPlayedByUser (String user, String song) {
        	long startTime = System.currentTimeMillis();
        	int timesPlayed = profilerImpl.getTimesPlayedByUser(user, song);
        	long elapsedTime = System.currentTimeMillis() - startTime;
        	String result1 = ("Song " + song + " played " + timesPlayed + " times by user " + user + ".("+ elapsedTime  + "ms)");
        	String result2 = ("Song " + song + " not played by user " + user + ". ("+ elapsedTime  + "ms");
        	if (timesPlayed != 0) {
        		System.out.println(result1);
        		return result1;
        	} else System.out.println(result2);
        	return result2;
        	 
        }
        
    String topThree(String song) {
			long startTime = System.currentTimeMillis();
			String[] top3 = (profilerImpl.getTopThreeUsersBySong(song)).split("\n");
			long elapsedTime = System.currentTimeMillis() - startTime;
			StringBuilder result = new StringBuilder();
			//String initialmessage = ("Song " + song + " (" + elapsedTime + "ms)");
			//System.out.println(initialmessage);
			//result.append(initialmessage + "\n");
			for (String go : top3) {
				String[] wow = go.split("\t");
				String resultline = ("User " + wow[0] + " listened " + wow[2] + " times to song " + wow[1] + ".");
				System.out.println(resultline);
				result.append(resultline + "\n");
				
			}
			return result.toString();
		}
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