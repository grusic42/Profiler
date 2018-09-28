package TasteProfile;


/**
* TasteProfile/_ProfilerStub.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from tasteprofile.idl
* Sunday, 16 September 2018 at 12:54:34 Central European Summer Time
*/


/* The service interface with the methods that can be invoked remotely by clients */
public class _ProfilerStub extends org.omg.CORBA.portable.ObjectImpl implements TasteProfile.Profiler
{


  /* Returns how many times a given song was played by all the users*/
  public int getTimesPlayed (String song_id)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("getTimesPlayed", true);
                $out.write_string (song_id);
                $in = _invoke ($out);
                int $result = $in.read_long ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return getTimesPlayed (song_id        );
            } finally {
                _releaseReply ($in);
            }
  } // getTimesPlayed


  /* Returns how many times a given song was played by a given user*/
  public int getTimesPlayedByUser (String user_id, String song_id)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("getTimesPlayedByUser", true);
                $out.write_string (user_id);
                $out.write_string (song_id);
                $in = _invoke ($out);
                int $result = $in.read_long ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return getTimesPlayedByUser (user_id, song_id        );
            } finally {
                _releaseReply ($in);
            }
  } // getTimesPlayedByUser


  /* Returns top 3 users listened to specified song*/
  public String getTopThreeUsersBySong (String song_id)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("getTopThreeUsersBySong", true);
                $out.write_string (song_id);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return getTopThreeUsersBySong (song_id        );
            } finally {
                _releaseReply ($in);
            }
  } // getTopThreeUsersBySong

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:TasteProfile/Profiler:1.0"};

  public String[] _ids ()
  {
    return (String[])__ids.clone ();
  }

  private void readObject (java.io.ObjectInputStream s) throws java.io.IOException
  {
     String str = s.readUTF ();
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     org.omg.CORBA.Object obj = orb.string_to_object (str);
     org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate ();
     _set_delegate (delegate);
   } finally {
     orb.destroy() ;
   }
  }

  private void writeObject (java.io.ObjectOutputStream s) throws java.io.IOException
  {
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     String str = orb.object_to_string (this);
     s.writeUTF (str);
   } finally {
     orb.destroy() ;
   }
  }

} // class _ProfilerStub
