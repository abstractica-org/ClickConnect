package org.abstractica.clickconnect.impl;

import org.abstractica.clickconnect.RoundClickSystem;
import org.abstractica.javacsg.*;

import java.util.ArrayList;
import java.util.List;

public class RoundClicker implements RoundClickSystem
{
	private final JavaCSG csg;
	private final double unit;


	public RoundClicker(JavaCSG csg, double unit)
	{
		this.csg = csg;
		this.unit = unit;
	}

	public Geometry3D getClicker(double extraLength, boolean tight)
	{
		double delta = 0.1;
		double tightness = tight ? 0.4 : 0.0;
		List<Vector2D> vertices = new ArrayList<>();
		vertices.add(csg.vector2D(0, 0.2));
		vertices.add(csg.vector2D(0.4*unit-delta, 0.2));
		vertices.add(csg.vector2D(0.4*unit-delta, 0.1*unit+tightness));
		vertices.add(csg.vector2D(0.3*unit-delta, 0.2*unit+tightness));
		vertices.add(csg.vector2D(0.3*unit-delta, ((0.8+extraLength)*unit)-tightness-0.2)); //Tighten the grip
		vertices.add(csg.vector2D(0.4*unit-delta, ((0.9+extraLength)*unit)-tightness-0.2)); //Tighten the grip
		vertices.add(csg.vector2D(0.3*unit-delta, (1.0+extraLength) * unit - 0.2));
		vertices.add(csg.vector2D(0, (1.0+extraLength) * unit - 0.2));
		vertices.add(csg.vector2D(0, 0.2*unit-0.2));
		Geometry2D half = csg.polygon2D(vertices);
		Geometry3D full3D = csg.rotateExtrude(csg.degrees(360), 128, half);
		Geometry3D slit = csg.box3D(unit, 0.16*unit, unit, false);
		slit = csg.translate3DZ((0.2 + extraLength)*unit).transform(slit);
		full3D = csg.difference3D(full3D, slit);
		Geometry3D sides = csg.box3D(0.50*unit, unit, (1+extraLength)*unit, false);
		full3D = csg.intersection3D(full3D, sides);
		Geometry2D text = csg.text2D(tight ? "T" : "L", 0.4*unit, 64);
		text = csg.mirror2D(1, 0).transform(text);
		text = csg.translate2D(0.2*unit, -(0.47*0.5)*unit).transform(text);
		Geometry3D text3D = csg.linearExtrude(0.4, false, text);
		full3D = csg.difference3D(full3D, text3D);
		full3D = csg.rotate3DY(csg.degrees(90)).transform(full3D);
		return full3D;
	}

	public Geometry3D getRoundHoleCutout(double extraLength)
	{
		double delta = 0.1;
		List<Vector2D> vertices = new ArrayList<>();
		vertices.add(csg.vector2D(0, -0.2));
		vertices.add(csg.vector2D(0.4*unit+delta, -0.2));
		vertices.add(csg.vector2D(0.4*unit+delta, 0.1*unit+0.4));
		vertices.add(csg.vector2D(0.3*unit+delta, 0.2*unit+0.4));
		vertices.add(csg.vector2D(0.3*unit+delta, 0.3*unit-0.4));
		vertices.add(csg.vector2D(0.4*unit+delta, 0.4*unit-0.4));
		vertices.add(csg.vector2D(0.4*unit+delta, (0.5+extraLength)*unit+0.2));
		vertices.add(csg.vector2D(0, (0.5+extraLength)*unit+0.2));
		Geometry2D half = csg.polygon2D(vertices);
		Geometry3D full3D = csg.rotateExtrude(csg.degrees(360), 128, half);
		return full3D;
	}

	public Geometry3D getLockedHoleCutout(double extraLength)
	{
		Geometry3D hole = getRoundHoleCutout(extraLength);
		Geometry3D sides = csg.box3D(0.50*unit, unit, (1+extraLength) * unit + 0.4, false);
		sides = csg.translate3DZ(-0.2).transform(sides);
		Geometry3D res = csg.intersection3D(hole, sides);
		return res;
	}

	public static void main(String[] args)
	{
		JavaCSG csg = JavaCSGFactory.createDefault();

		/*
		RoundClicker clicker = new RoundClicker(csg, 10);
		Geometry3D res = clicker.getClicker(0, true);
		csg.view(res);
		 */

		/*
		RoundClicker clicker = new RoundClicker(csg, 10);
		Geometry3D hole = clicker.getRoundHoleCutout(0);
		Geometry3D plate = csg.box3D(10, 10, 5, false);
		//plate = csg.translate3DZ(-5).transform(plate);
		Geometry3D res = csg.difference3D(plate, hole);
		csg.view(res);
		 */


		RoundClicker clicker = new RoundClicker(csg, 10);
		Geometry3D hole = clicker.getLockedHoleCutout(0);
		Geometry3D plate = csg.box3D(10, 10, 5, false);
		//plate = csg.translate3DZ(-5).transform(plate);
		Geometry3D res = csg.difference3D(plate, hole);
		csg.view(res);

	}
}
