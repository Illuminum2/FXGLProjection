package at.htl.fxglprojection.renderer;

import java.util.List;

import at.htl.fxglprojection.objects.Polygon3D;

public record ProjectedPolygon(Polygon3D source, List<Double> points, Double depth) { }