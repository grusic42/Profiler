package TasteProfile;


/**
* TasteProfile/Song.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from tasteprofile.idl
* Tuesday, October 2, 2018 12:57:26 PM CEST
*/


/*
valuetype UserCounter {
public string user_id;
public long songid_play_time;
};

valuetype TopThree {
public sequence<UserCounter> topThreeUsers;
};

valuetype SongProfile {
public long total_play_count;
public TopThree top_three_users;
};*/
public abstract class Song implements org.omg.CORBA.portable.StreamableValue
{
  public String id = null;
  public int play_count = (int)0;

  private static String[] _truncatable_ids = {
    TasteProfile.SongHelper.id ()
  };

  public String[] _truncatable_ids() {
    return _truncatable_ids;
  }

  public void _read (org.omg.CORBA.portable.InputStream istream)
  {
    this.id = istream.read_string ();
    this.play_count = istream.read_long ();
  }

  public void _write (org.omg.CORBA.portable.OutputStream ostream)
  {
    ostream.write_string (this.id);
    ostream.write_long (this.play_count);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return TasteProfile.SongHelper.type ();
  }
} // class Song