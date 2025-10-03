package org.abstractica.clickconnect;

import com.sun.jdi.connect.Connector;
import org.abstractica.clickconnect.impl.ConnectorSystemFactoryDefaultImpl;
import org.abstractica.javacsg.JavaCSG;

public interface ConnectorSystemFactory
{
	static ConnectorSystemFactory createDefault(JavaCSG csg)
	{
		return new ConnectorSystemFactoryDefaultImpl(csg);
	}

	ConnectorSystem createConnectorSystem(ConnectorSystemInfo info);
	ConnectorSystem get4x3mmSystem();
	ConnectorSystem get5mmSystem();
	ConnectorSystem get6mmSystem();
	ConnectorSystem get7mmSystem();
	ConnectorSystem get8mmSystem();
	ConnectorSystem get9mmSystem();
	ConnectorSystem get10mmSystem();
}
