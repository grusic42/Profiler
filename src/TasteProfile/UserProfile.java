package TasteProfile;


/**
* TasteProfile/UserProfile.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from tasteprofile.idl
* Tuesday, October 2, 2018 12:57:26 PM CEST
*/

public abstract class UserProfile implements org.omg.CORBA.portable.StreamableValue
{
  public String id = null;
  public int total_play_count = (int)0;
  public TasteProfile.Song songs[] = null;

  private static String[] _truncatable_ids = {
    TasteProfile.UserProfileHelper.id ()
  };

  public String[] _truncatable_ids() {
    return _truncatable_ids;
  }

  public void _read (org.omg.CORBA.portable.InputStream istream)
  {
    this.id = istream.read_string ();
    this.total_play_count = istream.read_long ();
    int _len0 = istream.read_long ();
    this.songs = new TasteProfile.Song[_len0];
    for (int _o1 = 0;_o1 < this.songs.length; ++_o1)
      this.songs[_o1] = TasteProfile.SongHelper.read (istream);
  }

  public void _write (org.omg.CORBA.portable.OutputStream ostream)
  {
    ostream.write_string (this.id);
    ostream.write_long (this.total_play_count);
    ostream.write_long (this.songs.length);
    for (int _i0 = 0;_i0 < this.songs.length; ++_i0)
      TasteProfile.SongHelper.write (ostream, this.songs[_i0]);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return TasteProfile.UserProfileHelper.type ();
  }
} // class UserProfile