package org.abstractica.clickconnect.experiments.hclicker;

import org.abstractica.javacsg.*;

import java.util.ArrayList;
import java.util.List;

public class HClicker
{
	private final JavaCSG csg;
	private final double unit;
	private final double slit;
	private double tension = 0.3;
	private double delta = 0.1;


	public HClicker(JavaCSG csg, double unit, double slit)
	{
		this.csg = csg;
		this.unit = unit;
		this.slit = slit;
	}

	public Geometry3D getClicker(double lengthA, double lengthB, double width)
	{
		if(lengthA < 10.0 || lengthB < 10.0) throw new IllegalArgumentException("Lengths must be at least 10.0");
		double extraLengthA = lengthA - 10.0;
		double extraLengthB = lengthB - 10.0;
		Geometry2D halfClickerA2D = getHalfClicker2D(extraLengthA);
		Geometry2D halfClickerB2D = getHalfClicker2D(extraLengthB);
		Geometry2D fullClicker2D = csg.union2D(halfClickerA2D, csg.mirror2D(0,1).transform(halfClickerB2D));
		Geometry3D clicker = csg.linearExtrude(width*unit-2*delta, true, fullClicker2D);
		return clicker;
	}

	public Geometry3D getHalfClicker(double length, double width)
	{
		if(length < 8.0) throw new IllegalArgumentException("Length must be at least 8.0");
		double extraLength = length - 8.0;
		Geometry2D halfClicker2D = getHalfClicker2D(extraLength);
		Geometry3D halfClicker = csg.linearExtrude(width*unit-2*delta, false, halfClicker2D);
		return halfClicker;
	}

	public Geometry3D getHole(double length, double width)
	{
		if(length < 10.0) throw new IllegalArgumentException("Lengths must be at least 8.0");
		double extraLength = length - 10.0;
		Geometry2D hole2D = getHole2D(extraLength);
		Geometry3D hole = csg.linearExtrude(width*unit+2*delta, true, hole2D);
		hole = csg.rotate3DX(csg.degrees(90)).transform(hole);
		return hole;
	}


	private Geometry2D getHalfClicker2D(double extraLength)
	{
		List<Vector2D> vertices = new ArrayList<>();
		vertices.add(csg.vector2D(0, 0));
		vertices.add(csg.vector2D(6*unit-delta, 0));
		vertices.add(csg.vector2D(6*unit-delta, 2*unit));
		vertices.add(csg.vector2D(5*unit-delta, 3*unit));
		vertices.add(csg.vector2D(5*unit-delta, (7+extraLength)*unit));
		vertices.add(csg.vector2D((6+tension)*unit-delta, (8+extraLength)*unit));
		vertices.add(csg.vector2D(5*unit-delta, (10+extraLength)*unit));
		vertices.add(csg.vector2D(0.5*slit*unit, (10+extraLength)*unit));
		vertices.add(csg.vector2D(0.5*slit*unit, (2+extraLength)*unit));
		vertices.add(csg.vector2D(0, (2+extraLength)*unit));
		Geometry2D quad2D = csg.polygon2D(vertices);
		Geometry2D half2D = csg.union2D(quad2D, csg.mirror2D(1,0).transform(quad2D));
		return half2D;
	}

	private Geometry2D getHole2D(double extraLength)
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
		Geometry2D half2D = csg.union2D(quad2D, csg.mirror2D(1,0).transform(quad2D));
		return half2D;
	}

	public static void main(String[] args)
	{
		double unit = 0.4;
		double slit = 3;
		double width = 8;
		double lengthA = 10;
		double lengthB = 20;
		JavaCSG csg = JavaCSGFactory.createDefault();
		HClicker hCLicker = new HClicker(csg, unit, slit);
		Geometry3D clicker = hCLicker.getClicker(lengthA, lengthB, width);
		clicker = csg.translate3DZ(width*0.5*unit).transform(clicker);
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
	}
}
