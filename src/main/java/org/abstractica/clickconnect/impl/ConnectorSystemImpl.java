package org.abstractica.clickconnect.impl;

import org.abstractica.clickconnect.ConnectorSystem;
import org.abstractica.clickconnect.ConnectorSystemInfo;
import org.abstractica.javacsg.*;

import java.util.ArrayList;
import java.util.List;

public class ConnectorSystemImpl implements ConnectorSystem
{
	private final JavaCSG csg;
	private final ConnectorSystemInfo info;

	public ConnectorSystemImpl(JavaCSG csg, ConnectorSystemInfo info)
	{
		this.csg = csg;
		this.info = info;
	}

	@Override
	public JavaCSG getJavaCSG()
	{
		return csg;
	}

	@Override
	public ConnectorSystemInfo getSystemInfo()
	{
		return info;
	}

	@Override
	public double getDiameter()
	{
		return info.diameter();
	}

	@Override
	public double getWidth()
	{
		return info.width();
	}

	@Override
	public double getConnectorHight()
	{
		return info.connectorHeight();
	}

	@Override
	public double getFlatHeadHeight()
	{
		return info.flatHeadHeight();
	}

	@Override
	public Geometry3D getRoundAxle(double length)
	{
		Geometry3D axle = csg.cylinder3D(info.axleDiameter(), length, info.angularResolution(), false);
		Geometry3D widthBox = getAxleWidthBox(length);
		axle = csg.intersection3D(axle, widthBox);
		return axle;
	}

	@Override
	public Geometry3D getRoundAxleConnector(boolean addSpacing, boolean addTension)
	{
		Geometry2D half2D = halfRoundConnector2D(addSpacing, addTension);
		Geometry3D full3D = csg.rotateExtrude(csg.degrees(360), info.angularResolution(), half2D);
		Geometry3D slit = csg.box3D(info.slitWidth(),info.axleDiameter()+2, info.slitLength()+1, false);
		slit = csg.translate3DZ(info.connectorHeight()-info.slitLength()).transform(slit);
		full3D = csg.difference3D(full3D, slit);
		Geometry3D widthBox = getAxleWidthBox(info.connectorHeight()+info.connectorBaseSpacing());
		full3D = csg.intersection3D(full3D, widthBox);
		return full3D;
	}

	@Override
	public Geometry3D getRoundAxleFlatHead(boolean addSpacing)
	{
		Geometry2D profile2D = getFlatHeadProfile(addSpacing);
		Geometry3D full3D = csg.rotateExtrude(csg.degrees(360), info.angularResolution(), profile2D);
		Geometry3D widthBox = getAxleWidthBox(info.flatHeadHeight());
		full3D = csg.intersection3D(full3D, widthBox);
		return full3D;
	}

	@Override
	public Geometry3D getRoundAxleTurnHole(double length)
	{
		return csg.cylinder3D(info.holeDiameter(), length, info.angularResolution(), false);
	}

	@Override
	public Geometry3D getRoundAxleLockHole(double length)
	{
		Geometry3D axleHole = getRoundAxleTurnHole(length);
		Geometry3D widthBox = getHoleWidthBox(length);
		axleHole = csg.intersection3D(axleHole, widthBox);
		return axleHole;
	}

	@Override
	public Geometry3D getRoundAxleTurnConnectorHole(boolean fixOverhang)
	{
		Geometry2D half2D = getHoleProfile2D(fixOverhang);
		return csg.rotateExtrude(csg.degrees(360), info.angularResolution(), half2D);
	}

	@Override
	public Geometry3D getRoundAxleLockConnectorHole(boolean fixOverhang)
	{
		Geometry3D full3D = getRoundAxleTurnConnectorHole(fixOverhang);
		Geometry3D widthBox = getHoleWidthBox(info.connectorHeight() + info.connectorBaseSpacing() + info.holeDiameter());
		full3D = csg.intersection3D(full3D, widthBox);
		return full3D;
	}

	@Override
	public Geometry3D getRoundAxleTurnFlatHeadHole()
	{
		Geometry2D profile2D = getFlatHeadHoleProfile();
		return csg.rotateExtrude(csg.degrees(360), info.angularResolution(), profile2D);
	}

	@Override
	public Geometry3D getRoundAxleLockFlatHeadHole()
	{
		Geometry3D full3D = getRoundAxleTurnFlatHeadHole();
		Geometry3D widthBox = getHoleWidthBox(info.flatHeadHeight()+info.flatHeadBaseSpacing());
		return csg.intersection3D(full3D, widthBox);
	}

	@Override
	public Geometry3D getRoundAxleSleeve(double length, double diameter, int angularResolution)
	{
		Geometry3D sleeve = csg.cylinder3D(diameter, length, angularResolution, false);
		Geometry3D hole = getRoundAxleLockHole(length);
		return csg.difference3D(sleeve, hole);
	}

	@Override
	public Geometry3D getRectAxle(double length)
	{
		return csg.box3D(info.axleDiameter(), info.axleWidth(), length, false);
	}

	@Override
	public Geometry3D getRectAxleConnector(boolean addSpacing, boolean addTension)
	{
		Geometry2D half2D = halfFlatConnector2D(addSpacing, addTension);
		Geometry2D full2D = csg.union2D(half2D, csg.mirror2D(1,0).transform(half2D));
		Geometry3D full3D = csg.linearExtrude(info.axleWidth(), true, full2D);
		full3D = csg.rotate3DX(csg.degrees(90)).transform(full3D);
		return full3D;
	}

	@Override
	public Geometry3D getRectAxleFlatHead(boolean addSpacing)
	{
		Geometry2D profile2D = getFlatHeadProfile(addSpacing);
		profile2D = csg.union2D(profile2D, csg.mirror2D(1,0).transform(profile2D));
		Geometry3D full3D = csg.linearExtrude(info.axleWidth(), true, profile2D);
		full3D = csg.rotate3DX(csg.degrees(90)).transform(full3D);
		return full3D;
	}

	@Override
	public Geometry3D getRectAxleHole(double length)
	{
		return csg.box3D(info.holeDiameter(), info.holeWidth(), length, false);
	}

	@Override
	public Geometry3D getRectAxleConnectorHole(boolean fixOverhang)
	{
		Geometry2D half2D = getHoleProfile2D(fixOverhang);
		Geometry2D full2D = csg.union2D(half2D, csg.mirror2D(1,0).transform(half2D));
		Geometry3D full3D = csg.linearExtrude(info.holeWidth(), true, full2D);
		full3D = csg.rotate3DX(csg.degrees(90)).transform(full3D);
		return full3D;
	}

	@Override
	public Geometry3D getRectFlatHeadHole()
	{
		Geometry2D profile2D = getFlatHeadHoleProfile();
		profile2D = csg.union2D(profile2D, csg.mirror2D(1,0).transform(profile2D));
		Geometry3D full3D = csg.linearExtrude(info.holeWidth(), true, profile2D);
		full3D = csg.rotate3DX(csg.degrees(90)).transform(full3D);
		return full3D;
	}

	@Override
	public Geometry3D getRectAxleSleeve(double length, double diameter, int angularResolution)
	{
		Geometry3D sleeve = csg.cylinder3D(diameter, length, angularResolution, false);
		Geometry3D hole = getRectAxleHole(length);
		return csg.difference3D(sleeve, hole);
	}

	//Private helper methods

	private Geometry3D getAxleWidthBox(double length)
	{
		Geometry3D widthBox = csg.box3D(info.axleDiameter()+2, info.axleWidth(), length+2, false);
		widthBox = csg.translate3DZ(-1).transform(widthBox);
		return widthBox;
	}

	private Geometry3D getHoleWidthBox(double length)
	{
		Geometry3D widthBox = csg.box3D(info.holeDiameter()+2, info.holeWidth(), length+2, false);
		widthBox = csg.translate3DZ(-1).transform(widthBox);
		return widthBox;
	}

	private Geometry2D halfRoundConnector2D(boolean addSpacing, boolean addTension)
	{
		List<Vector2D> vertices = new ArrayList<>();
		addConnectorProfile2D(vertices, addSpacing, addTension);
		vertices.add(csg.vector2D(0, info.connectorHeight() + info.connectorHeightAdjust()));
		Geometry2D res = csg.polygon2D(vertices);
		if(addSpacing)
		{
			res = csg.translate2DY(info.connectorBaseSpacing()).transform(res);
		}
		return res;
	}

	private Geometry2D halfFlatConnector2D(boolean addSpacing, boolean addTension)
	{
		List<Vector2D> vertices = new ArrayList<>();
		addConnectorProfile2D(vertices, addSpacing, addTension);
		addSlit2D(vertices);
		return csg.polygon2D(vertices);
	}

	private void addConnectorProfile2D(List<Vector2D> vertices, boolean addSpacing, boolean addTension)
	{
		double largeRadius = 0.5*info.axleDiameter();
		double smallRadius = largeRadius - info.barbSize();
		double spacing = addSpacing ? info.connectorBaseSpacing() : 0;
		double tension = addTension ? info.barbTension() : 0;
		vertices.add(csg.vector2D(0, -spacing));
		vertices.add(csg.vector2D(largeRadius, -spacing));
		vertices.add(csg.vector2D(largeRadius, info.connectorBaseHeight()));
		vertices.add(csg.vector2D(smallRadius, info.connectorBaseHeight() + info.barbSize()));
		vertices.add(csg.vector2D(smallRadius, info.connectorHeight() - (info.connectorBaseHeight() + info.barbSize())));
		vertices.add(csg.vector2D(largeRadius+tension, info.connectorHeight() - info.connectorBaseHeight()));
		vertices.add(csg.vector2D(smallRadius, info.connectorHeight() + info.connectorHeightAdjust()));
	}

	private Geometry2D getHoleProfile2D(boolean fixOverhang)
	{
		List<Vector2D> vertices = new ArrayList<>();
		double largeRadius = 0.5*info.holeDiameter();
		double smallRadius = largeRadius - info.barbSize();

		vertices.add(csg.vector2D(0, 0));
		vertices.add(csg.vector2D(largeRadius, 0));
		vertices.add(csg.vector2D(largeRadius, info.connectorBaseHeight()));
		vertices.add(csg.vector2D(smallRadius, info.connectorBaseHeight() + info.barbSize()));
		vertices.add(csg.vector2D(smallRadius, info.connectorHeight() - (info.connectorBaseHeight() + info.barbSize())));
		vertices.add(csg.vector2D(largeRadius, info.connectorHeight() - info.connectorBaseHeight()));
		vertices.add(csg.vector2D(largeRadius, info.connectorHeight() + info.connectorHoleHeightAdjust()));
		double y = info.connectorHeight() + info.connectorHoleHeightAdjust();
		y = fixOverhang ? y + 0.5*info.holeDiameter() : y;
		vertices.add(csg.vector2D(0, y));
		return csg.polygon2D(vertices);
	}

	private Geometry2D getFlatHeadProfile(boolean addSpacing)
	{
		double smallRadius = 0.5*info.axleDiameter();
		double largeRadius = 0.5*info.flatHeadAxleDiameter();
		double deltaRadius = 0.5*(info.flatHeadDiameter() - info.diameter());
		double base = info.flatHeadBaseHeight();
		double height = info.flatHeadHeight() + info.flatHeadHeightAdjust();
		List<Vector2D> vertices = new ArrayList<>();
		double spacing = addSpacing ? info.flatHeadBaseSpacing() : 0;
		vertices.add(csg.vector2D(0, -spacing));
		vertices.add(csg.vector2D(smallRadius, -spacing));
		vertices.add(csg.vector2D(smallRadius, base));
		vertices.add(csg.vector2D(largeRadius, base + deltaRadius));
		vertices.add(csg.vector2D(largeRadius, height));
		vertices.add(csg.vector2D(0, height));
		Geometry2D res = csg.polygon2D(vertices);
		if(addSpacing)
		{
			res = csg.translate2DY(info.flatHeadBaseSpacing()).transform(res);
		}
		return res;
	}

	private Geometry2D getFlatHeadHoleProfile()
	{
		double smallRadius = 0.5*info.holeDiameter();
		double largeRadius = 0.5*info.flatHeadHoleDiameter();
		double deltaRadius = 0.5*(info.flatHeadDiameter() - info.diameter());
		double base = info.flatHeadBaseHeight();
		double height = info.flatHeadHeight();
		List<Vector2D> vertices = new ArrayList<>();
		vertices.add(csg.vector2D(0, 0));
		vertices.add(csg.vector2D(smallRadius, 0));
		vertices.add(csg.vector2D(smallRadius, base));
		vertices.add(csg.vector2D(largeRadius, base + deltaRadius));
		vertices.add(csg.vector2D(largeRadius, height));
		vertices.add(csg.vector2D(0, height));
		return csg.polygon2D(vertices);
	}

	private void addSlit2D(List<Vector2D> vertices)
	{
		vertices.add(csg.vector2D(info.slitWidth()*0.5, info.connectorHeight()+info.connectorHeightAdjust()));
		vertices.add(csg.vector2D(info.slitWidth()*0.5, info.connectorHeight()-info.slitLength()));
		vertices.add(csg.vector2D(0, info.connectorHeight()-info.slitLength()));
	}
}
