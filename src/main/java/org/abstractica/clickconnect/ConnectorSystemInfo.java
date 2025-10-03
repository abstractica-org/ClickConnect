package org.abstractica.clickconnect;

public record ConnectorSystemInfo
	(
		double diameter,
		double axleDiameterAdjust,
		double holeDiameterAdjust,
		double width,
		double axleWidthAdjust,
		double holeWidthAdjust,
		double connectorHeight,
		double connectorHeightAdjust,
		double connectorHoleHeightAdjust,
		double connectorBaseHeight,
		double connectorBaseSpacing,
		double barbSize,
		double barbTension,
		double slitWidth,
		double slitLength,
		double flatHeadDiameter,
		double flatHeadAxleDiameterAdjust,
		double flatHeadHoleDiameterAdjust,
		double flatHeadHeight,
		double flatHeadHeightAdjust,
		double flatHeadBaseHeight,
		double flatHeadBaseSpacing,
		int angularResolution
	)
{
	public double axleDiameter()
	{
		return diameter + axleDiameterAdjust;
	}

	public double axleWidth()
	{
		return width + axleWidthAdjust;
	}

	public double holeDiameter()
	{
		return diameter + holeDiameterAdjust;
	}

	public double holeWidth()
	{
		return width + holeWidthAdjust;
	}

	public double flatHeadAxleDiameter()
	{
		return flatHeadDiameter + flatHeadAxleDiameterAdjust;
	}

	public double flatHeadHoleDiameter()
	{
		return flatHeadDiameter + flatHeadHoleDiameterAdjust;
	}

	public ConnectorSystemInfo scale(double scale)
	{
		return scale(scale, angularResolution);
	}

	public ConnectorSystemInfo scale(double scale, int newAngularResolution)
	{
		return new ConnectorSystemInfo
			(
				diameter * scale,
				axleDiameterAdjust,
				holeDiameterAdjust,
				width * scale,
				axleWidthAdjust,
				holeWidthAdjust,
				connectorHeight * scale,
				connectorHeightAdjust,
				connectorHoleHeightAdjust,
				connectorBaseHeight * scale,
				connectorBaseSpacing,
				barbSize * scale,
				barbTension,
				slitWidth * scale,
				slitLength * scale,
				flatHeadDiameter * scale,
				flatHeadAxleDiameterAdjust,
				flatHeadHoleDiameterAdjust,
				flatHeadHeight * scale,
				flatHeadHeightAdjust,
				flatHeadBaseHeight * scale,
				flatHeadBaseSpacing,
				newAngularResolution
			);
	}

	public static ConnectorSystemInfo get4x3mm()
	{
		return new ConnectorSystemInfo
			(
				4,
				0,
				0.3,
				3,
				0,
				0.4,
				4,
				-0.1,
				0.1,
				1,
				0,
				0.3,
				0.2,
				0.8,
				3,
				6,
				0,
				0.3,
				2,
				-0.2,
				0.4,
				0,
				128
			);
	}

	public static ConnectorSystemInfo get6x4dot5mm()
	{
		return new ConnectorSystemInfo
			(
				6,
				0,
				0.3,
				4.5,
				0,
				0.4,
				6,
				-0.1,
				0.1,
				1,
				0,
				0.3,
				0.2,
				1.2,
				4.5,
				8,
				0,
				0.3,
				2,
				-0.2,
				0.4,
				0,
				128
			);
	}

	public static ConnectorSystemInfo get8x6mm()
	{
		return new ConnectorSystemInfo
			(
				8,
				0,
				0.3,
				6,
				0,
				0.4,
				8,
				-0.1,
				0.1,
				1,
				0,
				0.4,
				0.2,
				2,
				6,
				10,
				0,
				0.3,
				2,
				-0.2,
				0.4,
				0,
				128
			);
	}



}
