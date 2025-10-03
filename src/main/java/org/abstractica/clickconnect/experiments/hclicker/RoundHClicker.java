package org.abstractica.clickconnect.experiments.hclicker;

import org.abstractica.javacsg.*;

import java.util.ArrayList;
import java.util.List;

public class RoundHClicker
{
	private final JavaCSG csg;
	private final double unit;
	private final double slit;
	private double delta = 0.1;

	public RoundHClicker(JavaCSG csg, double unit, double slit)
	{
		this.csg = csg;
		this.unit = unit;
		this.slit = slit;
	}

	public double getWidth()
	{
		return 8*unit;
	}

	public Geometry3D getClicker(double lengthA, double tensionA, double extraSpaceA, double lengthB, double tensionB, double extraSpaceB)
	{
		if(lengthA < 10.0 || lengthB < 10.0) throw new IllegalArgumentException("Lengths must be at least 10.0");

		double extraLengthA = lengthA - 10.0;
		double extraLengthB = lengthB - 10.0;
		Geometry3D halfClickerA = getHalfClicker(extraLengthA, tensionA, extraSpaceA);
		Geometry3D halfClickerB = getHalfClicker(extraLengthB, tensionB, extraSpaceB);
		halfClickerB = csg.mirror3D(0,1,0).transform(halfClickerB);
		Geometry3D clicker = csg.union3D(halfClickerA, halfClickerB);
		clicker = csg.translate3DZ(4*unit).transform(clicker);
		return clicker;
	}

	public Geometry3D getRoundHole(double length)
	{
		if(length < 10.0) throw new IllegalArgumentException("Lengths must be at least 10.0");
		double extraLength = length - 10.0;
		Geometry2D hole2D = getQuadHole2D(extraLength);
		Geometry3D hole = csg.rotateExtrude(csg.degrees(360), 128, hole2D);
		return hole;
	}

	public Geometry3D getHalfClicker(double extraLength, double tension, double extraSpace)
	{
		Geometry2D quad2D = getQuadClicker2D(extraLength, tension, extraSpace);
		Geometry3D half3D = csg.rotateExtrude(csg.degrees(360), 128, quad2D);
		Geometry3D slitBox = csg.box3D(slit*unit, 20*unit, 10*unit, false);
		slitBox = csg.translate3DZ((2+extraLength)*unit).transform(slitBox);
		half3D = csg.difference3D(half3D, slitBox);
		Geometry3D sidesBox = csg.box3D(20*unit, 8*unit, (12+extraLength)*unit, false);
		half3D = csg.intersection3D(half3D, sidesBox);
		half3D = csg.rotate3DX(csg.degrees(-90)).transform(half3D);
		return half3D;
	}

	private Geometry2D getQuadClicker2D(double extraLength, double tension, double extraSpace)
	{
		List<Vector2D> vertices = new ArrayList<>();
		vertices.add(csg.vector2D(0, 0));
		vertices.add(csg.vector2D(6*unit-delta, 0));
		vertices.add(csg.vector2D(6*unit-delta, 2*unit+extraSpace));
		vertices.add(csg.vector2D(5*unit-delta, 3*unit+extraSpace));
		vertices.add(csg.vector2D(5*unit-delta, (7+extraLength)*unit+extraSpace));
		vertices.add(csg.vector2D(6*unit+tension-delta, (8+extraLength)*unit+extraSpace));
		vertices.add(csg.vector2D(5*unit-delta, (10+extraLength)*unit+extraSpace));
		vertices.add(csg.vector2D(0, (10+extraLength)*unit+extraSpace));
		Geometry2D quad2D = csg.polygon2D(vertices);
		return quad2D;
	}

	private Geometry2D getQuadHole2D(double extraLength)
	{
		List<Vector2D> vertices = new ArrayList<>();
		vertices.add(csg.vector2D(0, 0));
		vertices.add(csg.vector2D(6*unit+delta, 0));
		vertices.add(csg.vector2D(6*unit+delta, 2*unit));
		vertices.add(csg.vector2D(5*unit+delta, 3*unit));
		vertices.add(csg.vector2D(5*unit+delta, (7+extraLength)*unit));
		vertices.add(csg.vector2D(6*unit+delta, (8+extraLength)*unit));
		vertices.add(csg.vector2D(6*unit+delta, (10+extraLength)*unit));
		vertices.add(csg.vector2D(0, (10+extraLength)*unit));
		Geometry2D quad2D = csg.polygon2D(vertices);
		return quad2D;
	}

	public static void main(String[] args)
	{
		double unit = 0.8;
		double lengthA = 10;
		double lengthB = 20;
		JavaCSG csg = JavaCSGFactory.createDefault();
		RoundHClicker hCLicker = new RoundHClicker(csg, unit, 4);
		Geometry3D clicker = hCLicker.getClicker(lengthA, 0.0, 0.2, lengthB, 0.2, 0.0);
		Geometry3D holeA = hCLicker.getRoundHole(lengthA);
		holeA = csg.translate3DZ(1).transform(holeA);
		Geometry3D plateA = csg.box3D(unit*16, unit*16, lengthA*unit+1, false);
		Geometry3D holeB = hCLicker.getRoundHole(lengthB);
		plateA = csg.difference3D(plateA, holeA);
		holeB = csg.translate3DZ(1).transform(holeB);
		Geometry3D plateB = csg.box3D(unit*16, unit*16, lengthB*unit+1, false);
		plateB = csg.difference3D(plateB, holeB);
		Geometry3D scene = csg.union3D(clicker, csg.translate3DX(unit*20+5).transform(plateA), csg.translate3DX(unit*40+10).transform(plateB));
		csg.view(scene);
		/*
		double unit = 1.0;
		double width = 8;
		double lengthA = 8;
		double lengthB = 16;
		JavaCSG csg = JavaCSGFactory.createDefault();
		RoundHClicker hCLicker = new RoundHClicker(csg, unit);
		Geometry3D clicker = hCLicker.getClicker(lengthA,lengthB, width);
		Geometry3D holeA = hCLicker.getHole(lengthA, width);
		holeA = csg.translate3DZ(1).transform(holeA);
		Geometry3D plateA = csg.box3D(unit*20, (width+4) * unit, lengthA*unit+1, false);
		plateA = csg.difference3D(plateA, holeA);
		Geometry3D holeB = hCLicker.getHole(lengthB, width);
		holeB = csg.translate3DZ(1).transform(holeB);
		Geometry3D plateB = csg.box3D(unit*20, (width+4) * unit, lengthB*unit+1, false);
		plateB = csg.difference3D(plateB, holeB);
		Geometry3D scene = csg.union3D(clicker, csg.translate3DX(unit*20+5).transform(plateA), csg.translate3DX(unit*40+10).transform(plateB));
		csg.view(scene);
		*/
	}
}
