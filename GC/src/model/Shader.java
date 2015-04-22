package model;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.light.Light;

public interface Shader {
	public Color3f shade(Point3d p, Vector3d n, Vector3d v, Light[] lights, Body[] bodies, Color3f baseColor);
}
