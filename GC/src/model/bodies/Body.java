package model.bodies;

public class Body {
	private Shape shape;
	private Material material;
	public Body(Shape shape, Material material) {
		this.shape = shape;
		this.material = material;
	}
	public Shape getShape() {
		return shape;
	}
	public Material getMaterial() {
		return material;
	}
}
