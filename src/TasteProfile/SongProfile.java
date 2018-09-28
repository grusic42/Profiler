package TasteProfile;


/**
* TasteProfile/SongProfile.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from tasteprofile.idl
* Friday, September 28, 2018 2:02:50 PM CEST
*/

public abstract class SongProfile implements org.omg.CORBA.portable.StreamableValue
{
  public int total_play_count = (int)0;
  public TasteProfile.TopThree top_three_users = null;

  private static String[] _truncatable_ids = {
    TasteProfile.SongProfileHelper.id ()
  };

  public String[] _truncatable_ids() {
    return _truncatable_ids;
  }

  public void _read (org.omg.CORBA.portable.InputStream istream)
  {
    this.total_play_count = istream.read_long ();
    this.top_three_users = TasteProfile.TopThreeHelper.read (istream);
  }

  public void _write (org.omg.CORBA.portable.OutputStream ostream)
  {
    ostream.write_long (this.total_play_count);
    TasteProfile.TopThreeHelper.write (ostream, this.top_three_users);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return TasteProfile.SongProfileHelper.type ();
  }
} // class SongProfile
