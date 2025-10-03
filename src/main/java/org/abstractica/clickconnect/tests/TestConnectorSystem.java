package org.abstractica.clickconnect.tests;

import org.abstractica.clickconnect.ConnectorSystem;
import org.abstractica.clickconnect.ConnectorSystemFactory;
import org.abstractica.clickconnect.ConnectorSystemInfo;
import org.abstractica.javacsg.Geometry3D;
import org.abstractica.javacsg.JavaCSG;
import org.abstractica.javacsg.JavaCSGFactory;

import java.util.ArrayList;
import java.util.List;

public class TestConnectorSystem
{
	private final JavaCSG csg;
	private final ConnectorSystemInfo csInfo;
	private final ConnectorSystem cs;

	protected TestConnectorSystem()
	{
		this.csg = JavaCSGFactory.createNoCaching();
		this.csInfo = getConnectorSystemInfo();
		this.cs = ConnectorSystemFactory.createDefault(csg).createConnectorSystem(csInfo);
	}

	private ConnectorSystemInfo getConnectorSystemInfo()
	{
		return ConnectorSystemInfo.get6x4dot5mm();
	}

	private Geometry3D lineUp(List<Geometry3D> list, double spacing)
	{
		Geometry3D res = list.get(0);
		for(int i = 1; i < list.size(); ++i)
		{
			res = csg.union3D(res, csg.translate3DX(i * spacing).transform(list.get(i)));
		}
		res = csg.translate3DX(-0.5 * (list.size() - 1) * spacing).transform(res);
		return res;
	}

	private Geometry3D getRoundConnector(boolean tension)
	{
		double yPos = 0;
		Geometry3D bottom = cs.getRoundAxleFlatHead(false);
		bottom = csg.rotate3DX(csg.degrees(180)).transform(bottom);
		bottom = csg.translate3DZ(cs.getFlatHeadHeight()).transform(bottom);
		yPos += cs.getFlatHeadHeight();
		Geometry3D middle = cs.getRoundAxle(5);
		middle = csg.translate3DZ(yPos).transform(middle);
		yPos += 5;
		Geometry3D top = cs.getRoundAxleConnector(false, tension);
		top = csg.translate3DZ(yPos).transform(top);
		Geometry3D res = csg.union3D(bottom, middle, top);
		res = csg.rotate3DX(csg.degrees(-90)).transform(res);
		res = csg.translate3DZ(0.5*csInfo.axleWidth()).transform(res);
		double length = cs.getFlatHeadHeight() + 5 + cs.getConnectorHight();
		res = csg.translate3DY(-0.5*length).transform(res);
		return res;
	}

	private Geometry3D getRectConnector(boolean tension)
	{
		double yPos = 0;
		Geometry3D bottom = cs.getRectAxleFlatHead(false);
		bottom = csg.rotate3DX(csg.degrees(180)).transform(bottom);
		bottom = csg.translate3DZ(cs.getFlatHeadHeight()).transform(bottom);
		yPos += cs.getFlatHeadHeight();
		Geometry3D middle = cs.getRectAxle(5);
		middle = csg.translate3DZ(yPos).transform(middle);
		yPos += 5;
		Geometry3D top = cs.getRectAxleConnector(false, tension);
		top = csg.translate3DZ(yPos).transform(top);
		Geometry3D res = csg.union3D(bottom, middle, top);
		res = csg.rotate3DX(csg.degrees(-90)).transform(res);
		res = csg.translate3DZ(0.5*csInfo.axleWidth()).transform(res);
		double length = cs.getFlatHeadHeight() + 5 + cs.getConnectorHight();
		res = csg.translate3DY(-0.5*length).transform(res);
		return res;
	}

	private Geometry3D roundAxleTest()
	{
		double plateThickness = 5;
		double plateSize = cs.getDiameter() + 6;
		List<Geometry3D> printList = new ArrayList<>();

		//Bottom plate
		Geometry3D bottomPlate = csg.box3D(plateSize, plateSize, cs.getFlatHeadHeight(), false);
		Geometry3D bottomHole = cs.getRoundAxleTurnFlatHeadHole();
		bottomPlate = csg.difference3D(bottomPlate, bottomHole);
		printList.add(bottomPlate);

		Geometry3D bottomPlate2 = csg.box3D(plateSize, plateSize, cs.getFlatHeadHeight(), false);
		Geometry3D bottomHole2 = cs.getRoundAxleLockFlatHeadHole();
		bottomPlate2 = csg.difference3D(bottomPlate2, bottomHole2);
		printList.add(bottomPlate2);

		//Middle plate
		Geometry3D middlePlate = csg.box3D(plateSize, plateSize, 5, false);
		Geometry3D middleHole = cs.getRoundAxleTurnHole(5);
		middlePlate = csg.difference3D(middlePlate, middleHole);
		printList.add(middlePlate);

		Geometry3D middlePlate2 = csg.box3D(plateSize, plateSize, 5, false);
		Geometry3D middleHole2 = cs.getRoundAxleLockHole(5);
		middlePlate2 = csg.difference3D(middlePlate2, middleHole2);
		printList.add(middlePlate2);

		//Top plate
		Geometry3D topPlate = csg.box3D(plateSize, plateSize, cs.getConnectorHight() + 1, false);
		Geometry3D topHole = cs.getRoundAxleTurnConnectorHole(false);
		topPlate = csg.difference3D(topPlate, topHole);
		topPlate = csg.rotate3DX(csg.degrees(180)).transform(topPlate);
		topPlate = csg.translate3DZ(cs.getConnectorHight()+1).transform(topPlate);
		printList.add(topPlate);

		Geometry3D topPlate2 = csg.box3D(plateSize, plateSize, cs.getConnectorHight() + 1, false);
		Geometry3D topHole2 = cs.getRoundAxleLockConnectorHole(false);
		topPlate2 = csg.difference3D(topPlate2, topHole2);
		topPlate2 = csg.rotate3DX(csg.degrees(180)).transform(topPlate2);
		topPlate2 = csg.translate3DZ(cs.getConnectorHight()+1).transform(topPlate2);
		printList.add(topPlate2);

		printList.add(getRoundConnector(false));
		printList.add(getRoundConnector(true));
		Geometry3D res = lineUp(printList, plateSize + 5);
		return res;
	}

	private Geometry3D rectAxleTest()
	{
		double plateSize = cs.getDiameter() + 6;
		List<Geometry3D> printList = new ArrayList<>();

		//Bottom plate
		Geometry3D bottomPlate = csg.box3D(plateSize, plateSize, cs.getFlatHeadHeight(), false);
		Geometry3D bottomHole = cs.getRectFlatHeadHole();
		bottomPlate = csg.difference3D(bottomPlate, bottomHole);
		printList.add(bottomPlate);

		//Middle plate
		Geometry3D middlePlate = csg.box3D(plateSize, plateSize, 5, false);
		Geometry3D middleHole = cs.getRectAxleHole(5);
		middlePlate = csg.difference3D(middlePlate, middleHole);
		printList.add(middlePlate);

		//Top plate
		Geometry3D topPlate = csg.box3D(plateSize, plateSize, cs.getConnectorHight() + 1, false);
		Geometry3D topHole = cs.getRectAxleConnectorHole(false);
		topPlate = csg.difference3D(topPlate, topHole);
		topPlate = csg.rotate3DX(csg.degrees(180)).transform(topPlate);
		topPlate = csg.translate3DZ(cs.getConnectorHight()+1).transform(topPlate);
		printList.add(topPlate);

		double length = cs.getFlatHeadHeight() + 5 + cs.getConnectorHight();
		printList.add(getRectConnector(false));
		printList.add(getRectConnector(true));
		Geometry3D res = lineUp(printList, plateSize + 5);
		return res;
	}

	public Geometry3D getPrintTest()
	{
		Geometry3D roundTest = roundAxleTest();
		roundTest = csg.translate3DY(0.5*(cs.getDiameter()+6+5)).transform(roundTest);
		Geometry3D rectTest = rectAxleTest();
		rectTest = csg.translate3DY(-0.5*(cs.getDiameter()+6+5)).transform(rectTest);
		Geometry3D res = csg.union3D(roundTest, rectTest);
		return res;
	}

	public static void main(String[] args)
	{
		TestConnectorSystem test = new TestConnectorSystem();
		Geometry3D printTest = test.getPrintTest();
		test.csg.view(printTest);
	}
}
