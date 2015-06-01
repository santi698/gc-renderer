package model.trees;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.Ray;
import model.shapes.BoundingBox;

public class KDNode extends BoundingBox {

	public static final double TOLERANCE = 1e-12;
	
	private static final int MAX_OBJECTS_PER_NODE = 8;
	private static final int MAX_OCTREE_DEPTH = 16;

	private KDNode parent, child[];
	private Traceable[] shapes;
	private double midx, midy, midz;
	private int depth;

	public KDNode(BoundingBox bbox, Traceable[] shapesArg, BoundingBox[] boxes) {
			this(bbox, shapesArg, boxes, null, 0);
	}

	public KDNode(BoundingBox bbox, Traceable[] shapesArg, BoundingBox[] boxes, KDNode parentNode, int nodeDepth){
		super(bbox);

		boolean inside[] = new boolean[shapesArg.length];
		
		int count, i;

		parent = parentNode;
		depth = nodeDepth;
		
		// Encontrar los objetos contenidos en este nodo
		for (i = 0, count = 0; i < shapesArg.length; i++) {
			if (boxes[i].intersects(this))
				if (shapesArg[i].intersectsBox(this)) {
					inside[i] = true;
					count++;
				}
		}

		// Contruir la lista de objetos de este nodo y sus bb
		shapes = new Traceable[count];
		if (count == 0)
			return;
		BoundingBox objBounds[] = new BoundingBox[count];
		for (i = 0, count = 0; i < shapesArg.length; i++) {
			if (inside[i]) {
				shapes[count] = shapesArg[i];
				objBounds[count++] = boxes[i];
			}
		}

		// Ramificar este nodo si corresponde
		split(objBounds);
	}
	
	/**
	 * Ramifica el nodo actual si no tiene muchos obtejos y el octree todavía
	 * nos es muy profundo. En ese caso, construye hasta 8 hijos según las
	 * BoundingBoxes de sus objetos y marca el nodo como no terminal.
	 */
	private void split(BoundingBox objBounds[]) {
		if (shapes.length <= MAX_OBJECTS_PER_NODE || depth >= MAX_OCTREE_DEPTH) {
			return;
		}

		boolean splitx, splity, splitz;
		findMidpoints(objBounds);
		splitx = (midx != x1);
		splity = (midy != y1);
		splitz = (midz != z1);
		if (!(splitx || splity || splitz))
			return;

		child = new KDNode[8];
		int d = depth + 1;
		// near SW
		BoundingBox bb = new BoundingBox(x0, midx, y0, midy, z0, midz);
		child[0] = new KDNode(bb, shapes, objBounds, this, d);
		if (splitz) { // far SW
			bb = new BoundingBox(x0, midx, y0, midy, z1, midz);
			child[1] = new KDNode(bb, shapes, objBounds, this, d);
		}
		if (splity) { // near NW
			bb = new BoundingBox(x0, midx, y1, midy, z0, midz);
			child[2] = new KDNode(bb, shapes, objBounds, this, d);
			if (splitz) { // far NW
				bb = new BoundingBox(x0, midx, y1, midy, z1, midz);
				child[3] = new KDNode(bb, shapes, objBounds, this, d);
			}
		}
		if (splitx) { // near SE
			bb = new BoundingBox(x1, midx, y0, midy, z0, midz);
			child[4] = new KDNode(bb, shapes, objBounds, this, d);
			if (splitz) { // far SE
				bb = new BoundingBox(x1, midx, y0, midy, z1, midz);
				child[5] = new KDNode(bb, shapes, objBounds, this, d);
			}
			if (splity) { // near NE
				bb = new BoundingBox(x1, midx, y1, midy, z0, midz);
				child[6] = new KDNode(bb, shapes, objBounds, this, d);
				if (splitz) { // far NE
					bb = new BoundingBox(x0, midx, y1, midy, z1, midz);
					child[7] = new KDNode(bb, shapes, objBounds, this, d);
				}
			}
		}
		shapes = null; // Marcar como nodo de ramificación (no terminal)
	}
	
	private void findMidpoints(BoundingBox objBounds[]) {
		// TODO: La eficiencia del octree se puede mejorar si estos puntos
		// medios no se setean por la mitad de la BoundingBox sino en planos
		// otros planos tomados convenientemente.
		Vector3d size = getSize();
		midx = size.x / 2;
		midy = size.y / 2;
		midz = size.z / 2;
	}
	
	public Traceable[] getObjects() {
		if (shapes == null)
			throw new RuntimeException("KDNode.getObjects "
					+ "invocado sobre un nodo no terminal.");
		return shapes;
	}
	
	public KDNode findNextNode(Ray r) {
		double t1, t2, tmax = Double.MAX_VALUE;
		Point3d orig = r.getOrigin();
		Vector3d dir = r.getDirection();
		KDNode current;

		if (parent == null)
			return null;

		// Buscar el último punto por donde pasa el rayo dentro de este nodo.
		if (dir.x != 0.0) {
			t1 = (x0 - orig.x) / dir.x;
			t2 = (x1 - orig.x) / dir.x;
			if (t1 < t2) {
				if (t2 < tmax)
					tmax = t2;
			} else {
				if (t1 < tmax)
					tmax = t1;
			}
		}
		if (dir.y != 0.0) {
			t1 = (y0 - orig.y) / dir.y;
			t2 = (y1 - orig.y) / dir.y;
			if (t1 < t2) {
				if (t2 < tmax)
					tmax = t2;
			} else {
				if (t1 < tmax)
					tmax = t1;
			}
		}
		if (dir.z != 0.0) {
			t1 = (z0 - orig.z) / dir.z;
			t2 = (z1 - orig.z) / dir.z;
			if (t1 < t2) {
				if (t2 < tmax)
					tmax = t2;
			} else {
				if (t1 < tmax)
					tmax = t1;
			}
		}

		// Meterlo dentro del nodo
		Vector3d nextPos = new Vector3d(orig.x + dir.x * tmax, orig.y + dir.y
				* tmax, orig.z + dir.z * tmax);
		nextPos.x += (dir.x > 0.0 ? KDNode.TOLERANCE
				: -KDNode.TOLERANCE);
		nextPos.y += (dir.y > 0.0 ? KDNode.TOLERANCE
				: -KDNode.TOLERANCE);
		nextPos.z += (dir.z > 0.0 ? KDNode.TOLERANCE
				: -KDNode.TOLERANCE);

		// Subir por el octree hasta encontrar un nodo que lo contenga
		current = parent;
		while (!current.contains( new Point3d(nextPos))) {
			current = current.parent;
			if (current == null)
				return null;
		}

		// Ahora volver a bajar por el octree hasta encontrar un nodo terminal
		while (current.shapes == null) {
			if (nextPos.x > current.midx) {
				if (nextPos.y > current.midy) {
					if (nextPos.z > current.midz)
						current = current.child[7];
					else
						current = current.child[6];
				} else {
					if (nextPos.z > current.midz)
						current = current.child[5];
					else
						current = current.child[4];
				}
			} else {
				if (nextPos.y > current.midy) {
					if (nextPos.z > current.midz)
						current = current.child[3];
					else
						current = current.child[2];
				} else {
					if (nextPos.z > current.midz)
						current = current.child[1];
					else
						current = current.child[0];
				}
			}
		}
		return current;
	}
	
	public KDNode findFirstNode(Ray r) {
		double t1, t2, tmin = -Double.MAX_VALUE, tmax = Double.MAX_VALUE;
		Point3d orig = r.getOrigin();
		Vector3d dir = r.getDirection();

		// Encontrar (si existe) el punto donde el rayo ingresa al nodo
		if (dir.x == 0.0) {
			if (orig.x < x0 || orig.x > x1)
				return null;
		} else {
			t1 = (x0 - orig.x) / dir.x;
			t2 = (x1 - orig.x) / dir.x;
			if (t1 < t2) {
				if (t1 > tmin)
					tmin = t1;
				if (t2 < tmax)
					tmax = t2;
			} else {
				if (t2 > tmin)
					tmin = t2;
				if (t1 < tmax)
					tmax = t1;
			}
			if (tmin > tmax || tmax < 0.0)
				return null;
		}
		if (dir.y == 0.0) {
			if (orig.y < y0 || orig.y > y1)
				return null;
		} else {
			t1 = (y0 - orig.y) / dir.y;
			t2 = (y1 - orig.y) / dir.y;
			if (t1 < t2) {
				if (t1 > tmin)
					tmin = t1;
				if (t2 < tmax)
					tmax = t2;
			} else {
				if (t2 > tmin)
					tmin = t2;
				if (t1 < tmax)
					tmax = t1;
			}
			if (tmin > tmax || tmax < 0.0)
				return null;
		}
		if (dir.z == 0.0) {
			if (orig.z < z0 || orig.z > z1)
				return null;
		} else {
			t1 = (z0 - orig.z) / dir.z;
			t2 = (z1 - orig.z) / dir.z;
			if (t1 < t2) {
				if (t1 > tmin)
					tmin = t1;
				if (t2 < tmax)
					tmax = t2;
			} else {
				if (t2 > tmin)
					tmin = t2;
				if (t1 < tmax)
					tmax = t1;
			}
			if (tmin > tmax || tmax < 0.0)
				return null;
		}

		// Meterlo dentro del nodo
		tmin += KDNode.TOLERANCE;
		Vector3d nextPos = new Vector3d(orig.x + dir.x * tmin, orig.y + dir.y
				* tmin, orig.z + dir.z * tmin);

		// Devolver el nodo terminal que contiene el punto
		return findNode(nextPos);
	}
	
	public KDNode findNode(Vector3d pos) {
		KDNode current;

		if (!contains(new Point3d(pos)))
			return null;
		
		current = this;
		while (current.shapes == null) {
			if (pos.x > current.midx) {
				if (pos.y > current.midy) {
					if (pos.z > current.midz)
						current = current.child[7];
					else
						current = current.child[6];
				} else {
					if (pos.z > current.midz)
						current = current.child[5];
					else
						current = current.child[4];
				}
			} else {
				if (pos.y > current.midy) {
					if (pos.z > current.midz)
						current = current.child[3];
					else
						current = current.child[2];
				} else {
					if (pos.z > current.midz)
						current = current.child[1];
					else
						current = current.child[0];
				}
			}
		}
		return current;
	}

}