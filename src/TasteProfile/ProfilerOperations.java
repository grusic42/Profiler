package TasteProfile;


/**
* TasteProfile/ProfilerOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from tasteprofile.idl
* Sunday, 16 September 2018 at 12:54:34 Central European Summer Time
*/


/* The service interface with the methods that can be invoked remotely by clients */
public interface ProfilerOperations 
{
  /* Returns how many times a given song was played by all the users*/
  int getTimesPlayed (String song_id);

  /* Returns how many times a given song was played by a given user*/
  int getTimesPlayedByUser (String user_id, String song_id);

  /* Returns top 3 users listened to specified song*/
  String getTopThreeUsersBySong (String song_id);
} // interface ProfilerOperations
