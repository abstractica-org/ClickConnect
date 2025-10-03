package org.abstractica.clickconnect.impl;

import org.abstractica.clickconnect.ConnectorSystem;
import org.abstractica.clickconnect.ConnectorSystemFactory;
import org.abstractica.clickconnect.ConnectorSystemInfo;
import org.abstractica.javacsg.JavaCSG;

public class ConnectorSystemFactoryDefaultImpl implements ConnectorSystemFactory
{
	private final JavaCSG csg;

	public ConnectorSystemFactoryDefaultImpl(JavaCSG csg)
	{
		this.csg = csg;
	}


	@Override
	public ConnectorSystem createConnectorSystem(ConnectorSystemInfo info)
	{
		return new ConnectorSystemImpl(csg, info);
	}

	@Override
	public ConnectorSystem get4x3mmSystem()
	{
		ConnectorSystemInfo info = new ConnectorSystemInfo
			(
				4,
				-0.2,
				0.2,
				3,
				-0.2,
				0.2,
				4,
				-0.1,
				0.1,
				1,
				0,
				0.4,
				0.2,
				2,
				4,
				8,
				-0.2,
				0.2,
				3,
				-0.2,
				0.2,
				0.2,
				128
			);
		return createConnectorSystem(info);
	}

	@Override
	public ConnectorSystem get5mmSystem()
	{
		return null;
	}

	@Override
	public ConnectorSystem get6mmSystem()
	{
		return null;
	}

	@Override
	public ConnectorSystem get7mmSystem()
	{
		return null;
	}

	@Override
	public ConnectorSystem get8mmSystem()
	{
		return null;
	}

	@Override
	public ConnectorSystem get9mmSystem()
	{
		return null;
	}

	@Override
	public ConnectorSystem get10mmSystem()
	{
		return null;
	}
}
