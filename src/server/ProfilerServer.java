package server;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import TasteProfile.Profiler;
import TasteProfile.ProfilerHelper;

public class ProfilerServer {

	public static void main(String[] args) {
		
		try {
			ORB orb = ORB.init(args, null);
			
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();
			
			ProfilerServant profilerImpl = new ProfilerServant();
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(profilerImpl);
			Profiler href = ProfilerHelper.narrow(ref);
			
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			
			String name = "Profiler";
			NameComponent path[] = ncRef.to_name( name );
			ncRef.rebind(path, href);
			
			System.out.println("Musical taste profiler server ready and waiting ...");
			orb.run();
			
			
		} catch(Exception e) {
			System.err.println("ERROR: " + e.getMessage());
			e.printStackTrace(System.out);
		}

	}

}
