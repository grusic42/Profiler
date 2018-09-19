package client;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import TasteProfile.Profiler;
import TasteProfile.ProfilerHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ProfilerClient {
	
	static Profiler profilerImpl;
	
    static final String CMD_TIMES_PLAYED = "getTimesPlayed ";
    static final String CMD_USER_TIMES_PLAYED   = "getTimesPlayedByUser ";
    static final String CMD_SONG_TOP_3   = "getTopThreeUsersBySong ";
    static final String CMD_HELP = "help";
    static final String CMD_QUIT = "quit";
    static final String CMD_READ_INPUT = "inputfile";
	
	
	public static class Input implements Runnable {

        public void run() {
            Scanner in = new Scanner(System.in);
            while (true) {
                String s = in.nextLine();
                if(s.startsWith(CMD_READ_INPUT)){
                    String filename = s.substring(CMD_READ_INPUT.length()+1);
                    System.out.println("Reading input from file " + filename);
                    
                    readInputFile(filename);
            	}else {
                	parse(s);
                }
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
            	System.out.println("Song " + song + " played " + profilerImpl.getTimesPlayedByUser(user, song) + " times by user " + user + ".");
            } else if (str.startsWith(CMD_SONG_TOP_3)) {
                String song = str.substring(CMD_SONG_TOP_3.length());
                topThree(song);
            } else if (str.startsWith(CMD_HELP)) {
            	help();
            } else if (str.startsWith(CMD_QUIT)) {
            System.exit(0);
        	}else {
                profilerImpl.sendMessage(str);
            }
        }
        
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
            		CMD_USER_TIMES_PLAYED + "[song] [user]\n" +
            		CMD_SONG_TOP_3 + "[song]\n" +
                    CMD_QUIT;
            System.out.println(str);
        }
        
        void loadPrompt() {
        	try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	System.out.println("...[working hard]...");
        }
        
        void topThree (String song) {
        	String[] top3 = (profilerImpl.getTopThreeUsersBySong(song)).split("\n");
        	loadPrompt();
        	System.out.println("Song " + song);
        	for (String go : top3) {
        		String[] wow = go.split("\t");
            	System.out.println("User " + wow[0] + " listened " + wow[2] + " times");
        	}
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
			
		} catch(Exception e) {
			System.out.println("ProfilerClient Error: " + e.getMessage());
			e.printStackTrace(System.out);
		}
	}

}
