package org.abstractica.clickconnect;

import org.abstractica.javacsg.Geometry3D;

public interface RoundClickSystem
{
	Geometry3D getClicker(double extraLength, boolean tight);
	Geometry3D getRoundHoleCutout(double extraLength);
	Geometry3D getLockedHoleCutout(double extraLength);
}
