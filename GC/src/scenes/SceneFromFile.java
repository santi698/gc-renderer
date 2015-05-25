package scenes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.Body;
import model.SimpleBody;
import model.cameras.Camera;
import model.light.Light;
import model.light.PointLight;
import model.materials.Material;
import model.shapes.Mesh;
import model.shapes.Shape;
import model.texture.ImageTexture;
import model.texture.Texture;
public class SceneFromFile /* extends Scene*/ {
	
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
		
		Point3d position = null;
		Point3d lookAt = null;
		Point3d up = null;
		double fov = 0;
		int xRes = 0;
		int yRes = 0;
		
		String currentLine;
		
		Map<String,Object> arguments = new HashMap<String,Object>();
		
		String lightType = "";
				
		boolean inLight = false;
		
		while(in.hasNextLine()){
			currentLine = in.nextLine();
			
			if(inCamera){
				if(currentLine.startsWith("LookAt")){
					String[] args = currentLine.split(" ");
					position = new Point3d(Double.parseDouble(args[1]),Double.parseDouble(args[2]),Double.parseDouble(args[3]));
					lookAt = new Point3d(Double.parseDouble(args[4]),Double.parseDouble(args[5]),Double.parseDouble(args[6]));
					up = new Point3d(Double.parseDouble(args[7]),Double.parseDouble(args[8]),Double.parseDouble(args[9]));
					
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
					
					//camera = new PinholeCamera();

					inCamera = false;
					inIncludes = true;
				}
			}else if (inIncludes){
				
				if(currentLine.startsWith("Include")){
					int firstQuote= currentLine.indexOf('"') + 1;
					int lastQuote= currentLine.lastIndexOf('"');
					String FileDirection = currentLine.substring(firstQuote,lastQuote);
					
					if(FileDirection.endsWith(".lxm\""))
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
					arguments.clear();
					lightType= "";
					inLight = false;
				}			
				
				if(inLight){
					if(currentLine.startsWith("AreaLightSource")){
						
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
		
		float intensity = ((List<Float>) arguments.get("gain")).get(0);
		
		switch(lightType){
			case "point":
				//The color of the light.
				Color3f color = (Color3f) arguments.get("I");
				
				//The location of the point light.
				List<Double> fromList = (List<Double>) arguments.get("from/to");
				Point3d from = new Point3d(fromList.get(0),fromList.get(1),fromList.get(2));
				
				light = new PointLight(from, color, intensity);
				
				break;
			case "infinite":
				//The color of the light.
				Color3f color2 = (Color3f) arguments.get("L");
				
				//The suggested number of shadow samples when computing illumination from the given light.
				int nsamples = ((List<Integer>) arguments.get("nsamples")).get(0);
				
				//light = new Infinite ?
				
				break;
			case "distant":
				//The color of the light.	
				Color3f color3 = (Color3f) arguments.get("L");

				//The two points defining the light direction. Default is down the z axis.
				List<Double> fromTo = (List<Double>) arguments.get("from/to");
				Point3d from2 = new Point3d(fromTo.get(0),fromTo.get(1),fromTo.get(2));
				Point3d to = new Point3d(fromTo.get(3),fromTo.get(4),fromTo.get(5));
				
				//Half angle of the light source in radians. Must be > 0 for soft shadows
				float theta = ((List<Float>)  arguments.get("theta")).get(0);
				
				//light = new Distant ?		
				
				break;
			default:
				System.out.println(lightType + " not supported.");
				break;
		}
		
		return light;
	}
	
	public void geometryParser(String fileDir) throws FileNotFoundException{
		Scanner in = new Scanner(new FileReader(fileDir));
		
		in.nextLine();in.nextLine(); //clears "# Geometry File" and the next line
		
		Material material = null;
		
		Map<String,Object> arguments = new HashMap<String,Object>();
		
		Vector3d position = null;
		Vector3d rotation = null;
		double scale = 0.0d;
		
		String shapeType = "";

		while(in.hasNextLine()){
			String currentLine = in.nextLine();
			
			if(currentLine.startsWith("Transform")){
				String[] values = currentLine.substring(11,currentLine.length()-1).split(" ");
								
				scale = Double.parseDouble(values[0]);
				//rotation = ;
				position = new Vector3d(Double.parseDouble(values[12]),Double.parseDouble(values[13]),Double.parseDouble(values[14]));

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
		
				bodies.add(new SimpleBody(getShapeFromArguments(shapeType,arguments,position,rotation,scale),material));
				System.out.println("added "+ shapeType  + " with material "+ material);
				material = null;
				arguments.clear();
				shapeType="";
			}
			
		}
		
		in.close();
		return;	
	}
	
	public Shape getShapeFromArguments(String shapeType, Map<String,Object> arguments, Vector3d position, Vector3d rotation, double scale){
		
		Shape shape = null;
		
		switch(shapeType){
		case "mesh":
			List<Integer> triindices = (List<Integer>)arguments.get("triindices");
			List<Double> P = (List<Double>)arguments.get("P");
			
			List<Float> UVs = null;
			if(arguments.containsKey("uv")){
				 UVs = (List<Float>) arguments.get("uv");
			}
			
			shape = new Mesh(position,rotation,scale,triindices,P,UVs);
			break;
		case "plane":
			//Vector3 de normales
			List<Double> N = (List<Double>)arguments.get("N");
			// shape = new Plane();
			break;
		case "box":
			float width = ((List<Float>) arguments.get("width")).get(0);
			float height = ((List<Float>) arguments.get("height")).get(0);
			float depth = ((List<Float>) arguments.get("depth")).get(0);
			//shape = new Box();
			break;
		case "sphere":
			float radius = ((List<Float>) arguments.get("radius")).get(0);
			
			//shape = new Sphere(radius);
			
			break;
		default:
			System.out.println(shapeType + " not supported.");
		}
		
		return shape;
	}
	
	public void materialParser(String fileDir) throws FileNotFoundException{
		Scanner in = new Scanner(new FileReader(fileDir));
		
		in.nextLine();in.nextLine(); //clears "# Materials File" and the next line
		
		Map<String,Object> arguments = new HashMap<String,Object>();
		String currentMaterialName = "";
	
		String currentTextureName = "";
		
		Map<String,Map<String,Object>> textures = new HashMap<String,Map<String,Object>>();
		
		while(in.hasNextLine()){
			String currentLine = in.nextLine();
			
			if(currentLine.startsWith("MakeNamedMaterial")){
				
				int firstQuote= currentLine.indexOf('"') + 1;
				int lastQuote= currentLine.lastIndexOf('"');
				currentMaterialName = currentLine.substring(firstQuote,lastQuote);
			
			}else if(currentLine.startsWith("\t")){
				
				parseArgument(currentLine,arguments);
				
			} else if(currentLine.startsWith("Texture")){
					
				currentTextureName = currentLine.split("\"")[1];
			
			} else if( currentLine.equals("") && !currentMaterialName.equals("")){
				if(currentMaterialName.equals("")){
					textures.put(currentTextureName, new HashMap<String,Object>(arguments));
				}else{
					if(currentTextureName.equals(""))
						materials.put(currentMaterialName, getMaterialFromArguments(arguments,null));
					else
						materials.put(currentMaterialName, getMaterialFromArguments(arguments,textures.get(currentTextureName)));

					currentMaterialName = "";
					currentTextureName = "";
				}
				
				arguments.clear();
			}
					
		}
		
		in.close();
		return;	
	}
	
	public Material getMaterialFromArguments(Map<String,Object> arguments, Map<String,Object> textureArgs){
		
		Material material = null;
		
		Texture texture = null;
		if(textureArgs != null){
			if(textureArgs.containsKey("filename")){
				texture = new ImageTexture( (String) textureArgs.get("filename"));
			}			
		}
		
		String type = (String)arguments.get("type");
		switch(type){
			case "matte":
				Color3f Kd = (Color3f)arguments.get("Kd");
				float sigma = ((List<Float>) arguments.get("sigma")).get(0);
				// material = new Matte();
				break;
			case "mirror":
				float _filmindex = ((List<Float>) arguments.get("filmindex")).get(0);
				Color3f _Kr = (Color3f)arguments.get("Kr");
				//material = new Mirror();
				break;
			case "glass":
				Color3f Kr = (Color3f)arguments.get("Kr");
				Color3f Kt = (Color3f)arguments.get("Kt");
				float index = ((List<Float>) arguments.get("index")).get(0);
				float filmindex = ((List<Float>) arguments.get("filmindex")).get(0);
				// material = new Glass();
				break;
			case "metal2":
				float uroughness = ((List<Float>) arguments.get("uroughness")).get(0);
				float vroughness = ((List<Float>) arguments.get("vroughness")).get(0);
				//material = new Metal2(texture,uroughness);
				break;
			default:
				System.out.println(type + " Material not supported");
				break;
		}
		
		return material;
		
	}	
	
	public void parseArgument(String line, Map<String,Object> arguments){
		String[] splitedLine = line.split("\"");
		
		String ArgumentType = splitedLine[1].split(" ")[0];
		String ArgumentName = splitedLine[1].split(" ")[1];
		
		String ArgumentValue = line.substring(ArgumentType.length() + ArgumentName.length() + 6,line.length()-1);
		
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
			default:
				System.out.println(ArgumentType + " not supported.");
				break;
		}
	}
	
}
