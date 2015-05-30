package scenes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.vecmath.Color3f;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import application.Main;
import util.Vectors;
import model.Body;
import model.SimpleBody;
import model.cameras.Camera;
import model.cameras.PinholeCamera;
import model.light.DirectionalLight;
import model.light.Light;
import model.light.PointLight;
import model.materials.Glass;
import model.materials.Material;
import model.materials.Matte;
import model.materials.Matte2;
import model.materials.Metal;
import model.materials.Mirror;
import model.shapes.BoundingBox;
import model.shapes.Mesh;
import model.shapes.Plane;
import model.shapes.Shape;
import model.shapes.sphere.SolidSphere;
import model.texture.ImageTexture;
import model.texture.PlainTexture;
import model.texture.Texture;
@SuppressWarnings("unchecked")
public class SceneFromFile implements Scene {
	
	Map<String,Material> materials;
	List<Light> lights;
	List<Body> bodies;
	Camera camera;
	int AASamples = 32;
	
	public SceneFromFile(String fileDir) throws FileNotFoundException{
		
		bodies = new ArrayList<Body>();
		lights = new ArrayList<Light>();
		materials = new HashMap<String,Material>();
		
		mainParser(fileDir);
		
	}
		
	public void mainParser(String fileDir) throws FileNotFoundException{
		Scanner in = new Scanner(new FileReader(fileDir));
				
		boolean inCamera = true;
		boolean inIncludes = false;
		
		Point3d position = new Point3d();
		Point3d lookAt = new Point3d(0,0,1);
		Vector3d up = new Vector3d(0,1,0);
		double fov = 90;
		int xRes = 800;
		int yRes = 600;
		
		String currentLine;
		
		Map<String,Object> arguments = new HashMap<String,Object>();
		
		String lightType = "";
				
		boolean inLight = false;
		
		while(in.hasNextLine()){
			currentLine = in.nextLine();
			if (currentLine.startsWith("#"))
				continue;
			
			if(inCamera){
				if(currentLine.startsWith("LookAt")){
					String[] args = currentLine.split(" ");
					position = new Point3d(Double.parseDouble(args[1]),Double.parseDouble(args[2]),Double.parseDouble(args[3]));
					lookAt = new Point3d(Double.parseDouble(args[4]),Double.parseDouble(args[5]),Double.parseDouble(args[6]));
					up = new Vector3d(Double.parseDouble(args[7]),Double.parseDouble(args[8]),Double.parseDouble(args[9]));
					
				}else if(currentLine.startsWith("\t\"float fov\"")){
					int firstBracket= currentLine.indexOf('[') + 1;
					int lastBracket= currentLine.lastIndexOf(']');
					fov = Double.parseDouble(currentLine.substring(firstBracket,lastBracket));
				}else if(currentLine.startsWith("\t\"integer xresolution\"")){
					int firstBracket= currentLine.indexOf('[') + 1;
					int lastBracket= currentLine.lastIndexOf(']');
					xRes = Integer.parseInt(currentLine.substring(firstBracket,lastBracket));
				}else if(currentLine.startsWith("\t\"integer yresolution\"")){
					int firstBracket= currentLine.indexOf('[') + 1;
					int lastBracket= currentLine.lastIndexOf(']');
					yRes = Integer.parseInt(currentLine.substring(firstBracket,lastBracket));
					camera = new PinholeCamera(position, lookAt, up, (fov/49.13)*0.035, xRes, yRes);

					inCamera = false;
					inIncludes = true;
				}
			}else if (inIncludes){
				
				if(currentLine.startsWith("Include")){
					
					int firstQuote= currentLine.indexOf('"') + 1;
					int lastQuote= currentLine.lastIndexOf('"');
					String FileDirection = currentLine.substring(firstQuote,lastQuote);
					
					int lastBracket= fileDir.lastIndexOf(File.separatorChar);
					String append = fileDir.substring(0,lastBracket);
					FileDirection = append + File.separatorChar + FileDirection;
					
					if(FileDirection.endsWith(".lxm"))
						materialParser(FileDirection);
					else
						geometryParser(FileDirection);
					
				}else if(currentLine.startsWith("AttributeBegin") || currentLine.startsWith("TransformBegin")){
					inLight = true;
					inIncludes = false;
				}
				
			}else{

				if(currentLine.startsWith("AttributeBegin") || currentLine.startsWith("TransformBegin")){
					inLight = true;
				}else if (currentLine.startsWith("AttributeEnd") || currentLine.startsWith("TransformEnd")){
					
					lights.add(getLightFromArguments(lightType,arguments));
					
					System.out.println("ADDED "+ lightType + " light");
					arguments.clear();
					lightType= "";
					inLight = false;
				}			
				
				if(inLight){
					if(currentLine.startsWith("LightSource")){
						
						int firstQuote= currentLine.indexOf('"') + 1;
						int lastQuote= currentLine.lastIndexOf('"');
						lightType = currentLine.substring(firstQuote,lastQuote);
						
					}else if(currentLine.startsWith("\t")){
			
						parseArgument(currentLine,arguments);
					
					}
				}
			}
		}
		
		in.close();
		return;	
	}
	
	public Light getLightFromArguments(String lightType, Map<String,Object> arguments){
		
		Light light = null;
		
		float intensity = ((List<Float>) arguments.get("float gain")).get(0);
		
		switch(lightType){
			case "point":
				//The color of the light.
				Color3f color = (Color3f) arguments.get("color L");
				
				//The location of the point light.
				List<Double> fromList = (List<Double>) arguments.get("point from");
				Point3d from = new Point3d(fromList.get(0),fromList.get(1),fromList.get(2));
				
				light = new PointLight(from, color, intensity);
				
				break;
			case "infinite":
				//The color of the light.
//				Color3f color2 = (Color3f) arguments.get("L");
				
				//light = new Infinite ? TODO
				
				break;
			case "distant":
				//The color of the light.	
				Color3f color3 = (Color3f) arguments.get("color L");

				//The two points defining the light direction. Default is down the z axis.
				List<Double> fromTo = (List<Double>) arguments.get("point from/to");
				Point3d from2 = new Point3d(fromTo.get(0),fromTo.get(1),fromTo.get(2));
				Point3d to = new Point3d(fromTo.get(3),fromTo.get(4),fromTo.get(5));
								
				light = new DirectionalLight(Vectors.sub(to, from2), color3, intensity);		
				
				break;
			default:
				System.out.println(lightType + "light not supported.");
				break;
		}
		
		return light;
	}
	
	public void geometryParser(String fileDir) throws FileNotFoundException{
		Scanner in = new Scanner(new FileReader(fileDir));
		
		in.nextLine();in.nextLine(); //clears "# Geometry File" and the next line
		
		Material material = null;
		
		Map<String,Object> arguments = new HashMap<String,Object>();
		
		Matrix4d transform = null;
		
		String shapeType = "";

		while(in.hasNextLine()){
			String currentLine = in.nextLine();
			
			if(currentLine.startsWith("Transform")){
				String[] values = currentLine.substring(11,currentLine.length()-1).split(" ");
					
				transform = new Matrix4d(	Double.parseDouble(values[0]),Double.parseDouble(values[1]),Double.parseDouble(values[2]),Double.parseDouble(values[3]),
												Double.parseDouble(values[4]),Double.parseDouble(values[5]),Double.parseDouble(values[6]),Double.parseDouble(values[7]),
												Double.parseDouble(values[8]),Double.parseDouble(values[9]),Double.parseDouble(values[10]),Double.parseDouble(values[11]),
												Double.parseDouble(values[12]),Double.parseDouble(values[13]),Double.parseDouble(values[14]),Double.parseDouble(values[15]));


			}else if(currentLine.startsWith("NamedMaterial")){
				
				int firstQuote= currentLine.indexOf('"') + 1;
				int lastQuote= currentLine.lastIndexOf('"');
				material = materials.get(currentLine.substring(firstQuote,lastQuote));
			
			}else if(currentLine.startsWith("Shape")){
				
				int firstQuote= currentLine.indexOf('"') + 1;
				int lastQuote= currentLine.lastIndexOf('"');
				shapeType = currentLine.substring(firstQuote,lastQuote);
				
			}else if(currentLine.startsWith("\t")){
				
				parseArgument(currentLine,arguments);
			
			}else if(currentLine.startsWith("AttributeEnd")){
		
				bodies.add(new SimpleBody(getShapeFromArguments(shapeType,arguments,transform),material));
				System.out.println("added "+ shapeType  + " with material "+ material);
				material = null;
				arguments.clear();
				shapeType="";
			}
			
		}
		
		in.close();
		return;	
	}
	
	public Shape getShapeFromArguments(String shapeType, Map<String,Object> arguments, Matrix4d transform){
		
		Shape shape = null;
		
		switch(shapeType){
		case "mesh":
			List<Integer> triindices = (List<Integer>)arguments.get("integer triindices");
			List<Double> P = (List<Double>)arguments.get("point P");
			List<Double> N = (List<Double>)arguments.get("normal N");
			
			List<Float> UVs = null;
			if(arguments.containsKey("float uv")){
				 UVs = (List<Float>) arguments.get("float uv");
			}
			
			shape = new Mesh(transform,triindices,P,N,UVs);
			break;
		case "plane":
			//Vector3 de normales
			List<Double> N2 = (List<Double>)arguments.get("normal N");
			Vector3d normal = new Vector3d(N2.get(0), N2.get(1), N2.get(2));
			shape = new Plane(normal, new Point3d(0,0,0));
			shape.transform(transform);
			break;
		case "box":
			float width = ((List<Float>) arguments.get("float width")).get(0);
			float height = ((List<Float>) arguments.get("float height")).get(0);
			float depth = ((List<Float>) arguments.get("float depth")).get(0);
			shape = new BoundingBox(-width/2, +width/2, -height/2, +height/2, -depth/2, +depth/2);
			shape.transform(transform);
			break;
		case "sphere":
			float radius = ((List<Float>) arguments.get("float radius")).get(0);
			
			shape = new SolidSphere(new Point3d(0,0,0), radius);
			shape.transform(transform);
			
			break;
		default:
			System.out.println(shapeType + " shape not supported.");
		}
		
		return shape;
	}
	
	public void materialParser(String fileDir) throws FileNotFoundException{
		Scanner in = new Scanner(new FileReader(fileDir));
		
		in.nextLine();in.nextLine(); //clears "# Materials File" and the next line
		
		Map<String,Object> arguments = new HashMap<String,Object>();
		String currentMaterialName = "";
	
		String currentTextureName = "";
		
		Map<String,Texture> textures = new HashMap<String,Texture>();
		
		while(in.hasNextLine()){
			String currentLine = in.nextLine();
			if (currentLine.startsWith("#"))
				continue;
					
			if(currentLine.startsWith("MakeNamedMaterial")){
				
				int firstQuote= currentLine.indexOf('"') + 1;
				int lastQuote= currentLine.lastIndexOf('"');
				currentMaterialName = currentLine.substring(firstQuote,lastQuote);
			
			}else if(currentLine.startsWith("\t")){
				
				parseArgument(currentLine,arguments);
				
			} else if(currentLine.startsWith("Texture")){
				currentTextureName = currentLine.split("\"")[1];
			} else if( currentLine.equals("")){
				if(currentMaterialName.equals("")){
					textures.put(currentTextureName, getTextureFromArguments(arguments));
					System.out.println("ADDED TEXTURE" + currentTextureName);
				}else{
					if(currentTextureName.equals("")){
						materials.put(currentMaterialName, getMaterialFromArguments(arguments,null));
					}else{
						materials.put(currentMaterialName, getMaterialFromArguments(arguments,textures));
					}
					System.out.println("ADDED MATERIAL " + currentMaterialName);

					currentMaterialName = "";
					currentTextureName = "";
				}
				
				arguments.clear();
			}
					
		}
		
		//Scanner closes before last empty line

		if(currentTextureName.equals("")){
			materials.put(currentMaterialName, getMaterialFromArguments(arguments,null));
		}else{
			materials.put(currentMaterialName, getMaterialFromArguments(arguments,textures));
		}
		System.out.println("ADDED MATERIAL " + currentMaterialName);
		
		in.close();
		return;	
	}
		
	public Texture getTextureFromArguments(Map<String,Object> arguments){
		if(arguments != null){
			if(arguments.containsKey("string filename")){
				return new ImageTexture("charizard/imagen.jpg");
//				return new ImageTexture( (String) arguments.get("string filename"));
			}			
		}
		return new PlainTexture(new Color3f());
	}
	
	public Material getMaterialFromArguments(Map<String,Object> arguments, Map<String,Texture> textures){
		
		Material material = null;
				
		Texture texture = null;
		if(arguments.containsKey("texture Kd"))
			texture = textures.get((String)arguments.get("texture Kd"));
		
		String type = (String)arguments.get("string type");
		switch(type){
			case "matte":
				
				Color3f Kd = new Color3f(1,1,1);
				if(arguments.containsKey("color Kd"))
					Kd = (Color3f)arguments.get("color Kd");
				
				float sigma = ((List<Float>) arguments.get("float sigma")).get(0);
				material = new Matte2(texture, sigma, Kd.x); //TODO
				break;
			case "mirror":
				Color3f _Kr = (Color3f)arguments.get("color Kr");
				material = new Mirror(_Kr.x); //TODO
				break;
			case "glass":
				Color3f Kr = (Color3f)arguments.get("color Kr");
				Color3f Kt = (Color3f)arguments.get("color Kt");
				float index = ((List<Float>) arguments.get("float index")).get(0);
				material = new Glass(Kr.x, Kt.x, texture, index);
				break;
			case "metal2":
				float uroughness = ((List<Float>) arguments.get("float uroughness")).get(0);
				material = new Metal(texture,uroughness);
				break;
			default:
				material = new Matte2(null,0.31,0.65);
				break;
		}
		
		return material;
		
	}	
	
	public void parseArgument(String line, Map<String,Object> arguments){
		String[] splitedLine = line.split("\"");
		
		String ArgumentType = splitedLine[1].split(" ")[0];
		String ArgumentName = splitedLine[1].split(" ")[1];
		
		String ArgumentValue = line.substring(ArgumentType.length() + ArgumentName.length() + 6,line.length()-1);
		
		//Fix for Kd being a color and also a texture
		ArgumentName = splitedLine[1];
		
		switch(ArgumentType){
			case "bool":
				
				if(ArgumentValue.equals("true"))
					arguments.put(ArgumentName, true);
				else
					arguments.put(ArgumentName, false);
				break;
				
			case "float":
				List<Float> doubles = new ArrayList<Float>();
				for(String s: ArgumentValue.split(" "))
					doubles.add(Float.parseFloat(s));
				
				arguments.put(ArgumentName,doubles);
				break;
				
			case "string":
				
				arguments.put(ArgumentName,ArgumentValue.substring(1, ArgumentValue.length()-1));
				break;
				
			case "color":
				float[] floats = new float[3];
				int i = 0;
				for(String s: ArgumentValue.split(" "))
					floats[i++] = Float.parseFloat(s);
				
				Color3f color = new Color3f(floats[0],floats[1],floats[2]);
				
				arguments.put(ArgumentName,color);

				break;
			case "integer":
				List<Integer> ints = new ArrayList<Integer>();
				for(String s: ArgumentValue.split(" "))
					ints.add(Integer.parseInt(s));
				
				arguments.put(ArgumentName,ints);

				break;
			case "point":
				List<Double> doubles1 = new ArrayList<Double>();
				for(String s: ArgumentValue.split(" "))
					doubles1.add(Double.parseDouble(s));
				
				arguments.put(ArgumentName,doubles1);
				break;
			case "normal":
				List<Double> doubles2 = new ArrayList<Double>();
				for(String s: ArgumentValue.split(" "))
					doubles2.add(Double.parseDouble(s));
				
				arguments.put(ArgumentName,doubles2);
				break;
			case "texture":
				arguments.put(ArgumentName,ArgumentValue.substring(1, ArgumentValue.length()-1));
				break;
			default:
				System.out.println(ArgumentType + " type not supported.");
				break;
		}
	}

	@Override
	public List<Light> getLights() {
		return lights;
	}

	@Override
	public List<Body> getObjects() {
		return bodies;
	}

	@Override
	public Camera getCamera() {
		return camera;
	}
	
}
