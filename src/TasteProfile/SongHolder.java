package TasteProfile;

/**
* TasteProfile/SongHolder.java .
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
public final class SongHolder implements org.omg.CORBA.portable.Streamable
{
  public TasteProfile.Song value = null;

  public SongHolder ()
  {
  }

  public SongHolder (TasteProfile.Song initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = TasteProfile.SongHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    TasteProfile.SongHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return TasteProfile.SongHelper.type ();
  }

}
