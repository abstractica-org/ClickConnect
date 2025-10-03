package org.abstractica.clickconnect;

import org.abstractica.javacsg.Geometry3D;
import org.abstractica.javacsg.JavaCSG;

public interface ConnectorSystem
{
	//Access to the underlying CSG
	JavaCSG getJavaCSG();
	ConnectorSystemInfo getSystemInfo();


	// Frequently used measurements
	double getDiameter(); // Without adjustments
	double getWidth(); //Without adjustments
	double getConnectorHight(); //Without adjustments
	double getFlatHeadHeight(); //Without adjustments

	// Round axle stuff
	Geometry3D getRoundAxle(double length);
	Geometry3D getRoundAxleConnector(boolean addSpacing, boolean addTension);
	Geometry3D getRoundAxleFlatHead(boolean addSpacing);

	// Round axle hole stuff
	Geometry3D getRoundAxleTurnHole(double length);
	Geometry3D getRoundAxleLockHole(double length);
	Geometry3D getRoundAxleTurnConnectorHole(boolean fixOverhang);
	Geometry3D getRoundAxleLockConnectorHole(boolean fixOverhang);
	Geometry3D getRoundAxleTurnFlatHeadHole();
	Geometry3D getRoundAxleLockFlatHeadHole();

	// Round axle sleeve stuff
	Geometry3D getRoundAxleSleeve(double length, double diameter, int angularResolution);

	// Rect axle stuff
	Geometry3D getRectAxle(double length);
	Geometry3D getRectAxleConnector(boolean addSpacing, boolean addTension);
	Geometry3D getRectAxleFlatHead(boolean addSpacing);

	// Rect axle hole stuff
	Geometry3D getRectAxleHole(double length);
	Geometry3D getRectAxleConnectorHole(boolean fixOverhang);
	Geometry3D getRectFlatHeadHole();

	// Rect axle sleeve stuff
	Geometry3D getRectAxleSleeve(double length, double diameter, int angularResolution);
}
